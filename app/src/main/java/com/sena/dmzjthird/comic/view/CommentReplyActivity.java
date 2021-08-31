package com.sena.dmzjthird.comic.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.bean.UploadImageBean;
import com.sena.dmzjthird.databinding.ActivityCommentReplyBinding;
import com.sena.dmzjthird.utils.HttpPostUtil;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CommentReplyActivity extends AppCompatActivity {

    private ActivityCommentReplyBinding binding;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.lengthCounter.setText(getString(R.string.length_counter, 0));
        binding.toolbar.setBackListener(v -> finish());

        String toReplyUsername = IntentUtil.getUsername(this);
        String toReplyContent = IntentUtil.getContent(this);
        String toUid = IntentUtil.getToUid(this);
        String toCommentId = IntentUtil.getToCommentId(this);
        String objId = IntentUtil.getObjectId(this);
        int classify = IntentUtil.getClassifyId(this);

        String uid = PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_UID);
        String token = PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_TOKEN);


        // reply文字提示
        // binding.toReplyUsername & binding.toReplyContent
        if (TextUtils.isEmpty(toReplyUsername)) {
            binding.toReplyContent.setVisibility(View.GONE);
            binding.toReplyUsername.setVisibility(View.GONE);
        } else {
            binding.toReplyUsername.setText(toReplyUsername);
            binding.toReplyContent.setText(toReplyUsername + ": " + toReplyContent);
        }

        // 发送发表评论请求
        binding.toolbar.setOtherListener(v -> {

            String content = binding.content.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "输入不得为空", Toast.LENGTH_SHORT).show();
                return;
            }

            Observable.create((ObservableOnSubscribe<JSONObject>) emitter -> {
                String imageId = null;


                HttpPostUtil postUtil = new HttpPostUtil("https://nnv3api.dmzj1.com/comment2/uploadImg");
                postUtil.addFileParameter(file);
                String result = postUtil.send();

                LogUtil.e(result);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray data = new JSONArray(jsonObject.getString("data"));

                if (data.length() > 0) {
                    imageId = data.get(0).toString();

                    LogUtil.e(imageId);
                }


                String requestUrl = getString(R.string.public_comment, classify, objId, content, uid, token, toUid, toCommentId);
                if (imageId != null) {
                    requestUrl += ("&img=" + imageId);
                }
                URL url = new URL(requestUrl);
                LogUtil.e("url: " + url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    LogUtil.e("HttpError: " + connection.getResponseCode());
                    return;
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                int len;
                byte[] buffer = new byte[1024];
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.close();
                connection.disconnect();
                emitter.onNext(new JSONObject(out.toString()));
                emitter.onComplete();

            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {

                        Toast.makeText(CommentReplyActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (jsonObject.getInt("code") == 0) {
                            finish();
                        }

                    });


        });

        // 字数变动提示
        binding.content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = binding.content.getText().length();
                binding.lengthCounter.setText(getString(R.string.length_counter, length));
            }
        });

        binding.addImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("使用该功能需要读写手机存储权限，请前往系统设置开启权限。");
                builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 2);
                    }
                });
                builder.setNegativeButton("取消", ((dialog, which) -> {
                }));
                builder.create().show();
            } else {
                openAlbum();
            }
        });

        binding.clear.setOnClickListener(v -> {
            binding.addImage.setImageDrawable(null);
            binding.clear.setVisibility(View.INVISIBLE);
        });


    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    setImage(data);
                }

                break;
            case 2:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    openAlbum();
                }
            default:
                break;
        }
    }

    private void setImage(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:/downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }

        if (imagePath != null) {
            file = new File(imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            binding.addImage.setImageBitmap(bitmap);
            binding.clear.setVisibility(View.VISIBLE);
        } else {
            LogUtil.e("无法打开图片");
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while (-1 != (len = in.read(buffer))) {
            out.write(buffer, 0, len);
        }
        in.close();
        return out.toByteArray();
    }

    private String encode(String value) throws  Exception {
        return URLEncoder.encode(value, "UTF-8");
    }
}
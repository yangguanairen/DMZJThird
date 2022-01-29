package com.sena.dmzjthird.account.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.UserRetrofitService;
import com.sena.dmzjthird.account.bean.UserResultBean;
import com.sena.dmzjthird.custom.clipView.ClipActivity;
import com.sena.dmzjthird.databinding.ActivityUpdateProfileBinding;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.UriUtil;
import com.sena.dmzjthird.utils.XPopUpUtil;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UpdateProfileActivity extends AppCompatActivity {

    private ActivityUpdateProfileBinding binding;

    private UserRetrofitService service;
    private ActivityResultLauncher<String> requestCameraPermission;
    private ActivityResultLauncher<Uri> getTakePicture;
    private ActivityResultLauncher getClipResult;
    private ActivityResultLauncher<String> requireStoragePermission;
    private ActivityResultLauncher getContent;

    private Uri targetUri;


    // 暂存上传的头像链接，save失败，不会更新头像
    private String uploadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        service = RetrofitHelper.getUserServer(UserRetrofitService.MY_BASE_URL);

        targetUri = UriUtil.createFileInPhoto(this, "cameraResult.jpg", "image/jpeg");

        initActivityResult();
        initClick();

//        initProfile();

    }

    // 根据现有信息初始化
    private void initProfile() {
        service.queryAccount(MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {

                    if (bean.getCode() == 100) {
                        finish();
                    }

                    UserResultBean.User user = bean.getUser();
                    Glide.with(this).load(user.getAvatar()).apply(RequestOptions.bitmapTransform(new RoundedCorners(180))).into(binding.avatar);
                    binding.nickname.setText(user.getNickname());
                    binding.sex.setText(user.getSex());
                    binding.birthday.setText(user.getAge());
                    binding.email.setText(user.getEmail());

                });
    }

    private void initActivityResult() {
        // 申请相机权限
        requestCameraPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
            Uri targetUri = null;
            if (!isGrant) {
                XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "需要开启相机权限");
            } else {
                getTakePicture.launch(targetUri);
            }
        });

        // 获取拍照结果
        getTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), isSave -> {
            if (!isSave) return;

            Intent intent = new Intent(this, ClipActivity.class);
            intent.putExtra("clipUriData", targetUri);
            getClipResult.launch(intent);
        });

        // 获取截图结果
        getClipResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() != RESULT_OK) return;
            Uri clipUri = result.getData().getParcelableExtra("clipUriData");
            if (clipUri == null) return;
            // 上传结果
            uploadImage(clipUri);
        });

        // 申请存储权限
        requireStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
            if (!isGrant) {
                XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "需要开启文件存储权限");
            } else {
                getContent.launch("image/*");
            }
        });
        // 获取相册图片
        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri == null) return;
            String docId = DocumentsContract.getDocumentId(uri);
            String type = docId.split(":")[0];
            if (!"image".equals(type)) return;

            Intent intent = new Intent(this, ClipActivity.class);
            intent.putExtra("clipUriData", uri);
            getClipResult.launch(intent);
        });
    }

    // 控件添加事件监听
    private void initClick() {
        binding.avatar.setOnClickListener(v -> new XPopup.Builder(this)
                .asCenterList("", new String[]{"相机", "相册"}, new int[]{}, (position, text) -> {
                    if (position == 0) {
                        checkCameraPermission();
                    } else if (position == 1) {
                        checkStoragePermission();
                    }
                }).show());

        binding.sex.setOnClickListener(v -> new XPopup.Builder(this)
                .asCenterList("请选择性别: ", new String[]{"男", "女", "保密"}, new int[]{}, (position, text) -> {
                    binding.sex.setText(text);
                }));
        binding.email.setOnClickListener(v -> new XPopup.Builder(this)
                .asInputConfirm("请输入邮箱", "", "", "邮箱", text -> {
                    if (!TextUtils.isEmpty(text)) binding.email.setText(text);
                }));
    }

    private void checkCameraPermission() {
        boolean isGrated = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        if (!isGrated) {
            requestCameraPermission.launch(Manifest.permission.CAMERA);
        } else {
            getTakePicture.launch(targetUri);
        }
    }

    private void checkStoragePermission() {
        boolean isGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!isGranted) {
            requireStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            getContent.launch("image/*");
        }
    }

    private void uploadImage(Uri uri) {

        Glide.with(this).load(uri).apply(RequestOptions.bitmapTransform(new RoundedCorners(180))).into(binding.avatar);


//        File file = UriUtil.getFileByUri(this, uri);
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(file, MediaType.parse("multipart/form-data"));
//        builder.addFormDataPart("file", file.getName(), body);
//        List<MultipartBody.Part> partList = builder.build().parts();
//
//        service.uploadImage(partList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(bean -> {
//                    if (bean.getCode() != 200) {
//                        new XPopup.Builder(this)
//                                .isDestroyOnDismiss(true)
//                                .asCustom(new CustomToast(this, R.drawable.ic_error_red, "", "上传失败，请重新尝试"))
//                                .show();
//                    } else {
//                        new XPopup.Builder(this)
//                                .isDestroyOnDismiss(true)
//                                .asCustom(new CustomToast(this, R.drawable.ic_check_green, "", "上传成功"))
//                                .show();
//
//                        uploadImageUrl = bean.getContent();
//                        Glide.with(this).load(uploadImageUrl)
//                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(180)))
//                                .into(binding.avatar);
//
//                    }
//                });

    }


}
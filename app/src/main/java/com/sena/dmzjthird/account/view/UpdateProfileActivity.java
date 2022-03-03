package com.sena.dmzjthird.account.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.bean.UserResultBean;
import com.sena.dmzjthird.custom.popup.DatePickerDialog;
import com.sena.dmzjthird.custom.clipView.ClipActivity;
import com.sena.dmzjthird.databinding.ActivityUpdateProfileBinding;
import com.sena.dmzjthird.utils.ImageUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.UriUtil;
import com.sena.dmzjthird.utils.ViewUtil;
import com.sena.dmzjthird.utils.XPopUpUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateProfileActivity extends AppCompatActivity {

    private ActivityUpdateProfileBinding binding;

    private MyRetrofitService service;
    private ActivityResultLauncher<String> requestCameraPermission;
    private ActivityResultLauncher<Uri> getTakePicture;
    private ActivityResultLauncher<Intent> getClipResult;
    private ActivityResultLauncher<String> requireStoragePermission;
    private ActivityResultLauncher<String> getContent;

    private Uri targetUri;


    // 暂存上传的头像链接，save失败，不会更新头像
    private String uploadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.toolbar)
                .init();

        service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);

        targetUri = UriUtil.createFileInDownload(this, "cameraResult.jpg", "image/jpeg");
        ViewUtil.addWaterRipple(binding.save);

        initActivityResult();
        initClick();

        initProfile();

    }

    // 根据现有信息初始化
    private void initProfile() {
        service.queryAccount(MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L))
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

            // 检测图片是否旋转，并修正
            File file = UriUtil.getFileByUri(this, targetUri);
            int degree = ImageUtil.calculateDegree(file);
            if (degree != 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                bitmap = ImageUtil.rotateBitmap(degree, bitmap);
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, getContentResolver().openOutputStream(targetUri));
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("修正图片旋转状态出错!!");
                }
            }

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

        binding.toolbar.setBackListener(v -> finish());

        binding.avatar.setOnClickListener(v -> new XPopup.Builder(this)
                .asCenterList("请选择来源", new String[]{"相机", "相册"}, new int[]{}, (position, text) -> {
                    if (position == 0) {
                        checkCameraPermission();
                    } else if (position == 1) {
                        checkStoragePermission();
                    }
                }).show());

        binding.updateBirthday.setOnClickListener(v -> new XPopup.Builder(this)
                .asCustom(new DatePickerDialog(this, binding.birthday))
                .show());
        binding.updateSex.setOnClickListener(v -> new XPopup.Builder(this)
                .asCenterList("请选择性别: ", new String[]{"男", "女", "保密"}, new int[]{}, (position, text) -> binding.sex.setText(text)).show());
        binding.updateEmail.setOnClickListener(v -> new XPopup.Builder(this)
                .asInputConfirm("请输入邮箱", "", "", "邮箱", text -> {
                    if (!TextUtils.isEmpty(text)) binding.email.setText(text);
                }).show());
        binding.save.setOnClickListener(v -> uploadProfile());
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

        // 直接加载本地
//        Glide.with(this).load(uri).apply(RequestOptions.bitmapTransform(new RoundedCorners(180))).into(binding.avatar);

        BasePopupView popupView = XPopUpUtil.showLoadingView(this);

        File file = UriUtil.getFileByUri(this, uri);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        builder.addFormDataPart("file", file.getName(), body);
        List<MultipartBody.Part> partList = builder.build().parts();

        service.uploadImage(partList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() != 200) {
                        XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "上传失败，请重新尝试");
                    } else {
                        popupView.dismiss();
                        XPopUpUtil.showCustomToast(this, R.drawable.ic_check_green, "上传成功");

                        uploadImageUrl = bean.getContent();
                        MyDataStore.getInstance(this).saveValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_AVATAR, uploadImageUrl);
                        // 加载云端图片
                        Glide.with(this).load(uploadImageUrl)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(180)))
                                .into(binding.avatar);

                    }
                });
    }

    private void uploadProfile() {
        Map<String, RequestBody> bodyMap = new HashMap<>();
        long uid = MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        bodyMap.put("uidStr", RequestBody.create(uid + "",
                MediaType.parse("multipart/form-data")));

        String avatar = uploadImageUrl;
        if (!"".equals(avatar) && avatar != null) {
            bodyMap.put("avatar", RequestBody.create(uploadImageUrl, MediaType.parse("multipart/form-data")));
        }

        String email = binding.email.getText().toString();
        if (!"".equals(email)) {
            bodyMap.put("email", RequestBody.create(email, MediaType.parse("multipart/form-data")));
        }

        String sex = binding.sex.getText().toString();
        if (!"".equals(avatar)) {
            bodyMap.put("sex", RequestBody.create(sex, MediaType.parse("multipart/form-data")));
        }

        String birthday = binding.birthday.getText().toString();
        if (!"".equals(birthday)) {
            bodyMap.put("birthday", RequestBody.create(birthday, MediaType.parse("multipart/form-data")));
        }

        BasePopupView popupView = XPopUpUtil.showLoadingView(this);
        service.updateAccount(bodyMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    popupView.dismiss();
                    if (bean == null || bean.getCode() != 200) XPopUpUtil.showCustomToast(UpdateProfileActivity.this, R.drawable.ic_error_red, "更新资料失败");
                    else XPopUpUtil.showCustomToast(UpdateProfileActivity.this, R.drawable.ic_check_green, "更新资料成功");
                });

    }

}
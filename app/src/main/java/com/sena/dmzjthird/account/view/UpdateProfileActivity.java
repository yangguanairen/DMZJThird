package com.sena.dmzjthird.account.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.UserRetrofitService;
import com.sena.dmzjthird.account.bean.ResultBean;
import com.sena.dmzjthird.account.bean.UserResultBean;
import com.sena.dmzjthird.custom.CustomToast;
import com.sena.dmzjthird.databinding.ActivityUpdateProfileBinding;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.UriUtil;

import java.io.File;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateProfileActivity extends AppCompatActivity {

    private ActivityUpdateProfileBinding binding;

    private UserRetrofitService service;
    private ActivityResultLauncher<String> getPermission;
    private ActivityResultLauncher getContent;


    // 暂存上传的头像链接，save失败，不会更新头像
    private String uploadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getUserServer(UserRetrofitService.MY_BASE_URL);

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
        getPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (!result) {
                new XPopup.Builder(this)
                        .isDestroyOnDismiss(true)
                        .asCustom(new CustomToast(this, R.drawable.ic_error_red, "", "需要开启文件存储权限"))
                        .show();
            } else {
                getContent.launch("image/*");
            }
        });
        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                return;
            }

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
                            new XPopup.Builder(this)
                                    .isDestroyOnDismiss(true)
                                    .asCustom(new CustomToast(this, R.drawable.ic_error_red, "", "上传失败，请重新尝试"))
                                    .show();
                        } else {
                            new XPopup.Builder(this)
                                    .isDestroyOnDismiss(true)
                                    .asCustom(new CustomToast(this, R.drawable.ic_check_green, "", "上传成功"))
                                    .show();

                            uploadImageUrl = bean.getContent();
                            Glide.with(this).load(uploadImageUrl)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(180)))
                                    .into(binding.avatar);

                        }
                    });

        });
    }

    // 控件添加事件监听
    private void initView() {

    }


}
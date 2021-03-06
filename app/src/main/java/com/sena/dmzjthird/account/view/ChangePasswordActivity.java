package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.bean.ResultBean;
import com.sena.dmzjthird.databinding.ActivityChangePasswordBinding;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.ViewHelper;
import com.sena.dmzjthird.utils.XPopUpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;

    private MyRetrofitService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);

        initView();
    }

    private void initView() {
        ViewHelper.immersiveStatus(this, binding.toolbar);
        binding.toolbar.setBackListener(v -> finish());
        binding.save.setOnClickListener(v -> changePassword());

    }

    private void changePassword() {


        String originalPassword = Objects.requireNonNull(binding.originalLayout.getEditText()).getText().toString();
        String newPassword = Objects.requireNonNull(binding.newLayout.getEditText()).getText().toString();
        String confirmPassword = Objects.requireNonNull(binding.confirmLayout.getEditText()).getText().toString();

        if (!confirmPassword.equals(newPassword)) {
            XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "?????????????????????");
            return ;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{5,16}");
        if (!pattern.matcher(newPassword).matches()) {
            XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "???????????????6-16???????????????");
            return ;
        }

        Map<String, RequestBody> bodyMap = new HashMap<>();
        long uid = MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        bodyMap.put("uidStr", RequestBody.create(uid + "", MediaType.parse("multipart/form-data")));
        bodyMap.put("oldPass", RequestBody.create(originalPassword, MediaType.parse("multipart/form-data")));
        bodyMap.put("newPass", RequestBody.create(newPassword, MediaType.parse("multipart/form-data")));

        BasePopupView popupView = XPopUpUtil.showLoadingView(this);
        service.updatePassword(bodyMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultBean bean) {
                        popupView.dismiss();
                        if (bean.getCode() == 100)  {
                            XPopUpUtil.showCustomToast(ChangePasswordActivity.this, R.drawable.ic_error_red, bean.getContent());
                        } else {
                            XPopUpUtil.showCustomToast(ChangePasswordActivity.this, R.drawable.ic_check_green, bean.getContent());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        popupView.dismiss();
                        XPopUpUtil.showCustomToast(ChangePasswordActivity.this, R.drawable.ic_error_red, "????????????");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
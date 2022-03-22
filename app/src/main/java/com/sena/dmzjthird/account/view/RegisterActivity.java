package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.bean.UserResultBean;
import com.sena.dmzjthird.databinding.ActivityRegisterBinding;
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

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private MyRetrofitService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);

        initView();
    }

    private void initView() {
        ViewHelper.immersiveStatus(this, binding.toolbar);
        binding.register.setOnClickListener(v -> register());
    }

    private void register() {

        String nickname = Objects.requireNonNull(binding.usernameLayout.getEditText()).getText().toString();
        String password = Objects.requireNonNull(binding.passwordLayout.getEditText()).getText().toString();
        String confirm = Objects.requireNonNull(binding.confirmLayout.getEditText()).getText().toString();

        if (!Pattern.matches("[a-zA-Z0-9\u4e00-\u9fa5]{4,12}", nickname)) {
            XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "昵称需要4-12中英文、数字");
            return;
        }
        if (!confirm.equals(password)) {
            XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "两次密码不一致");
            return;
        }
        if (!Pattern.matches("[a-zA-Z0-9]{5,16}", password)) {
            XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "密码需要6-16英文、数字");
            return;
        }

        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("nickname", RequestBody.create(nickname, MediaType.parse("multipart/form-data")));
        bodyMap.put("password", RequestBody.create(password, MediaType.parse("multipart/form-data")));

        BasePopupView popup = XPopUpUtil.showLoadingView(this);

        service.createAccount(bodyMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserResultBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull UserResultBean bean) {
                        popup.dismiss();
                        if (bean.getCode() != 200) {
                            XPopUpUtil.showCustomToast(RegisterActivity.this, R.drawable.ic_error_red, "注册失败，请重新尝试!!");
                        } else {
                            XPopUpUtil.showCustomCheckToast(RegisterActivity.this, "注册成功!!");
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}
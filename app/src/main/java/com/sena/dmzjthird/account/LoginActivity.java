package com.sena.dmzjthird.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.bean.LoginBean;
import com.sena.dmzjthird.databinding.ActivityLoginBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private RetrofitService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getServer(RetrofitService.BASE_USER_URL);

        binding.toolbar.setBackListener(v -> finish());

        binding.login.setOnClickListener(v -> {
            String username = Objects.requireNonNull(binding.usernameLayout.getEditText()).getText().toString();
            String password = Objects.requireNonNull(binding.passwordLayout.getEditText()).getText().toString();

            if ("".equals(username) || "".equals(password)) {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, RequestBody> bodyMap = new HashMap<>();
            bodyMap.put("passwd", RequestBody.create(password,
                    MediaType.parse("multipart/form-data")));
            bodyMap.put("nickname", RequestBody.create(username,
                    MediaType.parse("multipart/form-data")));

            service.confirmAccount(bodyMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginBean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull LoginBean bean) {
                            if (bean.getResult() == 1) {

                                PreferenceHelper.setStringByKey(LoginActivity.this,
                                        PreferenceHelper.USER_UID, bean.getData().getUid());
                                PreferenceHelper.setStringByKey(LoginActivity.this,
                                        PreferenceHelper.USER_TOKEN, bean.getData().getDmzj_token());

                                PreferenceHelper.setStringByKey(LoginActivity.this,
                                        PreferenceHelper.USER_NICKNAME, bean.getData().getNickname());
                                PreferenceHelper.setStringByKey(LoginActivity.this,
                                        PreferenceHelper.USER_AVATAR, bean.getData().getPhoto());


                                setResult(RESULT_OK, null);

                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            if (e instanceof HttpException) {
                                LogUtil.d("HttpError: " + ((HttpException) e).code());
                            } else {
                                LogUtil.d("OtherError: " + e.getMessage());
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        });

    }
}
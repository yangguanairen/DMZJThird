package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.UserRetrofitService;
import com.sena.dmzjthird.account.bean.LoginBean;
import com.sena.dmzjthird.account.bean.ResultBean;
import com.sena.dmzjthird.databinding.ActivityLoginBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserRetrofitService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getUserServer(UserRetrofitService.MY_BASE_URL);

        binding.toolbar.setBackListener(v -> finish());

        binding.login.setOnClickListener(v -> login());

        binding.register.setOnClickListener(v -> IntentUtil.goToActivity(this, RegisterActivity.class));

    }

    private void login() {

        String nickname = Objects.requireNonNull(binding.usernameLayout.getEditText()).getText().toString();
        String password = Objects.requireNonNull(binding.passwordLayout.getEditText()).getText().toString();

        if ("".equals(nickname) || "".equals(password)) {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("password", RequestBody.create(password,
                MediaType.parse("multipart/form-data")));
        bodyMap.put("nickname", RequestBody.create(nickname,
                MediaType.parse("multipart/form-data")));

        service.loginAccount(bodyMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        MyDataStore.getInstance(LoginActivity.this).saveValue(MyDataStore.DATA_STORE_USER,
                                MyDataStore.USER_UID, bean.getUser().getUid());
                        MyDataStore.getInstance(LoginActivity.this).saveValue(MyDataStore.DATA_STORE_USER,
                                MyDataStore.USER_NICKNAME, bean.getUser().getNickname());
                        // "http://avatar.dmzj.com/78/12/7812c2f9fff43511dc2fec3fa1219948.png"
                        MyDataStore.getInstance(LoginActivity.this).saveValue(MyDataStore.DATA_STORE_USER,
                                MyDataStore.USER_AVATAR, bean.getUser().getAvatar());

                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
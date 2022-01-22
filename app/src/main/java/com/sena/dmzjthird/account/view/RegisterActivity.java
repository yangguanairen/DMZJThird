package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.account.UserRetrofitService;
import com.sena.dmzjthird.account.bean.UserResultBean;
import com.sena.dmzjthird.databinding.ActivityRegisterBinding;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private UserRetrofitService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getUserServer(UserRetrofitService.MY_BASE_URL);

        binding.register.setOnClickListener(v -> register());

    }



    private void register() {



        String nickname = Objects.requireNonNull(binding.usernameLayout.getEditText()).getText().toString();
        String password = Objects.requireNonNull(binding.passwordLayout.getEditText()).getText().toString();
        String confirm = Objects.requireNonNull(binding.confirmLayout.getEditText()).getText().toString();

        if ("".equals(nickname) || "".equals(password) || "".equals(confirm)) {
            Toast.makeText(this, "昵称或密码为空!!", Toast.LENGTH_SHORT).show();
            return ;
        } else if (!password.equals(confirm)) {
            Toast.makeText(this, "密码错误!!", Toast.LENGTH_SHORT).show();
            return ;
        }

        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("nickname", RequestBody.create(nickname, MediaType.parse("multipart/form-data")));
        bodyMap.put("password", RequestBody.create(password, MediaType.parse("multipart/form-data")));



        service.createAccount(bodyMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {

                    if (bean.getCode() != 200) {

                        Toast.makeText(this, "注册失败，请重新尝试!", Toast.LENGTH_SHORT).show();

                    } else {

                        finish();

                    }
                });

    }


}
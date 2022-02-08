package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.UserRetrofitService;
import com.sena.dmzjthird.databinding.ActivityChangePasswordBinding;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.XPopUpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;

    private UserRetrofitService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getUserServer(UserRetrofitService.MY_BASE_URL);

        binding.save.setOnClickListener(v -> changePassword());

    }

    private void changePassword() {


        String originalPassword = Objects.requireNonNull(binding.originalLayout.getEditText()).getText().toString();
        String newPassword = binding.newLayout.getEditText().getText().toString();
        String confirmPassword = binding.confirmLayout.getEditText().getText().toString();

        if (!confirmPassword.equals(newPassword)) {
            XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "两次密码不一致");
            return ;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{5,16}");
        if (!pattern.matcher(newPassword).matches()) {
            XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "新密码需要6-16英文、数字");
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
                .subscribe(bean -> {
                    popupView.dismiss();
                    if (bean == null ) {
                        XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, "更新失败");
                    } else if (bean.getCode() == 100)  {
                        XPopUpUtil.showCustomToast(this, R.drawable.ic_error_red, bean.getContent());
                    } else {
                        XPopUpUtil.showCustomToast(this, R.drawable.ic_check_green, bean.getContent());
                    }
                });

    }
}
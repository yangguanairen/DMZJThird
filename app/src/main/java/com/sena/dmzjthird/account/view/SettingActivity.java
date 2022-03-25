package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.ActivitySettingBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.ViewHelper;
import com.sena.dmzjthird.utils.XPopUpUtil;


public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

    }

    private void initView() {

        ViewHelper.immersiveStatus(this, binding.toolbar);

        binding.toolbar.setBackListener(v -> finish());

        binding.comicReadSetting.setOnClickListener(v -> IntentUtil.goToActivity(this, SettingComicReadActivity.class));

        binding.changePass.setOnClickListener(v -> {

            long uid = MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
            if (uid == 0L) {
                XPopUpUtil.showCustomErrorToast(this, getString(R.string.not_login));
                return ;
            }

            IntentUtil.goToActivity(this, ChangePasswordActivity.class);

        });

        binding.novelReadSetting.setOnClickListener(v -> IntentUtil.goToActivity(this, SettingNovelReadActivity.class));
    }

}
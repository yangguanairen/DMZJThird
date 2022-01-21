package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sena.dmzjthird.databinding.ActivitySettingBinding;


public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.normalToolbar.setBackListener(v -> finish());

        binding.settingComicRead.setOnClickListener(v -> startActivity(new Intent(this, SettingComicReadActivity.class)));


    }

}
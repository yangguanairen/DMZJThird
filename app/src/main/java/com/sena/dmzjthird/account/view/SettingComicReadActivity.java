package com.sena.dmzjthird.account.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.sena.dmzjthird.databinding.ActivitySettingComicReadBinding;
import com.sena.dmzjthird.utils.MyDataStore;

public class SettingComicReadActivity extends AppCompatActivity {

    private ActivitySettingComicReadBinding binding;

    private MyDataStore dataStore;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingComicReadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataStore = MyDataStore.getInstance(this);

        binding.normalToolbar.setBackListener(v -> finish());

        initClick();

        initState();

    }


    private void initState() {

        binding.content.lightSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SYSTEM_BRIGHTNESS, false));
        binding.content.lightSeekBar.setMax(getResources().getInteger(
                getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android")
        ));
        binding.content.lightSeekBar.setProgress(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_BRIGHTNESS, 0));
        binding.content.verticalSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_VERTICAL_MODE, false));
        binding.content.modeSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_COMIC_MODE, false));
        binding.content.keepSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_KEEP_LIGHT, false));
        binding.content.fullSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_FULL_SCREEN, false));
        binding.content.stateSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SHOW_STATE, false));

    }

    private void initClick() {

        binding.content.lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                binding.content.lightCons.setVisibility(isChecked ? View.GONE : View.VISIBLE));



        binding.content.verticalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.content.modeSwitch.setChecked(false);
            binding.content.modeCons.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
        binding = null;

    }

    private void saveState() {
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SYSTEM_BRIGHTNESS, binding.content.lightSwitch.isChecked());
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_BRIGHTNESS, binding.content.lightSeekBar.getProgress());
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_VERTICAL_MODE, binding.content.verticalSwitch.isChecked());
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_COMIC_MODE, binding.content.modeSwitch.isChecked());
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_KEEP_LIGHT, binding.content.keepSwitch.isChecked());
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_FULL_SCREEN, binding.content.fullSwitch.isChecked());
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SHOW_STATE, binding.content.stateSwitch.isChecked());

    }
}
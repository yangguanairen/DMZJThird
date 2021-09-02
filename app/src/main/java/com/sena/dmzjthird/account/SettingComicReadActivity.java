package com.sena.dmzjthird.account;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.sena.dmzjthird.databinding.ActivitySettingComicReadBinding;
import com.sena.dmzjthird.utils.PreferenceHelper;

public class SettingComicReadActivity extends AppCompatActivity {

    private ActivitySettingComicReadBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingComicReadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.normalToolbar.setBackListener(v -> finish());


        binding.lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.lightCons.setVisibility(View.GONE);
                // 使用系统亮度
                // 修改当前窗口亮度为系统亮度
            } else {
                binding.lightCons.setVisibility(View.VISIBLE);
                // 自己调节亮度
            }
        });

        binding.lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 修改当前屏幕亮度为seekbar刻度亮度
                // 屏幕亮度为0~1，设置(double) progress/10， 默认progress值2
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.verticalSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                binding.modeCons.setVisibility(View.GONE);
                binding.modeSwitch.setChecked(false);
                // 漫画阅读模式改为垂直阅读，关闭日漫模式
            } else {
                binding.modeCons.setVisibility(View.VISIBLE);
                // 漫画阅读模式改为翻页
            }
        }));

    }


    @Override
    protected void onStart() {
        super.onStart();
        //设置状态
        Log.d("jc", "onStart: " + PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_USE_SYSTEM_BRIGHTNESS));
        binding.lightSwitch.setChecked(PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_USE_SYSTEM_BRIGHTNESS));
        binding.lightSeekBar.setProgress(PreferenceHelper.findIntByKey(this, PreferenceHelper.SEEKBAR_BRIGHTNESS));
        binding.verticalSwitch.setChecked(PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_VERTICAL_MODE));
        binding.modeSwitch.setChecked(PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_JAPANESE_COMIC_MODE));
        binding.keepSwitch.setChecked(PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_KEEP_LIGHT_ALWAYS));
        binding.fullSwitch.setChecked(PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_FULL_SCREEN));
        binding.stateSwitch.setChecked(PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_SHOW_STATE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 存储信息
        Log.d("jc", "onStop: " + binding.lightSwitch.isChecked());
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_USE_SYSTEM_BRIGHTNESS, binding.lightSwitch.isChecked());
        PreferenceHelper.setIntByKey(this, PreferenceHelper.SEEKBAR_BRIGHTNESS, binding.lightSeekBar.getProgress());
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_VERTICAL_MODE, binding.verticalSwitch.isChecked());
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_JAPANESE_COMIC_MODE, binding.modeSwitch.isChecked());
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_KEEP_LIGHT_ALWAYS, binding.keepSwitch.isChecked());
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_FULL_SCREEN, binding.fullSwitch.isChecked());
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_SHOW_STATE, binding.stateSwitch.isChecked());
        Log.d("jc", "onStop: " + "save is done");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;







    }
}
package com.sena.dmzjthird.custom.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lxj.xpopup.core.BottomPopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.MyDataStore;

/**
 * FileName: CustomBottomPopup
 * Author: JiaoCan
 * Date: 2022/2/22 11:59
 */

@SuppressLint("ViewConstructor")
public class CustomBottomPopup extends BottomPopupView {

    private final MyDataStore dataStore;
    private boolean isUseSystemBrightness;
    private int brightness;
    private boolean isVerticalMode;
    private boolean isComicMode;
    private boolean isKeepLight;
    private boolean isFullScreen;
    private boolean isShowState;

    private final int maxBrightness;

    private final Callbacks callback;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch lightSwitch;
    SeekBar lightSeekBar;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch verticalSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch modeSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch keepSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch fullSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch stateSwitch;
    LinearLayout contentLayout;
    ConstraintLayout brightnessLayout;
    ConstraintLayout modeLayout;

    public CustomBottomPopup(@NonNull Context context, int maxBrightness) {
        super(context);
        callback = (Callbacks) context;
        dataStore = MyDataStore.getInstance(context);
        this.maxBrightness = maxBrightness;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.content_setting_comic_read;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        lightSwitch = findViewById(R.id.lightSwitch);
        lightSeekBar = findViewById(R.id.lightSeekBar);
        verticalSwitch = findViewById(R.id.verticalSwitch);
        modeSwitch = findViewById(R.id.modeSwitch);
        keepSwitch = findViewById(R.id.keepSwitch);
        fullSwitch = findViewById(R.id.fullSwitch);
        stateSwitch = findViewById(R.id.stateSwitch);

        contentLayout = findViewById(R.id.content);
        brightnessLayout = findViewById(R.id.lightCons);
        modeLayout = findViewById(R.id.modeCons);

        initClick();

        initState();

    }

    private void initState() {

        contentLayout.setBackgroundColor(Color.parseColor("#424242"));

        lightSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SYSTEM_BRIGHTNESS, false));
        lightSeekBar.setMax(maxBrightness);
        lightSeekBar.setProgress(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_BRIGHTNESS, 0));
        verticalSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_VERTICAL_MODE, false));
        modeSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_COMIC_MODE, false));
        keepSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_KEEP_LIGHT, false));
        fullSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_FULL_SCREEN, false));
        stateSwitch.setChecked(dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SHOW_STATE, false));

    }

    private void initClick() {

        lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isUseSystemBrightness = isChecked;
            brightnessLayout.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            // 使用/关闭系统亮度，对当前页面亮度进行处理
            if (isChecked) {
                callback.onUseSystemBrightness();
            } else {
                lightSeekBar.setProgress(brightness);
            }
        });

        lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
                if (!isUseSystemBrightness) return ;
                callback.onBrightnessChange(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        verticalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isVerticalMode = isChecked;
            modeSwitch.setChecked(!isChecked && modeSwitch.isChecked());
            modeLayout.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            callback.onVerticalModeChange(isChecked);
        });

        modeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isComicMode = isChecked;
            if (isVerticalMode) return ;
            callback.onComicModeChange(isChecked);
        });

        fullSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isFullScreen = isChecked;
            callback.onFullScreenChange(isChecked);
        });

        keepSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isKeepLight = isChecked;
            callback.onKeepLightChange(isChecked);
        });

        stateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isShowState = isChecked;
            callback.onShowStateChange(isChecked);
        });

    }

    private void saveState() {

        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SYSTEM_BRIGHTNESS, isUseSystemBrightness);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_BRIGHTNESS, brightness);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_VERTICAL_MODE, isVerticalMode);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_COMIC_MODE, isComicMode);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_FULL_SCREEN, isFullScreen);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_KEEP_LIGHT, isKeepLight);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SHOW_STATE, isShowState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveState();
    }

    public interface Callbacks {

        void onUseSystemBrightness();

        void onBrightnessChange(int brightness);

        void onVerticalModeChange(boolean flag);

        void onComicModeChange(boolean flag);

        void onFullScreenChange(boolean flag);

        void onKeepLightChange(boolean flag);

        void onShowStateChange(boolean flag);
    }
}

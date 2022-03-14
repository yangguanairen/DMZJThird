package com.sena.dmzjthird.custom.readerComic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.lxj.xpopup.core.BottomPopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.view.ComicViewActivity;

import com.sena.dmzjthird.utils.MyDataStore;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/13
 * Time: 18:45
 */
public class ComicSettingPopup extends BottomPopupView {

    private final Context mContext;
    private final ComicViewVM vm;
    private final MyDataStore dataStore;
    private boolean isUseSystemBrightness;
    private int brightness;
    private boolean isVerticalMode;
    private boolean isKeepLight;
    private boolean isFullScreen;
    private boolean isShowState;

    private int systemMaxBrightness;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch lightSwitch;
    SeekBar lightSeekBar;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch verticalSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch keepSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch fullSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch stateSwitch;
    LinearLayout contentLayout;
    ConstraintLayout brightnessLayout;
    ConstraintLayout modeLayout;

    public ComicSettingPopup(@NonNull Context context) {
        super(context);
        mContext = context;
        vm = new ViewModelProvider((AppCompatActivity) context).get(ComicViewVM.class);
        dataStore = MyDataStore.getInstance(context);
        initData();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.content_setting_comic_read;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    private void initData() {

        systemMaxBrightness = getResources().getInteger(
                getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android")
        );

        isUseSystemBrightness = dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SYSTEM_BRIGHTNESS, false);
        brightness = dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_BRIGHTNESS, systemMaxBrightness / 2);
        isVerticalMode = dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_VERTICAL_MODE, false);
        isKeepLight = dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_KEEP_LIGHT, false);
        isFullScreen = dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_FULL_SCREEN, false);
        isShowState = dataStore.getValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SHOW_STATE, true);


        if (isUseSystemBrightness) {
            resetBright();
        } else {
            changeBright();
        }

        changeFullScreenMode();
        changeKeepLight();
        changeShowState();
    }

    private void initView() {
        lightSwitch = findViewById(R.id.lightSwitch);
        lightSeekBar = findViewById(R.id.lightSeekBar);
        verticalSwitch = findViewById(R.id.verticalSwitch);
        keepSwitch = findViewById(R.id.keepSwitch);
        fullSwitch = findViewById(R.id.fullSwitch);
        stateSwitch = findViewById(R.id.stateSwitch);

        contentLayout = findViewById(R.id.content);
        brightnessLayout = findViewById(R.id.lightCons);
        modeLayout = findViewById(R.id.modeCons);

        contentLayout.setBackgroundColor(Color.parseColor("#424242"));

        lightSwitch.setChecked(isUseSystemBrightness);
        lightSeekBar.setMax(systemMaxBrightness);
        lightSeekBar.setProgress(brightness);
        verticalSwitch.setChecked(isVerticalMode);
        keepSwitch.setChecked(isKeepLight);
        fullSwitch.setChecked(isFullScreen);
        stateSwitch.setChecked(isShowState);

        if (isUseSystemBrightness) {
            brightnessLayout.setVisibility(GONE);
        }

        addClick();
    }

    private void addClick() {
        lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isUseSystemBrightness = isChecked;
            brightnessLayout.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            // 使用/关闭系统亮度，对当前页面亮度进行处理
            if (isChecked) {
                resetBright();
            } else {
                changeBright();
            }
        });

        lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
                if (isUseSystemBrightness) return ;
                changeBright();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        verticalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });

        fullSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isFullScreen = isChecked;
            changeFullScreenMode();
        });

        keepSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isKeepLight = isChecked;
            changeKeepLight();
        });

        stateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isShowState = isChecked;
            changeShowState();
        });
    }

    private void resetBright() {
        Window window = ((ComicViewActivity) mContext).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        window.setAttributes(params);
    }

    private void changeBright() {
        Window window = ((ComicViewActivity) mContext).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.screenBrightness = brightness / (float) systemMaxBrightness;
        window.setAttributes(params);
    }

    private void changeFullScreenMode() {
        vm.isFullMode.postValue(isFullScreen);
//        ComicTopView comicTopView = ((ComicViewActivity) mContext).findViewById(R.id.toolbar);

    }

    private void changeKeepLight() {
        Window window = ((ComicViewActivity) mContext).getWindow();
        if (isKeepLight) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void changeShowState() {
        vm.isShowState.postValue(isShowState);
    }

    private void saveData() {

        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SYSTEM_BRIGHTNESS, isUseSystemBrightness);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_BRIGHTNESS, brightness);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_VERTICAL_MODE, isVerticalMode);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_FULL_SCREEN, isFullScreen);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_KEEP_LIGHT, isKeepLight);
        dataStore.saveValue(MyDataStore.DATA_STORE_READ_SETTING, MyDataStore.SETTING_SHOW_STATE, isShowState);

    }

}

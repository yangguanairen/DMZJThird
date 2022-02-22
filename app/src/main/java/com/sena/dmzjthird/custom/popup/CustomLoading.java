package com.sena.dmzjthird.custom.popup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.custom.ProgressWheel;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/1/22
 * Time: 12:05
 */
public class CustomLoading extends CenterPopupView {

    private ProgressWheel progressWheel;

    public CustomLoading(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_loading;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        progressWheel = findViewById(R.id.progress);
        progressWheel.spin();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressWheel != null) progressWheel.stopSpinning();

    }
}

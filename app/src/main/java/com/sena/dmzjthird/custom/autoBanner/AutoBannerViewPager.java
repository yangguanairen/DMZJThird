package com.sena.dmzjthird.custom.autoBanner;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * FileName: AutoBannerViewPager
 * Author: JiaoCan
 * Date: 2022/3/14 10:07
 */

public class AutoBannerViewPager extends ViewPager {

    public AutoBannerViewPager(@NonNull Context context) {
        this(context, null);
    }

    public AutoBannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}

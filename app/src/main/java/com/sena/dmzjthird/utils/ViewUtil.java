package com.sena.dmzjthird.utils;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.sena.dmzjthird.R;

/**
 * FileName: ViewUtil
 * Author: JiaoCan
 * Date: 2022/1/30 10:20
 */

public class ViewUtil {

    public static void addWaterRipple(View view) {
        int[] attrs = new int[]{R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = view.getContext().obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
        view.setForeground(drawableFromTheme);
        ta.recycle();
    }

}

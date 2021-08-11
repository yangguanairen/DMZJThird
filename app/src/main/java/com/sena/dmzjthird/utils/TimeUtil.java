package com.sena.dmzjthird.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/11
 * Time: 12:54
 */
public class TimeUtil {

    public static String millConvertToDate(long time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }

    public static String millConvertToDate(String time) {
        return millConvertToDate(Long.parseLong(time));
    }
}

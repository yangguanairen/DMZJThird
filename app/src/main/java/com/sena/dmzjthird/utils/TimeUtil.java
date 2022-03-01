package com.sena.dmzjthird.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/11
 * Time: 12:54
 */
public class TimeUtil {

    public static String millConvertToDate(long time) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        long currentMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentMillis);

        String objectStr = format.format(time);
        String currentStr = format.format(currentDate);

        int year = Integer.parseInt(currentStr.substring(0, 4)) - Integer.parseInt(objectStr.substring(0, 4));
        int month = Integer.parseInt(currentStr.substring(5, 7)) - Integer.parseInt(objectStr.substring(5, 7));
        int day = Integer.parseInt(currentStr.substring(8, 10)) - Integer.parseInt(objectStr.substring(8, 10));
        int hour = Integer.parseInt(currentStr.substring(11, 13)) - Integer.parseInt(objectStr.substring(11, 13));
        int min = Integer.parseInt(currentStr.substring(14, 16)) - Integer.parseInt(objectStr.substring(14, 16));
        int s = Integer.parseInt(currentStr.substring(17)) - Integer.parseInt(objectStr.substring(17));

        if (year > 0) {
            return objectStr.substring(0, 10);
        } else if (month > 0) {
            return objectStr.substring(5, 10);
        } else if (day > 0) {
            if (day == 1) {
                return "昨天";
            } else if (day == 2) {
                return "前天";
            } else {
                return objectStr.substring(5, 10);
            }
        } else if (hour > 0) {
            return hour + "小时前";
        } else if (min > 0) {
            return min + "分钟前";
        } else {
            return s + "秒前";
        }

    }

    public static String millConvertToDate(String time) {
        return millConvertToDate(Long.parseLong(time));
    }
}

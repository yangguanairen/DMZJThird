package com.sena.dmzjthird.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/7/27
 * Time: 15:14
 */
public class PreferenceHelper {

    public static final String MY_SP = "my_sp";

//    public static final String THEME_KEY = "theme_key";
//    public static final String THEME_BLUE_VALUE = "blue";
//    public static final String THEME_PINK_VALUE = "pink";
//    public static final String THEME_GREEN_VALUE = "green";
//    public static final String THEME_PURPLE_VALUE = "purple";
//    public static final String THEME_RED_VALUE = "red";
//    public static final String THEME_YELLOW_VALUE = "yellow";

    public static final String COMIC_READ_SETTING_LIGHT_MODE = "comic_read_setting_light_mode";
    public static final String COMIC_READ_SETTING_LIGHT_PROGRESS = "comic_read_setting_light_progress";
    public static final String COMIC_READ_SETTING_READ_MODE = "comic_read_setting_read_mode";
    public static final String COMIC_READ_SETTING_COMIC_MODE = "comic_read_setting_comic_mode";
    public static final String COMIC_READ_SETTING_KEEP_SCREEN = "comic_read_setting_keep_screen";
    public static final String COMIC_READ_SETTING_FULLSCREEN = "comic_read_setting_fullscreen";
    public static final String COMIC_READ_SETTING_STATE = "comic_read_setting_state";




    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences(MY_SP, Context.MODE_PRIVATE);
    }


    public static String findStringByKey(Context context, String key) {
        return getSharePreference(context).getString(key, null);
    }

    public static boolean findBooleanByKey(Context context, String key) {
        return getSharePreference(context).getBoolean(key, false);
    }

    public static int findIntByKey(Context context, String key) {
        return getSharePreference(context).getInt(key, 0);
    }

    public static void setStringByKey(Context context, String key, String value) {
        getSharePreference(context).edit().putString(key, value).apply();
    }

    public static void setBooleanByKey(Context context, String key, boolean value) {
        getSharePreference(context).edit().putBoolean(key, value).apply();
    }

    public static void setIntByKey(Context context, String key, int value) {
        getSharePreference(context).edit().putInt(key, value).apply();
    }

}

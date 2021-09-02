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

    public static final String IS_USE_SYSTEM_BRIGHTNESS = "is_use_system_brightness";
    public static final String SEEKBAR_BRIGHTNESS = "seekar_brightness";
    public static final String IS_VERTICAL_MODE = "is_vertical_mode";
    public static final String IS_JAPANESE_COMIC_MODE = "is_japanese_comic_mode";
    public static final String IS_KEEP_LIGHT_ALWAYS = "is_keep_light_always";
    public static final String IS_FULL_SCREEN = "is_full_screen";
    public static final String IS_SHOW_STATE = "is_show_state";


    public static final String USER_UID = "user_uid";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_NICKNAME = "user_nickname";
    public static final String USER_AVATAR = "user_avatar";




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

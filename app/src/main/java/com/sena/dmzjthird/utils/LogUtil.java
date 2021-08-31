package com.sena.dmzjthird.utils;

import android.util.Log;

import retrofit2.HttpException;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:12
 */
public class LogUtil {

    private static final boolean showLog = true;
    private static final String TAG = "jc";
    private static final int maxSize = 1024;

    private interface Print {
        void print(String tag, String message);
    }

    private static void printString(String s, Print print) {
        while (s.length() > 1024) {
            print.print(TAG, s.substring(0, 1024));
            s = s.substring(1024);
        }
        print.print(TAG, s);
    }

    public static void d(String s) {
        if (showLog) {
            printString(s, (a, b) -> Log.d(TAG, b));
        }
    }

    public static void e(String s) {
        if (showLog) {
            printString(s, (a, b) -> Log.e(TAG, b));
        }
    }

    public static void i(String s) {
        if (showLog) {
            printString(s, (a, b) -> Log.i(TAG, b));
        }
    }

    public static void v(String s) {
        if (showLog) {
            printString(s, (a, b) -> Log.v(TAG, b));
        }
    }

    public static void w(String s) {
        if (showLog) {
            printString(s, (a, b) -> Log.w(TAG, b));
        }
    }

    public static void internetError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            e("HttpError: " + ((HttpException) throwable).code());
        } else {
            e("OtherError: " + throwable.getMessage());
        }
    }

}

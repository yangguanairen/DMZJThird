package com.sena.dmzjthird.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * FileName: UriUtils
 * Author: JiaoCan
 * Date: 2021/12/10 18:47
 */

public class UriUtil {


    public static String getFileAbsolutePath(Context context, Uri uri) {

        if (context == null || uri == null) return null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

        }



        return null;
    }


    public static String getPath(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(column));
            }
            if (cursor != null) cursor.close();
        } catch (Exception e) {
            LogUtil.e("Error in getPath");
        }

        return path;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String getPath(Context context, Uri uri) {

        File file = null;
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {

            file = new File(uri.getPath());

        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {

            ContentResolver contentResolver = context.getContentResolver();

            try {
                Cursor cursor = contentResolver.query(uri, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    InputStream is = contentResolver.openInputStream(uri);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random()+1)*1000) + displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    FileUtils.copy(is, fos);
                    file  = cache;

                    cursor.close();
                    fos.close();
                    is.close();
                }
            } catch (Exception e) {
                LogUtil.e("Error in getPath at android api Q");
            }

        }

        return file == null ? null : file.getAbsolutePath();
    }




}

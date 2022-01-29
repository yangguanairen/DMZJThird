package com.sena.dmzjthird.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;


import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FileName: UriUtils
 * Author: JiaoCan
 * Date: 2021/12/10 18:47
 */

public class UriUtil {


    private static String getFilePath(Context context, Uri uri) {

        File file = null;
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {

            file = new File(uri.getPath());

        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {

            ContentResolver contentResolver = context.getContentResolver();

            try {
                Cursor cursor = contentResolver.query(uri, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

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


    public static File getFileByUri(Context context, Uri uri) {
        File file = null;
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {

            file = new File(uri.getPath());

        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {

            ContentResolver contentResolver = context.getContentResolver();
            @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, null, null);
            if (cursor.moveToFirst()) {
                @SuppressLint("Range")
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                try {
                    InputStream is = contentResolver.openInputStream(uri);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    FileUtils.copy(is, fos);
                    file = cache;
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return file;
    }

    public static Uri createFileInDownload(Context context, String fileName, String mimeType) {

        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        values.put(MediaStore.Downloads.MIME_TYPE, mimeType);
        values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/example");
        Uri insertUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

        @SuppressLint("Recycle") Cursor cursor = resolver.query(insertUri, null, null,null);
        if (cursor != null && cursor.getCount() == 1) resolver.delete(insertUri, null);
        return resolver.insert(insertUri, values);

    }



    public static Uri createFileInPhoto(Context context, String fileName, String mimeType) {

        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        Uri insertUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        @SuppressLint("Recycle") Cursor cursor = resolver.query(insertUri, null, null,null);
        if (cursor != null && cursor.getCount() == 1) resolver.delete(insertUri, null);
        return resolver.insert(insertUri, values);


    }
}

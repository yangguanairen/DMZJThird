package com.sena.dmzjthird.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
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

            if (DocumentsContract.isDocumentUri(context, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.ndroid.externalstorage.documents".equals(uri.getAuthority())) {

                    String[] split = docId.split(":");
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://download/public_downloads"), Long.valueOf(docId));
                    return getPath(context, contentUri, null, null);

                } else if ("com.android.providers.meida.documents".equals(uri.getAuthority())) {

                    String[] split = docId.split(":");
                    String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getPath(context, contentUri, selection, selectionArgs);

                }

            } else if ("content".equalsIgnoreCase(uri.getScheme())) {

                if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                    return uri.getLastPathSegment();
                }
                return getPath(context, uri, null, null);

            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getPath(context, uri);
        }

        return null;
    }


    private static String getPath(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        Cursor cursor;
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
    private static String getPath(Context context, Uri uri) {

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

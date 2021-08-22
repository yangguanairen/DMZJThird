package com.sena.dmzjthird.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import com.sena.dmzjthird.comic.view.AuthorInfoActivity;
import com.sena.dmzjthird.comic.view.ComicClassifyActivity;
import com.sena.dmzjthird.comic.view.ComicInfoActivity;
import com.sena.dmzjthird.comic.view.ComicTopicInfoActivity;
import com.sena.dmzjthird.comic.view.ComicViewActivity;

import java.io.Serializable;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/22
 * Time: 12:21
 */
public class IntentUtil {

    private static final String USER_ID = "user_id";
    private static final String COMIC_ID = "comic_id";
    private static final String CHAPTER_ID = "chapter_id";
    private static final String TOPIC_ID = "topic_id";
    private static final String AUTHOR_ID = "author_id";
    private static final String CLASSIFY_ID = "classify_id";

    private static final String SERIAL_DATA = "serial_data";


    public static void goToComicInfoActivity(Context context, String comicId) {

        Intent intent = new Intent(context, ComicInfoActivity.class);
        intent.putExtra(COMIC_ID, comicId);
        context.startActivity(intent);
    }

    public static void goToComicViewActivity(Context context, String comicId, String chapterId, Serializable data) {
        Intent intent = new Intent(context, ComicViewActivity.class);
        intent.putExtra(COMIC_ID, comicId);
        intent.putExtra(CHAPTER_ID, chapterId);
        intent.putExtra(SERIAL_DATA, data);
        context.startActivity(intent);
    }

    public static void goToComicClassifyActivity(Context context, String tagId) {
        Intent intent = new Intent(context, ComicClassifyActivity.class);
        intent.putExtra(CLASSIFY_ID, tagId);
        context.startActivity(intent);
    }

    public static void goToComicTopicInfoActivity(Context context, String topicId) {
        Intent intent = new Intent(context, ComicTopicInfoActivity.class);
        intent.putExtra(TOPIC_ID, topicId);
        context.startActivity(intent);
    }

    public static void goToAuthorInfoActivity(Context context, String authorId) {
        Intent intent = new Intent(context, AuthorInfoActivity.class);
        intent.putExtra(AUTHOR_ID, authorId);
        context.startActivity(intent);
    }

    public static void goToActivity(Context context, Class<?> target) {
        context.startActivity(new Intent(context, target));
    }

    public static String getComicId(Activity activity) {
        activity.getClass();
        return activity.getIntent().getStringExtra(COMIC_ID);
    }

    public static String getChapterId(Activity activity) {
        return activity.getIntent().getStringExtra(CHAPTER_ID);
    }

    public static Serializable getSerialize(Activity activity) {
        return activity.getIntent().getSerializableExtra(SERIAL_DATA);
    }

    public static String getClassifyTagId(Activity activity) {
        return activity.getIntent().getStringExtra(CLASSIFY_ID);
    }

    public static String getTopicId(Activity activity) {
        return activity.getIntent().getStringExtra(TOPIC_ID);
    }

    public static String getAuthorId(Activity activity) {
        return activity.getIntent().getStringExtra(AUTHOR_ID);
    }




}

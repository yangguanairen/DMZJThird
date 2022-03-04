package com.sena.dmzjthird.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sena.dmzjthird.comic.view.AuthorInfoActivity;
import com.sena.dmzjthird.comic.view.ComicFilterActivity;
import com.sena.dmzjthird.comic.view.ComicInfoActivity;
import com.sena.dmzjthird.comic.view.ComicTopicInfoActivity;
import com.sena.dmzjthird.comic.view.ComicViewActivity;
import com.sena.dmzjthird.comic.view.CommentReplyActivity;
import com.sena.dmzjthird.comic.view.UserInfoActivity;
import com.sena.dmzjthird.custom.LargeImageActivity;
import com.sena.dmzjthird.custom.WebViewActivity;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/22
 * Time: 12:21
 */
public class IntentUtil {

    private static final String USER_ID = "user_id";
    private static final String TOPIC_ID = "topic_id";
    private static final String AUTHOR_ID = "author_id";
    private static final String CLASSIFY_ID = "classify_id";

    private static final String OBJECT_ID = "object_id";
    private static final String OBJECT_COVER = "object_cover";
    private static final String OBJECT_NAME = "object_name";
    private static final String CHAPTER_ID = "chapter_id";
    private static final String CHAPTER_NAME = "object_chapter_name";

    private static final String TO_UID = "to_uid";
    private static final String TO_COMMENT_ID = "to_comment_id";
    private static final String USERNAME = "username";
    private static final String CONTENT = "content";
    private static final String COVER_URL = "cover_url";

    private static final String NEWS_ID = "news_id";
    private static final String NEWS_TITLE = "news_title";
    private static final String NEWS_COVER = "news_cover";
    private static final String NEWS_PAGE_URL = "news_page_url";


    public static void goToComicInfoActivity(Context context, String comicId) {

        Intent intent = new Intent(context, ComicInfoActivity.class);
        intent.putExtra(OBJECT_ID, comicId);
        context.startActivity(intent);
    }

    public static void goToComicViewActivity(Context context, String comicId, String cover, String comicName, String chapterId, String chapterName) {
        Intent intent = new Intent(context, ComicViewActivity.class);
        intent.putExtra(OBJECT_ID, comicId);
        intent.putExtra(OBJECT_COVER, cover);
        intent.putExtra(OBJECT_NAME, comicName);
        intent.putExtra(CHAPTER_ID, chapterId);
        intent.putExtra(CHAPTER_NAME, chapterName);
        context.startActivity(intent);
    }

    public static void goToComicClassifyActivity(Context context, String tagId) {
        Intent intent = new Intent(context, ComicFilterActivity.class);
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

    public static void goToUserInfoActivity(Context context, String userId) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(USER_ID, userId);
        context.startActivity(intent);
    }

    public static void goToCommentReplyActivity(Context context, int classify, String objId, String toUid, String toCommentId,
                                                 String toReplyUsername, String toReplyContent) {
        Intent intent = new Intent(context, CommentReplyActivity.class);
        intent.putExtra(OBJECT_ID, objId);
        intent.putExtra(TO_UID, toUid);
        intent.putExtra(TO_COMMENT_ID, toCommentId);
        intent.putExtra(USERNAME, toReplyUsername);
        intent.putExtra(CONTENT, toReplyContent);
        intent.putExtra(CLASSIFY_ID, classify);
        context.startActivity(intent);
    }

    public static void goToLargeImageActivity(Context context, String url) {
        Intent intent = new Intent(context, LargeImageActivity.class);
        intent.putExtra(COVER_URL, url);
        context.startActivity(intent);
    }

    public static void goToWebViewActivity(Context context, String newsId, String newsTitle, String newsCover, String newsPageUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(NEWS_ID, newsId);
        intent.putExtra(NEWS_TITLE, newsTitle);
        intent.putExtra(NEWS_COVER, newsCover);
        intent.putExtra(NEWS_PAGE_URL, newsPageUrl);
        context.startActivity(intent);
    }

    public static void goToActivity(Context context, Class<?> target) {
        context.startActivity(new Intent(context, target));
    }

    public static String getUserId(Activity activity) {
        return activity.getIntent().getStringExtra(USER_ID);
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

    public static String getToUid(Activity activity) {
        return activity.getIntent().getStringExtra(TO_UID);
    }

    public static String getToCommentId(Activity activity) {
        return activity.getIntent().getStringExtra(TO_COMMENT_ID);
    }

    public static String getUsername(Activity activity) {
        return activity.getIntent().getStringExtra(USERNAME);
    }

    public static String getContent(Activity activity) {
        return activity.getIntent().getStringExtra(CONTENT);
    }

    public static int getClassifyId(Activity activity) {
        return activity.getIntent().getIntExtra(CLASSIFY_ID, -1);
    }

    public static String getCoverUrl(Activity activity) {
        return activity.getIntent().getStringExtra(COVER_URL);
    }

    public static String getNewsId(Activity activity) {
        return activity.getIntent().getStringExtra(NEWS_ID);
    }

    public static String getNewsTitle(Activity activity) {
        return activity.getIntent().getStringExtra(NEWS_TITLE);
    }

    public static String getNewsCover(Activity activity) {
        return activity.getIntent().getStringExtra(NEWS_COVER);
    }

    public static String getNewsPageUrl(Activity activity) {
        return activity.getIntent().getStringExtra(NEWS_PAGE_URL);
    }

    public static String getObjectId(Activity activity) {
        return activity.getIntent().getStringExtra(OBJECT_ID);
    }

    public static String getObjectCover(Activity activity) {
        return activity.getIntent().getStringExtra(OBJECT_COVER);
    }

    public static String getObjectName(Activity activity) {
        return activity.getIntent().getStringExtra(OBJECT_NAME);
    }

    public static String getChapterId(Activity activity) {
        return activity.getIntent().getStringExtra(CHAPTER_ID);
    }

    public static String getChapterName(Activity activity) {
        return activity.getIntent().getStringExtra(CHAPTER_NAME);
    }

}

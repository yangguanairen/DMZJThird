package com.sena.dmzjthird.room;

import android.content.Context;

import androidx.room.Room;

import com.sena.dmzjthird.comic.bean.ComicDownloadBean;
import com.sena.dmzjthird.room.chapter.Chapter;
import com.sena.dmzjthird.room.comic.Comic;
import com.sena.dmzjthird.utils.LogUtil;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/14
 * Time: 20:25
 */
public class RoomHelper {

    private static RoomHelper INSTANCE;
    private static MyRoomDatabase database;


    public static final String STATUS_FINISH = "已完成";
    public static final String STATUS_DOWNLOADING = "下载中";
    public static final String STATUS_NOT_START = "未开始";

    public static RoomHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RoomHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RoomHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    private RoomHelper(Context context) {
        database = Room.databaseBuilder(context, MyRoomDatabase.class, "download.db").build();
    }

    public static List<Comic> getAllComic() {
        return database.comicDao().getAllComic();
    }

    public static synchronized void initComic(String comicId, String comicName, String comicCover) {
        Comic comic = database.comicDao().query(comicId);
        if (comic == null) {
            comic = new Comic();
            comic.comicId = comicId;
            comic.comicName = comicName;
            comic.comicCover = comicCover;
            comic.totalChapter = 0;
            comic.totalSize = 0;
            comic.finishChapter = 0;
            comic.finishSize = 0;
            database.comicDao().insert(comic);
        }
    }

    public static void updateComicTotalChapterAndFileSize(String comicId, long fileSize) {
        Comic comic = database.comicDao().query(comicId);
        if (comic == null) {
            LogUtil.e("Comic为null");
            return ;
        }
        database.comicDao().updateTotalChapterAndTotalSize(comicId, comic.totalChapter + 1, comic.totalSize + fileSize);
    }

    public static void updateComicFinishChapterAndFileSize(String comicId, long fileSize) {
        Comic comic = database.comicDao().query(comicId);
        if (comic == null) {
            LogUtil.e("Comic为null");
            return ;
        }
        database.comicDao().updateFinishChapterAndFinishSize(comicId, comic.finishChapter + 1, comic.finishSize + fileSize);
    }

    public static List<Chapter> getAllChapter(String comicId) {
        return database.chapterDao().getAllChapter(comicId);
    }

    public static void insertChapter(String comicId, ComicDownloadBean bean) {
        if (!queryChapter(comicId, bean.getId())) return ;
        Chapter chapter = new Chapter();
        chapter.comicId = comicId;
        chapter.chapterId = bean.getId();
        chapter.chapterName = bean.getChapter_name();
        chapter.folder_name = bean.getFolder();
        chapter.urlList = bean.getPage_url();
        chapter.totalPage = bean.getSum_pages();
        chapter.finishPage = 0;
        chapter.fileSize = bean.getFilesize();
        chapter.status = STATUS_NOT_START;
        database.chapterDao().insert(chapter);
    }

    public static void updateChapterFinishPage(String comicId, String chapterId, int finishPage) {
        database.chapterDao().updateFinishPage(comicId, chapterId, finishPage);
    }

    public static void updateChapterStatus(String comicId, String chapterId, String status) {
        database.chapterDao().updateChapterStatus(comicId, chapterId, status);
    }

    public static boolean queryChapter(String comicId, String chapterId) {
        Chapter chapter = database.chapterDao().queryChapter(comicId, chapterId);
        return chapter == null;
    }

}

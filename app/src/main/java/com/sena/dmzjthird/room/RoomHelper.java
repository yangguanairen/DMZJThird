package com.sena.dmzjthird.room;

import android.content.Context;

import androidx.room.Room;

import com.sena.dmzjthird.comic.bean.ComicChapterInfoBean;
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

    public List<Comic> getAllComic() {
        return database.comicDao().getAllComic();
    }

    public synchronized void initComic(String comicId, String comicName, String comicCover) {
        Comic comic = database.comicDao().query(comicId);
        if (comic == null) {
            comic = new Comic();
            comic.comicId = comicId;
            comic.comicName = comicName;
            comic.comicCover = comicCover;
            comic.totalChapter = 0;
            comic.finishChapter = 0;
            database.comicDao().insert(comic);
        }
    }

    public void updateComicTotalChapter(String comicId) {
        Comic comic = database.comicDao().query(comicId);
        if (comic == null) {
            LogUtil.e("Comic为null");
            return ;
        }
        database.comicDao().updateTotalChapterAndTotalSize(comicId, comic.totalChapter + 1);
    }

    public void updateComicFinishChapter(String comicId) {
        Comic comic = database.comicDao().query(comicId);
        if (comic == null) {
            LogUtil.e("Comic为null");
            return ;
        }
        database.comicDao().updateFinishChapterAndFinishSize(comicId, comic.finishChapter + 1);
    }

    public static List<Chapter> getAllChapter(String comicId) {
        return database.chapterDao().getAllChapter(comicId);
    }

    public void insertChapter(String comicName, ComicChapterInfoBean bean) {
        if (!queryChapter(bean.getComic_id(), bean.getChapter_id())) return ;
        Chapter chapter = new Chapter();
        chapter.comicId = bean.getComic_id();
        chapter.chapterId = bean.getChapter_id();
        chapter.chapterName = bean.getTitle();
        chapter.urlList = bean.getPage_url();
        chapter.folder_name = comicName + "/" + bean.getTitle();
        chapter.totalPage = bean.getPicnum();
        chapter.finishPage = 0;
        chapter.status = STATUS_NOT_START;
        database.chapterDao().insert(chapter);
    }

    public void updateChapterFinishPage(String comicId, String chapterId, int finishPage) {
        database.chapterDao().updateFinishPage(comicId, chapterId, finishPage);
    }

    public void updateChapterStatus(String comicId, String chapterId, String status) {
        database.chapterDao().updateChapterStatus(comicId, chapterId, status);
    }

    public boolean queryChapter(String comicId, String chapterId) {
        Chapter chapter = database.chapterDao().queryChapter(comicId, chapterId);
        return chapter == null;
    }

}

package com.sena.dmzjthird.download;

import android.content.Context;

import com.sena.dmzjthird.room.Chapter;
import com.sena.dmzjthird.room.Comic;
import com.sena.dmzjthird.room.MyRoomDatabase;
import com.sena.dmzjthird.room.RoomHelper;
import com.sena.dmzjthird.room.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: DownHelper
 * Author: JiaoCan
 * Date: 2022/3/16 11:15
 */

public class DownloadHelper {

    public static List<DownloadComicBean> getAllData(Context context) {
        MyRoomDatabase database = RoomHelper.getInstance(context);
        return getAllComic(database);
    }

    private static List<DownloadComicBean> getAllComic(MyRoomDatabase database) {
        List<DownloadComicBean> comicBeans = new ArrayList<>();

        List<Comic> comicList = database.comicDao().getAllComic();
        for (Comic comic: comicList) {
            DownloadComicBean comicBean = new DownloadComicBean();
            comicBean.setComicId(comic.comicId);
            comicBean.setComicName(comic.comicName);
            comicBean.setComicCover(comic.comicCover);

            List<DownloadChapterBean> chapterBeans = new ArrayList<>();
            comicBean.setTotalSize(getAllChapter(database, comic.comicId, chapterBeans));
            comicBean.setChapterList(chapterBeans);
            comicBean.setTotalChapter(chapterBeans.size());
            comicBeans.add(comicBean);
        }
        return comicBeans;
    }

    private static int getAllChapter(MyRoomDatabase database, String comicId, List<DownloadChapterBean> chapterBeans) {
        List<Chapter> chapterList = database.chapterDao().getAllChapter(comicId);

        int totalSize = 0;  // 漫画可下载的总容量
        for (Chapter chapter: chapterList) {
            DownloadChapterBean chapterBean = new DownloadChapterBean();
            chapterBean.setChapterId(chapter.chapterId);
            chapterBean.setChapterName(chapter.chapterName);
            chapterBean.setSortName(chapter.sortName);
            chapterBean.setTotalPages(chapter.chapterPages);
            chapterBean.setTotalSize(chapter.chapterSize);
            totalSize += chapter.chapterSize;
            chapterBean.setUrlList(getAllUrl(database, comicId, chapter.chapterId));
            chapterBeans.add(chapterBean);
        }

        return totalSize;
    }

    private static List<DownloadUrlBean> getAllUrl(MyRoomDatabase database, String comicId, String chapterId) {
        List<Url> urlList = database.urlDao().getAllUrl(comicId, chapterId);
        List<DownloadUrlBean> urlBeans = new ArrayList<>();
        for (Url url: urlList) {
            urlBeans.add(new DownloadUrlBean(url.imageUrl, url.status));
        }
        return urlBeans;
    }

}

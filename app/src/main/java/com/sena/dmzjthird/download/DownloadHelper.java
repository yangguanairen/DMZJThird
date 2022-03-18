package com.sena.dmzjthird.download;

import android.content.Context;

import com.sena.dmzjthird.room.chapter.Chapter;
import com.sena.dmzjthird.room.comic.Comic;
import com.sena.dmzjthird.room.MyRoomDatabase;
import com.sena.dmzjthird.room.RoomHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: DownHelper
 * Author: JiaoCan
 * Date: 2022/3/16 11:15
 */

public class DownloadHelper {

    public static List<DownloadBean> getAllData(Context context) {
        MyRoomDatabase database = RoomHelper.getInstance(context);

        List<DownloadBean> downloadBeans = new ArrayList<>();

        List<Comic> comicList = database.comicDao().getAllComic();
        for (Comic comic: comicList) {

            DownloadBean bean = new DownloadBean();
            bean.setComic(comic);

            List<Chapter> chapterList = database.chapterDao().getAllChapter(comic.comicId);
            bean.setChapterList(chapterList);

            downloadBeans.add(bean);
        }
        return downloadBeans;
    }


}

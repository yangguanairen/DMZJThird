package com.sena.dmzjthird.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.sena.dmzjthird.room.chapter.Chapter;
import com.sena.dmzjthird.room.chapter.ChapterDao;
import com.sena.dmzjthird.room.comic.Comic;
import com.sena.dmzjthird.room.comic.ComicDao;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/14
 * Time: 20:30
 */

@Database(entities = {Comic.class, Chapter.class}, version = 1, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {

    public abstract ComicDao comicDao();

    public abstract ChapterDao chapterDao();


}

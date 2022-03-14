package com.sena.dmzjthird.room;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/14
 * Time: 20:30
 */

@Database(entities = {Comic.class}, version = 1, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {

    public abstract ComicDao comicDao();



}

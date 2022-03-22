package com.sena.dmzjthird.room.comic;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/14
 * Time: 20:36
 */

@Entity(tableName = "comic")
public class Comic implements Serializable {

    @PrimaryKey @NonNull
    @ColumnInfo(name = "comic_id")
    public String comicId;

    @ColumnInfo(name = "comic_name")
    public String comicName;

    @ColumnInfo(name = "comic_cover")
    public String comicCover;

    @ColumnInfo(name = "total_chapter")
    public int totalChapter;

    @ColumnInfo(name = "finish_chapter")
    public int finishChapter;

}

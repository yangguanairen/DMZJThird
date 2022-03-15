package com.sena.dmzjthird.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * FileName: Url
 * Author: JiaoCan
 * Date: 2022/3/15 9:02
 */

@Entity(tableName = "url", primaryKeys = {"comic_id", "chapter_id", "image_url"})
public class Url {

    @NonNull
    @ColumnInfo(name = "comic_id")
    public String comicId;
    @NonNull
    @ColumnInfo(name = "chapter_id")
    public String chapterId;
    @NonNull
    @ColumnInfo(name = "image_url")
    public String imageUrl;
    @ColumnInfo(name = "status")
    public String status;

}

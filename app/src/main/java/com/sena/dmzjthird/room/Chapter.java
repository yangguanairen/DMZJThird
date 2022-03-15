package com.sena.dmzjthird.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * FileName: Chapter
 * Author: JiaoCan
 * Date: 2022/3/15 8:35
 */

@Entity(tableName = "chapter", primaryKeys = {"comic_id", "chapter_id"})
public class Chapter {

    @NonNull
    @ColumnInfo(name = "comic_id")
    public String comicId;
    @NonNull
    @ColumnInfo(name = "chapter_id")
    public String chapterId;
    @ColumnInfo(name = "chapter_name")
    public String chapterName;
    @ColumnInfo(name = "sort_name")
    public String sortName;
    @ColumnInfo(name = "chapter_pages")
    public int chapterPages;
    @ColumnInfo(name = "chapter_size")
    public int chapterSize;
    @ColumnInfo(name = "status")
    public String status;
}

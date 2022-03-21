package com.sena.dmzjthird.room.chapter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.util.List;

/**
 * FileName: Chapter
 * Author: JiaoCan
 * Date: 2022/3/15 8:35
 */

@Entity(tableName = "chapter", primaryKeys = {"comic_id", "chapter_id"})
@TypeConverters(StringItemConverter.class)
public class Chapter {

    @NonNull
    @ColumnInfo(name = "comic_id")
    public String comicId;

    @NonNull
    @ColumnInfo(name = "chapter_id")
    public String chapterId;

    @ColumnInfo(name = "chapter_name")
    public String chapterName;

    @ColumnInfo(name = "folder_name")
    public String folder_name;

    @ColumnInfo(name = "url_list")
    public List<String> urlList;

    @ColumnInfo(name = "total_page")
    public int totalPage;

    @ColumnInfo(name = "finish_page")
    public int finishPage;

    @ColumnInfo(name = "file_size")
    public long fileSize;

    @ColumnInfo(name = "status")
    public String status;
}



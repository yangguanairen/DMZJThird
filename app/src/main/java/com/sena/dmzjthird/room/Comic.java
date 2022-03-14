package com.sena.dmzjthird.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @PrimaryKey
    @NonNull
    public String comicId;
    public String comicName;
    public String comicCover;
    public String totalPages;
    public String totalSize;


}

package com.sena.dmzjthird.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/14
 * Time: 20:31
 */

@Dao
public interface ComicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addComic(Comic... comic);

    @Update
    void updateComic(Comic comic);

    @Delete
    void deleteComic(Comic comic);

    @Query("SELECT * FROM comic")
    List<Comic> getAllComic();

}

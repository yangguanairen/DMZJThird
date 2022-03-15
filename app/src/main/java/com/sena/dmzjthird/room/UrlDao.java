package com.sena.dmzjthird.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UrlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUrl(Url... url);

    @Query("select * from url where comic_id = :comicId and chapter_id = :chapterId")
    List<Url> getAllUrl(String comicId, String chapterId);

    @Query("delete from url where comic_id = :comicId and chapter_id = :chapterId")
    int deleteUrl(String comicId, String chapterId);

}

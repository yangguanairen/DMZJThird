package com.sena.dmzjthird.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * FileName: ChapterDao
 * Author: JiaoCan
 * Date: 2022/3/15 8:43
 */

@Dao
public interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addChapter(Chapter... chapter);

    @Query("delete from chapter where comic_id = :comicId and chapter_id = :chapterId")
    int deleteChapter(String comicId, String chapterId);

    @Query("delete from chapter where comic_id = :comicId")
    int deleteChapterByComicId(String comicId);

    @Query("select * from chapter where comic_id = :comicId")
    List<Chapter> getAllChapter(String comicId);

    @Query("select * from chapter where comic_id = :comicId and chapter_id = :chapterId")
    List<Chapter> queryChapter(String comicId, String chapterId);
}

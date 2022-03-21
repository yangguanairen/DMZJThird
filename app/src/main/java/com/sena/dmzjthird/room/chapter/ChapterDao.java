package com.sena.dmzjthird.room.chapter;

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
    void insert(com.sena.dmzjthird.room.chapter.Chapter... chapter);

    @Query("delete from chapter where comic_id = :comicId and chapter_id = :chapterId")
    int deleteSingleItem(String comicId, String chapterId);

    @Query("delete from chapter where comic_id = :comicId")
    int deleteByComicId(String comicId);

    @Query("select * from chapter where comic_id = :comicId")
    List<com.sena.dmzjthird.room.chapter.Chapter> getAllChapter(String comicId);

    @Query("select * from chapter where comic_id = :comicId and chapter_id = :chapterId limit 1")
    com.sena.dmzjthird.room.chapter.Chapter queryChapter(String comicId, String chapterId);

    @Query("update chapter set status = :status where comic_id = :comicId and chapter_id = :chapterId")
    int updateChapterStatus(String comicId, String chapterId, String status);

    @Query("update chapter set finish_page = :finishPage where comic_id = :comicId and chapter_id = :chapterId")
    int updateFinishPage(String comicId, String chapterId, int finishPage);
}

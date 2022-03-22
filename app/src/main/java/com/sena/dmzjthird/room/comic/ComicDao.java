package com.sena.dmzjthird.room.comic;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sena.dmzjthird.room.comic.Comic;

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
    void insert(Comic... comic);

    @Query("delete from comic where comic_id = :comicId")
    void deleteSingleItem(String comicId);

    @Query("SELECT * FROM comic")
    List<Comic> getAllComic();

    @Query("select * from comic where comic_id = :comicId limit 1")
    Comic query(String comicId);

    @Query("update comic set total_chapter = :totalChapter where comic_id = :comicId")
    int updateTotalChapterAndTotalSize(String comicId, int totalChapter);

    @Query("update comic set finish_chapter = :finishChapter where comic_id = :comicId")
    int updateFinishChapterAndFinishSize(String comicId, int finishChapter);
}

package com.sena.dmzjthird.download;

import com.sena.dmzjthird.room.chapter.Chapter;
import com.sena.dmzjthird.room.comic.Comic;

import java.util.List;

/**
 * FileName: DownloadBean
 * Author: JiaoCan
 * Date: 2022/3/18 11:07
 */

public class DownloadBean {

    private Comic comic;
    private List<Chapter> chapterList;


    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public List<Chapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }
}

package com.sena.dmzjthird.download;

import java.util.List;

/**
 * FileName: DownloadBean
 * Author: JiaoCan
 * Date: 2022/3/16 10:39
 */

public class DownloadComicBean {

    private String comicId;
    private String comicName;
    private String comicCover;
    private int totalChapter;
    private int totalSize;
    private List<DownloadChapterBean> chapterList;

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getComicCover() {
        return comicCover;
    }

    public void setComicCover(String comicCover) {
        this.comicCover = comicCover;
    }

    public int getTotalChapter() {
        return totalChapter;
    }

    public void setTotalChapter(int totalChapter) {
        this.totalChapter = totalChapter;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<DownloadChapterBean> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<DownloadChapterBean> chapterList) {
        this.chapterList = chapterList;
    }
}


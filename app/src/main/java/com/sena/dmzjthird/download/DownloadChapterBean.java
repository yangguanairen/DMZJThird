package com.sena.dmzjthird.download;

import java.util.List;

/**
 * FileName: DownloadChapterBena
 * Author: JiaoCan
 * Date: 2022/3/16 10:50
 */

public class DownloadChapterBean {

    private String chapterId;
    private String chapterName;
    private String sortName;
    private int totalPages;
    private int totalSize;
    private List<DownloadUrlBean> urlList;



    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<DownloadUrlBean> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<DownloadUrlBean> urlList) {
        this.urlList = urlList;
    }
}

package com.sena.dmzjthird.download;

import java.util.List;

/**
 * FileName: DownloadInfo
 * Author: JiaoCan
 * Date: 2022/3/15 11:15
 */

public class DownloadInfo {

    public static final long TOTAL_ERROR = -1;

    private List<DownloadUrlBean> urlList;
    private List<String> fileNameList;
    private int downloadedNum;
    private long total;

    private String comicName;
    private String chapterName;
    private String tag;

    public DownloadInfo(List<DownloadUrlBean> urlList) {
        this.urlList = urlList;
    }

    public static long getTotalError() {
        return TOTAL_ERROR;
    }

    public List<DownloadUrlBean> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<DownloadUrlBean> urlList) {
        this.urlList = urlList;
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }

    public int getDownloadedNum() {
        return downloadedNum;
    }

    public void setDownloadedNum(int downloadedNum) {
        this.downloadedNum = downloadedNum;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

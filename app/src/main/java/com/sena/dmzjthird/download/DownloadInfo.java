package com.sena.dmzjthird.download;

import java.util.List;

/**
 * FileName: DownloadInfo
 * Author: JiaoCan
 * Date: 2022/3/15 11:15
 */

public class DownloadInfo {

    public static final long TOTAL_ERROR = -1;

    private List<String> urlList;
    private List<String> nameList;
    private int totalPage;
    private int finishPage;

    private String tag; // 唯一标识符

    public static long getTotalError() {
        return TOTAL_ERROR;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getFinishPage() {
        return finishPage;
    }

    public void setFinishPage(int finishPage) {
        this.finishPage = finishPage;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

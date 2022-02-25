package com.sena.dmzjthird.custom;

/**
 * FileName: AutoBannerData
 * Author: JiaoCan
 * Date: 2022/2/25 14:29
 */

public class AutoBannerData {

    private String objectId;
    private String title;
    private String coverUrl;
    private int type;      // 0：漫画；1：小说；2：新闻
    private String pageUrl;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}

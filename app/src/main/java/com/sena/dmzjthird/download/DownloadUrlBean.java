package com.sena.dmzjthird.download;

/**
 * FileName: DownloadUrlBean
 * Author: JiaoCan
 * Date: 2022/3/16 10:51
 */

public class DownloadUrlBean {

    private String url;
    private String status;

    public DownloadUrlBean() {
    }

    public DownloadUrlBean(String url, String status) {
        this.url = url;
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.sena.dmzjthird.account.bean;

/**
 * FileName: UserHistoryBean
 * Author: JiaoCan
 * Date: 2022/3/4 14:18
 */

public class UserHistoryBean {

    private long id;
    private long uid;
    private String comicId;
    private String cover;
    private String comicName;
    private String chapterId;
    private String chapterName;
    private long cTime;
    private long aTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

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

    public long getcTime() {
        return cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
    }

    public long getaTime() {
        return aTime;
    }

    public void setaTime(long aTime) {
        this.aTime = aTime;
    }
}

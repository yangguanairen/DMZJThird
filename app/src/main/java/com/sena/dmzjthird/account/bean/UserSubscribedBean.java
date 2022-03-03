package com.sena.dmzjthird.account.bean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/15
 * Time: 11:50
 */
public class UserSubscribedBean {

    private long id;
    private long uid;
    private String comicId;
    private String cover;
    private String comicName;
    private String author;
    private long cTime;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getcTime() {
        return cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
    }
}

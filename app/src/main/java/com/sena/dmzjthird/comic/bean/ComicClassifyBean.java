package com.sena.dmzjthird.comic.bean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/14
 * Time: 11:48
 */
public class ComicClassifyBean {
    private String id;
    private String title;
    private String authors;
    private String status;
    private String cover;
    private String types;
    private long last_updatetime;
    private String num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public long getLast_updatetime() {
        return last_updatetime;
    }

    public void setLast_updatetime(long last_updatetime) {
        this.last_updatetime = last_updatetime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}

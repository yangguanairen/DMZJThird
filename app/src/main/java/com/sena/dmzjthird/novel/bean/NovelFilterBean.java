package com.sena.dmzjthird.novel.bean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/27
 * Time: 10:55
 */
public class NovelFilterBean {

    private String cover;
    private String name;
    private String authors;
    private String id;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

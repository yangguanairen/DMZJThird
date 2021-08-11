package com.sena.dmzjthird.comic.bean;

public class ComicSubscribeRankBean {

    private String id;
    private String name;
    private String comic_py;
    private String cover;
    private String authors;
    private String types;
    private long last_updatetime;
    private int sub_num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComic_py() {
        return comic_py;
    }

    public void setComic_py(String comic_py) {
        this.comic_py = comic_py;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
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

    public int getSub_num() {
        return sub_num;
    }

    public void setSub_num(int sub_num) {
        this.sub_num = sub_num;
    }
}

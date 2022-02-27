package com.sena.dmzjthird.novel.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/26
 * Time: 12:44
 */
public class NovelRankBean {

    private String id;
    private long last_update_time;
    private String name;
    private List<String> types;
    private String cover;
    private String authors;
    private String last_update_chapter_name;
    private int top;
    private int subscribe_amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(long last_update_time) {
        this.last_update_time = last_update_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
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

    public String getLast_update_chapter_name() {
        return last_update_chapter_name;
    }

    public void setLast_update_chapter_name(String last_update_chapter_name) {
        this.last_update_chapter_name = last_update_chapter_name;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getSubscribe_amount() {
        return subscribe_amount;
    }

    public void setSubscribe_amount(int subscribe_amount) {
        this.subscribe_amount = subscribe_amount;
    }
}

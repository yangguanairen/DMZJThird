package com.sena.dmzjthird.novel.bean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/26
 * Time: 12:33
 */
public class NovelCategoryBean {

    private int tag_id;
    private String title;
    private String cover;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}

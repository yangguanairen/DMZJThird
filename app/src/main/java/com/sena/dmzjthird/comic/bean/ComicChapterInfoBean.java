package com.sena.dmzjthird.comic.bean;

import java.util.List;

/**
 * FileName: ComicViewBean
 * Author: JiaoCan
 * Date: 2022/2/24 15:26
 */

public class ComicChapterInfoBean {

    private String chapter_id;
    private String comic_id;
    private String title;
    private int chapter_order;
    private int direction;
    private List<String> page_url;
    private int picnum;
    private int comment_count;

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChapter_order() {
        return chapter_order;
    }

    public void setChapter_order(int chapter_order) {
        this.chapter_order = chapter_order;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public List<String> getPage_url() {
        return page_url;
    }

    public void setPage_url(List<String> page_url) {
        this.page_url = page_url;
    }

    public int getPicnum() {
        return picnum;
    }

    public void setPicnum(int picnum) {
        this.picnum = picnum;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }
}

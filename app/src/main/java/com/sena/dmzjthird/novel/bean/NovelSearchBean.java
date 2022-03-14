package com.sena.dmzjthird.novel.bean;

/**
 * FileName: NovelSearchBean
 * Author: JiaoCan
 * Date: 2022/3/14 11:08
 */

public class NovelSearchBean {

    private String _biz;
    private long addtime;
    private String authors;
    private int copyright;
    private String cover;
    private int hidden;
    private int host_hits;
    private String last_name;
    private int status;
    private String title;
    private String types;
    private String id;

    public String get_biz() {
        return _biz;
    }

    public void set_biz(String _biz) {
        this._biz = _biz;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public int getHost_hits() {
        return host_hits;
    }

    public void setHost_hits(int host_hits) {
        this.host_hits = host_hits;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

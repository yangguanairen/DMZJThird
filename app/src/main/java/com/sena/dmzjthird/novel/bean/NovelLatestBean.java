package com.sena.dmzjthird.novel.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/26
 * Time: 11:44
 */
public class NovelLatestBean {

    private String id;
    private String status;
    private String name;
    private String authors;
    private String cover;
    private List<String> types;
    private String last_update_chapter_id;
    private String last_update_volume_id;
    private String last_update_volume_name;
    private String last_update_chapter_name;
    private long last_update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getLast_update_chapter_id() {
        return last_update_chapter_id;
    }

    public void setLast_update_chapter_id(String last_update_chapter_id) {
        this.last_update_chapter_id = last_update_chapter_id;
    }

    public String getLast_update_volume_id() {
        return last_update_volume_id;
    }

    public void setLast_update_volume_id(String last_update_volume_id) {
        this.last_update_volume_id = last_update_volume_id;
    }

    public String getLast_update_volume_name() {
        return last_update_volume_name;
    }

    public void setLast_update_volume_name(String last_update_volume_name) {
        this.last_update_volume_name = last_update_volume_name;
    }

    public String getLast_update_chapter_name() {
        return last_update_chapter_name;
    }

    public void setLast_update_chapter_name(String last_update_chapter_name) {
        this.last_update_chapter_name = last_update_chapter_name;
    }

    public long getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(long last_update_time) {
        this.last_update_time = last_update_time;
    }
}

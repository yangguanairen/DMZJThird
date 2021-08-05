package com.sena.dmzjthird.comic.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:00
 */
public class ComicRecommendBean {
    private int category_id;
    private String title;
    private int sort;
    private List<Data> data;

    public ComicRecommendBean(int category_id, String title, int sort, List<Data> data) {
        this.category_id = category_id;
        this.title = title;
        this.sort = sort;
        this.data = data;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        private String cover;
        private String title;
        private String sub_title;
        private int type;
        private String url;
        private String obj_id;
        private String status;

        public Data(String cover, String title, String sub_title, int type, String url, String obj_id, String status) {
            this.cover = cover;
            this.title = title;
            this.sub_title = sub_title;
            this.type = type;
            this.url = url;
            this.obj_id = obj_id;
            this.status = status;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getObj_id() {
            return obj_id;
        }

        public void setObj_id(String obj_id) {
            this.obj_id = obj_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

package com.sena.dmzjthird.comic.bean;

import java.util.List;

/**
 * FileName: ComicRecommendNewBean
 * Author: JiaoCan
 * Date: 2022/3/2 13:58
 */

public class ComicRecommendNewBean {

    private int category_id;
    private String title;
    private int sort;
    private List<ComicRecommendItem> data;

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

    public List<ComicRecommendItem> getData() {
        return data;
    }

    public void setData(List<ComicRecommendItem> data) {
        this.data = data;
    }

    public static class ComicRecommendItem {
        private String cover;
        private String title;
        private String sub_title;
        private int type;
        private String url;
        private String obj_id;
        private String status;

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

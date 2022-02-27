package com.sena.dmzjthird.novel.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/26
 * Time: 15:56
 */
public class NovelRecommendBean {

    private int category_id;
    private int sort;
    private String title;
    private List<NovelRecommendData> data;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NovelRecommendData> getData() {
        return data;
    }

    public void setData(List<NovelRecommendData> data) {
        this.data = data;
    }

    public class NovelRecommendData {

        private String id;
        private String obj_id;
        private String title;
        private String cover;
        private String url;
        private int type;
        private String sub_title;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObj_id() {
            return obj_id;
        }

        public void setObj_id(String obj_id) {
            this.obj_id = obj_id;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}

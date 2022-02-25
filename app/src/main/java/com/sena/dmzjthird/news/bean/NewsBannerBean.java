package com.sena.dmzjthird.news.bean;

import java.util.List;

/**
 * FileName: NewsBannerBean
 * Author: JiaoCan
 * Date: 2022/2/25 15:03
 */

public class NewsBannerBean {

    private int code;
    private String msg;
    private List<NewsBannerData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewsBannerData> getData() {
        return data;
    }

    public void setData(List<NewsBannerData> data) {
        this.data = data;
    }

    public class NewsBannerData {
        private int id;
        private String title;
        private String pic_url;
        private String object_id;
        private String object_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }

        public String getObject_url() {
            return object_url;
        }

        public void setObject_url(String object_url) {
            this.object_url = object_url;
        }
    }





}

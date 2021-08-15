package com.sena.dmzjthird.comic.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/15
 * Time: 20:58
 */
public class ComicTopicInfoBean {

    private int code;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String mobile_header_pic;
        private String title;
        private String page_url;
        private String description;
        private List<Comics> comics;
        private int comment_amount;

        public String getMobile_header_pic() {
            return mobile_header_pic;
        }

        public void setMobile_header_pic(String mobile_header_pic) {
            this.mobile_header_pic = mobile_header_pic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPage_url() {
            return page_url;
        }

        public void setPage_url(String page_url) {
            this.page_url = page_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Comics> getComics() {
            return comics;
        }

        public void setComics(List<Comics> comics) {
            this.comics = comics;
        }

        public int getComment_amount() {
            return comment_amount;
        }

        public void setComment_amount(int comment_amount) {
            this.comment_amount = comment_amount;
        }
    }

    public class Comics {
        private String cover;
        private String recommend_brief;
        private String recommend_reason;
        private String id;
        private String name;
        private String alias_name;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getRecommend_brief() {
            return recommend_brief;
        }

        public void setRecommend_brief(String recommend_brief) {
            this.recommend_brief = recommend_brief;
        }

        public String getRecommend_reason() {
            return recommend_reason;
        }

        public void setRecommend_reason(String recommend_reason) {
            this.recommend_reason = recommend_reason;
        }

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

        public String getAlias_name() {
            return alias_name;
        }

        public void setAlias_name(String alias_name) {
            this.alias_name = alias_name;
        }
    }
}

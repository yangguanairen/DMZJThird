package com.sena.dmzjthird.comic.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: ComicRecommendLikeBean
 * Author: JiaoCan
 * Date: 2022/3/2 14:31
 */

public class ComicRecommendLikeBean {

    private int code;
    private String msg;
    private ComicRecommendData data;

    public List<ComicRecommendNewBean.ComicRecommendItem> convertToRecommendItem() {
        List<ComicRecommendNewBean.ComicRecommendItem> itemList = new ArrayList<>();
        for (ComicRecommendLikeItem item: data.getData()) {
            ComicRecommendNewBean.ComicRecommendItem recommendItem = new ComicRecommendNewBean.ComicRecommendItem();
            recommendItem.setObj_id(item.getId());
            recommendItem.setCover(item.getCover());
            recommendItem.setTitle(item.getTitle());
            recommendItem.setSub_title(item.getAuthor());
            recommendItem.setStatus(item.getStatus());
            itemList.add(recommendItem);
        }
        return itemList;
    }

    public ComicRecommendNewBean convertToRecommendBean() {
        ComicRecommendNewBean bean = new ComicRecommendNewBean();
        bean.setCategory_id(50);
        bean.setTitle("猜你喜欢");
        bean.setSort(data.sort);
        bean.setData(convertToRecommendItem());
        return bean;
    }

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

    public ComicRecommendData getData() {
        return data;
    }

    public void setData(ComicRecommendData data) {
        this.data = data;
    }

    public class ComicRecommendData {
        private int category_id;
        private String title;
        private int sort;
        private List<ComicRecommendLikeItem> data;

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

        public List<ComicRecommendLikeItem> getData() {
            return data;
        }

        public void setData(List<ComicRecommendLikeItem> data) {
            this.data = data;
        }
    }


    public class ComicRecommendLikeItem {
        private String id;
        private String title;
        private String author;
        private String status;
        private String cover;
        private long num;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public long getNum() {
            return num;
        }

        public void setNum(long num) {
            this.num = num;
        }
    }




}

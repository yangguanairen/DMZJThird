package com.sena.dmzjthird.comic.bean;

import java.util.List;

public class ComicInfoBean {

    private String title;

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public class Data {

        private String id;
        private String comic_id;
        private String chapter_name;
        private int chapter_order;
        private int chaptertype;
        private String title;
        private int sort;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getComic_id() {
            return comic_id;
        }

        public void setComic_id(String comic_id) {
            this.comic_id = comic_id;
        }

        public String getChapter_name() {
            return chapter_name;
        }

        public void setChapter_name(String chapter_name) {
            this.chapter_name = chapter_name;
        }

        public int getChapter_order() {
            return chapter_order;
        }

        public void setChapter_order(int chapter_order) {
            this.chapter_order = chapter_order;
        }

        public int getChaptertype() {
            return chaptertype;
        }

        public void setChaptertype(int chaptertype) {
            this.chaptertype = chaptertype;
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
    }

}

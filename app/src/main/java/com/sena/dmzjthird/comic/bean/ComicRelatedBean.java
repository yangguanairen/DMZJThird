package com.sena.dmzjthird.comic.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 20:05
 */
public class ComicRelatedBean {


    private List<Author_Comics> author_comics;
    private List<Data> theme_comics;
    private List<Novel> novel;

    public List<Author_Comics> getAuthor_comics() {
        return author_comics;
    }

    public void setAuthor_comics(List<Author_Comics> author_comics) {
        this.author_comics = author_comics;
    }

    public List<Data> getTheme_comics() {
        return theme_comics;
    }

    public void setTheme_comics(List<Data> theme_comics) {
        this.theme_comics = theme_comics;
    }

    public List<Novel> getNovel() {
        return novel;
    }

    public void setNovel(List<Novel> novel) {
        this.novel = novel;
    }

    public static class Author_Comics {

        private String author_name;
        private String author_id;
        private List<Data> data;

        public Author_Comics(String author_name, String author_id, List<Data> data) {
            this.author_name = author_name;
            this.author_id = author_id;
            this.data = data;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

    }

//    public class Theme_Comics {
//
//        private String id;
//        private String name;
//        private String cover;
//        private String status;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getCover() {
//            return cover;
//        }
//
//        public void setCover(String cover) {
//            this.cover = cover;
//        }
//
//        public String getStatus() {
//            return status;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//    }

    public class Novel {

    }

    public class Data {
        private String id;
        private String name;
        private String cover;
        private String status;

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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}

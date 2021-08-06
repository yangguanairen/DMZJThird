package com.sena.dmzjthird.comic.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/6
 * Time: 20:56
 */
public class ComicRecommendChildBean3 {
    private int code;
    private String msg;
    private Data1 data;

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

    public Data1 getData() {
        return data;
    }

    public void setData(Data1 data) {
        this.data = data;
    }

    public class Data1 {
        private int category_id;
        private String title;
        private int sort;
        private List<Data> data;

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

        public class Data {
            private String authors;
            private String cover;
            private String id;
            private String status;
            private int sub_read;
            private String title;
            private String uid;

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

            public int getSub_read() {
                return sub_read;
            }

            public void setSub_read(int sub_read) {
                this.sub_read = sub_read;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }
}

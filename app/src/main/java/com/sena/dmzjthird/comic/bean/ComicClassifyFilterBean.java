package com.sena.dmzjthird.comic.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/14
 * Time: 16:41
 */
public class ComicClassifyFilterBean {

    private String title;
    public List<Items> items;

    public ComicClassifyFilterBean(String title, List<Items> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public static class Items {
        private String tag_id;
        private String tag_name;

        public Items(String tag_id, String tag_name) {
            this.tag_id = tag_id;
            this.tag_name = tag_name;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }
    }

}

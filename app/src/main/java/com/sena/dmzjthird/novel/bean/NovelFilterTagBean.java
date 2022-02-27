package com.sena.dmzjthird.novel.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/27
 * Time: 10:52
 */
public class NovelFilterTagBean {

    private String title;
    private List<NovelFilterItem> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NovelFilterItem> getItems() {
        return items;
    }

    public void setItems(List<NovelFilterItem> items) {
        this.items = items;
    }

    public static class NovelFilterItem {

        private int tag_id;
        private String tag_name;

        public NovelFilterItem(int tag_id, String tag_name) {
            this.tag_id = tag_id;
            this.tag_name = tag_name;
        }

        public int getTag_id() {
            return tag_id;
        }

        public void setTag_id(int tag_id) {
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

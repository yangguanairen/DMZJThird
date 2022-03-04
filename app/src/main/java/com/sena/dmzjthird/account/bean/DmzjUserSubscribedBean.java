package com.sena.dmzjthird.account.bean;

/**
 * FileName: DmzjUserSubscribedBean
 * Author: JiaoCan
 * Date: 2022/3/4 13:37
 */

public class DmzjUserSubscribedBean {

    private String name;
    private String sub_update;
    private String sub_img;
    private long sub_uptime;
    private String sub_first_letter;
    private int sub_readed;
    private String id;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_update() {
        return sub_update;
    }

    public void setSub_update(String sub_update) {
        this.sub_update = sub_update;
    }

    public String getSub_img() {
        return sub_img;
    }

    public void setSub_img(String sub_img) {
        this.sub_img = sub_img;
    }

    public long getSub_uptime() {
        return sub_uptime;
    }

    public void setSub_uptime(long sub_uptime) {
        this.sub_uptime = sub_uptime;
    }

    public String getSub_first_letter() {
        return sub_first_letter;
    }

    public void setSub_first_letter(String sub_first_letter) {
        this.sub_first_letter = sub_first_letter;
    }

    public int getSub_readed() {
        return sub_readed;
    }

    public void setSub_readed(int sub_readed) {
        this.sub_readed = sub_readed;
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
}

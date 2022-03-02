package com.sena.dmzjthird.comic.bean;

/**
 * FileName: ComicRecommendOtherBean
 * Author: JiaoCan
 * Date: 2022/3/2 14:46
 */

public class ComicRecommendOtherBean {

    private int code;
    private String msg;
    private ComicRecommendNewBean data;

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

    public ComicRecommendNewBean getData() {
        return data;
    }

    public void setData(ComicRecommendNewBean data) {
        this.data = data;
    }
}

package com.sena.dmzjthird.account.bean;

/**
 * FileName: ResultBean
 * Author: JiaoCan
 * Date: 2022/1/21 9:41
 */

public class ResultBean {

    private int code;
    private String content;

    public ResultBean(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

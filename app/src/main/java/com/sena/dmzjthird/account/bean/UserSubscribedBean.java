package com.sena.dmzjthird.account.bean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/15
 * Time: 11:50
 */
public class UserSubscribedBean {

    private Long id;
    private Long uid;
    private String objectId;
    private String objectCover;
    private String objectName;
    private String author;
    private int objectType;
    private Long cTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectCover() {
        return objectCover;
    }

    public void setObjectCover(String objectCover) {
        this.objectCover = objectCover;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public Long getcTime() {
        return cTime;
    }

    public void setcTime(Long cTime) {
        this.cTime = cTime;
    }
}

package com.sena.dmzjthird.account.bean;

/**
 * FileName: UserHistoryBean
 * Author: JiaoCan
 * Date: 2022/3/4 14:18
 */

public class UserHistoryBean {

    private long id;
    private long uid;
    private String objectId;
    private String objectCover;
    private String objectName;
    private int objectType;
    private int volumeId;       // 轻小说卷id
    private String volumeName;  // 轻小说卷名
    private int chapterId;
    private String chapterName;
    private long cTime;
    private long aTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
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

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public int getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(int volumeId) {
        this.volumeId = volumeId;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public long getcTime() {
        return cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
    }

    public long getaTime() {
        return aTime;
    }

    public void setaTime(long aTime) {
        this.aTime = aTime;
    }
}

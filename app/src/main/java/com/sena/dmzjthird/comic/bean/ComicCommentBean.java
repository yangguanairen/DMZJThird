package com.sena.dmzjthird.comic.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/23
 * Time: 14:46
 */
public class ComicCommentBean {


    private String id;                  // 评论id
    private String obj_id;              // 漫画id
    private String content;             // 文字内容
    private List<String> upload_images;       // 图片内容, 只能上传一张照片
    private int like_amount;            // 点赞数量
    private int is_passed;              // 1: 表示未删除; 2: 表示评论已删除; 很奇怪，删除了还把原评论传到客户端
    private int masterCommentNum;       // 回复数量
    private List<MasterComment> masterComments = new ArrayList<>();
    private long create_time;           // 创建时间
    private String sender_uid;          // 发送人uid
    private String nickname;            // 发送人昵称
    private String cover;               // 发送人头像
    private int sex;                    // 发送人性别, 1: 男性; 2: 女性
    private String origin_comment_id;
    private String to_comment_id;
    private String to_uid;
    private int is_goods;

    public ComicCommentBean() {

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObj_id() {
        return obj_id;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }

    public String getTo_comment_id() {
        return to_comment_id;
    }

    public void setTo_comment_id(String to_comment_id) {
        this.to_comment_id = to_comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public int getIs_goods() {
        return is_goods;
    }

    public void setIs_goods(int is_goods) {
        this.is_goods = is_goods;
    }

    public int getIs_passed() {
        return is_passed;
    }

    public void setIs_passed(int is_passed) {
        this.is_passed = is_passed;
    }

    public List<String> getUpload_images() {
        return upload_images;
    }

    public void setUpload_images(List<String> upload_images) {
        this.upload_images = upload_images;
    }

    public int getLike_amount() {
        return like_amount;
    }

    public void setLike_amount(int like_amount) {
        this.like_amount = like_amount;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getOrigin_comment_id() {
        return origin_comment_id;
    }

    public void setOrigin_comment_id(String origin_comment_id) {
        this.origin_comment_id = origin_comment_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getMasterCommentNum() {
        return masterCommentNum;
    }

    public void setMasterCommentNum(int masterCommentNum) {
        this.masterCommentNum = masterCommentNum;
    }

    public List<MasterComment> getMasterComments() {
        return masterComments;
    }

    public void setMasterComments(List<MasterComment> masterComments) {
        this.masterComments = masterComments;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public static class MasterComment {
        private String id;            // 评论id
        private String obj_id;        // 漫画id
        private String content;       // 文字内容
        private List<String> upload_images; // 图片内容
        private int like_amount;      // 点赞数量
        private int is_passed;        // 是否删除, 1: 没有; 2: 删除
        private long create_time;     // 创建时间
        private String sender_uid;    // 发送人id
        private String nickname;      // 发送人昵称
        private String cover;         // 发送人头像
        private int sex;              // 发送人性别, 1: 男性; 2: 女性
        private String to_uid;
        private String to_comment_id;
        private String origin_comment_id;
        private int is_goods;

        public MasterComment() {

        }

        public int getIs_passed() {
            return is_passed;
        }

        public void setIs_passed(int is_passed) {
            this.is_passed = is_passed;
        }

        public String getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(String to_uid) {
            this.to_uid = to_uid;
        }

        public String getTo_comment_id() {
            return to_comment_id;
        }

        public void setTo_comment_id(String to_comment_id) {
            this.to_comment_id = to_comment_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getUpload_images() {
            return upload_images;
        }

        public void setUpload_images(List<String> upload_images) {
            this.upload_images = upload_images;
        }

        public int getLike_amount() {
            return like_amount;
        }

        public void setLike_amount(int like_amount) {
            this.like_amount = like_amount;
        }

        public String getSender_uid() {
            return sender_uid;
        }

        public void setSender_uid(String sender_uid) {
            this.sender_uid = sender_uid;
        }

        public String getOrigin_comment_id() {
            return origin_comment_id;
        }

        public void setOrigin_comment_id(String origin_comment_id) {
            this.origin_comment_id = origin_comment_id;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getIs_goods() {
            return is_goods;
        }

        public void setIs_goods(int is_goods) {
            this.is_goods = is_goods;
        }

        public String getObj_id() {
            return obj_id;
        }

        public void setObj_id(String obj_id) {
            this.obj_id = obj_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}

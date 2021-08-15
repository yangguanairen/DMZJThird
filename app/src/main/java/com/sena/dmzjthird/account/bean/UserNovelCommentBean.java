package com.sena.dmzjthird.account.bean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/15
 * Time: 17:38
 */
public class UserNovelCommentBean {

    private String comment_id;
    private String content;
    private String origin_comment_id;
    private int like_amount;
    private long create_time;
    private String obj_id;
    private int reply_amount;
    private String to_comment_id;
    private String obj_name;
    private String obj_cover;
    private String page_url;
    private MasterComment masterComment;

    public MasterComment getMasterComment() {
        return masterComment;
    }

    public void setMasterComment(MasterComment masterComment) {
        this.masterComment = masterComment;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrigin_comment_id() {
        return origin_comment_id;
    }

    public void setOrigin_comment_id(String origin_comment_id) {
        this.origin_comment_id = origin_comment_id;
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

    public String getObj_id() {
        return obj_id;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }

    public int getReply_amount() {
        return reply_amount;
    }

    public void setReply_amount(int reply_amount) {
        this.reply_amount = reply_amount;
    }

    public String getTo_comment_id() {
        return to_comment_id;
    }

    public void setTo_comment_id(String to_comment_id) {
        this.to_comment_id = to_comment_id;
    }

    public String getObj_name() {
        return obj_name;
    }

    public void setObj_name(String obj_name) {
        this.obj_name = obj_name;
    }

    public String getObj_cover() {
        return obj_cover;
    }

    public void setObj_cover(String obj_cover) {
        this.obj_cover = obj_cover;
    }

    public String getPage_url() {
        return page_url;
    }

    public void setPage_url(String page_url) {
        this.page_url = page_url;
    }

    public class MasterComment {
        private String id;
        private int is_pressed;
        private int status;
        private int is_goods;
        private String upload_image;
        private String obj_id;
        private String content;
        private String sender_uid;
        private int like_amount;
        private long create_time;
        private String to_uid;
        private String origin_comment_id;
        private int reply_amount;
        private String cover;
        private String nickname;
        private int hot_comment_amount;
        private int sex;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIs_pressed() {
            return is_pressed;
        }

        public void setIs_pressed(int is_pressed) {
            this.is_pressed = is_pressed;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_goods() {
            return is_goods;
        }

        public void setIs_goods(int is_goods) {
            this.is_goods = is_goods;
        }

        public String getUpload_image() {
            return upload_image;
        }

        public void setUpload_image(String upload_image) {
            this.upload_image = upload_image;
        }

        public String getObj_id() {
            return obj_id;
        }

        public void setObj_id(String obj_id) {
            this.obj_id = obj_id;
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

        public int getReply_amount() {
            return reply_amount;
        }

        public void setReply_amount(int reply_amount) {
            this.reply_amount = reply_amount;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getHot_comment_amount() {
            return hot_comment_amount;
        }

        public void setHot_comment_amount(int hot_comment_amount) {
            this.hot_comment_amount = hot_comment_amount;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}

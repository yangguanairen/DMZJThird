package com.sena.dmzjthird.account.bean;

/**
 * FileName: UserResultBean
 * Author: JiaoCan
 * Date: 2022/1/21 10:22
 */

public class UserResultBean {

    private int code;
    private User user;

    public UserResultBean(int code, User user) {
        this.code = code;
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User {

        private long uid;
        private String nickname;
        private String password;
        private String avatar;
        private String email;
        private String sex;
        private String age;

        public User(long uid, String nickname, String password, String avatar, String email, String sex, String age) {
            this.uid = uid;
            this.nickname = nickname;
            this.password = password;
            this.avatar = avatar;
            this.email = email;
            this.sex = sex;
            this.age = age;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}

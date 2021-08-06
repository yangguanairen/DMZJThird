package com.sena.dmzjthird.account.bean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/6
 * Time: 17:48
 */
public class LoginBean {

    private int result;
    private String msg;
    private Data data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String uid;
        private String nickname;
        private String dmzj_token;
        private String photo;
        private String bind_phone;
        private String email;
        private String passwd;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getDmzj_token() {
            return dmzj_token;
        }

        public void setDmzj_token(String dmzj_token) {
            this.dmzj_token = dmzj_token;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getBind_phone() {
            return bind_phone;
        }

        public void setBind_phone(String bind_phone) {
            this.bind_phone = bind_phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
    }
}

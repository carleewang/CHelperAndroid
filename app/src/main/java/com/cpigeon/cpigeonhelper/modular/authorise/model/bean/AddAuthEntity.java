package com.cpigeon.cpigeonhelper.modular.authorise.model.bean;

/**
 * 添加授权实体类
 * Created by Administrator on 2017/9/21.
 */

public class AddAuthEntity {


    /**
     * isGytUser : false
     * authUserInfo : {"phone":"18408249304","sex":"男","uid":20251,"name":"zg18408249304","headimgUrl":"http://www.cpigeon.com/Content/faces/20170719112540.jpg","nickname":"我就是我","sign":" 你是谁？","email":""}
     */

    private boolean isGytUser;
    private AuthUserInfoBean authUserInfo;

    public boolean isIsGytUser() {
        return isGytUser;
    }

    public void setIsGytUser(boolean isGytUser) {
        this.isGytUser = isGytUser;
    }

    public AuthUserInfoBean getAuthUserInfo() {
        return authUserInfo;
    }

    public void setAuthUserInfo(AuthUserInfoBean authUserInfo) {
        this.authUserInfo = authUserInfo;
    }

    public static class AuthUserInfoBean {
        /**
         * phone : 18408249304
         * sex : 男
         * uid : 20251
         * name : zg18408249304
         * headimgUrl : http://www.cpigeon.com/Content/faces/20170719112540.jpg
         * nickname : 我就是我
         * sign :  你是谁？
         * email :
         */

        private String phone;//手机号
        private String sex;//性别
        private int uid;//用户id
        private String name;//用户名
        private String headimgUrl;//用户头像
        private String nickname;//昵称
        private String sign;//签名
        private String email;//邮箱

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadimgUrl() {
            return headimgUrl;
        }

        public void setHeadimgUrl(String headimgUrl) {
            this.headimgUrl = headimgUrl;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

package com.cpigeon.cpigeonhelper.modular.authorise.model.bean;

/**
 * 授权列表主页实体类
 * Created by Administrator on 2017/9/20.
 */

public class AuthHomeEntity {


    /**
     * authUserInfo : {"phone":"18328495932","sex":"","uid":14165,"name":"zg18328495932","headimgUrl":"http://www.cpigeon.com/Content/faces/20170503171819.png","nickname":"xxdddddddgvv","sign":"","email":"2851551313@qq.com"}
     * authTime : 2017-09-0609:55:46
     * raceName : 555555
     * authStatus : 0
     */

    private AuthUserInfoBean authUserInfo;//身份验证用户信息
    private String authTime;//认证时间
    private String raceName;//竞赛名称
    private int authStatus;//身份验证状态
    private int raceId;//比赛id

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public AuthUserInfoBean getAuthUserInfo() {
        return authUserInfo;
    }

    public void setAuthUserInfo(AuthUserInfoBean authUserInfo) {
        this.authUserInfo = authUserInfo;
    }

    public String getAuthTime() {
        return authTime;
    }

    public void setAuthTime(String authTime) {
        this.authTime = authTime;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public static class AuthUserInfoBean {
        /**
         * phone : 18328495932
         * sex :
         * uid : 14165
         * name : zg18328495932
         * headimgUrl : http://www.cpigeon.com/Content/faces/20170503171819.png
         * nickname : xxdddddddgvv
         * sign :
         * email : 2851551313@qq.com
         */

        private String phone;//用户电话
        private String sex;//性别
        private int uid;//用户id
        private String name;//用户名称
        private String headimgUrl;//用户头像
        private String nickname;//昵称
        private String sign;//标志
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

package com.cpigeon.cpigeonhelper.modular.authorise.model.bean;

/**
 *
 * （未用到）
 * Created by Administrator on 2017/9/22.
 */

public class GYTAuthRaceListEntity {


    /**
     * stateCode : 0
     * createTime : 2017-08-24 26 14:35:19
     * raceName : 加连接
     * id : 336
     * mEndTime : 0001-01-01 08:00:00
     * state : 未开始监控
     * authTime : 2017-09-04 14:24:12
     * authUserInfo : {"phone":"13628035975","sex":"保密","uid":17458,"name":"zg13628035975","headimgUrl":"http://www.cpigeon.com/Content/faces/20170412161704.png","nickname":"ado","sign":"","email":""}
     * muid : 0
     * flyingTime : 0001-01-01 08:00:00
     * longitude : 0
     * mTime : 0001-01-0108:00:00
     * raceImage :
     * uid : 5473
     * latitude : 0
     * flyingArea :
     */

    private int stateCode;//状态码
    private String createTime;//创建时间
    private String raceName;//竞赛名称
    private int id;//
    private String mEndTime;//结束时间
    private String state;//状态
    private String authTime;//认证时间
    private AuthUserInfoBean authUserInfo;//身份验证用户信息
    private int muid;//
    private String flyingTime;//飞行时间
    private int longitude;//经度
    private String mTime;//
    private String raceImage;//比赛图片
    private int uid;//
    private int latitude;//纬度
    private String flyingArea;//飞行区域

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMEndTime() {
        return mEndTime;
    }

    public void setMEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuthTime() {
        return authTime;
    }

    public void setAuthTime(String authTime) {
        this.authTime = authTime;
    }

    public AuthUserInfoBean getAuthUserInfo() {
        return authUserInfo;
    }

    public void setAuthUserInfo(AuthUserInfoBean authUserInfo) {
        this.authUserInfo = authUserInfo;
    }

    public int getMuid() {
        return muid;
    }

    public void setMuid(int muid) {
        this.muid = muid;
    }

    public String getFlyingTime() {
        return flyingTime;
    }

    public void setFlyingTime(String flyingTime) {
        this.flyingTime = flyingTime;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getMTime() {
        return mTime;
    }

    public void setMTime(String mTime) {
        this.mTime = mTime;
    }

    public String getRaceImage() {
        return raceImage;
    }

    public void setRaceImage(String raceImage) {
        this.raceImage = raceImage;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public String getFlyingArea() {
        return flyingArea;
    }

    public void setFlyingArea(String flyingArea) {
        this.flyingArea = flyingArea;
    }

    public static class AuthUserInfoBean {
        /**
         * phone : 13628035975
         * sex : 保密
         * uid : 17458
         * name : zg13628035975
         * headimgUrl : http://www.cpigeon.com/Content/faces/20170412161704.png
         * nickname : ado
         * sign :
         * email :
         */

        private String phone;//电话
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

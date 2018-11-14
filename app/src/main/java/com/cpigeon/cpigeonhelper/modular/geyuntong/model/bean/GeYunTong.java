package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Administrator on 2017/9/23.
 */

public class GeYunTong implements Parcelable {

    /**
     * stateCode : 2
     * createTime : 2017-09-2018: 18: 46
     * raceName : 测试感觉
     * authStatus : 1
     * id : 413
     * mEndTime : 2017-09-2216: 25: 27
     * state : 监控结束
     * authTime : 2017-09-2018: 19: 41
     * authUserInfo : {"phone":18408249730,"sex":"男","uid":19138,"name":"zg18408249730","headimgUrl":"http: //www.cpigeon.com/Content/faces/20170914155311.jpg","nickname":"啦啦啦","sign":"Meiyouta","email":""}
     * muid : 19138.0
     * flyingTime : 0001-01-0108: 00: 00
     * longitude : 0
     * mTime : 2017-09-2018: 20: 11
     * raceImage :
     * raceUserInfo : {"phone":13730873310,"sex":"男","uid":5473,"name":"pigeonof","headimgUrl":"http: //www.cpigeon.com/Content/faces/zgw_avatar1_2016012505492613O20DYJWB.jpg","nickname":"中鸽网","sign":"cpigeon","email":"2851551312@qq.com"}
     * uid : 5473
     * latitude : 0
     * flyingArea :
     */

    private int stateCode;//状态码  【0：未开始监控的；1：正在监控中：2：监控结束】
    private String createTime;//创建时间
    private String raceName;//竞赛名称
    private int authStatus;//身份验证状态  0,等待接受比赛授权验证，1，接受授权比赛成功
    private int id;//比赛id
    private String mEndTime;//结束时间
    private String state;//状态
    private String authTime;//授权时间
    private AuthUserInfoBean authUserInfo;//被授权用户信息
    private int muid;//监控用户人的id
    private String flyingTime;//飞行时间
    private double longitude;//经度
    private String mTime;//监控启动时间
    private String raceImage;//比赛图像
    private RaceUserInfoBean raceUserInfo;//创建比赛的用户信息
    private int uid;//用户id
    private double latitude;//纬度
    private String flyingArea;//飞行区域
    private int tag = 1;//判断点击状态 (1:未选中状态，2：选中状态)


    protected GeYunTong(Parcel in) {
        stateCode = in.readInt();
        createTime = in.readString();
        raceName = in.readString();
        authStatus = in.readInt();
        id = in.readInt();
        mEndTime = in.readString();
        state = in.readString();
        authTime = in.readString();
        muid = in.readInt();
        flyingTime = in.readString();
        longitude = in.readInt();
        mTime = in.readString();
        raceImage = in.readString();
        uid = in.readInt();
        latitude = in.readInt();
        flyingArea = in.readString();
        tag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stateCode);
        dest.writeString(createTime);
        dest.writeString(raceName);
        dest.writeInt(authStatus);
        dest.writeInt(id);
        dest.writeString(mEndTime);
        dest.writeString(state);
        dest.writeString(authTime);
        dest.writeInt(muid);
        dest.writeString(flyingTime);
        dest.writeDouble(longitude);
        dest.writeString(mTime);
        dest.writeString(raceImage);
        dest.writeInt(uid);
        dest.writeDouble(latitude);
        dest.writeString(flyingArea);
        dest.writeInt(tag);
    }

    public static final Creator<GeYunTong> CREATOR = new Creator<GeYunTong>() {
        @Override
        public GeYunTong createFromParcel(Parcel in) {
            return new GeYunTong(in);
        }

        @Override
        public GeYunTong[] newArray(int size) {
            return new GeYunTong[size];
        }
    };

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

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
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

    public double getLongitude() {
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

    public RaceUserInfoBean getRaceUserInfo() {
        return raceUserInfo;
    }

    public void setRaceUserInfo(RaceUserInfoBean raceUserInfo) {
        this.raceUserInfo = raceUserInfo;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public double getLatitude() {
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

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * 身份验证用户信息实体类
     */
    public static class AuthUserInfoBean implements Parcelable {
        /**
         * phone : 18408249730
         * sex : 男
         * uid : 19138
         * name : zg18408249730
         * headimgUrl : http: //www.cpigeon.com/Content/faces/20170914155311.jpg
         * nickname : 啦啦啦
         * sign : Meiyouta
         * email :
         */

        private long phone;//电话
        private String sex;//性别
        private int uid;//用户id
        private String name;//用户名
        private String headimgUrl;//用户头像地址
        private String nickname;//昵称
        private String sign;//签名
        private String email;//邮箱

        protected AuthUserInfoBean(Parcel in) {
            phone = in.readLong();
            sex = in.readString();
            uid = in.readInt();
            name = in.readString();
            headimgUrl = in.readString();
            nickname = in.readString();
            sign = in.readString();
            email = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(phone);
            dest.writeString(sex);
            dest.writeInt(uid);
            dest.writeString(name);
            dest.writeString(headimgUrl);
            dest.writeString(nickname);
            dest.writeString(sign);
            dest.writeString(email);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<AuthUserInfoBean> CREATOR = new Creator<AuthUserInfoBean>() {
            @Override
            public AuthUserInfoBean createFromParcel(Parcel in) {
                return new AuthUserInfoBean(in);
            }

            @Override
            public AuthUserInfoBean[] newArray(int size) {
                return new AuthUserInfoBean[size];
            }
        };

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
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

    /**
     * 比赛用户 信息实体类
     */
    public static class RaceUserInfoBean implements Parcelable {
        /**
         * phone : 13730873310
         * sex : 男
         * uid : 5473
         * name : pigeonof
         * headimgUrl : http: //www.cpigeon.com/Content/faces/zgw_avatar1_2016012505492613O20DYJWB.jpg
         * nickname : 中鸽网
         * sign : cpigeon
         * email : 2851551312@qq.com
         */

        private long phone;//电话
        private String sex;//性别
        private int uid;//用户id
        private String name;//用户名
        private String headimgUrl;//用户头像
        private String nickname;//昵称
        private String sign;//签名
        private String email;//邮箱

        protected RaceUserInfoBean(Parcel in) {
            phone = in.readLong();
            sex = in.readString();
            uid = in.readInt();
            name = in.readString();
            headimgUrl = in.readString();
            nickname = in.readString();
            sign = in.readString();
            email = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(phone);
            dest.writeString(sex);
            dest.writeInt(uid);
            dest.writeString(name);
            dest.writeString(headimgUrl);
            dest.writeString(nickname);
            dest.writeString(sign);
            dest.writeString(email);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RaceUserInfoBean> CREATOR = new Creator<RaceUserInfoBean>() {
            @Override
            public RaceUserInfoBean createFromParcel(Parcel in) {
                return new RaceUserInfoBean(in);
            }

            @Override
            public RaceUserInfoBean[] newArray(int size) {
                return new RaceUserInfoBean[size];
            }
        };

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
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

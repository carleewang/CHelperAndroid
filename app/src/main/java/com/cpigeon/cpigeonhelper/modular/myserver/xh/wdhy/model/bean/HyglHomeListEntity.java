package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class HyglHomeListEntity {


//    {"status":true,"errorCode":0,"msg":"","data":{
//        "count_qb":0,//数字类型，全部会员个数
//                "count_zc":0,//数字类型，在册状态会员个数
//                "count_js":0,//数字类型，禁赛状态会员个数
//                "count_cm":0,//数字类型，除名状态会员个数
//                "datalist":[{
//            "id":50,//索引ID，作为参数传递到会员详细UI
//                    "handphone":"13688888888",//会员手机号码
//                    "name":"会员姓名",//会员姓名
//                    "status":"在册",//会员状态
//                    "geshewd":"",//鸽舍所在纬度
//                    "geshejd":"",//鸽舍所在经度
//                    "pn":"222222",//棚号
//                    "geshemc":""//鸽舍名称
//        }]
//    }}

    /**
     * count_cm : 1.0
     * count_js : 1.0
     * count_zc : 6.0
     * datalist : [{"id":74,"handphone":"","shijian":"2018-02-26","name":"","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"","geshewd":"","geshejd":"","pn":"","geshemc":""},{"id":50,"handphone":"13633012212","shijian":"2017-07-18","name":"13213","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"在册","geshewd":"","geshejd":"","pn":"324234","geshemc":""},{"id":49,"handphone":"13866669999","shijian":"2015-10-15","name":"熊大","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"在册","geshewd":"38.892311","geshejd":"103.2212322","pn":"12313","geshemc":""},{"id":48,"handphone":"13866669999","shijian":"2015-10-15","name":"rrt","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"禁赛","geshewd":"","geshejd":"","pn":"23423","geshemc":""},{"id":47,"handphone":"13999989962","shijian":"2015-10-15","name":"13132","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"除名","geshewd":"","geshejd":"","pn":"1231231s","geshemc":""},{"id":46,"handphone":"13889896666","shijian":"2015-10-15","name":"测试","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"在册","geshewd":"","geshejd":"","pn":"123123","geshemc":""},{"id":31,"handphone":"12345678911","shijian":"2003-01-08","name":"阿","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"在册","geshewd":"33.010000","geshejd":"133.010000","pn":"A0812311","geshemc":"蓝翔鸽舍"},{"id":30,"handphone":"13730873310","shijian":"2018-01-01","name":"小王","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"在册","geshewd":"33.22322","geshejd":"123.2222222","pn":"33332","geshemc":"中鸽网"},{"id":29,"handphone":"12345678911","shijian":"2013-12-18","name":"啊实打实","touxiang":"http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png","status":"在册","geshewd":"33.335610","geshejd":"114.484536","pn":"1231231","geshemc":"123123"}]
     * count_qb : 9.0
     */

    private double count_cm;//数字类型，除名状态会员个数
    private double count_js;//数字类型，禁赛状态会员个数
    private double count_zc;//数字类型，在册状态会员个数
    private double count_qb;//数字类型，全部会员个数
    private List<DatalistBean> datalist;

    public double getCount_cm() {
        return count_cm;
    }

    public void setCount_cm(double count_cm) {
        this.count_cm = count_cm;
    }

    public double getCount_js() {
        return count_js;
    }

    public void setCount_js(double count_js) {
        this.count_js = count_js;
    }

    public double getCount_zc() {
        return count_zc;
    }

    public void setCount_zc(double count_zc) {
        this.count_zc = count_zc;
    }

    public double getCount_qb() {
        return count_qb;
    }

    public void setCount_qb(double count_qb) {
        this.count_qb = count_qb;
    }

    public List<DatalistBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<DatalistBean> datalist) {
        this.datalist = datalist;
    }

    public static class DatalistBean implements Parcelable {
        /**
         * id : 74.0
         * handphone :
         * shijian : 2018-02-26
         * name :
         * touxiang : http://www.cpigeon.com/uploadfiles/pigeonassocation/member/default.png
         * status :
         * geshewd :
         * geshejd :
         * pn :
         * geshemc :
         */

        private String id;//索引ID，作为参数传递到会员详细UI
        private String handphone;//会员手机号码
        private String shijian;
        private String name;//会员姓名
        private String touxiang;
        private String status;//会员状态
        private String geshewd;//鸽舍所在纬度
        private String geshejd;//鸽舍所在经度
        private String pn;//棚号
        private String geshemc;//鸽舍名称
        private String xhuid;//协会用户id
        private String xhmc;//协会名称


        protected DatalistBean(Parcel in) {
            id = in.readString();
            handphone = in.readString();
            shijian = in.readString();
            name = in.readString();
            touxiang = in.readString();
            status = in.readString();
            geshewd = in.readString();
            geshejd = in.readString();
            pn = in.readString();
            geshemc = in.readString();
            xhuid = in.readString();
            xhmc = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(handphone);
            dest.writeString(shijian);
            dest.writeString(name);
            dest.writeString(touxiang);
            dest.writeString(status);
            dest.writeString(geshewd);
            dest.writeString(geshejd);
            dest.writeString(pn);
            dest.writeString(geshemc);
            dest.writeString(xhuid);
            dest.writeString(xhmc);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DatalistBean> CREATOR = new Creator<DatalistBean>() {
            @Override
            public DatalistBean createFromParcel(Parcel in) {
                return new DatalistBean(in);
            }

            @Override
            public DatalistBean[] newArray(int size) {
                return new DatalistBean[size];
            }
        };


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHandphone() {
            return handphone;
        }

        public void setHandphone(String handphone) {
            this.handphone = handphone;
        }

        public String getShijian() {
            return shijian;
        }

        public void setShijian(String shijian) {
            this.shijian = shijian;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTouxiang() {
            return touxiang;
        }

        public void setTouxiang(String touxiang) {
            this.touxiang = touxiang;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGeshewd() {
            return geshewd;
        }

        public void setGeshewd(String geshewd) {
            this.geshewd = geshewd;
        }

        public String getGeshejd() {
            return geshejd;
        }

        public void setGeshejd(String geshejd) {
            this.geshejd = geshejd;
        }

        public String getPn() {
            return pn;
        }

        public void setPn(String pn) {
            this.pn = pn;
        }

        public String getGeshemc() {
            return geshemc;
        }

        public void setGeshemc(String geshemc) {
            this.geshemc = geshemc;
        }

        public String getXhuid() {
            return xhuid;
        }

        public void setXhuid(String xhuid) {
            this.xhuid = xhuid;
        }

        public String getXhmc() {
            return xhmc;
        }

        public void setXhmc(String xhmc) {
            this.xhmc = xhmc;
        }
    }
}

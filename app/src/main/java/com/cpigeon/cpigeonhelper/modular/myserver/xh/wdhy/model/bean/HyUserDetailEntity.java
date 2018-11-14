package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * 会员详细
 * Created by Administrator on 2018/3/30.
 */

public class HyUserDetailEntity implements Serializable {

    /**
     * basicinfo : {"career":"职工","sex":"男","handphone":"13889896666","birthday":"2003/2/6 0:00:00","name":"测试","education":"小学"}
     * gesheinfo : {"issaomiao":0,"gesheaddr":"副食店发12312","zhucetime":"2015/10/15 0:00:00","pn":"123123","geshelo":"","geshename":"","geshela":""}
     * zhuangtai : 在册
     */

    private BasicinfoBean basicinfo;//基本信息
    private GesheinfoBean gesheinfo;//鸽舍资料
    private String zhuangtai;//状态：在册|禁赛|除名

    public BasicinfoBean getBasicinfo() {
        return basicinfo;
    }

    public void setBasicinfo(BasicinfoBean basicinfo) {
        this.basicinfo = basicinfo;
    }

    public GesheinfoBean getGesheinfo() {
        return gesheinfo;
    }

    public void setGesheinfo(GesheinfoBean gesheinfo) {
        this.gesheinfo = gesheinfo;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public static class BasicinfoBean implements Serializable {
        /**
         * career : 职工
         * sex : 男
         * handphone : 13889896666
         * birthday : 2003/2/6 0:00:00
         * name : 测试
         * education : 小学
         */

        private String career;//职业
        private String sex;//性别
        private String handphone;//手机号码
        private String birthday;//生日
        private String name;//姓名
        private String education;//文化程度
        private String mid;//
        private String touxiang;//
        private String xhuid;//协会用户id

        public String getCareer() {
            return career;
        }

        public void setCareer(String career) {
            this.career = career;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHandphone() {
            return handphone;
        }

        public void setHandphone(String handphone) {
            this.handphone = handphone;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }


        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getTouxiang() {
            return touxiang;
        }

        public void setTouxiang(String touxiang) {
            this.touxiang = touxiang;
        }

        public String getXhuid() {
            return xhuid;
        }

        public void setXhuid(String xhuid) {
            this.xhuid = xhuid;
        }
    }

    public static class GesheinfoBean implements Serializable {
        /**
         * issaomiao : 0
         * gesheaddr : 副食店发12312
         * zhucetime : 2015/10/15 0:00:00
         * pn : 123123
         * geshelo :
         * geshename :
         * geshela :
         */

        private int issaomiao;//数字类型，是否扫描用户，1为扫描用户
        private String gesheaddr;//鸽舍所在地址
        private String zhucetime;//注册入会时间
        private String pn;//棚号
        private String geshelo;//鸽舍坐标东经
        private String geshename;//鸽舍坐标北纬
        private String geshela;//鸽舍名称
        private String csbh;//参赛编号

        public int getIssaomiao() {
            return issaomiao;
        }

        public void setIssaomiao(int issaomiao) {
            this.issaomiao = issaomiao;
        }

        public String getGesheaddr() {
            return gesheaddr;
        }

        public void setGesheaddr(String gesheaddr) {
            this.gesheaddr = gesheaddr;
        }

        public String getZhucetime() {
            return zhucetime;
        }

        public void setZhucetime(String zhucetime) {
            this.zhucetime = zhucetime;
        }

        public String getPn() {
            return pn;
        }

        public void setPn(String pn) {
            this.pn = pn;
        }

        public String getGeshelo() {
            return geshelo;
        }

        public void setGeshelo(String geshelo) {
            this.geshelo = geshelo;
        }

        public String getGeshename() {
            return geshename;
        }

        public void setGeshename(String geshename) {
            this.geshename = geshename;
        }

        public String getGeshela() {
            return geshela;
        }

        public void setGeshela(String geshela) {
            this.geshela = geshela;
        }

        public String getCsbh() {
            return csbh;
        }

        public void setCsbh(String csbh) {
            this.csbh = csbh;
        }
    }
}

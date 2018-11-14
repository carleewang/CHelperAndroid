package com.cpigeon.cpigeonhelper.modular.saigetong.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15.
 */

public class GeZhuFootEntity {
    /**
     * duoxuan : true
     * footlist : [{"eye":"","sex":"","color":"雨白条","id":547558,"foot":"2018-22-0011112"}]
     */

    private boolean duoxuan;
    private List<FootlistBean> footlist;

    public boolean isDuoxuan() {
        return duoxuan;
    }

    public void setDuoxuan(boolean duoxuan) {
        this.duoxuan = duoxuan;
    }

    public List<FootlistBean> getFootlist() {
        return footlist;
    }

    public void setFootlist(List<FootlistBean> footlist) {
        this.footlist = footlist;
    }

    public static class FootlistBean {
        private int id;//
        private String color;//羽色
        private String sex;//雌雄
        private String address;//地址
        private String foot;//足环
        private String tel;//电话
        private String eye;//眼睛
        private String sjhm;
        private String xingming;
        private String cskh;
        private String gpmc;//公棚名称
        private int clickTag;//选择tag，1：未选中，2：选中

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFoot() {
            return foot;
        }

        public void setFoot(String foot) {
            this.foot = foot;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEye() {
            return eye;
        }

        public void setEye(String eye) {
            this.eye = eye;
        }

        public String getSjhm() {
            return sjhm;
        }

        public void setSjhm(String sjhm) {
            this.sjhm = sjhm;
        }

        public String getXingming() {
            return xingming;
        }

        public void setXingming(String xingming) {
            this.xingming = xingming;
        }

        public String getCskh() {
            return cskh;
        }

        public void setCskh(String cskh) {
            this.cskh = cskh;
        }

        public int getClickTag() {
            return clickTag;
        }

        public void setClickTag(int clickTag) {
            this.clickTag = clickTag;
        }

        public String getGpmc() {
            return gpmc;
        }

        public void setGpmc(String gpmc) {
            this.gpmc = gpmc;
        }
    }


//    private String id;//足环信息ID
//    private String foot;//足环号码
//    private int clickTag;//选择tag，1：未选中，2：选中
//
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getFoot() {
//        return foot;
//    }
//
//    public void setFoot(String foot) {
//        this.foot = foot;
//    }
//
//    public int getClickTag() {
//        return clickTag;
//    }
//
//    public void setClickTag(int clickTag) {
//        this.clickTag = clickTag;
//    }
}

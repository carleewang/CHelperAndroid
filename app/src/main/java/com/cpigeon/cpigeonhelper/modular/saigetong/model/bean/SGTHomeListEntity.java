package com.cpigeon.cpigeonhelper.modular.saigetong.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */

public class SGTHomeListEntity implements Serializable {


    /**
     * id : 39185
     * count : 17
     * data : [{"cskh":"0404","id":39185,"foot":"2015-22-0857327"},{"cskh":"0404","id":39186,"foot":"2015-22-0857328"}]
     * sjhm : 13882160595
     * cskh : 0404
     * xingming : 苏家彬
     */

    private int id;
    private String count;
    private String sjhm;
    private String cskh;
    private String xingming;
    private List<DataBean> data;
    private int tag = 1;//点击展开tag 1 收缩，2展开
    private int imgTag = -1;//图片选择 1：首页图片选择

    public int getImgTag() {
        return imgTag;
    }

    public void setImgTag(int imgTag) {
        this.imgTag = imgTag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getCskh() {
        return cskh;
    }

    public void setCskh(String cskh) {
        this.cskh = cskh;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * cskh : 0404
         * id : 39185
         * foot : 2015-22-0857327
         */

        private String cskh;
        private String id;
        private String foot;
        private String color;
        private String pic = "0";

        public DataBean() {

        }

        public DataBean(String foot, String id) {
            this.foot = foot;
            this.id = id;
        }

        public String getCskh() {
            return cskh;
        }

        public void setCskh(String cskh) {
            this.cskh = cskh;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFoot() {
            return foot;
        }

        public void setFoot(String foot) {
            this.foot = foot;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }


        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}

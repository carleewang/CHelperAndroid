package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/15.
 */

public class FootAdminEntity implements Serializable {


    /**
     * year : 2018
     * name : 12312
     * id : 1
     * foot : 12312
     */

    private String year;//年份
    private String buyname;//购买者姓名
    private String id;//索引ID
    private String foot;//足环号码
    private String dsd;//代售点
    private String jfzt;//缴费状态：0未缴费，1已缴费
    private String zhjg;//足环价格
    private String cszt;//出售状态：0未出售，1已出售
    private SlygBean slyg;
    private String buyuid;//购买者ID
    private String dsdname;//代售点名字


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBuyname() {
        return buyname;
    }

    public void setBuyname(String buyname) {
        this.buyname = buyname;
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

    public String getDsd() {
        return dsd;
    }

    public void setDsd(String dsd) {
        this.dsd = dsd;
    }

    public String getJfzt() {
        return jfzt;
    }

    public void setJfzt(String jfzt) {
        this.jfzt = jfzt;
    }

    public String getZhjg() {
        return zhjg;
    }

    public void setZhjg(String zhjg) {
        this.zhjg = zhjg;
    }

    public String getCszt() {
        return cszt;
    }

    public void setCszt(String cszt) {
        this.cszt = cszt;
    }

    public SlygBean getSlyg() {
        return slyg;
    }

    public void setSlyg(SlygBean slyg) {
        this.slyg = slyg;
    }

    public String getBuyuid() {
        return buyuid;
    }

    public void setBuyuid(String buyuid) {
        this.buyuid = buyuid;
    }

    public String getDsdname() {
        return dsdname;
    }

    public void setDsdname(String dsdname) {
        this.dsdname = dsdname;
    }

    public static class SlygBean implements Serializable {
        /**
         * filetype : 图片
         * fileurl :
         */

        private String filetype;//上笼验鸽文件类型
        private String fileurl;//上笼验鸽文件url
        private String filepic;//缩略图

        public String getFiletype() {
            return filetype;
        }

        public void setFiletype(String filetype) {
            this.filetype = filetype;
        }

        public String getFileurl() {
            return fileurl;
        }

        public void setFileurl(String fileurl) {
            this.fileurl = fileurl;
        }

        public String getFilepic() {
            return filepic;
        }

        public void setFilepic(String filepic) {
            this.filepic = filepic;
        }
    }
}

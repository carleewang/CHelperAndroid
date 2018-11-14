package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class HFInfoEntity {


    /**
     * wjflist : [{"hyxm":"会员姓名"}]
     * yingshouhf : 0
     * yjflist : [{"hyxm":"会员姓名","jfmoney":"0"}]
     * yishouhy : 0
     * huifei : 0
     * yishouhf : 0
     */

    private String yingshouhf;//应收会费
    private String yishouhy;//已收费会员个数
    private String huifei;//会费
    private String yishouhf;//已收会费
    private int tags = 0;// 0 正常数据  1 获取服务器数据为空   2 获取数据异常
    private List<WjflistBean> wjflist;//未交费会员列表
    private List<YjflistBean> yjflist;//已交费会员列表

    public HFInfoEntity(int tags) {
        this.tags = tags;
    }

    public HFInfoEntity() {
        this.tags = tags;
    }

    public int getTags() {
        return tags;
    }

    public void setTags(int tags) {
        this.tags = tags;
    }

    public String getYingshouhf() {
        return yingshouhf;
    }

    public void setYingshouhf(String yingshouhf) {
        this.yingshouhf = yingshouhf;
    }

    public String getYishouhy() {
        return yishouhy;
    }

    public void setYishouhy(String yishouhy) {
        this.yishouhy = yishouhy;
    }

    public String getHuifei() {
        return huifei;
    }

    public void setHuifei(String huifei) {
        this.huifei = huifei;
    }

    public String getYishouhf() {
        return yishouhf;
    }

    public void setYishouhf(String yishouhf) {
        this.yishouhf = yishouhf;
    }

    public List<WjflistBean> getWjflist() {
        return wjflist;
    }

    public void setWjflist(List<WjflistBean> wjflist) {
        this.wjflist = wjflist;
    }

    public List<YjflistBean> getYjflist() {
        return yjflist;
    }

    public void setYjflist(List<YjflistBean> yjflist) {
        this.yjflist = yjflist;
    }

    public static class WjflistBean {
        /**
         * hyxm : 会员姓名
         * jfmoney : 0
         */

        private String hyxm;
        private String jfmoney;

        public String getHyxm() {
            return hyxm;
        }

        public void setHyxm(String hyxm) {
            this.hyxm = hyxm;
        }

        public String getJfmoney() {
            return jfmoney;
        }

        public void setJfmoney(String jfmoney) {
            this.jfmoney = jfmoney;
        }
    }

    public static class YjflistBean {
        /**
         * hyxm : 会员姓名
         * jfmoney : 0
         */

        private String hyxm;
        private String jfmoney;

        public String getHyxm() {
            return hyxm;
        }

        public void setHyxm(String hyxm) {
            this.hyxm = hyxm;
        }

        public String getJfmoney() {
            return jfmoney;
        }

        public void setJfmoney(String jfmoney) {
            this.jfmoney = jfmoney;
        }
    }
}

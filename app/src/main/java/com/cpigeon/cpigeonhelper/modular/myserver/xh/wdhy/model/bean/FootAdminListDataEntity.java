package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 */

public class FootAdminListDataEntity implements Serializable {


    /**
     * totalcount : 1000
     * totalcountcurr : 1000
     * footlist : [{"year":"2018","name":"","id":"1065508","foot":"2018-22-223455"}]
     * totalpaysum : 10000
     */

    private String totalcount;//总计足环数
    private String totalcountcurr;//当前搜索条件足环数
    private String totalpaysum;//总金额
    private List<FootlistBean> footlist;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public String getTotalcountcurr() {
        return totalcountcurr;
    }

    public void setTotalcountcurr(String totalcountcurr) {
        this.totalcountcurr = totalcountcurr;
    }

    public String getTotalpaysum() {
        return totalpaysum;
    }

    public void setTotalpaysum(String totalpaysum) {
        this.totalpaysum = totalpaysum;
    }

    public List<FootlistBean> getFootlist() {
        return footlist;
    }

    public void setFootlist(List<FootlistBean> footlist) {
        this.footlist = footlist;
    }

    public static class FootlistBean implements Serializable {
        /**
         * year : 2018
         * name :
         * id : 1065508
         * foot : 2018-22-223455
         */

        private String year;
        private String name;
        private String id;
        private String foot;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }
}

package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class ChildFoodAdminListEntity {

    /**
     * footlist : [{"id":"3","foot1":"2018-22-1","foot2":"2018-22-29","xhmc":"协会名称","footnum":"0","type":"特比环"}]
     * footcount : 10000
     * footprice : 100000
     */

    private String footcount;//总发行足环数量
    private String footprice;////总金额
    private List<FootlistBean> footlist;

    public String getFootcount() {
        return footcount;
    }

    public void setFootcount(String footcount) {
        this.footcount = footcount;
    }

    public String getFootprice() {
        return footprice;
    }

    public void setFootprice(String footprice) {
        this.footprice = footprice;
    }

    public List<FootlistBean> getFootlist() {
        return footlist;
    }

    public void setFootlist(List<FootlistBean> footlist) {
        this.footlist = footlist;
    }

    public static class FootlistBean implements Serializable {
        /**
         * id : 3
         * foot1 : 2018-22-1
         * foot2 : 2018-22-29
         * xhmc : 协会名称
         * footnum : 0
         * type : 特比环
         */

        private String id;//索引ID
        private String foot1;//开始足环号码
        private String foot2;//结束足环号码
        private String xhmc;//协会名称
        private String footnum;//号段足环号码个数
        private String type;//类型
        private String xhid;//协会id
        private String xhuid;//协会用户id

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFoot1() {
            return foot1;
        }

        public void setFoot1(String foot1) {
            this.foot1 = foot1;
        }

        public String getFoot2() {
            return foot2;
        }

        public void setFoot2(String foot2) {
            this.foot2 = foot2;
        }

        public String getXhmc() {
            return xhmc;
        }

        public void setXhmc(String xhmc) {
            this.xhmc = xhmc;
        }

        public String getFootnum() {
            return footnum;
        }

        public void setFootnum(String footnum) {
            this.footnum = footnum;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getXhid() {
            return xhid;
        }

        public void setXhid(String xhid) {
            this.xhid = xhid;
        }

        public String getXhuid() {
            return xhuid;
        }

        public void setXhuid(String xhuid) {
            this.xhuid = xhuid;
        }
    }
}

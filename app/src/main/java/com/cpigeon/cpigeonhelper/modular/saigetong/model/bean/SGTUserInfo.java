package com.cpigeon.cpigeonhelper.modular.saigetong.model.bean;

/**
 * 赛鸽通用户信息实体类
 * Created by Administrator on 2018/1/19.
 */

public class SGTUserInfo {


//    {
//        "gzcount":10000,//入棚赛鸽总数
//            "gpmc":"",//公棚名称
//            "bjrpkssj":"2018-01-18",//本届入棚开始时间
//            "dqsj":"2018-01-18",//到期时间
//            "piccountsf":2,//收费拍照张数{tag:标签名称,count:图片数量}
//            "piccountrp":1,//入棚拍照张数{tag:标签名称,count:图片数量}
//            "piccountrc":100,//日常拍照张数{tag:标签名称,count:图片数量}
//            "piccountbs":200,//比赛拍照张数{tag:标签名称,count:图片数量}
//            "kjcountrl":1000,//空间总大小
//            "kjcountyy":50,//空间已用大小
//            "kjcountsy":950//空间剩余大小
//    }

    /**
     * gzcount : 10000
     * gpmc :
     * bjrpkssj : 2018-01-18
     * dqsj : 2018-01-18
     * piccountsf : {"tag":"标签名称","count":"图片数量"}
     * piccountrp : {"tag":"标签名称","count":"图片数量"}
     * piccountrc : {"tag":"标签名称","count":"图片数量"}
     * piccountbs : {"tag":"标签名称","count":"图片数量"}
     * kjcountrl : 1000
     * kjcountyy : 50
     * kjcountsy : 950
     */

    private String gzcount;//入棚赛鸽总数
    private String gpmc;//公棚名称
    private String bjrpkssj;//本届入棚开始时间
    private String dqsj;//到期时间
    private PiccountsfBean piccountsf;//收费拍照张数{tag:标签名称,count:图片数量}
    private PiccountrpBean piccountrp;//入棚拍照张数{tag:标签名称,count:图片数量}
    private PiccountrcBean piccountrc;//日常拍照张数{tag:标签名称,count:图片数量}
    private PiccountbsBean piccountbs;//比赛拍照张数{tag:标签名称,count:图片数量}
    private PiccounthjBean piccounthj;//获奖荣誉张数{tag:标签名称,count:图片数量}
    private String kjcountrl;//空间总大小
    private String kjcountyy;//空间已用大小
    private String kjcountsy;//空间剩余大小
    private String gpkrys;//可容羽数
    private String ktsj;//开通时间

    public String getKtsj() {
        return ktsj;
    }

    public void setKtsj(String ktsj) {
        this.ktsj = ktsj;
    }

    public String getGpkrys() {
        return gpkrys;
    }

    public void setGpkrys(String gpkrys) {
        this.gpkrys = gpkrys;
    }

    public String getGzcount() {
        return gzcount;
    }

    public void setGzcount(String gzcount) {
        this.gzcount = gzcount;
    }

    public String getGpmc() {
        return gpmc;
    }

    public void setGpmc(String gpmc) {
        this.gpmc = gpmc;
    }

    public String getBjrpkssj() {
        return bjrpkssj;
    }

    public void setBjrpkssj(String bjrpkssj) {
        this.bjrpkssj = bjrpkssj;
    }

    public String getDqsj() {
        return dqsj;
    }

    public void setDqsj(String dqsj) {
        this.dqsj = dqsj;
    }

    public PiccountsfBean getPiccountsf() {
        return piccountsf;
    }

    public void setPiccountsf(PiccountsfBean piccountsf) {
        this.piccountsf = piccountsf;
    }

    public PiccountrpBean getPiccountrp() {
        return piccountrp;
    }

    public void setPiccountrp(PiccountrpBean piccountrp) {
        this.piccountrp = piccountrp;
    }

    public PiccountrcBean getPiccountrc() {
        return piccountrc;
    }

    public void setPiccountrc(PiccountrcBean piccountrc) {
        this.piccountrc = piccountrc;
    }

    public PiccountbsBean getPiccountbs() {
        return piccountbs;
    }

    public void setPiccountbs(PiccountbsBean piccountbs) {
        this.piccountbs = piccountbs;
    }

    public String getKjcountrl() {
        return kjcountrl;
    }

    public void setKjcountrl(String kjcountrl) {
        this.kjcountrl = kjcountrl;
    }

    public String getKjcountyy() {
        return kjcountyy;
    }

    public void setKjcountyy(String kjcountyy) {
        this.kjcountyy = kjcountyy;
    }

    public String getKjcountsy() {
        return kjcountsy;
    }

    public void setKjcountsy(String kjcountsy) {
        this.kjcountsy = kjcountsy;
    }

    public PiccounthjBean getPiccounthj() {
        return piccounthj;
    }

    public void setPiccounthj(PiccounthjBean piccounthj) {
        this.piccounthj = piccounthj;
    }

    public static class PiccountsfBean {
        /**
         * tag : 标签名称
         * count : 图片数量
         */

        private String tag;
        private String count;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public static class PiccountrpBean {
        /**
         * tag : 标签名称
         * count : 图片数量
         */

        private String tag;
        private String count;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public static class PiccountrcBean {
        /**
         * tag : 标签名称
         * count : 图片数量
         */

        private String tag;
        private String count;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public static class PiccountbsBean {
        /**
         * tag : 标签名称
         * count : 图片数量
         */

        private String tag;
        private String count;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public static class PiccounthjBean {
        /**
         * tag : 标签名称
         * count : 图片数量
         */

        private String tag;
        private String count;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}

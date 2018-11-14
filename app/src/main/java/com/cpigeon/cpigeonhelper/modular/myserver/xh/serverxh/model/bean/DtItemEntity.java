package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * item动态详情
 * Created by Administrator on 2017/12/14.
 */

public class DtItemEntity implements Serializable {


//    {
//        "fbsj": "2006/10/9 13:03:00",
//            "title": "崇州市羊马信鸽协会",
//            "dtid": 13,
//            "content": "赛事马信鸽协会奖赛 ",
//            "pl": 1,
//            "tplist": [
//        {
//            "imgurl": "http://118.123.244.89 818/uploadfiles/xh/picture/gc/94455_a213bf3ed667df34ec7c6490fc67dcad.jpg"
//        }
//    ],
//        "source": "来源"
//    }


    /**
     * fbsj : 2006/10/9 13:03:00
     * title : 崇州市羊马信鸽协会
     * dtid : 13
     * content : 赛事马信鸽协会奖赛
     * pl : 1
     * tplist : [{"imgurl":"http://118.123.244.89 818/uploadfiles/xh/picture/gc/94455_a213bf3ed667df34ec7c6490fc67dcad.jpg"}]
     * source : 来源
     */

    private String fbsj;
    private String title;
    private int dtid;
    private String content;
    private int pl;
    private String source;
    private boolean isupdate;
    private List<TplistBean> tplist;


    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDtid() {
        return dtid;
    }

    public void setDtid(int dtid) {
        this.dtid = dtid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPl() {
        return pl;
    }

    public void setPl(int pl) {
        this.pl = pl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<TplistBean> getTplist() {
        return tplist;
    }

    public void setTplist(List<TplistBean> tplist) {
        this.tplist = tplist;
    }


    public boolean isupdate() {
        return isupdate;
    }

    public void setIsupdate(boolean isupdate) {
        this.isupdate = isupdate;
    }

    public static class TplistBean implements Serializable {
        /**
         * imgurl : http://118.123.244.89 818/uploadfiles/xh/picture/gc/94455_a213bf3ed667df34ec7c6490fc67dcad.jpg
         */

        private String imgurl;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }


}

package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 */

public class GcItemEntity implements Serializable {
    /**
     * fbsj : 2006/10/9 13:03:00
     * title : 崇州市羊马信鸽协会
     * gcid : 13
     * content : 赛事马信鸽协会奖赛
     * pl : 1
     * tplist : [{"imgurl":"http://118.123.244.89:818/uploadfiles/xh/picture/gc/94455_a213bf3ed667df34ec7c6490fc67dcad.jpg"}]
     */

//    {"status":true,"errorCode":0,"msg":"","data":{
//        "fbsj":"2006/10/9 13:03:00",		//发布时间
//                "title":"崇州市羊马信鸽协会",		//标题
//                "gcid":13,				//规程ID
//                "content":"赛事马信鸽协会奖赛 ",	//规程内容
//                "pl":1,					//是否开启评论：0关闭；1开启
//                "tplist":[
//        {"imgurl":"http://118.123.244.89:818/uploadfiles/xh/picture/gc/94455_a213bf3ed667df34ec7c6490fc67dcad.jpg"}]图片列表
//    }}



    private String fbsj;
    private String title;
    private int gcid;
    private String content;
    private int pl;//是否开启评论：0关闭；1开启
    private boolean  isupdate ;
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

    public int getGcid() {
        return gcid;
    }

    public void setGcid(int gcid) {
        this.gcid = gcid;
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
         * imgurl : http://118.123.244.89:818/uploadfiles/xh/picture/gc/94455_a213bf3ed667df34ec7c6490fc67dcad.jpg
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

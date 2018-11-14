package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 */

public class GcListEntity {

//    {"status":true,"errorCode":0,"msg":"","data":{
//        "fbsj":"2006/10/9 13:03:00",		//发布时间
//                "title":"崇州市羊马信鸽协会",		//标题
//                "gcid":13,				//规程ID
//                "content":"赛事马信鸽协会奖赛 ",	//规程内容
//                "pl":1,					//是否开启评论：0关闭；1开启
//                "tplist":图片列表
//    }}

    private String fbsj;
    private String title;
    private int gcid;

    private String content;
    private int pl;
    private List<String> tplist;
    private int clickTag = 1;//点击tag  1  没有点击  2  点击

    public int getClickTag() {
        return clickTag;
    }

    public void setClickTag(int clickTag) {
        this.clickTag = clickTag;
    }

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

    public List<String> getTplist() {
        return tplist;
    }

    public void setTplist(List<String> tplist) {
        this.tplist = tplist;
    }
}

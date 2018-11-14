package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean;

/**
 * Created by Administrator on 2017/12/14.
 */

public class DtListEntity {

//    {
//        "fbsj":"2017/12/6 15:08:41",	//发布时间
//            "dtid":2000,			//动态索引ID
//            "title":"这里是标题"		//标题
//    }

    /**
     * fbsj : 2017/12/6 15:08:41
     * dtid : 2000
     * title : 这里是标题
     */

    private String fbsj;
    private int dtid;
    private String title;
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

    public int getDtid() {
        return dtid;
    }

    public void setDtid(int dtid) {
        this.dtid = dtid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}

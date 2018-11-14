package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean;

/**
 * Created by Administrator on 2018/1/10.
 */

public class DelGcDtEntity {
    private String time;//时间
    private int id;//id
    private String title;//标题
    private int clickTag = 1;//点击tag  1  没有点击  2  点击

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClickTag() {
        return clickTag;
    }

    public void setClickTag(int clickTag) {
        this.clickTag = clickTag;
    }
}

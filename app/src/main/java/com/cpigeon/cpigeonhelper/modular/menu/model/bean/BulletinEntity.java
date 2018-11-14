package com.cpigeon.cpigeonhelper.modular.menu.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 公告通知实体类
 * Created by Administrator on 2017/9/20.
 */

public class BulletinEntity extends RealmObject {

    /**
     * title : 中鸽网各地鸽舍全新改版
     * time : 2015-07-14 17:15:21
     * content : sdfsf
     * id : 26
     * istop : 0
     */
    @PrimaryKey
    private int id;
    private String title;
    private String time;
    private String content;

    private int istop;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIstop() {
        return istop;
    }

    public void setIstop(int istop) {
        this.istop = istop;
    }
}

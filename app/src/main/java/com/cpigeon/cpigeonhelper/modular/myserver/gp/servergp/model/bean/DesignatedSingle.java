package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/28.
 */

public class DesignatedSingle implements Serializable {

    private  String  title;
    private  String  content;

    public DesignatedSingle(String title, String content, int tag) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

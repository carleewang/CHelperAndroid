package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

/**
 * 上级发行足环
 * Created by Administrator on 2018/6/20.
 */

public class IssueFoot {


    /**
     * id : 1
     */

    private String id;//索引ID，传递给确定导入接口
    /**
     * type : 普通足环
     */

    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

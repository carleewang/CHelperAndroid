package com.cpigeon.cpigeonhelper.modular.home.model.bean;

import com.cpigeon.cpigeonhelper.utils.http.GsonUtil;

/**
 * Created by Administrator on 2017/11/29.
 */

public class DiZhenEntity {


    /**
     * Success : true
     * Data : 11月28日11时45分新疆和田地区策勒县发生3.0级地震,11月27日15时11分新爱尔兰地区发生6.0级地震,11月27日11时30分青海玉树州杂多县发生3.2级地震,11月24日4时55分台湾云林县发生4.2级地震,11月23日22时12分西藏林芝市巴宜区发生3.2级地震,11月23日17时43分重庆武隆区发生5.0级地震,11月23日15时13分西藏林芝市巴宜区发生4.2级地震
     * Message : null
     */

    private boolean Success;
    private String Data;
    private Object Message;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public Object getMessage() {
        return Message;
    }

    public void setMessage(Object Message) {
        this.Message = Message;
    }

    public String toJsonString() {
        return GsonUtil.toJson(this);
    }
}

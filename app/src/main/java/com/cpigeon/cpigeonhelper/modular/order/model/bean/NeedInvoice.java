package com.cpigeon.cpigeonhelper.modular.order.model.bean;

/**
 * Created by Administrator on 2018/6/13.
 */

public class NeedInvoice {

    private boolean isNeedInvoice;
    private String id;

    private NeedInvoice(Builder builder) {
        setNeedInvoice(builder.isNeedInvoice);
        setId(builder.id);
    }

    public boolean isNeedInvoice() {
        return isNeedInvoice;
    }

    public void setNeedInvoice(boolean needInvoice) {
        isNeedInvoice = needInvoice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public static final class Builder {
        private boolean isNeedInvoice;
        private String id;

        public Builder() {
        }

        public Builder isNeedInvoice(boolean val) {
            isNeedInvoice = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public NeedInvoice build() {
            return new NeedInvoice(this);
        }
    }
}

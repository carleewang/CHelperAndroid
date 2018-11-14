package com.cpigeon.cpigeonhelper.modular.order.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/13.
 */

public class InvoiceEntity   implements Serializable {


    /**
     * id : 1
     * lxr :
     * yx : xiaohl
     * lx : 电子发票
     * sh : 20180208
     * dz :
     * dh :
     * dwmc : 中鸽信鸽网
     */

    private String id;//发票信息索引ID
    private String lxr;//收件人
    private String yx;//收件人电子邮箱
    private String lx;//类型：纸质发票|电子发票
    private String sh;//税号
    private String dz;//地址
    private String dh;//电话
    private String dwmc;//单位名称
    private String c;//收件人所在市
    private String p;//收件人所在省
    private String a;//收件人所在区县

    private InvoiceEntity(Builder builder) {
        setId(builder.id);
        setLxr(builder.lxr);
        setYx(builder.yx);
        setLx(builder.lx);
        setSh(builder.sh);
        setDz(builder.dz);
        setDh(builder.dh);
        setDwmc(builder.dwmc);
        setC(builder.c);
        setP(builder.p);
        setA(builder.a);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getYx() {
        return yx;
    }

    public void setYx(String yx) {
        this.yx = yx;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getSh() {
        return sh;
    }

    public void setSh(String sh) {
        this.sh = sh;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }


    public static final class Builder {
        private String id;
        private String lxr;
        private String yx;
        private String lx;
        private String sh;
        private String dz;
        private String dh;
        private String dwmc;
        private String c;
        private String p;
        private String a;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder lxr(String val) {
            lxr = val;
            return this;
        }

        public Builder yx(String val) {
            yx = val;
            return this;
        }

        public Builder lx(String val) {
            lx = val;
            return this;
        }

        public Builder sh(String val) {
            sh = val;
            return this;
        }

        public Builder dz(String val) {
            dz = val;
            return this;
        }

        public Builder dh(String val) {
            dh = val;
            return this;
        }

        public Builder dwmc(String val) {
            dwmc = val;
            return this;
        }

        public Builder c(String val) {
            c = val;
            return this;
        }

        public Builder p(String val) {
            p = val;
            return this;
        }

        public Builder a(String val) {
            a = val;
            return this;
        }

        public InvoiceEntity build() {
            return new InvoiceEntity(this);
        }
    }
}

package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

/**
 * 51.获取鸽运通定位信息实体类
 * Created by Administrator on 2017/11/10.
 */

public class RaceLocationEntity {


    /**
     * {"sj": 1497579973,"tq": {"sj": 1497578400,"fl": "5","fx": " 西 南 ","mc": " 多 云","wd": 22},"sd": 0,"lid": 31,"wd": 30.668366,"jd": 104.032464}
     * <p>
     * sj : 1497579973
     * tq : {"sj":1497578400,"fl":"5","fx":" 西 南 ","mc":" 多 云","wd":22}
     * sd : 0
     * lid : 31
     * wd : 30.668366
     * jd : 104.032464
     */

    private int sj;
    private TqBean tq;
    private int sd;
    private int lid;
    private double wd;
    private double jd;

    public int getSj() {
        return sj;
    }

    public void setSj(int sj) {
        this.sj = sj;
    }

    public TqBean getTq() {
        return tq;
    }

    public void setTq(TqBean tq) {
        this.tq = tq;
    }

    public int getSd() {
        return sd;
    }

    public void setSd(int sd) {
        this.sd = sd;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public double getWd() {
        return wd;
    }

    public void setWd(double wd) {
        this.wd = wd;
    }

    public double getJd() {
        return jd;
    }

    public void setJd(double jd) {
        this.jd = jd;
    }

    public static class TqBean {
        /**
         * sj : 1497578400
         * fl : 5
         * fx :  西 南
         * mc :  多 云
         * wd : 22
         */

        private int sj;
        private String fl;
        private String fx;
        private String mc;
        private int wd;

        public int getSj() {
            return sj;
        }

        public void setSj(int sj) {
            this.sj = sj;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getMc() {
            return mc;
        }

        public void setMc(String mc) {
            this.mc = mc;
        }

        public int getWd() {
            return wd;
        }

        public void setWd(int wd) {
            this.wd = wd;
        }
    }
}

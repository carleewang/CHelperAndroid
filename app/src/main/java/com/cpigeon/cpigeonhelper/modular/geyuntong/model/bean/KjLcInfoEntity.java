package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

/**
 * Created by Administrator on 2018/3/1.
 */

public class KjLcInfoEntity {
    private String kongju;//double类型，空距（单位：KM）
    private String licheng;//int类型，里程（单位：米）

    public String getKongju() {
        return kongju;
    }

    public void setKongju(String kongju) {
        this.kongju = kongju;
    }

    public String getLicheng() {
        return licheng;
    }

    public void setLicheng(String licheng) {
        this.licheng = licheng;
    }
}

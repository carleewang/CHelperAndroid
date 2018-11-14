package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class ImgVideoViewImpl implements ImgVideoView {
    @Override
    public void getTagData(List<TagEntitiy> datas) {

    }

    @Override
    public void uploadSucceed() {

    }

    @Override
    public void uploadFail(String msg) {

    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

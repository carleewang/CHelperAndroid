package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;

import java.util.List;

/**
 * 上传图片视频View层
 * Created by Administrator on 2017/10/18.
 */

public interface ImgVideoView extends IView {

    void getTagData(List<TagEntitiy> datas);//获取标签数据

    void uploadSucceed();//上传成功

    void uploadFail(String  msg);//上传失败

}

package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTOpenAndRenewEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public interface XGTView extends IView {

    void isUploadIdCardInfo(ApiResponse apiResponse);//上传身份证结果

    void isXGTInfo(ApiResponse<XGTEntity> apiResponse, String msg);//训鸽通信息

    //训鸽通升级续费回调
    void getXTGOpenAndRenewInfo(List<XGTOpenAndRenewEntity> openInfo, List<XGTOpenAndRenewEntity> renewInfo, String msg);

}

package com.cpigeon.cpigeonhelper.modular.menu.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.LogbookEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface LogbookView extends IView {
    void getDatas(ApiResponse<List<LogbookEntity>> datas, String msg, Throwable mThrowable);
}

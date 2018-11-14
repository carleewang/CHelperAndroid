package com.cpigeon.cpigeonhelper.message;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;


/**
 * Created by Zhu TingYu on 2018/1/9.
 */

public class CPAPIHttpUtil<T> extends HttpUtil<T> {

    public static <T> HttpUtil<T> build() {
        HttpUtil<T> httpUtil = HttpUtil.builder();
        httpUtil.addHeader("u", AssociationData.getUserToken());
        httpUtil.setHeadUrl(MyApplication.getContext().getString(R.string.api_head_cpapi_url));
        httpUtil.addQueryString("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        httpUtil.setUserId("u", String.valueOf(AssociationData.getUserId()));
        return httpUtil;
    }
}

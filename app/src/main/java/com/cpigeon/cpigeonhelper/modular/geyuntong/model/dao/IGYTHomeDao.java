package com.cpigeon.cpigeonhelper.modular.geyuntong.model.dao;

import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */

public interface IGYTHomeDao extends IBaseDao{
    void dowdGYTData(String token,Map<String, Object> params);//获取我的鸽运通信息
    void downGYTStatisticalData(String token,Map<String, Object> params);//获取鸽运通用户的统计信息
}

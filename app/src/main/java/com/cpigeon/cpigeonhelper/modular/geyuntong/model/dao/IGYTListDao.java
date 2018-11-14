package com.cpigeon.cpigeonhelper.modular.geyuntong.model.dao;

import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/27.
 */

public interface IGYTListDao extends IBaseDao{
    void downGYTRaceList(String userToken, Map<String, Object> urlParams);
}

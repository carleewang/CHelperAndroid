package com.cpigeon.cpigeonhelper.modular.lineweather.presenter;


import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.UllageToolEntity;
import com.cpigeon.cpigeonhelper.modular.lineweather.model.bean.GetGongPengListEntity;
import com.cpigeon.cpigeonhelper.modular.lineweather.model.bean.GetSiFangDiEntity;
import com.cpigeon.cpigeonhelper.modular.lineweather.view.viewdeo.ILineWeatherView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AssociationListEntity;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/7.
 */

public class LineWeatherPresenter extends BasePresenter {

    public LineWeatherPresenter(IView mView) {
        super(mView);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    //获取公棚坐标信息
    public void getTool_GetGongPengInfo(String str, Consumer<List<GetGongPengListEntity>> consumer) {
        submitRequestThrowError(ILineWeatherView.getTool_GetGongPengInfoData(str).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }


    //获取协会信息
    public void getAssociationInfo(String str,String c,String p, Consumer<List<AssociationListEntity>> consumer) {
        submitRequestThrowError(ILineWeatherView.getAssociationInfoData(str, p,c).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }


    //获取司放地信息
    public void getTool_GetSiFangDi(String str, Consumer<List<GetSiFangDiEntity>> consumer) {
        submitRequestThrowError(ILineWeatherView.getTool_GetSiFangDiData(str).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }

    //获取司放地信息
    public void getKongJuData(Map<String, String> body, Consumer<UllageToolEntity> consumer) {
        submitRequestThrowError(ILineWeatherView.getKongju(body).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return null;
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }

}

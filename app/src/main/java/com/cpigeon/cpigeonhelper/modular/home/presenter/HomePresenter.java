package com.cpigeon.cpigeonhelper.modular.home.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.AppInfoEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.DiZhenEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;
import com.cpigeon.cpigeonhelper.modular.home.model.dao.IHomeDao;
import com.cpigeon.cpigeonhelper.modular.home.model.daoimpl.HomeImpl;
import com.cpigeon.cpigeonhelper.modular.home.view.viewdao.IHomeView;
import com.cpigeon.cpigeonhelper.utils.CommonTool;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/5.
 */

public class HomePresenter extends BasePresenter<IHomeView, HomeImpl> {


    public HomePresenter(IHomeView mView) {
        super(mView);
    }

    @Override
    protected HomeImpl initDao() {
        return new HomeImpl();
    }


    /**
     * 获取头部数据
     */
    public void getHeadData() {
        HomeImpl homeAds = new HomeImpl();
        homeAds.downBannerData();//开始下载数据
        homeAds.getdata = new IHomeDao.GetDownData() {

            @Override
            public void getdata(List<HomeAd> homeAds) {
                mView.getHomeAdData(homeAds);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取鸽运通比赛列表
     */
    public void getGYTRaceList(int ps, int pi) {
        Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("uid", AssociationData.getUserId());
        urlParams.put("type", AssociationData.getUserType());
        urlParams.put("ps", 3);
        urlParams.put("pi", 1);

        mDao.downGYTRaceList(AssociationData.getUserToken(), urlParams);
        mDao.getServerData = new IBaseDao.GetServerData<List<GeYunTong>>() {
            @Override
            public void getdata(ApiResponse<List<GeYunTong>> listApiResponse) {

                switch (listApiResponse.getErrorCode()) {
                    case 0://请求成功
                        mView.getGYTRaceLists(listApiResponse.getData());
                        break;
                    case 1000://用户Id校验失败
                        mView.getErrorNews("用户Id校验失败");
                        break;
                    case 20001://组织类型错误
                        mView.getErrorNews("组织类型错误");
                        break;
                    case 20002://没有关联的协会（公棚）信息
//                        Log.d(TAG, "getdata: 没有关联的协会（公棚）信息");
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 单独 获取地震内容  舍弃
     */
    public void getDiZhenInfo() {
        mDao.getDizhen();
        mDao.getDiZhenInfo = new IBaseDao.GetServerDatas<DiZhenEntity>() {
            @Override
            public void getdata(DiZhenEntity stringApiResponse) {
                Log.d("homedata", "3   " + stringApiResponse.toJsonString());
                if (stringApiResponse.isSuccess()) {
                    mView.diZhenInfo(stringApiResponse.getData());
                } else {
                    mView.diZhenInfo("暂未获取到地震信息");
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 单独获取磁暴内容  舍弃
     */
    public void getCiBao() {
        mDao.getCiBao();
        mDao.getCiBaoInfo = new IBaseDao.GetServerDatas<DiZhenEntity>() {
            @Override
            public void getdata(DiZhenEntity stringApiResponse) {
                Log.d("homedata", "2   " + stringApiResponse.toJsonString());
                if (stringApiResponse.isSuccess()) {
                    mView.ciBaoInfo(stringApiResponse.getData());
                } else {
                    mView.ciBaoInfo("暂未获取到地震信息");
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取地震和磁暴内容
     */
    public void getDiZhenCiBao() {
        mDao.getDiZhenCiBao();
        mDao.getDiZhenCiBaoInfo = new IBaseDao.GetServerDatas<DiZhenEntity>() {
            @Override
            public void getdata(DiZhenEntity stringApiResponse) {
                Log.d("homedata", "1   " + stringApiResponse.toJsonString());
                if (stringApiResponse.isSuccess()) {
                    mView.diZhenCiBaoInfo(stringApiResponse.getData());
                } else {
                    mView.diZhenCiBaoInfo("暂未获取到地震和磁暴信息");
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

//----------------------------------------------------------------------------------------------------------------------------

    /**
     * 显示广告
     */
    public MZBannerView showAd2(List<HomeAd> homeAds, MZBannerView mBanner, Context mContext) {
        List<HomeAd> data = new ArrayList<>();
        data.clear();
        for (HomeAd dd : homeAds) {
            if (dd.getType().equals("2")) {
                data.add(dd);
            }
        }

        mBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {

                if (data != null && data.size() > 0) {
                    //点击广告
                    String url = data.get(position).getAdUrl();
                    Log.d(TAG, "onPageClick1: " + position);
                    Log.d(TAG, "onPageClick2: " + url);

                    //判断是不是网站URL
                    if (CommonTool.Compile(url, CommonTool.PATTERN_WEB_URL)) {
                        Intent intent1 = new Intent(mContext, WebViewActivity.class);
                        intent1.putExtra(WebViewActivity.EXTRA_URL, url);
                        mContext.startActivity(intent1);
                    } else {

                    }
                }
            }
        });

        mBanner.setPages(data, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder(mContext);
            }
        });

        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        return mBanner;
    }

    //APP使用信息设置 统计数据
    public void setAppInfo() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("t", "中鸽助手APP");//类型：中鸽助手APP|中鸽网APP
        postParams.put("ly", "安卓");//来源：安卓|苹果
        postParams.put("sb", AssociationData.DEV);//设备信息

        mDao.setAppInfoData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getAppInfoData = new IBaseDao.GetServerData<AppInfoEntity>() {
            @Override
            public void getdata(ApiResponse<AppInfoEntity> listApiResponse) {
                Log.d("denglu", "getdata: " + listApiResponse.toJsonString());
                mView.appInfoDataReturn(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.appInfoDataReturn(null, null, throwable);
            }
        };
    }
}

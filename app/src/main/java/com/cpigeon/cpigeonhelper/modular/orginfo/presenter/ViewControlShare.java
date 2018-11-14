package com.cpigeon.cpigeonhelper.modular.orginfo.presenter;

import android.content.Context;
import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.fragment.ShareDialogFragment;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.UserInfoView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ViewControlShare {

    //分享结果回调  下载
    public static UMShareListener getShareResultsDown(Context mContext, ShareDialogFragment dialogFragment, String type) {

        return new UMShareListener() {

            UserInfoView mUserInfoView = new UserInfoView() {
                @Override
                public void getMyInfoData(ApiResponse<MyInfoEntity> myInfoApiResponse, String msg) {

                }

                @Override
                public void getGbmxData(ApiResponse<List<GbListEntity>> myInfoApiResponse, String msg, Throwable mThrowable) {

                }

                @Override
                public void getShareCodeData(ApiResponse<ShareCodeEntity> myInfoApiResponse, String msg) {

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
            };
            //分享的是图片
            UserInfoPresenter userInfoPresenter = new UserInfoPresenter(mUserInfoView);

            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d("xiaohlshare", "onResult: 0-->"+share_media);
                dialogFragment.dismiss();
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

                try {
                    if (!type.equals("")) {
                        if (type.equals("tp")) {
                            userInfoPresenter.getShareImgVideo("tp");
                        } else if (type.equals("sp")) {
                            //分享的是视频
                            userInfoPresenter.getShareImgVideo("sp");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("xiaohlshare", "onResult: 1-->"+share_media);
                CommonUitls.showSweetDialog(mContext, "分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Log.d("xiaohlshare", "onResult: 2-->"+throwable.getLocalizedMessage());
//                CommonUitls.showSweetDialog(mContext, "分享失败" + throwable.getLocalizedMessage());
                CommonUitls.showSweetDialog(mContext, "分享失败！可能您分享的平台没有安装。");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Log.d("xiaohlshare", "onResult: 3->"+share_media);
                CommonUitls.showSweetDialog(mContext, "取消分享");
            }
        };
    }


}

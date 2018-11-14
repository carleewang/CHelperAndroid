package com.cpigeon.cpigeonhelper.modular.guide.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.guide.presenter.SplashPresenter;
import com.cpigeon.cpigeonhelper.modular.guide.view.viewdao.ISplashView;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.AppInfoEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;
import com.cpigeon.cpigeonhelper.modular.home.presenter.HomePresenter;
import com.cpigeon.cpigeonhelper.modular.home.view.activity.HomeNewActivity2;
import com.cpigeon.cpigeonhelper.modular.home.view.viewdao.IHomeView;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.LoginActivity;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.SharedPreferencesTool;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 闪图页
 * Created by Administrator on 2017/9/5.
 */

public class SplashActivity extends BaseActivity implements ISplashView, IHomeView {

    private boolean isOwnDev = false;//用户是否已经登录变量

    private SplashPresenter presenter;//控制层

    private HomePresenter homePresenter;//控制层

    @BindView(R.id.imageView)
    ImageView splashImg;//闪图图片

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 1);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        homePresenter = new HomePresenter(this);//加载闪图图片
        homePresenter.getHeadData();

        presenter = new SplashPresenter(this);//初始化控制层
        presenter.isLogin();
        startActivity();//开始跳转

        //读取缓存图片作为闪图
        String s = SharedPreferencesTool.Get(this, "SplashImgCache", "", SharedPreferencesTool.SPLASH_IMG_CACHE);
        Log.d("myimgcache", "initView缓存图片地址:1 " + s);
        if (!s.isEmpty()) {
            Log.d("myimgcache", "initView缓存图片地址:2 ");
            Glide.with(mContext)
                    .load(s)
                    .into(splashImg);
        }
    }

    @Override
    public void getHomeAdData(List<HomeAd> homeAds) {

        if (homeAds.size() != 0) {//有头部数据
            //设置闪图图片
            for (int i = 0; i < homeAds.size(); i++) {
                if (homeAds.get(i).getType().equals("3")) {

                    SharedPreferencesTool.Save(this, "SplashImgCache", homeAds.get(i).getAdImageUrl(), SharedPreferencesTool.SPLASH_IMG_CACHE);
                    Glide.with(mContext)
                            .load(homeAds.get(i).getAdImageUrl())
                            .into(splashImg);
                    break;
                }
            }
        } else {
            //无头部数据

        }
    }

    /**
     * 耗时
     */
    private void startActivity() {
        Observable.timer(4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> isPermission());
    }

    /**
     * 跳转
     */
    private void isPermission() {
        finishTask();
    }

    public void finishTask() {
//        if (AssociationData.checkIsLogin() && isOwnDev) {
//            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//        } else {
//            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//        }


        if (!RealmUtils.getInstance().existIsLoginAppEntity()) {
            //是否第一次进入app
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        } else {
            if (AssociationData.checkIsLogin() && isOwnDev) {
                startActivity(new Intent(SplashActivity.this, HomeNewActivity2.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }


        AppManager.getAppManager().killActivity(mWeakReference);
    }

    /**
     * 上一次是否已经登录过账号
     *
     * @param type
     */
    @Override
    public void isLastLogin(int type) {
        switch (type) {
            case 1://已经登录过,跳转到主页
                isOwnDev = true;
                break;
            case 2://用户没有登录过账号，跳转到登录页
                isOwnDev = false;
                break;
        }
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

    /**
     * 获取错误消息
     *
     * @param str
     */
    @Override
    public void getErrorNews(String str) {
        CommonUitls.showToast(this, str); //提示用户错误消息
    }


    /**
     * 异常相关
     *
     * @param throwable
     */
    @Override
    public void getThrowable(Throwable throwable) {
        try {
            CommonUitls.exceptionHandling(this, throwable);

            if (throwable instanceof SocketTimeoutException) {
                CommonUitls.showToast(this, "连接超时");

            } else if (throwable instanceof ConnectException) {

            } else if (throwable instanceof RuntimeException) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getGYTRaceLists(List<GeYunTong> geYunTongDatas) {

    }

    @Override
    public void diZhenInfo(String diZhenStr) {

    }

    @Override
    public void ciBaoInfo(String ciBaoStr) {

    }

    @Override
    public void diZhenCiBaoInfo(String ciBaoStr) {

    }

    @Override
    public void appInfoDataReturn(ApiResponse<AppInfoEntity> listApiResponse, String msg, Throwable mThrowable) {

    }
}

package com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.authorise.view.activity.AddNewAuthActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTongs;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TabEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTMonitorPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.PigeonMonitorActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter.PigMonFgmtAdapter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.MonitorViewImpl;
import com.cpigeon.cpigeonhelper.service.DetailsService1;
import com.cpigeon.cpigeonhelper.ui.CustomViewPager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 鸽车监控总的fragment
 * Created by Administrator on 2017/10/10.
 */

public class PigeonMonitorFragment extends BaseFragment {

    //    @BindView(R.id.common_tabs)
    public static CommonTabLayout commonTabLayout;//导航条
    //    @BindView(R.id.view_pager)
    public static CustomViewPager mViewPager;//ViewPager
    public static FrameLayout ff_tag_z;//ViewPager
    @BindView(R.id.prompt_img)
    ImageView promptImg;
    @BindView(R.id.prompt_btn)
    TextView promptBtn;
    @BindView(R.id.prompt_btn2)
    TextView promptBtn2;
    @BindView(R.id.prompt_ll)
    LinearLayout promptLl;//提示内容展示

    private PigMonFgmtAdapter mPigMonFgmtAdapter;


    public boolean blakTag = false;//activity按back 提示监控比赛不能退出

    private GYTMonitorPresenter presenter;//控制层


    private int[] mIconUnselectIds = {
            R.mipmap.jiankong, R.mipmap.zhaopian,
            R.mipmap.shipin};
    private int[] mIconSelectIds = {
            R.mipmap.jiankong_on, R.mipmap.zhaopian_on,
            R.mipmap.shipin_on};


    private int showViewTag = -1;// -1 没有显示地图  1  开始比赛成功

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    public static PigeonMonitorFragment newInstance() {
        return new PigeonMonitorFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_pigeon_monitor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        if (PigeonMonitorActivity.isLoadFrist) {
            PigeonMonitorActivity.isLoadFrist = false;
        } else {
            container.removeAllViews();
        }
        return super.onCreateView(inflater, container, state);
    }

    @Override
    public void finishCreateView(Bundle state) {

        mViewPager = null;

        if (PigeonMonitorActivity.tags == 1) {
            commonTabLayout = (CommonTabLayout) parentView.findViewById(R.id.common_tabs);
            mViewPager = (CustomViewPager) parentView.findViewById(R.id.view_pager);
            ff_tag_z = (FrameLayout) parentView.findViewById(R.id.ff_tag_z);

            mViewPager.setVisibility(View.VISIBLE);
            mViewPager.setScanScroll(true);
            promptLl.setVisibility(View.GONE);//隐藏错误视图内容

            initViewPager();

            blakTag = true;
            LogUtil.print("jian kong load 1");
        } else if (PigeonMonitorActivity.tags == -1) {
            LogUtil.print("jian kong load 2");
            commonTabLayout = (CommonTabLayout) parentView.findViewById(R.id.common_tabs);
            mViewPager = (CustomViewPager) parentView.findViewById(R.id.view_pager);
            ff_tag_z = (FrameLayout) parentView.findViewById(R.id.ff_tag_z);

            presenter = new GYTMonitorPresenter(new MonitorViewImpl() {
                //开启比赛结果
                @Override
                public void openMonitorResults(ApiResponse<Object> dataApiResponse, String msg) {
                    try {
                        if (dataApiResponse.getErrorCode() == 0) {
                            //开启比赛成功
                            startSucceed();
                            //发布事件（通知比赛列表刷新数据）
                            EventBus.getDefault().post("playListRefresh");
                        } else {
                            CommonUitls.showSweetDialog(getActivity(), msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            initViewPager();
            initViews(MonitorData.getMonitorStateCode());//初始化视图展示

            if (RealmUtils.getServiceType().equals("geyuntong")) {
                //鸽运通
                initButtonView(MonitorData.getMonitorStateSq());
            } else if (RealmUtils.getServiceType().equals("xungetong")) {
                //训鸽通
                promptBtn2.setVisibility(View.GONE);
            }

        }
    }

    //初始化授权按钮
    private void initButtonView(int sqState) {
        Intent intent = new Intent(getActivity(), AddNewAuthActivity.class);
        Log.d("sqState", "initButtonView: " + sqState);
        switch (sqState) {
            case 1://自己创建的比赛  未开启监控
                promptBtn2.setText("授权监控");
                promptBtn2.setOnClickListener(view -> {
                    intent.putExtra("title", "添加授权");
                    intent.putExtra("MonitorGeYunTongs", MonitorData.getMonitorGeYunTongs());//比赛id
                    getActivity().startActivity(intent);
                });
                break;
            case 2://自己创建的比赛， 开启监控
                promptBtn2.setText(R.string.str_bgsq);
                promptBtn2.setOnClickListener(view -> {
                    intent.putExtra("title", "变更授权");
                    intent.putExtra("MonitorGeYunTongs", MonitorData.getMonitorGeYunTongs());//比赛id
                    getActivity().startActivity(intent);
                });
                break;
            case 3://接受授权状态   授权方  未开始监控
                promptBtn2.setText(R.string.str_bgsq);
                promptBtn2.setOnClickListener(view -> {
                    intent.putExtra("title", "变更授权");
                    intent.putExtra("MonitorGeYunTongs", MonitorData.getMonitorGeYunTongs());//比赛id
                    getActivity().startActivity(intent);
                });
                break;
            case 4:////接受授权状态   授权方  开启监控
                promptBtn2.setVisibility(View.GONE);
                break;
            case 5://未接受授权状态   授权方  未开始监控
                promptBtn2.setText(R.string.str_bgsq);
                promptBtn2.setOnClickListener(view -> {
                    intent.putExtra("title", "变更授权");
                    intent.putExtra("MonitorGeYunTongs", MonitorData.getMonitorGeYunTongs());//比赛id
                    getActivity().startActivity(intent);
                });
                break;
            case 9://接受比赛  被授权方  未开始监控
                promptBtn2.setVisibility(View.GONE);
                break;
            case 10://接受比赛  被授权方  开始监控
                promptBtn2.setVisibility(View.GONE);
                break;
            default:
                promptBtn2.setVisibility(View.GONE);
        }
    }

    private void initViews(int stateCode) {
        //【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
        switch (stateCode) {
            case 0:
//                getActivity().startService(new Intent(getActivity(), CoreService.class));//hl 开始服务，长连接
                getActivity().startService(new Intent(getActivity(), DetailsService1.class));//hl 开始服务，长连接
                DetailsService1.isStartLocate = false;
                promptLl.setVisibility(View.VISIBLE);//隐藏错误视图内容
                mViewPager.setVisibility(View.GONE);//viewPager滑动

                promptImg.setImageResource(R.mipmap.start_monitor_hint);
                promptBtn.setText("开始监控");
                break;
            case 1:
//                getActivity().startService(new Intent(getActivity(), CoreService.class));//hl 开始服务，长连接
                getActivity().startService(new Intent(getActivity(), DetailsService1.class));//hl 开始服务，长连接
                promptLl.setVisibility(View.VISIBLE);//隐藏错误视图内容
                mViewPager.setVisibility(View.GONE);//viewPager滑动
                promptImg.setImageResource(R.mipmap.continue_monitor_hint);
                promptBtn.setText("继续监控");
                break;
            case 2:
                promptLl.setVisibility(View.GONE);//隐藏错误视图内容
                mViewPager.setVisibility(View.VISIBLE);//viewPager滑动
                break;
            case 3:
                promptLl.setVisibility(View.GONE);//隐藏错误视图内容
                mViewPager.setVisibility(View.VISIBLE);//viewPager滑动
                break;
        }
    }

    @Override
    public void onDetach() {
        blakTag = false;
        super.onDetach();
    }

    private void initViewPager() {

        for (int i = 0; i < mIconUnselectIds.length; i++) {
            mTabEntities.add(new TabEntity("", mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mViewPager.setOffscreenPageLimit(3);//预加载
        mPigMonFgmtAdapter = new PigMonFgmtAdapter(getChildFragmentManager(), getActivity());
        mPigMonFgmtAdapter.notifyDataSetChanged();

        mViewPager.setAdapter(mPigMonFgmtAdapter);
        mViewPager.getAdapter().notifyDataSetChanged();
        commonTabLayout.setTabData(mTabEntities);
//        commonTabLayout.showDot(2); //显示未读红点
        //切换fragment，导航条变化
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("sqjk", "滑动viewpager: " + position);
                commonTabLayout.setCurrentTab(position);

//                smallVideoHelper.releaseVideoPlayer();
                GSYVideoPlayer.releaseAllVideos();

//                JCVideoPlayerStandard.releaseAllVideos();//切换页面去掉后台播放的视频
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //点击导航条，fragment切换
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.d("sqjk", "点击导航条: " + position);
                mViewPager.setCurrentItem(position);//view默认显示第一个页面
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.setCurrentItem(0);//view默认显示第一个页面
    }


    @Override
    public void onPause() {
//        JCVideoPlayerStandard.releaseAllVideos();//切换页面去掉后台播放的视频
        GSYVideoPlayer.releaseAllVideos();
        super.onPause();
    }

    @OnClick({R.id.prompt_btn, R.id.tv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.prompt_btn://点击开始监控
                //StateCode【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
                if (MonitorData.getMonitorStateCode() == 0 || MonitorData.getMonitorStateCode() == 1) {//未开始监控状态
//                    if (CoreService.isconcatenon) {//长连接连接成功
                    if (DetailsService1.isconcatenon) {//长连接连接成功
                        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("温馨提示");
                        dialog.setContentText("开启监控后，耗电较高，请保持电量和网络通畅");
                        dialog.setCancelable(true);
                        dialog.setCancelText("取消");
                        dialog.setConfirmText("确定");
                        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                if (MonitorData.getMonitorStateCode() == 0) {
                                    presenter.startMonitor();//开启监控
                                } else if (MonitorData.getMonitorStateCode() == 1) {
                                    startSucceed();
                                }
                                dialog.dismiss();//关闭提示框
                            }
                        });
                        dialog.show();
                    } else {
                        CommonUitls.showSweetAlertDialog(getActivity(), "温馨提示", "长连接开启失败");
                    }
                } else {
                    promptLl.setVisibility(View.GONE);//隐藏错误视图内容
                    mViewPager.setVisibility(View.VISIBLE);//viewPager滑动
                }

                break;
            case R.id.tv_setting://跳转电量设置。

                Intent intent1 = new Intent(getActivity(), WebViewActivity.class);
                intent1.putExtra(WebViewActivity.EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.MONITOR_HELP);
                startActivity(intent1);

                break;
        }
    }


    /**
     * 成功开启比赛显示视图，加载数据
     */
    private void startSucceed() {
        try {
            showViewTag = 1;
            //保存点击item的比赛id
            GeYunTongs geYunTongs = new GeYunTongs();
            geYunTongs.setId(MonitorData.getMonitorId());//比赛id
            //StateCode【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
            geYunTongs.setStateCode(1);//状态码
            geYunTongs.setmEndTime("");//结束时间

//        Log.d(TAG, "监控启动时间: " + MonitorData.getMonitorMTime());
            if (MonitorData.getMonitorMTime().equals("0001-01-01 08:00:00")) {
                geYunTongs.setmTime(DateUtils.getStringDate());//监控启动时间
            } else {
                geYunTongs.setmTime(MonitorData.getMonitorMTime());//监控启动时间
            }

            geYunTongs.setLongitude(MonitorData.getMonitorFlyLo());//经度
            geYunTongs.setLatitude(MonitorData.getMonitorFlyLa());//纬度
            geYunTongs.setState("监控中");//监控状态

            //删除单条鸽运通内容
            if (!RealmUtils.getInstance().existGYTBeanInfo()) {
                RealmUtils.getInstance().insertGYTBeanInfo(geYunTongs);//添加用户数据
            } else {
                RealmUtils.getInstance().deleteGYTBeanInfo();//删除数据
                RealmUtils.getInstance().insertGYTBeanInfo(geYunTongs);//添加用户数据
            }
            blakTag = true;
            promptLl.setVisibility(View.GONE);//隐藏错误视图内容
            mViewPager.setVisibility(View.VISIBLE);//viewPager滑动
        } catch (Exception e) {
            Log.d("dingweitianqicx", "startSucceed: 异常");
            e.printStackTrace();
        }
    }
}

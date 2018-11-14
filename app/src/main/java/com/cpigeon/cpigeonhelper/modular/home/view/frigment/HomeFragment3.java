package com.cpigeon.cpigeonhelper.modular.home.view.frigment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.message.ui.home.PigeonMessageHomeFragment;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ServiceType;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTHomePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.GYTHomeActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTHomeViewImpl;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;
import com.cpigeon.cpigeonhelper.modular.home.presenter.HomePresenter;
import com.cpigeon.cpigeonhelper.modular.home.presenter.HomePresenter2;
import com.cpigeon.cpigeonhelper.modular.home.view.activity.UllageToolActivity;
import com.cpigeon.cpigeonhelper.modular.home.view.viewdao.IHomeViewImpl;
import com.cpigeon.cpigeonhelper.modular.home.view.viewdao.ViewControl;
import com.cpigeon.cpigeonhelper.modular.lineweather.view.activity.LineWeatherActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity.DesignatedListActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity.RpSmsSetActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity.SlSmsListActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity.XsSmsListActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.activity.GyjlHomeActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity.GameGcActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity.PlayAdminActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.OrgInforViewImpl;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2.GpsgHomeActivity;
import com.cpigeon.cpigeonhelper.ui.AlwaysMarqueeTextView;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.zhouwei.mzbanner.MZBannerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cpigeon.cpigeonhelper.common.db.AssociationData.getUserAccountTypeInt;


/**
 * 主页首页fragment
 * Created by Administrator on 2017/11/23.
 */

public class HomeFragment3 extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;//刷新布局

    @BindView(R.id.home_banner)
    MZBannerView mBanner;//轮播图

    @BindView(R.id.home_tv1)
    TextView homeTv1;
    @BindView(R.id.home_tv2)
    TextView homeTv2;
    @BindView(R.id.home_imgbtn1)
    ImageButton homeImgbtn1;
    @BindView(R.id.home_imgbtn2)
    ImageButton homeImgbtn2;
    @BindView(R.id.home_imgbtn3)
    ImageButton homeImgbtn3;
    @BindView(R.id.home_tv3)
    TextView homeTv3;
    @BindView(R.id.home_imgbtn4)
    ImageButton homeImgbtn4;
    @BindView(R.id.home_imgbtn5)
    ImageButton homeImgbtn5;
    @BindView(R.id.home_imgbtn6)
    ImageButton homeImgbtn6;

    @BindView(R.id.ll_hygl)
    LinearLayout ll_hygl;//会员管理布局
    @BindView(R.id.home_imgbtn7)
    ImageButton homeImgbtn7;
    @BindView(R.id.home_imgbtn8)
    ImageButton homeImgbtn8;
    @BindView(R.id.home_imgbtn9)
    ImageButton homeImgbtn9;

    @BindView(R.id.home_imgbtn10)
    ImageButton homeImgbtn10;//公棚赛鸽授权
    @BindView(R.id.ll_wdfw2)
    LinearLayout ll_wdfw2;//第四布局  我的服务  授权

    @BindView(R.id.ll1_lin3)
    LinearLayout ll1_lin3;//协会服务第三排
    @BindView(R.id.home_imgbtn13)
    ImageButton home_imgbtn13;//协会插组指定


    @BindView(R.id.tv_sggj_tv1)
    ImageButton tv_sggj_tv1;
    @BindView(R.id.tv_sggj_tv2)
    ImageButton tv_sggj_tv2;
    @BindView(R.id.tv_sggj_tv3)
    ImageButton tv_sggj_tv3;

    //我的服务
    @BindView(R.id.wdfw_lin2)
    LinearLayout wdfw_lin2;//我的服务第二排布局
    @BindView(R.id.wdfw_btn4)
    ImageButton wdfw_btn4;
    @BindView(R.id.wdfw_btn5)
    ImageButton wdfw_btn5;
    @BindView(R.id.wdfw_btn6)
    ImageButton wdfw_btn6;

    @BindView(R.id.tv_always_marquee)
    AlwaysMarqueeTextView mAlwaysMarquee;//地震显示

    private TextView ciBao, diZhen;//磁暴，地震
    private Button dialogDetermine;//diaolog确定按钮

    private HomePresenter mHomePresenter;//控制层
    private OrgInforPresenter mOrgInforPresenter;
    private GYTHomePresenter mGYTHomePresenter;//

    private int orgType = 0;//1：公棚 2：协会  3：个人
    private int orgTypes = 0;//1：公棚 2：协会  3：个人
    private int clickTag = -1;// 1：点击鸽运通   2：点击赛鸽通

    private CustomAlertDialog dialog;
    private LinearLayout dialogLayout;//dialog总布局

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home3;
    }

    @Override
    public void finishCreateView(Bundle state) {

        mHomePresenter = new HomePresenter(new IHomeViewImpl() {
            //地震和磁暴信息
            @Override
            public void diZhenCiBaoInfo(String ciBaoStr) {
                Log.d("dizheng", "diZhenInfo: 3");

                try {
                    mAlwaysMarquee.setText(ciBaoStr);
                    String[] str = ciBaoStr.split("\\*");
                    diZhen.setText(str[0].trim());
                    ciBao.setText(str[1].trim());
                } catch (Exception e) {

                    RxUtils.delayed(10000, aLong -> {
                                mHomePresenter.getDiZhenCiBao();//获取地震磁暴信息
                            }
                    );
                    Log.d(TAG, "diZhenCiBaoInfo: " + e.getLocalizedMessage());
                }
            }

            /**
             * 轮播数据获取完成
             */
            @Override
            public void getHomeAdData(List<HomeAd> homeAds) {
                if (homeAds.size() != 0) {
                    mBanner = mHomePresenter.showAd2(homeAds, mBanner, getContext());//加载轮播图
                } else {

                }
            }
        });//初始化控制层、
        mGYTHomePresenter = new GYTHomePresenter(new GYTHomeViewImpl() {
        });

        initDialog();//初始化点击轮播文字dialog

        initRefreshLayout();
    }

    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            ll_wdfw2.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(true);
            initView();
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            ll_wdfw2.setVisibility(View.GONE);
            mOrgInforPresenter.getServiceUserType();//获取组织类型
        });
    }

    private void initView() {
        mHomePresenter.getHeadData();//获取头部数据
        mHomePresenter.getDiZhenCiBao();//获取地震磁暴信息

        mGYTHomePresenter.gytServerDatas(AssociationData.getUserAccountTypeStrings(), "geyuntong");// 获取鸽运通服务数据
        mOrgInforPresenter = new OrgInforPresenter(new OrgInforViewImpl() {
            /**
             * 获取组织信息成功回调
             */
            @Override
            public void validationSucceed(OrgInfo data) {
                homeTv2.setText(data.getName());
            }

            //获取用户类型判断（当前用户类型,是否授权）
            @Override
            public void getUserTypeSuccend(ApiResponse<UserType> userTypeApiResponse, String msg, Throwable mThrowable) {
                try {
                    HomePresenter2.stopSRL(mSwipeRefreshLayout);
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                    if (clickTag == 2) {
                        HomePresenter2.isOpenSGT(userTypeApiResponse, getActivity());//跳转赛鸽通判断
                    } else if (clickTag == 3) {
                        HomePresenter2.isOpenGYTs(userTypeApiResponse.getData(), getActivity());//跳转鸽运通判断
                    } else if (clickTag == -1) {
                        if (userTypeApiResponse.getData().getAuthusers() == 1) {
                            ll_wdfw2.setVisibility(View.VISIBLE);
                            if (userTypeApiResponse.getData().getSqpzuid() > 0) {
                                homeImgbtn10.setImageResource(R.mipmap.shouquanpz);
                                homeImgbtn10.setOnClickListener(view -> {
                                    Intent intent = new Intent(getActivity(), GpsgHomeActivity.class);
                                    intent.putExtra("sqpzuid", userTypeApiResponse.getData().getSqpzuid());//授权拍照  id
                                    startActivity(intent);
                                });
                            }
                        }
                    } else if (clickTag == 4) {
                        //会员管理
                        HomePresenter2.isOpenWdhy(userTypeApiResponse, getActivity());
                    }
                    clickTag = -1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (getUserAccountTypeInt() == 1 || getUserAccountTypeInt() == 2) {
            mOrgInforPresenter.getOrgInforData();//获取组织信息数据
        } else {
            homeTv2.setText(AssociationData.getUserName());
        }

        mOrgInforPresenter.getServiceUserType();//获取组织类型

        orgTypes = getUserAccountTypeInt();

        //把授权用户当做个人用户 处理
        if (orgTypes == 4 || orgTypes == 5) {
            orgType = 3;
        } else {
            orgType = orgTypes;
        }

        if (orgType == 1) {
            //公棚
            ll_hygl.setVisibility(View.VISIBLE);
            wdfw_lin2.setVisibility(View.VISIBLE);
        }

        if (orgType == 2) {
            //协会
            ll_hygl.setVisibility(View.VISIBLE);
            ll1_lin3.setVisibility(View.VISIBLE);
            wdfw_lin2.setVisibility(View.VISIBLE);
        }

        ViewControl.initView3(orgType, homeTv1, homeTv3, homeImgbtn1, homeImgbtn2, homeImgbtn3, homeImgbtn4,
                homeImgbtn5, homeImgbtn6, homeImgbtn7, homeImgbtn8, homeImgbtn9, homeImgbtn10, home_imgbtn13,
                tv_sggj_tv1, tv_sggj_tv2, tv_sggj_tv3, wdfw_btn4, wdfw_btn5, wdfw_btn6);
    }

    /**
     * 初始化点击轮播文字的dialog
     */
    private void initDialog() {

        dialogLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_home_dialog, null);
        diZhen = (TextView) dialogLayout.findViewById(R.id.dialog_dizhen);
        ciBao = (TextView) dialogLayout.findViewById(R.id.dialog_cibao);
        dialogDetermine = (Button) dialogLayout.findViewById(R.id.dialog_determine);
        dialogDetermine.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog = new CustomAlertDialog(getActivity());
        dialog.setContentView(dialogLayout);
        //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBanner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        mBanner.start();//开始轮播
    }

    private Intent intent;

    //orgType = 0;//1：公棚 2：协会  3：个人
    @OnClick({R.id.home_imgbtn1, R.id.home_imgbtn2, R.id.home_imgbtn3, R.id.home_imgbtn4, R.id.home_imgbtn5,
            R.id.home_imgbtn6, R.id.home_imgbtn7, R.id.home_imgbtn8, R.id.home_imgbtn9, R.id.home_imgbtn13,
            R.id.tv_always_marquee, R.id.tv_sggj_tv1, R.id.tv_sggj_tv2, R.id.tv_sggj_tv3, R.id.wdfw_btn4})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }
        switch (view.getId()) {
            case R.id.home_imgbtn1:
                switch (orgType) {
                    case 1://公棚     入棚短信
                        startActivity(new Intent(getActivity(), RpSmsSetActivity.class));
                        break;
                    case 2://协会
                        // 短信设置
                        intent = new Intent(getActivity(), PlayAdminActivity.class);
                        intent.putExtra("service_type", 2);
                        startActivity(intent);
                        break;
                    case 3://个人     鸽运通
                        RealmUtils.preservationServiceType(new ServiceType("xungetong"));
                        startActivity(new Intent(getActivity(), GYTHomeActivity.class));//跳转到训鸽通列表页面
                        break;
                }

                break;
            case R.id.home_imgbtn2:
                switch (orgType) {
                    case 1://公棚     训赛短信
                        startActivity(new Intent(getActivity(), XsSmsListActivity.class));
                        break;
                    case 2://协会
                        //插组指定
                        intent = new Intent(getActivity(), DesignatedListActivity.class);
                        intent.putExtra("serviceType", 1);//协会插组指定
                        startActivity(intent);


                        break;
                    case 3://个人
                        PigeonMessageHomeFragment.startPigeonMessageHome(getActivity());
                        break;
                }
                break;
            case R.id.home_imgbtn3:
                switch (orgType) {
                    case 1://公棚     上笼短信
                        startActivity(new Intent(getActivity(), SlSmsListActivity.class));
                        break;
                    case 2://协会
                        // 比赛管理
                        intent = new Intent(getActivity(), PlayAdminActivity.class);
                        intent.putExtra("service_type", 1);
                        startActivity(intent);

                        break;
                    case 3://个人
                        clickTag = 3;
                        mLoadDataDialog.show();
                        mOrgInforPresenter.getServiceUserType();//获取组织类型
                        break;
                }

                break;
            case R.id.home_imgbtn4:
                switch (orgType) {
                    case 1://公棚 赛鸽通
                        clickTag = 2;
                        mLoadDataDialog.show();
                        mOrgInforPresenter.getServiceUserType();//获取组织类型
                        break;
                    case 2://协会 鸽运通
                        clickTag = 3;
                        mLoadDataDialog.show();
                        mOrgInforPresenter.getServiceUserType();//获取组织类型
                        break;
                    case 3://个人 中鸽网
                        startCpigeon();
                        break;
                }

                break;
            case R.id.home_imgbtn5:
                switch (orgType) {
                    case 1://公棚  鸽信通
                        PigeonMessageHomeFragment.startPigeonMessageHome(getActivity());
                        break;
                    case 2://协会 鸽信通
                        PigeonMessageHomeFragment.startPigeonMessageHome(getActivity());
                        break;

                    case 3://个人 足环查询
                        Uri uri = Uri.parse(ApiConstants.BASE_URL + ApiConstants.GRFW_ZHCX);
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                }

                break;
            case R.id.home_imgbtn6:
                switch (orgType) {
                    case 1://公棚 鸽运通
                        clickTag = 3;
                        mLoadDataDialog.show();
                        mOrgInforPresenter.getServiceUserType();//获取组织类型
                        break;
                    case 2://协会  训鸽通
                        RealmUtils.preservationServiceType(new ServiceType("xungetong"));
                        startActivity(new Intent(getActivity(), GYTHomeActivity.class));//跳转到鸽运通列表页面
                        break;
                    case 3://个人  天下鸽谱
                        Uri uri = Uri.parse(ApiConstants.BASE_URL + ApiConstants.GRFW_TXGP);
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.home_imgbtn7:
                switch (orgType) {
                    case 1://公棚
                        //插组别名指定
                        intent = new Intent(getActivity(), DesignatedListActivity.class);
                        intent.putExtra("serviceType", 2);//协会插组指定
                        startActivity(intent);
                        break;
                    case 2://协会
                        //  赛事规程
                        intent = new Intent(getActivity(), GameGcActivity.class);
                        intent.putExtra("type", "ssgc");
                        startActivity(intent);

                        break;
                    case 3://个人

                        break;
                }

                break;
            case R.id.home_imgbtn8:

                switch (orgType) {
                    case 1://公棚

                        break;
                    case 2://协会
                        // 协会动态
                        intent = new Intent(getActivity(), GameGcActivity.class);
                        intent.putExtra("type", "xhdt");
                        startActivity(intent);

                        break;
                    case 3://个人

                        break;
                }
                break;
            case R.id.home_imgbtn9:

                switch (orgType) {
                    case 1://公棚

                        break;
                    case 2://协会
                        //会员管理
                        clickTag = 4;
                        mLoadDataDialog.show();
                        mOrgInforPresenter.getServiceUserType();//获取组织类型
                        break;
                    case 3://个人

                        break;
                }

                break;

            case R.id.home_imgbtn13:
                switch (orgType) {
                    case 1://公棚

                        break;
                    case 2://协会
                        //鸽友交流
                        intent = new Intent(getActivity(), GyjlHomeActivity.class);
                        startActivity(intent);

                        break;
                    case 3://个人

                        break;
                }
                break;

            case R.id.wdfw_btn4:
                //我的服务
                switch (orgType) {
                    case 1://公棚 中鸽网
                        startCpigeon();
                        break;
                    case 2://协会  赛线天气
                        startActivity(new Intent(getActivity(), LineWeatherActivity.class));
                        break;
                    case 3://个人  赛线天气
                        startActivity(new Intent(getActivity(), LineWeatherActivity.class));
                        break;
                }

                break;
            case R.id.tv_sggj_tv1:
                //赛线天气
                startActivity(new Intent(getActivity(), LineWeatherActivity.class));
                break;

            case R.id.tv_sggj_tv2:
                //空距计算
                startActivity(new Intent(getActivity(), UllageToolActivity.class));
                break;

            case R.id.tv_sggj_tv3:

                break;
            case R.id.tv_always_marquee://点击地震轮播文字
                dialog.show();
                break;
        }
    }

    //跳转中鸽网
    private void startCpigeon() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.cpigeon.app");
            startActivity(intent);

        } catch (Exception e) {
            Log.d("xiaohlsta", "startCpigeon: 异常" + e.getLocalizedMessage());

            Uri uri = Uri.parse(ApiConstants.BASE_URL + ApiConstants.GRFW_ZGW_APP);
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
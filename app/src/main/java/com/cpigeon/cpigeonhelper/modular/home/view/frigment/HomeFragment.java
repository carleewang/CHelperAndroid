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
import android.widget.ImageView;
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
import com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.ShootVideoSettingActivity;
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
 * Created by Administrator on 2018/6/7.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.home_banner)
    MZBannerView mBanner;
    @BindView(R.id.tv_always_marquee)
    AlwaysMarqueeTextView mAlwaysMarquee;//地震磁暴tv
    @BindView(R.id.img_gl_ll1_1)
    ImageView imgGlLl11;
    @BindView(R.id.tv_gl_ll1_1)
    TextView tvGlLl11;
    @BindView(R.id.ll_gl_ll1_1)
    LinearLayout llGlLl11;
    @BindView(R.id.img_gl_ll1_2)
    ImageView imgGlLl12;
    @BindView(R.id.tv_gl_ll1_2)
    TextView tvGlLl12;
    @BindView(R.id.ll_gl_ll1_2)
    LinearLayout llGlLl12;
    @BindView(R.id.img_gl_ll1_3)
    ImageView imgGlLl13;
    @BindView(R.id.tv_gl_ll1_3)
    TextView tvGlLl13;
    @BindView(R.id.ll_gl_ll1_3)
    LinearLayout llGlLl13;
    @BindView(R.id.img_gl_ll1_4)
    ImageView imgGlLl14;
    @BindView(R.id.tv_gl_ll1_4)
    TextView tvGlLl14;
    @BindView(R.id.ll_gl_ll1_4)
    LinearLayout llGlLl14;

    @BindView(R.id.ll_gl_ll1z)
    LinearLayout llGlLl1z;
    @BindView(R.id.ll_gl_ll2z)
    LinearLayout llGlLl2z;
    @BindView(R.id.ll_wdfw_ll1z)
    LinearLayout llWdfwLl1z;
    @BindView(R.id.ll_wdfw_ll2z)
    LinearLayout llWdfwLl2z;

    @BindView(R.id.ll_wdfw_ll3z)
    LinearLayout llWdfwLl3z;
    @BindView(R.id.img_wdfw_ll3_1)
    ImageView imgWdfwLl31;
    @BindView(R.id.tv_wdfw_ll3_1)
    TextView tvWdfwLl31;
    @BindView(R.id.ll_wdfw_ll3_1)
    LinearLayout llWdfwLl31;
    @BindView(R.id.img_wdfw_ll3_2)
    ImageView imgWdfwLl32;
    @BindView(R.id.tv_wdfw_ll3_2)
    TextView tvWdfwLl32;
    @BindView(R.id.ll_wdfw_ll3_2)
    LinearLayout llWdfwLl32;
    @BindView(R.id.img_wdfw_ll3_3)
    ImageView imgWdfwLl33;
    @BindView(R.id.tv_wdfw_ll3_3)
    TextView tvWdfwLl33;
    @BindView(R.id.ll_wdfw_ll3_3)
    LinearLayout llWdfwLl33;
    @BindView(R.id.img_wdfw_ll3_4)
    ImageView imgWdfwLl34;
    @BindView(R.id.tv_wdfw_ll3_4)
    TextView tvWdfwLl34;
    @BindView(R.id.ll_wdfw_ll3_4)
    LinearLayout llWdfwLl34;



    @BindView(R.id.img_gl_ll2_1)
    ImageView imgGlLl21;
    @BindView(R.id.tv_gl_ll2_1)
    TextView tvGlLl21;
    @BindView(R.id.ll_gl_ll2_1)
    LinearLayout llGlLl21;
    @BindView(R.id.img_gl_ll2_2)
    ImageView imgGlLl22;
    @BindView(R.id.tv_gl_ll2_2)
    TextView tvGlLl22;
    @BindView(R.id.ll_gl_ll2_2)
    LinearLayout llGlLl22;
    @BindView(R.id.img_gl_ll2_3)
    ImageView imgGlLl23;
    @BindView(R.id.tv_gl_ll2_3)
    TextView tvGlLl23;
    @BindView(R.id.ll_gl_ll2_3)
    LinearLayout llGlLl23;
    @BindView(R.id.img_gl_ll2_4)
    ImageView imgGlLl24;
    @BindView(R.id.tv_gl_ll2_4)
    TextView tvGlLl24;
    @BindView(R.id.ll_gl_ll2_4)
    LinearLayout llGlLl24;
    @BindView(R.id.ll_divline)
    LinearLayout llDivline;
    @BindView(R.id.img_wdfw_ll1_1)
    ImageView imgWdfwLl11;
    @BindView(R.id.tv_wdfw_ll1_1)
    TextView tvWdfwLl11;
    @BindView(R.id.ll_wdfw_ll1_1)
    LinearLayout llWdfwLl11;
    @BindView(R.id.img_wdfw_ll1_2)
    ImageView imgWdfwLl12;
    @BindView(R.id.tv_wdfw_ll1_2)
    TextView tvWdfwLl12;
    @BindView(R.id.ll_wdfw_ll1_2)
    LinearLayout llWdfwLl12;
    @BindView(R.id.img_wdfw_ll1_3)
    ImageView imgWdfwLl13;
    @BindView(R.id.tv_wdfw_ll1_3)
    TextView tvWdfwLl13;
    @BindView(R.id.ll_wdfw_ll1_3)
    LinearLayout llWdfwLl13;
    @BindView(R.id.img_wdfw_ll1_4)
    ImageView imgWdfwLl14;
    @BindView(R.id.tv_wdfw_ll1_4)
    TextView tvWdfwLl14;
    @BindView(R.id.ll_wdfw_ll1_4)
    LinearLayout llWdfwLl14;
    @BindView(R.id.img_wdfw_ll2_1)
    ImageView imgWdfwLl21;
    @BindView(R.id.tv_wdfw_ll2_1)
    TextView tvWdfwLl21;
    @BindView(R.id.ll_wdfw_ll2_1)
    LinearLayout llWdfwLl21;
    @BindView(R.id.img_wdfw_ll2_2)
    ImageView imgWdfwLl22;
    @BindView(R.id.tv_wdfw_ll2_2)
    TextView tvWdfwLl22;
    @BindView(R.id.ll_wdfw_ll2_2)
    LinearLayout llWdfwLl22;
    @BindView(R.id.img_wdfw_ll2_3)
    ImageView imgWdfwLl23;
    @BindView(R.id.tv_wdfw_ll2_3)
    TextView tvWdfwLl23;
    @BindView(R.id.ll_wdfw_ll2_3)
    LinearLayout llWdfwLl23;
    @BindView(R.id.img_wdfw_ll2_4)
    ImageView imgWdfwLl24;
    @BindView(R.id.tv_wdfw_ll2_4)
    TextView tvWdfwLl24;
    @BindView(R.id.ll_wdfw_ll2_4)
    LinearLayout llWdfwLl24;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_gl_name)
    TextView tv_gl_name;//

    @BindView(R.id.ll_gl_z)
    LinearLayout ll_gl_z;//管理总布局
    @BindView(R.id.ll_fw_z)
    LinearLayout ll_fw_z;//服务总布局

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView ciBao, diZhen;//磁暴，地震
    private Button dialogDetermine;//diaolog确定按钮

    private HomePresenter mHomePresenter;//控制层
    private OrgInforPresenter mOrgInforPresenter;
    private GYTHomePresenter mGYTHomePresenter;//

    private int orgType = 0;//1：公棚 2：协会  3：个人
    private int orgTypes = 0;//1：公棚 2：协会  3：个人
    private int clickTag = -1;// 1：点击鸽运通   2：点击赛鸽通

    private Intent intent;

    private CustomAlertDialog dialog;
    private LinearLayout dialogLayout;//dialog总布局

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    public void finishCreateView(Bundle state) {

        mHomePresenter = new HomePresenter(new IHomeViewImpl() {
            //地震和磁暴信息
            @Override
            public void diZhenCiBaoInfo(String ciBaoStr) {
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
            mSwipeRefreshLayout.setRefreshing(true);
            initView();
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
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
//                homeTv2.setText(data.getName());
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
                    } else if (clickTag == 4) {
                        //会员管理
                        HomePresenter2.isOpenWdhy(userTypeApiResponse, getActivity());
                    }

                    if (orgType == 2) {
                        //协会 会员管理
                        startHyAdmin(userTypeApiResponse);
                    }

                    startAuthorisePicture(userTypeApiResponse, orgType); //公棚赛鸽 授权拍照 跳转 ui初始化

                    clickTag = -1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (getUserAccountTypeInt() == 1 || getUserAccountTypeInt() == 2) {
            mOrgInforPresenter.getOrgInforData();//获取组织信息数据
        } else {
        }

        mOrgInforPresenter.getServiceUserType();//获取组织类型

        orgTypes = getUserAccountTypeInt();

        //把授权用户当做个人用户 处理
        if (orgTypes == 4 || orgTypes == 5) {
            orgType = 3;
        } else {
            orgType = orgTypes;
        }

        ViewControl.initView(orgType, tvHint, tv_gl_name,
                ll_gl_z, llGlLl1z, llGlLl2z,
                ll_fw_z, llWdfwLl1z, llWdfwLl2z,

                imgGlLl11, tvGlLl11,
                imgGlLl12, tvGlLl12,
                imgGlLl13, tvGlLl13,
                imgGlLl14, tvGlLl14,

                imgGlLl21, tvGlLl21,
                imgGlLl22, tvGlLl22,
                imgGlLl23, tvGlLl23,
                imgGlLl24, tvGlLl24,

                imgWdfwLl11, tvWdfwLl11,
                imgWdfwLl12, tvWdfwLl12,
                imgWdfwLl13, tvWdfwLl13,
                imgWdfwLl14, tvWdfwLl14,

                imgWdfwLl21, tvWdfwLl21,
                imgWdfwLl22, tvWdfwLl22,
                imgWdfwLl23, tvWdfwLl23,
                imgWdfwLl24, tvWdfwLl24
        );
    }

    @OnClick({R.id.tv_always_marquee, R.id.ll_gl_ll1_1, R.id.ll_gl_ll1_2, R.id.ll_gl_ll1_3, R.id.ll_gl_ll1_4, R.id.ll_gl_ll2_1, R.id.ll_gl_ll2_2, R.id.ll_gl_ll2_3, R.id.ll_gl_ll2_4, R.id.ll_wdfw_ll1_1, R.id.ll_wdfw_ll1_2, R.id.ll_wdfw_ll1_3, R.id.ll_wdfw_ll1_4, R.id.ll_wdfw_ll2_1, R.id.ll_wdfw_ll2_2, R.id.ll_wdfw_ll2_3, R.id.ll_wdfw_ll2_4})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }
        switch (view.getId()) {

            case R.id.tv_always_marquee://点击地震轮播文字
                dialog.show();
                break;

            case R.id.ll_gl_ll1_1:

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
                    case 3://个人

                        break;
                }

                break;
            case R.id.ll_gl_ll1_2:

                switch (orgType) {
                    case 1://公棚  {   训赛短信
                        startActivity(new Intent(getActivity(), XsSmsListActivity.class));
                        break;
                    case 2://协会 插组管理
                        intent = new Intent(getActivity(), DesignatedListActivity.class);
                        intent.putExtra("serviceType", 1);//协会插组指定
                        startActivity(intent);
                        break;
                    case 3://个人
                        break;
                }

                break;
            case R.id.ll_gl_ll1_3:

                switch (orgType) {
                    case 1://公棚     上笼短信
                        startActivity(new Intent(getActivity(), SlSmsListActivity.class));
                        break;
                    case 2://协会  比赛管理
                        intent = new Intent(getActivity(), PlayAdminActivity.class);
                        intent.putExtra("service_type", 1);
                        startActivity(intent);


                        break;
                    case 3://个人
                        break;
                }

                break;
            case R.id.ll_gl_ll1_4:
                switch (orgType) {
                    case 1://公棚   插组管理 //1:协会插组指定 2：公棚插组指定
                        intent = new Intent(getActivity(), DesignatedListActivity.class);
                        intent.putExtra("serviceType", 2);
                        startActivity(intent);
                        break;
                    case 2://协会  // 赛事规程
                        intent = new Intent(getActivity(), GameGcActivity.class);
                        intent.putExtra("type", "ssgc");
                        startActivity(intent);
                        break;
                    case 3://个人

                        break;
                }
                break;
            case R.id.ll_gl_ll2_1:

                switch (orgType) {
                    case 1://公棚

                        break;
                    case 2://协会   协会动态
                        intent = new Intent(getActivity(), GameGcActivity.class);
                        intent.putExtra("type", "xhdt");
                        startActivity(intent);

                        break;
                    case 3://个人

                        break;
                }

                break;
            case R.id.ll_gl_ll2_2:

                switch (orgType) {
                    case 1://公棚

                        break;
                    case 2://协会

                        break;
                    case 3://个人

                        break;
                }

                break;
            case R.id.ll_gl_ll2_3:
                switch (orgType) {
                    case 1://公棚

                        break;
                    case 2://协会

                        break;
                    case 3://个人

                        break;
                }

                break;
            case R.id.ll_gl_ll2_4:

                switch (orgType) {
                    case 1://公棚

                        break;
                    case 2://协会
                        break;
                    case 3://个人

                        break;
                }

                break;
            case R.id.ll_wdfw_ll1_1:

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
                    case 3://个人  训鸽通
                        RealmUtils.preservationServiceType(new ServiceType("xungetong"));
                        startActivity(new Intent(getActivity(), GYTHomeActivity.class));//跳转到训鸽通列表页面
                        break;
                }

                break;
            case R.id.ll_wdfw_ll1_2:

                switch (orgType) {
                    case 1://公棚  鸽信通
                        PigeonMessageHomeFragment.startPigeonMessageHome(getActivity());
                        break;
                    case 2://协会 鸽信通
                        PigeonMessageHomeFragment.startPigeonMessageHome(getActivity());
                        break;

                    case 3://个人  鸽信通
                        PigeonMessageHomeFragment.startPigeonMessageHome(getActivity());
                        break;
                }

                break;
            case R.id.ll_wdfw_ll1_3:

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
                    case 3://个人  鸽运通
                        clickTag = 3;
                        mLoadDataDialog.show();
                        mOrgInforPresenter.getServiceUserType();//获取组织类型
                        break;
                }
                break;
            case R.id.ll_wdfw_ll1_4:
                switch (orgType) {
                    case 1://公棚  授权拍照

                        break;
                    case 2://协会 赛线天气
                        startActivity(new Intent(getActivity(), LineWeatherActivity.class));
                        break;
                    case 3://个人 赛线天气
                        startActivity(new Intent(getActivity(), LineWeatherActivity.class));
                        break;
                }

                break;
            case R.id.ll_wdfw_ll2_1:
                switch (orgType) {
                    case 1://公棚
                        break;
                    case 2://协会 空距计算
                        startActivity(new Intent(getActivity(), UllageToolActivity.class));
                        break;
                    case 3://个人 空距计算
                        startActivity(new Intent(getActivity(), UllageToolActivity.class));
                        break;
                }
                break;
            case R.id.ll_wdfw_ll2_2:
                switch (orgType) {
                    case 1://公棚
                        break;
                    case 2://协会 中鸽网
                        startCpigeon();
                        break;
                    case 3://个人 中鸽网
                        startCpigeon();
                        break;
                }
                break;
            case R.id.ll_wdfw_ll2_3:
                switch (orgType) {
                    case 1://公棚
                        break;
                    case 2://协会

                        break;
                    case 3://个人   天下鸽谱
                        Uri uri = Uri.parse(ApiConstants.BASE_URL + ApiConstants.GRFW_TXGP);
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.ll_wdfw_ll2_4:
                break;
        }
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


    //点击下级协会
    private void initChildAssociation(ApiResponse<UserType> userTypeApiResponse) {
        ChildAssociationDialogFragment mChildDialog = new ChildAssociationDialogFragment();
        mChildDialog.setInitData(userTypeApiResponse);
        mChildDialog.show(getActivity().getFragmentManager(), "aa");
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

    //公棚赛鸽 授权拍照 跳转 ui初始化
    private void startAuthorisePicture(ApiResponse<UserType> userTypeApiResponse, int orgType) {
        switch (orgType) {
            case 1://公棚
                if (userTypeApiResponse.getData().getAuthusers() == 1) {//跳转授权拍照
                    startAuthorisePicture(userTypeApiResponse, llWdfwLl14);
                    llWdfwLl21.setOnClickListener(view -> {
                        //赛线天气
                        startActivity(new Intent(getActivity(), LineWeatherActivity.class));
                    });
                    llWdfwLl22.setOnClickListener(view -> {
                        //空距计算
                        startActivity(new Intent(getActivity(), UllageToolActivity.class));
                    });
                    llWdfwLl23.setOnClickListener(view -> {
                        //中鸽网
                        startCpigeon();
                    });


                    imgWdfwLl14.setImageResource(R.mipmap.shouquanpz1);//授权拍照
                    tvWdfwLl14.setText(getString(R.string.str_authorization_taking_pictures));

                    imgWdfwLl21.setImageResource(R.mipmap.saixiantq1);//赛线天气
                    tvWdfwLl21.setText(getString(R.string.str_line_weather));
                    imgWdfwLl22.setImageResource(R.mipmap.kongjujs1);//空距计算
                    tvWdfwLl22.setText(getString(R.string.str_ullage));
                    imgWdfwLl23.setImageResource(R.mipmap.zhonggew1);//中鸽网
                    tvWdfwLl23.setText(getString(R.string.str_cpigeon));
                    startShoot(imgWdfwLl24, tvWdfwLl24, llWdfwLl24);//随行拍
                } else {

                    llWdfwLl14.setOnClickListener(view -> {
                        //赛线天气
                        startActivity(new Intent(getActivity(), LineWeatherActivity.class));
                    });
                    llWdfwLl21.setOnClickListener(view -> {
                        //空距计算
                        startActivity(new Intent(getActivity(), UllageToolActivity.class));
                    });
                    llWdfwLl22.setOnClickListener(view -> {
                        //中鸽网
                        startCpigeon();
                    });

                    imgWdfwLl14.setImageResource(R.mipmap.saixiantq1);//赛线天气
                    tvWdfwLl14.setText(getString(R.string.str_line_weather));

                    imgWdfwLl21.setImageResource(R.mipmap.kongjujs1);//空距计算
                    tvWdfwLl21.setText(getString(R.string.str_ullage));
                    imgWdfwLl22.setImageResource(R.mipmap.zhonggew1);//中鸽网
                    tvWdfwLl22.setText(getString(R.string.str_cpigeon));

                    startShoot(imgWdfwLl23, tvWdfwLl23, llWdfwLl23);//随行拍


                    imgWdfwLl24.setImageBitmap(null);//暂无
                    tvWdfwLl24.setText("");

                }
                break;
            case 2://协会
                if (userTypeApiResponse.getData().getAuthusers() == 1) {//跳转授权拍照
                    imgWdfwLl23.setImageResource(R.mipmap.shouquanpz2);//授权拍照
                    tvWdfwLl23.setText(getString(R.string.str_authorization_taking_pictures));

                    startAuthorisePicture(userTypeApiResponse, llWdfwLl23);

                    startShoot(imgWdfwLl24, tvWdfwLl24, llWdfwLl24);//随行拍
                } else {
                    startShoot(imgWdfwLl23, tvWdfwLl23, llWdfwLl23);//随行拍
                }

                break;
            case 3://个人
                if (userTypeApiResponse.getData().getAuthusers() == 1) {//跳转授权拍照
                    imgWdfwLl24.setImageResource(R.mipmap.shouquanpz3);//授权拍照
                    tvWdfwLl24.setText(getString(R.string.str_authorization_taking_pictures));
                    startAuthorisePicture(userTypeApiResponse, llWdfwLl24);

                    llWdfwLl3z.setVisibility(View.VISIBLE);
                    startShoot(imgWdfwLl31, tvWdfwLl31, llWdfwLl31);//随行拍
                } else {

                    llWdfwLl3z.setVisibility(View.GONE);
                    startShoot(imgWdfwLl24, tvWdfwLl24, llWdfwLl24);//随行拍
                }
                break;
        }
    }

    //会员管理 下级协会
    private void startHyAdmin(ApiResponse<UserType> userTypeApiResponse) {
        if (userTypeApiResponse.getData().getShenggehui().getPowers().equals("1")) {

            imgGlLl22.setImageResource(R.mipmap.geyoujl2);//鸽友交流
            tvGlLl22.setText("鸽友交流");
            imgGlLl23.setImageResource(R.mipmap.xiajixh);//下级协会
            tvGlLl23.setText("下级协会");
            imgGlLl24.setImageBitmap(null);
            tvGlLl24.setText("");

            llGlLl22.setOnClickListener(view -> {
                //鸽友交流
                intent = new Intent(getActivity(), GyjlHomeActivity.class);
                startActivity(intent);

            });

            llGlLl23.setOnClickListener(view -> {
                //下级协会
                initChildAssociation(userTypeApiResponse);
            });

            llGlLl24.setOnClickListener(null);

        } else {

            imgGlLl22.setImageResource(R.mipmap.wodehy2);//我的会员
            tvGlLl22.setText("我的会员");
            imgGlLl23.setImageResource(R.mipmap.geyoujl2);//鸽友交流
            tvGlLl23.setText("鸽友交流");
            imgGlLl24.setImageBitmap(null);//
            tvGlLl24.setText("");

            llGlLl22.setOnClickListener(view -> {
                // 会员管理
                clickTag = 4;
                mLoadDataDialog.show();
                mOrgInforPresenter.getServiceUserType();//获取组织类型
            });

            llGlLl23.setOnClickListener(view -> {
                //鸽友交流
                intent = new Intent(getActivity(), GyjlHomeActivity.class);
                startActivity(intent);
            });

            llGlLl24.setOnClickListener(null);
        }
    }

    //跳转随行拍
    private void startShoot(ImageView mImageView, TextView mTextView, LinearLayout mLinearLayout) {

        mImageView.setImageResource(R.mipmap.suixingp2);//随行拍
        mTextView.setText(getString(R.string.str_at_any_time_shooting));
        mLinearLayout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ShootVideoSettingActivity.class));
        });


    }

    //点击授权拍照
    private void startAuthorisePicture(ApiResponse<UserType> userTypeApiResponse, View views) {
        if (userTypeApiResponse.getData().getSqpzuid() > 0) {
            views.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), GpsgHomeActivity.class);
                intent.putExtra("sqpzuid", userTypeApiResponse.getData().getSqpzuid());//授权拍照  id
                startActivity(intent);
            });
        }
    }
}

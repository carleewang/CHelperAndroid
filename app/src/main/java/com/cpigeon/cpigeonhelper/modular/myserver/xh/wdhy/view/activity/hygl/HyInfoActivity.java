package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserIdInfo;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;


/**
 * 会员信息页面
 * Created by Administrator on 2018/3/24.
 */
public class HyInfoActivity extends ToolbarBaseActivity {

    @BindView(R.id.img_user_tx)
    ImageView imgUserTx;
    @BindView(R.id.tv_sfxx)
    TextView tvSfxx;
    @BindView(R.id.tv_lssj)
    TextView tvLssj;
    @BindView(R.id.tv_ndpx)
    TextView tvNdpx;
    @BindView(R.id.tv_ndjf)
    TextView tvNdjf;
    @BindView(R.id.tv_cfjl)
    TextView tvCfjl;

    @BindView(R.id.tv_user_name)
    TextView tv_user_name;//名字
    @BindView(R.id.tv_phone)
    TextView tv_phone;//电话
    @BindView(R.id.tv_ph)
    TextView tv_ph;//棚号

    @BindView(R.id.img_state)
    ImageView img_state;

    @BindView(R.id.ll_sfxx)
    LinearLayout ll_sfxx;
    @BindView(R.id.ll_lssj)
    LinearLayout ll_lssj;
    @BindView(R.id.ll_ndpx)
    LinearLayout ll_ndpx;
    @BindView(R.id.ll_ndjf)
    LinearLayout ll_ndjf;
    @BindView(R.id.ll_zhgm)
    LinearLayout ll_zhgm;
    @BindView(R.id.ll_cfjl)
    LinearLayout ll_cfjl;
    @BindView(R.id.rl_user_info)
    RelativeLayout rl_user_info;

    @BindView(R.id.img_fsxx_jt)
    AppCompatImageView img_fsxx_jt;

    @BindView(R.id.view_dividingline)
    View view_dividingline;//

    @BindView(R.id.img_man)
    ImageView img_man;//

    private HyglHomeListEntity.DatalistBean dataBean;

    private MemberPresenter mMemberPresenter;//控制层

    private HyUserDetailEntity mHyUserDetailEntity;//

    private HyUserIdInfo mHyUserIdInfo;

    private String type = "myself";//look :上级协会查看   myself：自己查看

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hy_info;
    }

    @Override
    protected void setStatusBar() {

        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, HyInfoActivity.this::finish);
        setTitle("会员信息");
        dataBean = getIntent().getParcelableExtra("databean");

        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getHyUserDetail(ApiResponse<HyUserDetailEntity> listApiResponse, String msg, Throwable mThrowable) {
                try {

                    if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HyInfoActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                        return;
                    }

                    //获取协会会员身份证信息
                    mHyUserDetailEntity = listApiResponse.getData();

                    initView(mHyUserDetailEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    HyInfoActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getHyUserIdInfo(ApiResponse<HyUserIdInfo> idInfoApiResponse, String msg, Throwable mThrowable) {
                try {
                    HyInfoActivity.this.mHyUserIdInfo = idInfoApiResponse.getData();
                    if (idInfoApiResponse.getErrorCode() == 0) {
                        tvSfxx.setText(idInfoApiResponse.getData().getSfzhm());
                    } else if (idInfoApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HyInfoActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        //没有找到身份证信息

                        if (type.equals("myself")) {
                            //自己查看
                            if (mHyUserDetailEntity.getZhuangtai().equals("除名")) {
                                img_fsxx_jt.setVisibility(View.GONE);
                                tvSfxx.setHint("暂无身份证信息");
                                ll_sfxx.setClickable(false);
                            } else {
                                tvSfxx.setHint("请补拍");
                            }
                        } else {
                            //上级协会查看
                            img_fsxx_jt.setVisibility(View.GONE);
                            tvSfxx.setHint("暂无身份证信息");
                            ll_sfxx.setClickable(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    HyInfoActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        if (dataBean == null) return;

        mMemberPresenter.getXHHYGL_GetUserDetail(dataBean.getId(), dataBean.getXhuid(), type);

        RxUtils.delayed(300,aLong -> {
            mMemberPresenter.getXHHYGL_GetUserIdInfo(String.valueOf(dataBean.getId()), dataBean.getXhuid(), type);
        });
    }

    //初始化数据
    private void initView(HyUserDetailEntity mHyUserDetailEntity) {

        if (mHyUserDetailEntity == null) {
            return;
        }

        if (type.equals("myself")) {
            //自己查看
            if (mHyUserDetailEntity.getZhuangtai().equals("除名")) {

            }

        } else {
            //上级协会查看
            ll_ndjf.setVisibility(View.GONE);//缴费记录不能查看
            view_dividingline.setVisibility(View.GONE);//缴费记录不能查看
        }

        if (mHyUserDetailEntity.getZhuangtai().equals("除名")) {
            Glide.with(this).load(mHyUserDetailEntity.getBasicinfo().getTouxiang())
                    .bitmapTransform(new GrayscaleTransformation(mContext))
                    .into(imgUserTx);
        } else {
            Glide.with(this).load(mHyUserDetailEntity.getBasicinfo().getTouxiang()).into(imgUserTx);
        }

        try {
            tv_user_name.setText(mHyUserDetailEntity.getBasicinfo().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tv_phone.setText(mHyUserDetailEntity.getBasicinfo().getHandphone());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (mHyUserDetailEntity.getBasicinfo().getSex().equals("男")) {
                img_man.setImageResource(R.mipmap.ic_hy_man);
            } else if (mHyUserDetailEntity.getBasicinfo().getSex().equals("女")) {
                img_man.setImageResource(R.mipmap.ic_hy_woman);
            } else {
                img_man.setImageResource(R.mipmap.ic_hy_man);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tv_ph.setText(String.valueOf("棚号：" + mHyUserDetailEntity.getGesheinfo().getPn()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置状态
        switch (mHyUserDetailEntity.getZhuangtai()) {
            case "在册":
                img_state.setImageResource(R.mipmap.hy_state_zc);
                break;
            case "禁赛":
                img_state.setImageResource(R.mipmap.hy_state_js);
                break;
            case "除名":
                img_state.setImageResource(R.mipmap.hy_state_cm);
                break;
        }

    }

    private Intent intent;

    @OnClick({R.id.ll_sfxx, R.id.ll_lssj, R.id.ll_ndpx, R.id.ll_ndjf, R.id.ll_zhgm, R.id.ll_cfjl, R.id.rl_user_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user_info:
                //个人信息
                if (mHyUserDetailEntity == null) return;
                intent = new Intent(this, HyPersonalInfoActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("data", mHyUserDetailEntity);
                startActivity(intent);
                break;
            case R.id.ll_sfxx:
                //身份信息
                intent = new Intent(this, IdInfoActivity.class);

                if (mHyUserIdInfo != null) {
                    intent.putExtra("mHyUserIdInfo", mHyUserIdInfo);
                }

                intent.putExtra("type", type);
                intent.putExtra("data", mHyUserDetailEntity);
                startActivity(intent);
                break;
            case R.id.ll_lssj:
                //历史赛绩
                if (mHyUserDetailEntity == null) return;
                intent = new Intent(this, HistoryLeagueListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("data", mHyUserDetailEntity);
                startActivity(intent);
                break;
            case R.id.ll_ndpx:
                //年度评选
                intent = new Intent(this, YearSelectionListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("data", mHyUserDetailEntity);
                startActivity(intent);
                break;
            case R.id.ll_ndjf:
                //年度缴费
                intent = new Intent(this, YearPayCostListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("data", mHyUserDetailEntity);
                startActivity(intent);
                break;
            case R.id.ll_zhgm:
                //足环购买
                intent = new Intent(this, FootBuyListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("data", mHyUserDetailEntity);
                startActivity(intent);
                break;
            case R.id.ll_cfjl:
                //处罚记录
                intent = new Intent(this, PenalizeRecordListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("data", mHyUserDetailEntity);
                startActivity(intent);
                break;
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.HYGL_LIST_REFRESH)) {

            mMemberPresenter.getXHHYGL_GetUserIdInfo(String.valueOf(dataBean.getId()), dataBean.getXhuid(), type);

            if (dataBean == null) return;

            mMemberPresenter.getXHHYGL_GetUserDetail(dataBean.getId(), dataBean.getXhuid(), type);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
        dataBean = null;
        mHyUserDetailEntity = null;
        mHyUserIdInfo = null;
        mHyUserIdInfo = null;
    }
}

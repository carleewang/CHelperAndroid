package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hfjl;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HFInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.DuesPayPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter.HfjlHomePagerAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.DuesPayViewImpl;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.picker.PickerAdmin;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 会费缴纳
 * Created by Administrator on 2018/3/23.
 */

public class HfjlHomeActivity extends BaseActivity {

    @BindView(R.id.tv_year_z)
    TextView tv_year_z;
    @BindView(R.id.tv_year_receivable)
    TextView tv_year_receivable;//应收会费
    @BindView(R.id.tv_year_receivable_num)
    TextView tv_year_receivable_num;//应收会费金额
    @BindView(R.id.tv_numbers)
    TextView tv_numbers;//已收会员人数

    @BindView(R.id.tv_ass_money)
    TextView tv_ass_money;//会费金额

    @BindView(R.id.tv_money_received)
    TextView tv_money_received;//已收会费
    @BindView(R.id.tv_money_received_num)
    TextView tv_money_received_num;//已收会费金额

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTab;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.tv_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DuesPayPresenter mDuesPayPresenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getLayoutId() {
//        return R.layout.activity_hyjl_home;
        return R.layout.fragment_pigeon_circle_layout;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        toolbarTitle.setText("会费缴纳汇总");
    }

    @Override
    protected void initToolBar() {
        toolbar.setTitle(null);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            AppManager.getAppManager().killActivity(mWeakReference);
        });

        toolbar.getMenu().add("设置会费").setOnMenuItemClickListener(item -> {
            MyMemberDialogUtil.initInputDialog(this, tv_ass_money.getText().toString().substring(0, tv_ass_money.getText().toString().length() - 1), "输入会费金额", "请如实填写会费金额，以便查看！", InputType.TYPE_CLASS_NUMBER,
                    new MyMemberDialogUtil.DialogClickListener() {
                        @Override
                        public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                            dialog.dismiss();
                            if (etStr.isEmpty() || etStr.length() == 0) return;
                            mDuesPayPresenter.setXHHYGL_SetHuiFei(tv_year_z.getText().toString(), etStr);
                        }
                    });
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        initView(DateUtils.getStringDateY());

        mDuesPayPresenter = new DuesPayPresenter(new DuesPayViewImpl() {

            @Override
            public void getHFInfoResults(ApiResponse<HFInfoEntity> listApiResponse, String msg, Throwable mThrowable) {

                try {

                    if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HfjlHomeActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                        return;
                    }

                    switch (listApiResponse.getErrorCode()) {
                        case 0:
                            //应收会费
                            tv_year_receivable_num.setText(listApiResponse.getData().getYingshouhf() + "元");
                            //已收会费
                            tv_money_received_num.setText(listApiResponse.getData().getYishouhf() + "元");

                            //会费金额
                            tv_ass_money.setText(listApiResponse.getData().getHuifei() + "元");

                            //已收会员人数
                            tv_numbers.setText(listApiResponse.getData().getYishouhy());

                            //发布事件（刷新数据）
                            EventBus.getDefault().post(listApiResponse.getData());
                            break;
                        default:
                            EventBus.getDefault().post(new HFInfoEntity(1));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (mThrowable != null) {
                        EventBus.getDefault().post(new HFInfoEntity(2));
                        HfjlHomeActivity.this.getThrowable(mThrowable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {

                    if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HfjlHomeActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                        return;
                    }

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HfjlHomeActivity.this, dialog -> {
                        dialog.dismiss();
                    });//弹出提示

                    againRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    HfjlHomeActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                super.getErrorNews(str);
                try {
                    HfjlHomeActivity.this.getErrorNews(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        HfjlHomePagerAdapter mAdapter = new HfjlHomePagerAdapter(getSupportFragmentManager(),
                getApplicationContext());
        mViewPager.setOffscreenPageLimit(5);//预加载
        mViewPager.setAdapter(mAdapter);//设置关联的适配器
        mSlidingTab.setViewPager(mViewPager);//设置导航条关联的viewpager
        mViewPager.setCurrentItem(0);

        againRequest();
    }

    //请求数据
    private void againRequest() {
        //获取年度会费
        mDuesPayPresenter.setXHHYGL_SetHuiFei(tv_year_z.getText().toString());
    }

    private void initView(String time) {
        tv_year_z.setText(time);//设置现在时间
        tv_year_receivable.setText(time + " 应收会费");//
        tv_money_received.setText(time + " 已收会费");//
    }

    @OnClick({R.id.ll_year_z})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_year_z:

                PickerAdmin.showPicker(HfjlHomeActivity.this, 0, new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        initView(item);
                        againRequest();
                    }
                });
                break;
        }
    }
}

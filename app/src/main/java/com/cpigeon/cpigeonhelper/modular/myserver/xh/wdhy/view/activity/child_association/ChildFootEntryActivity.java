package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.child_association;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.lineweather.model.bean.ContactModel2;
import com.cpigeon.cpigeonhelper.modular.lineweather.view.activity.SelectAssociationActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AssociationListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildFoodAdminListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 下级协会 足环录入
 * Created by Administrator on 2018/6/25.
 */

public class ChildFootEntryActivity extends ToolbarBaseActivity {

    @BindView(R.id.et_f1)
    EditText et_f1;
    @BindView(R.id.et_f2)
    EditText et_f2;


    @BindView(R.id.tv_foot_type)
    TextView tv_foot_type;//
    @BindView(R.id.tv_association)
    TextView tv_association;//

    @BindView(R.id.tv_money_z)
    TextView tv_money_z;//总金额

    @BindView(R.id.img_foot_type)
    ImageView img_foot_type;//足环类型价格箭头
    @BindView(R.id.rl_btn2)
    RelativeLayout rl_btn2;//足环类型选择

    private double price = 0;//足环单价

    private String footType = "普通足环";//lx   //类型：普通足环|特比环
    private String xhuid = "";
    private String xhid = "";
    private String xhmc = "";

    private String type = "add";//add 添加 edit 修改


    private FootAdminPresenter mFootAdminPresenter;
    private AssociationListEntity mData;
    private ChildFoodAdminListEntity.FootlistBean mFootDtata;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_child_foot_entry;
    }

    @Override
    protected void setStatusBar() {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, ChildFootEntryActivity.this::finish);
        setTitle("足环发行");

        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        //不能选择特比环
        img_foot_type.setVisibility(View.GONE);
        rl_btn2.setClickable(false);

        if (type.equals("add_ordinary")) {
            //添加普通足环
            price = Double.parseDouble(getIntent().getStringExtra("price"));
            tv_foot_type.setText("普通足环");
            footType = "普通足环";
            setTitle("普通足环发行");
        } else if (type.equals("add_tubby")) {
            //添加特比足环
            price = Double.parseDouble(getIntent().getStringExtra("price"));
            tv_foot_type.setText("特比环");
            footType = "特比环";

            setTitle("特比环发行");
        } else if (type.equals("edit")) {
            //修改普通足环
            mFootDtata = (ChildFoodAdminListEntity.FootlistBean) getIntent().getSerializableExtra("data");
            price = Double.parseDouble(getIntent().getStringExtra("price"));
            initData(mFootDtata);
        }

        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {
            @Override
            public void getFootAdminResultsData(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                    if (mThrowable != null) {
                        ChildFootEntryActivity.this.getThrowable(mThrowable);
                        return;
                    }

                    try {
                        if (listApiResponse.getErrorCode() == 0) {
                            EventBus.getDefault().post(EventBusService.CHILD_FOOT_ENTRY_LIST_REFRESH);

                            ChildFootEntryActivity.this.errSweetAlertDialog = SweetAlertDialogUtil.showDialog(ChildFootEntryActivity.this.errSweetAlertDialog, msg, ChildFootEntryActivity.this, dialog -> {
                                dialog.dismiss();
                                AppManager.getAppManager().killActivity(mWeakReference);
                            });//弹出提示
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            ChildFootEntryActivity.this.errSweetAlertDialog = SweetAlertDialogUtil.showDialog(ChildFootEntryActivity.this.errSweetAlertDialog, msg, ChildFootEntryActivity.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            ChildFootEntryActivity.this.getErrorNews(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                ChildFootEntryActivity.this.getErrorNews(str);
            }
        });


        et_f1.setRawInputType(Configuration.KEYBOARD_QWERTY);
        et_f2.setRawInputType(Configuration.KEYBOARD_QWERTY);

        et_f1.addTextChangedListener(etTextChangedListener());
        et_f2.addTextChangedListener(etTextChangedListener());
    }

    //连号足环输入监听
    private TextWatcher etTextChangedListener() {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_money_z.setText(String.valueOf("总金额：0元"));
                checkFoot(et_f1.getText().toString(), et_f2.getText().toString());
            }
        };
    }


    //初始化数据
    private void initData(ChildFoodAdminListEntity.FootlistBean mFootDtata) {
        try {

            if (mFootDtata.getType().equals("普通足环")){
                setTitle("普通足环发行");
            }else {
                setTitle("特比环发行");
            }

            et_f1.setText(mFootDtata.getFoot1());
            et_f2.setText(mFootDtata.getFoot2());
            tv_foot_type.setText(mFootDtata.getType());
            tv_association.setText(mFootDtata.getXhmc());
            footType = mFootDtata.getType();
            xhid = mFootDtata.getXhid();
            xhuid = mFootDtata.getXhuid();
            xhmc = mFootDtata.getXhmc();
            checkFoot(et_f1.getText().toString(), et_f2.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_sure2, R.id.rl_btn, R.id.rl_btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure2:
                //确定添加
                try {
                    if (type.equals("add_ordinary") || type.equals("add_tubby")) {
                        if (xhuid.isEmpty()) {
                            ChildFootEntryActivity.this.getErrorNews("请选择协会");
                            return;
                        }

                        if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();

                        mFootAdminPresenter.getXHHYGL_SJGH_AddFoot(footType, xhuid,
                                xhid, xhmc, et_f1.getText().toString(),
                                et_f2.getText().toString());
                    } else if (type.equals("edit")) {
                        if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();
                        mFootAdminPresenter.getXHHYGL_SJGH_EditFoot(mFootDtata.getId(), footType, xhuid,
                                xhid, xhmc, et_f1.getText().toString(),
                                et_f2.getText().toString());
                    }

                } catch (Exception e) {
                    Log.d(TAG, "onViewClicked: " + e.getLocalizedMessage());
                }
                break;
            case R.id.rl_btn:
                //选择协会
                startActivityForResult(new Intent(this, SelectAssociationActivity.class), 0x0032);
                break;
            case R.id.rl_btn2:
                //选择足环类型
                new SaActionSheetDialog(ChildFootEntryActivity.this)
                        .builder()
                        .addSheetItem("普通足环", OnSheetItemClickListenerState)
                        .addSheetItem("特比环", OnSheetItemClickListenerState)
                        .show();

                break;

//            case R.id.et_f1:
//                //起始足环号码
//                //连号 起始足环1
//                FootDialog.initFootDialogOne1(this, et_f1.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
//                    @Override
//                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
//                        dialog.dismiss();
//                        et_f1.setText(str);
//                        checkFoot(et_f1.getText().toString(), et_f2.getText().toString());
//                    }
//                });
//                break;
//            case R.id.et_f2:
//                //终止足环号码
//                FootDialog.initFootDialogOne1(this, et_f2.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
//                    @Override
//                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
//                        dialog.dismiss();
//                        et_f2.setText(str);
//                        checkFoot(et_f1.getText().toString(), et_f2.getText().toString());
//
//                    }
//                });
//                break;
        }
    }

    private void checkFoot(String str1, String str2) {
        //判断输入值
        if (!str1.isEmpty()) {
            String[] strDefault = str1.split("-");
            if (strDefault.length == 3) {

                //判断终止足环号
                if (!str2.isEmpty()) {

                    String[] strDefault2 = str2.split("-");
                    if (strDefault2.length == 3) {

                        if (!strDefault[0].equals(strDefault2[0])) {
                            //足环前半段不相同
//                            ChildFootEntryActivity.this.getErrorNews("请填写正确的足环号码,起始足环号码必须小于终止足环号码");
                        }

                        if (!strDefault[1].equals(strDefault2[1])) {
                            //足环中半段不相同
//                            ChildFootEntryActivity.this.getErrorNews("请填写正确的足环号码,起始足环号码必须小于终止足环号码");
                        }

                        //足环后半段不相同
                        try {
                            double size = Integer.valueOf(strDefault2[2]) - Integer.valueOf(strDefault[2]) + 1;

                            NumberFormat NF = NumberFormat.getInstance();
                            NF.setGroupingUsed(false);//去掉科学计数法显示
                            if (size > 0) {
                                tv_money_z.setText(String.valueOf("总金额：" +NF.format((size * price))  + "元"));
                            } else {
//                                ChildFootEntryActivity.this.getErrorNews("起始足环号码必须小于终止足环号码");
                            }
                        } catch (NumberFormatException e) {
                            ChildFootEntryActivity.this.getErrorNews("请填写正确的足环号码");
                        }
                    }
                }
            }
        }
    }


    /**
     * 弹出选择状态
     */
    private Intent intent;
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //普通足环
                    footType = "普通足环";
                    tv_foot_type.setText("普通足环");
                    break;
                case 2:
                    //特比环
                    footType = "特比环";
                    tv_foot_type.setText("特比环");
                    break;
            }
        }
    };


    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(ContactModel2.MembersEntity2 entity) {

        mData = (AssociationListEntity) entity.getData();
        xhuid = mData.getXhuid();
        xhid = mData.getXhid();
        xhmc = mData.getXhname();
        tv_association.setText(mData.getXhname());

        Log.d("xiaohls", "onEventMainThread:-----------s ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

}

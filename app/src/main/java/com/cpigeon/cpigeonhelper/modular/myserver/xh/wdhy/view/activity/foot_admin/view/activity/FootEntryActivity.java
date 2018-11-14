package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootPriceEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.FootDialog;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 足环录入
 * Created by Administrator on 2018/6/15.
 */

public class FootEntryActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_btn1)
    TextView tvBtn1;
    @BindView(R.id.tv_btn2)
    TextView tvBtn2;
    @BindView(R.id.ll_btn)
    LinearLayout llBtn;
    @BindView(R.id.btn_sure1)
    Button btnSure1;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.btn_sure2)
    Button btnSure2;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.et_f)
    TextView et_f;
    @BindView(R.id.et_f1)
    TextView et_f1;
    @BindView(R.id.et_f2)
    TextView et_f2;


    @BindView(R.id.tv_hint_price)
    TextView tv_hint_price;//单价提示
    @BindView(R.id.tv_money_z)
    TextView tv_money_z;//总金额

    private FootAdminPresenter mFootAdminPresenter;
    private double price = 0;//足环单价
    private String y;//年份


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_foot_entry_entry;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, FootEntryActivity.this::finish);
        setTitle("足环录入");
        price = 0;
        y = DateUtils.getStringDateY();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        et_f.setRawInputType(Configuration.KEYBOARD_QWERTY);
        et_f1.setRawInputType(Configuration.KEYBOARD_QWERTY);
        et_f2.setRawInputType(Configuration.KEYBOARD_QWERTY);

        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {
            @Override
            public void getFootAdminResultsData(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
                    if (mThrowable != null) {
                        FootEntryActivity.this.getThrowable(mThrowable);
                        return;
                    }

                    if (listApiResponse.getErrorCode() == 0) {
                        EventBus.getDefault().post(EventBusService.FOOT_ADMIN_REFRESH);
                        et_f.setText("");
                        et_f1.setText("");
                        et_f2.setText("");
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootEntryActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }

                    FootEntryActivity.this.getErrorNews(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getXHHYGL_ZHGL_GetFootPrice(ApiResponse<FootPriceEntity> listApiResponse, String msg, Throwable mThrowable) {
                //设置足环单价
                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
                    if (listApiResponse.getErrorCode() == 0) {
                        price = Double.valueOf(listApiResponse.getData().getPrice());
                        if (Double.valueOf(listApiResponse.getData().getPrice()) == 0) {
                            tv_hint_price.setVisibility(View.VISIBLE);
                            FootEntryActivity.this.setTopRightButton("单价", () -> {
                                MyMemberDialogUtil.initInputDialog(FootEntryActivity.this, listApiResponse.getData().getPrice(), "输入单价", "请填写数字即可!", InputType.TYPE_CLASS_TEXT,
                                        new MyMemberDialogUtil.DialogClickListener() {
                                            @Override
                                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                                dialog.dismiss();
                                                if (etStr.isEmpty() || etStr.length() == 0) {
                                                    FootEntryActivity.this.getErrorNews("输入价格不能为空");
                                                    return;
                                                }

                                                mFootAdminPresenter.getXHHYGL_ZHGL_SetFootPrice(y, "0", etStr);
                                            }
                                        });
                            });
                        } else {
                            tv_hint_price.setVisibility(View.INVISIBLE);
                            setTopRightButton("", null);
                        }
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootEntryActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        FootEntryActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getXHHYGL_ZHGL_SetFootPrice(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                //设置足环单价
                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
                    if (listApiResponse.getErrorCode() == 0) {
                        //足环单价
                        mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice(y, "0");
                    }
                    FootEntryActivity.this.getErrorNews(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //足环单价
        mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice(y, "0");
    }

    @OnClick({R.id.tv_btn1, R.id.tv_btn2, R.id.ll_btn, R.id.btn_sure1, R.id.btn_sure2, R.id.et_f, R.id.et_f1, R.id.et_f2})
    public void onViewClicked(View view) {

        if (price == 0) {
            FootEntryActivity.this.getErrorNews("请先设置单价后录入足环号码");
            return;
        }
        switch (view.getId()) {
            case R.id.tv_btn1:
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);
                tvBtn1.setBackgroundResource(R.drawable.background_round_white_1);
                tvBtn2.setBackgroundResource(R.drawable.background_white1);

                tvBtn1.setTextColor(getResources().getColor(R.color.black));
                tvBtn2.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case R.id.tv_btn2:
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
                tvBtn1.setBackgroundResource(R.drawable.background_white1);
                tvBtn2.setBackgroundResource(R.drawable.background_round_white_1);

                tvBtn1.setTextColor(getResources().getColor(R.color.colorWhite));
                tvBtn2.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.ll_btn:
                break;
            case R.id.btn_sure1:
                if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();

                mFootAdminPresenter.getXHHYGL_ZHGL_Add("普通足环", 1, et_f.getText().toString(), "", "");
                break;
            case R.id.btn_sure2:
                if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();
                mFootAdminPresenter.getXHHYGL_ZHGL_Add("普通足环", 2, "", et_f1.getText().toString(), et_f2.getText().toString());
                break;
            case R.id.et_f:
                //单个足环
                FootDialog.initFootDialogOne1(this, et_f.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
                    @Override
                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
                        dialog.dismiss();
                        et_f.setText(str);
                    }
                });
                break;
            case R.id.et_f1:
                //连号 起始足环1
                FootDialog.initFootDialogOne1(this, et_f1.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
                    @Override
                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
                        dialog.dismiss();
                        et_f1.setText(str);

                        checkFoot(et_f1.getText().toString(), et_f2.getText().toString());
                    }
                });
                break;
            case R.id.et_f2:
                //连号 起始足环2
                FootDialog.initFootDialogOne1(this, et_f2.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
                    @Override
                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
                        dialog.dismiss();
                        et_f2.setText(str);
                        checkFoot(et_f1.getText().toString(), et_f2.getText().toString());
                    }
                });
                break;
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
                            FootEntryActivity.this.getErrorNews("请填写正确的足环号码,起始足环号码必须小于终止足环号码");
                        }

                        if (!strDefault[1].equals(strDefault2[1])) {
                            //足环中半段不相同
                            FootEntryActivity.this.getErrorNews("请填写正确的足环号码,起始足环号码必须小于终止足环号码");
                        }

                        //足环后半段不相同
                        try {
                            int size = Integer.valueOf(strDefault2[2]) - Integer.valueOf(strDefault[2])+1;

                            NumberFormat NF = NumberFormat.getInstance();
                            NF.setGroupingUsed(false);//去掉科学计数法显示
                            if (size > 0) {
                                tv_money_z.setText(String.valueOf("总金额：" +NF.format((size * price))  + "元"));
                            } else {
                                FootEntryActivity.this.getErrorNews("起始足环号码必须小于终止足环号码");
                            }
                        } catch (NumberFormatException e) {

                            FootEntryActivity.this.getErrorNews("请填写正确的足环号码");
                        }
                    }
                }
            }
        }
    }
}

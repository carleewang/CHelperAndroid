package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 比特环录入界面
 * Created by Administrator on 2018/6/14.
 */

public class TubbyRingEntryActivity extends ToolbarBaseActivity {


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
    EditText et_f;
    @BindView(R.id.et_f1)
    EditText et_f1;
    @BindView(R.id.et_f2)
    EditText et_f2;

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
        return R.layout.activity_tubby_ring_entry;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, TubbyRingEntryActivity.this::finish);
        setTitle("特比环录入");
        y = DateUtils.getStringDateY();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {
            @Override
            public void getFootAdminResultsData(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                    if (mThrowable != null) {
                        TubbyRingEntryActivity.this.getThrowable(mThrowable);
                        return;
                    }

                    if (listApiResponse.getErrorCode() == 0) {
                        EventBus.getDefault().post(EventBusService.FOOT_ADMIN_REFRESH);
                        et_f.setText("");
                        et_f1.setText("");
                        et_f2.setText("");
                    }else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, TubbyRingEntryActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }

                    TubbyRingEntryActivity.this.getErrorNews(msg);
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
                            TubbyRingEntryActivity.this.setTopRightButton("单价", () -> {
                                MyMemberDialogUtil.initInputDialog(TubbyRingEntryActivity.this, listApiResponse.getData().getPrice(), "输入单价", "请填写数字即可!", InputType.TYPE_CLASS_TEXT,
                                        new MyMemberDialogUtil.DialogClickListener() {
                                            @Override
                                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                                dialog.dismiss();
                                                if (etStr.isEmpty() || etStr.length() == 0) {
                                                    TubbyRingEntryActivity.this.getErrorNews("输入价格不能为空");
                                                    return;
                                                }

                                                mFootAdminPresenter.getXHHYGL_ZHGL_SetFootPrice(y, "1", etStr);
                                            }
                                        });
                            });
                        } else {
                            tv_hint_price.setVisibility(View.INVISIBLE);
                            setTopRightButton("", null);
                        }
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, TubbyRingEntryActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        TubbyRingEntryActivity.this.getErrorNews(msg);
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
                    if (listApiResponse.getErrorCode()==0){
                        //足环单价
                        mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice(y, "1");
                    }
                    TubbyRingEntryActivity.this.getErrorNews(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        //足环单价
        mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice(y, "1");

        et_f.setRawInputType(Configuration.KEYBOARD_QWERTY);
        et_f1.setRawInputType(Configuration.KEYBOARD_QWERTY);
        et_f2.setRawInputType(Configuration.KEYBOARD_QWERTY);

        et_f1.addTextChangedListener(etTextChangedListener());
        et_f2.addTextChangedListener(etTextChangedListener());
    }

    //连号足环输入监听
    private TextWatcher etTextChangedListener(){

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


    @OnClick({R.id.tv_btn1, R.id.tv_btn2, R.id.ll_btn, R.id.btn_sure1, R.id.btn_sure2})
    public void onViewClicked(View view) {
        if (price==0){
            TubbyRingEntryActivity.this.getErrorNews("请先设置单价后录入足环号码");
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
                mFootAdminPresenter.getXHHYGL_ZHGL_Add("特比环",1, et_f.getText().toString(), "", "");
                break;
            case R.id.btn_sure2:
                if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();
                mFootAdminPresenter.getXHHYGL_ZHGL_Add("特比环",2, "", et_f1.getText().toString(), et_f2.getText().toString());
                break;

//            case R.id.et_f:
//                //单号 起始足环1
//                FootDialog.initFootDialogOne(this, et_f.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
//                    @Override
//                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
//                        dialog.dismiss();
//                        et_f.setText(str);
//                    }
//                });
//                break;
//            case R.id.et_f1:
//                //连号 起始足环1
//                FootDialog.initFootDialogOne1(this, et_f1.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
//                    @Override
//                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
//                        dialog.dismiss();
//                        et_f1.setText(str);
//
//                        checkFoot(et_f1.getText().toString(), et_f2.getText().toString());
//                    }
//                });
//                break;
//            case R.id.et_f2:
//                //连号 起始足环2
//                FootDialog.initFootDialogOne1(this, et_f2.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
//                    @Override
//                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
//                        dialog.dismiss();
//                        et_f2.setText(str);
//                        checkFoot(et_f1.getText().toString(), et_f2.getText().toString());
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
//                            TubbyRingEntryActivity.this.getErrorNews("请填写正确的足环号码,起始足环号码必须小于终止足环号码");
                        }

                        if (!strDefault[1].equals(strDefault2[1])) {
                            //足环中半段不相同
//                            TubbyRingEntryActivity.this.getErrorNews("请填写正确的足环号码,起始足环号码必须小于终止足环号码");
                        }

                        //足环后半段不相同
                        try {
                            int size = Integer.valueOf(strDefault2[2]) - Integer.valueOf(strDefault[2]) +1;

                            NumberFormat NF = NumberFormat.getInstance();
                            NF.setGroupingUsed(false);//去掉科学计数法显示
                            if (size > 0) {
                                tv_money_z.setText(String.valueOf("总金额：" +NF.format((size * price))  + "元"));
                            } else {
//                                TubbyRingEntryActivity.this.getErrorNews("起始足环号码必须小于终止足环号码");
                            }
                        } catch (NumberFormatException e) {
                            TubbyRingEntryActivity.this.getErrorNews("请填写正确的足环号码");
                        }
                    }
                }
            }
        }
    }
}

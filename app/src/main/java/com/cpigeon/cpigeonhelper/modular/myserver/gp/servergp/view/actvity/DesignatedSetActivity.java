package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GP_GetChaZuEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.presenter.GpSmsPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao.GpSmsViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cpigeon.cpigeonhelper.R.id.et_sure1;


/**
 * Created by Administrator on 2018/5/16.
 */

public class DesignatedSetActivity extends ToolbarBaseActivity {

    @BindView(R.id.tx_name)
    TextView txName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tx_money)
    TextView txMoney;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.ll_divline)
    LinearLayout llDivline;

    @BindView(et_sure1)
    EditText etSure1;
    @BindView(R.id.et_sure2)
    EditText etSure2;
    @BindView(R.id.et_sure3)
    EditText etSure3;

    private String zb;
    private String tid;
    private GpSmsPresenter mGpSmsPresenter;

    private int serviceType = -1;//1:协会插组指定 2：公棚插组指定

    @BindView(R.id.ll_checkbox1)
    LinearLayout ll_checkbox1;
    @BindView(R.id.ll_checkbox2)
    LinearLayout ll_checkbox2;
    @BindView(R.id.ll_checkbox3)
    LinearLayout ll_checkbox3;

    private int cb1 = 1, cb2 = 1, cb3 = 1;//cb1:  1：未选中  2：规则选中

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_designated_set;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTopLeftButton(R.drawable.ic_back, DesignatedSetActivity.this::finish);

        cb1 = 1;
        cb2 = 1;
        cb3 = 1;
        zb = getIntent().getStringExtra("zb");
        tid = getIntent().getStringExtra("tid");
        serviceType = getIntent().getIntExtra("serviceType", -1);
        setTitle(zb + "组设置");

        mGpSmsPresenter = new GpSmsPresenter(new GpSmsViewImpl() {
            @Override
            public void getGP_GetChaZu(ApiResponse<GP_GetChaZuEntity> listApiResponse, String msg, Throwable throwable) {

                try {
                    if (throwable != null) {
                        DesignatedSetActivity.this.getThrowable(throwable);
                        return;
                    }

                    if (listApiResponse.getErrorCode() == 0) {

                        etName.setText(listApiResponse.getData().getBm());

                        if (Integer.valueOf(listApiResponse.getData().getCsf()) == 0) {
                            etMoney.setText("");
                        } else {
                            etMoney.setText(listApiResponse.getData().getCsf());
                        }

                        try {
                            if (Integer.valueOf(listApiResponse.getData().getGz1()) != 0) {
                                etSure1.setText(listApiResponse.getData().getGz1());
                            } else {
                                etSure1.setText("");
                            }
                        } catch (NumberFormatException e) {
                            etSure1.setText("");
                        }

                        try {
                            if (Integer.valueOf(listApiResponse.getData().getGz2()) != 0) {
                                etSure2.setText(listApiResponse.getData().getGz2());
                            } else {
                                etSure2.setText("");
                            }
                        } catch (NumberFormatException e) {
                            etSure2.setText("");
                        }

                        try {
                            if (Integer.valueOf(listApiResponse.getData().getGz3()) != 0) {
                                etSure3.setText(listApiResponse.getData().getGz3());
                            } else {
                                etSure3.setText("");
                            }
                        } catch (NumberFormatException e) {
                            etSure3.setText("");
                        }

                        if (Integer.valueOf(listApiResponse.getData().getGz1()) > 0) {
                            cb1 = 2;
                            cb2 = 1;
                            cb3 = 1;

                            ll_checkbox1.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box2));
                            ll_checkbox2.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
                            ll_checkbox3.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
                        }

                        if (Integer.valueOf(listApiResponse.getData().getGz2()) > 0) {
                            cb1 = 1;
                            cb2 = 2;
                            cb3 = 1;

                            ll_checkbox1.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
                            ll_checkbox2.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box2));
                            ll_checkbox3.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
                        }
                        if (Integer.valueOf(listApiResponse.getData().getGz3()) > 0) {
                            cb1 = 1;
                            cb2 = 1;
                            cb3 = 2;

                            ll_checkbox1.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
                            ll_checkbox2.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
                            ll_checkbox3.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box2));
                        }

                    } else {
                        DesignatedSetActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setGP_SetChaZu(ApiResponse<Object> listApiResponse, String msg, Throwable throwable) {

                try {

                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                    if (throwable != null) {
                        DesignatedSetActivity.this.getThrowable(throwable);
                        return;
                    }

                    if (listApiResponse.getErrorCode() == 0) {
                        //发布事件（刷新数据）
                        EventBus.getDefault().post(EventBusService.DESIGNATED_REFRESH);

                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, DesignatedSetActivity.this, dialog -> {
                            dialog.dismiss();
                            AppManager.getAppManager().killActivity(mWeakReference);
                        });
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, DesignatedSetActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        DesignatedSetActivity.this.getErrorNews(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                DesignatedSetActivity.this.getErrorNews(str);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                DesignatedSetActivity.this.getThrowable(throwable);
            }
        });
        mGpSmsPresenter.getGP_GetChaZu(serviceType, tid, zb);

        etSure1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                click1();
                return false;
            }
        });

        etSure2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                click2();
                return false;
            }
        });


        etSure3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                click3();
                return false;
            }
        });

        //输入别名不能输入元
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().indexOf("元") != -1) {
                    DesignatedSetActivity.this.errSweetAlertDialog = SweetAlertDialogUtil.showDialog(DesignatedSetActivity.this.errSweetAlertDialog, "为插组设置别名，请不要使用人民币相关字符，如元。", DesignatedSetActivity.this);
                    try {
                        etName.setText(s.toString().substring(0, s.toString().length() - 1));
                        etName.setSelection(etName.getText().toString().length());//光标移动到最后的位置
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @OnClick({R.id.ll_checkbox1, R.id.ll_checkbox2, R.id.ll_checkbox3, R.id.btn_sure, R.id.et_sure1, R.id.et_sure2, R.id.et_sure3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_sure1:
            case R.id.ll_checkbox1:
                click1();
                break;
            case R.id.et_sure2:
            case R.id.ll_checkbox2:
                click2();
                break;
            case R.id.et_sure3:
            case R.id.ll_checkbox3:
                click3();
                break;
            case R.id.btn_sure:

                if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
                mLoadDataDialog.show();
                mGpSmsPresenter.setGP_SetChaZu(serviceType, tid, zb,
                        etName.getText().toString(), etMoney.getText().toString(),
                        etSure1.getText().toString(), etSure2.getText().toString(),
                        etSure3.getText().toString(), cb1, cb2, cb3
                );

                break;
        }
    }

    public void click1() {

        if (showHint()) return;

        cb1 = 2;
        cb2 = 1;
        cb3 = 1;

        etSure2.setText("");
        etSure3.setText("");

        ll_checkbox1.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box2));
        ll_checkbox2.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
        ll_checkbox3.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
        showSoftInputFromWindow(this, etSure1);
    }

    public void click2() {
        if (showHint()) return;

        cb1 = 1;
        cb2 = 2;
        cb3 = 1;

        etSure1.setText("");
        etSure3.setText("");

        ll_checkbox1.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
        ll_checkbox2.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box2));
        ll_checkbox3.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
        showSoftInputFromWindow(this, etSure2);
    }

    public void click3() {

        if (showHint()) return;

        cb1 = 1;
        cb2 = 1;
        cb3 = 2;

        etSure1.setText("");
        etSure2.setText("");

        ll_checkbox1.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
        ll_checkbox2.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box3));
        ll_checkbox3.setBackground(getResources().getDrawable(R.drawable.bg_color_white_line_box2));

        showSoftInputFromWindow(this, etSure3);
    }

    private boolean showHint() {
        if (etMoney.getText().toString().isEmpty()) {
            DesignatedSetActivity.this.getErrorNews("请输入参赛费!");
            return true;
        }

        if (Integer.valueOf(etMoney.getText().toString()) == 0) {
            DesignatedSetActivity.this.getErrorNews("输入参赛费金额不能为0");
            return true;
        }

        return false;
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(Activity activity, EditText editText) {

        try {
            editText.setSelection(editText.getText().toString().length());//光标移动到最后的位置
        } catch (Exception e) {
            e.printStackTrace();
        }

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

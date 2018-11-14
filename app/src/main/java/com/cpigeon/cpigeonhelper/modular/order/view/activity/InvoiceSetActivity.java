package com.cpigeon.cpigeonhelper.modular.order.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.InvoiceEntity;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.view.viewdao.OrderViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.picker.AddressPickTask;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * 发票
 * Created by Administrator on 2018/6/12.
 */

public class InvoiceSetActivity extends ToolbarBaseActivity {

    @BindView(R.id.et_unit_name)
    EditText et_unit_name;//单位名称
    @BindView(R.id.et_ein_name)
    EditText et_ein_name;//税号
    @BindView(R.id.et_email)
    EditText et_email;//收件人邮箱

    @BindView(R.id.et_paper_name)
    EditText et_paper_name;//收件人姓名
    @BindView(R.id.et_paper_phone)
    EditText et_paper_phone;//收件人电话
    @BindView(R.id.et_paper_address)
    EditText et_paper_address;//收件人地址

    @BindView(R.id.et_paper_area)
    TextView et_paper_area;//收件人所在地区

    @BindView(R.id.spinner_way)
    Spinner spinner_way;
    @BindView(R.id.ll_zzfp)
    LinearLayout ll_zzfp;
    @BindView(R.id.ll_dzfp)
    LinearLayout ll_dzfp;

    private OrderPresenter mOrderPresenter;

    private int tag = -1;  // 1: 点击确定  2:  点击删除

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_invoice_set;
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTopLeftButton(R.drawable.ic_back, InvoiceSetActivity.this::finish);

        setTitle("发票设置");

        //数据
        List<String> data_list = new ArrayList<String>();
        data_list.add("电子发票");
        data_list.add("纸质发票");

        spinner_way.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 == 0) {
                    ll_dzfp.setVisibility(View.VISIBLE);
                    ll_zzfp.setVisibility(View.GONE);
                } else if (arg2 == 1) {
                    ll_dzfp.setVisibility(View.GONE);
                    ll_zzfp.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //适配器
        ArrayAdapter arr_adapter = new ArrayAdapter<String>(this, R.layout.item_spinner_layout, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner_way.setAdapter(arr_adapter);

        mOrderPresenter = new OrderPresenter(new OrderViewImpl() {

            @Override
            public void getThrowable(Throwable throwable) {
                InvoiceSetActivity.this.getThrowable(throwable);
            }

            @Override
            public void getErrorNews(String str) {
                InvoiceSetActivity.this.getErrorNews(str);
            }

            @Override
            public void getInvoiceData(ApiResponse<InvoiceEntity> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (mThrowable != null) {
                        InvoiceSetActivity.this.getThrowable(mThrowable);
                        return;
                    }

                    if (listApiResponse.getErrorCode() == 0) {
                        et_unit_name.setText(listApiResponse.getData().getDwmc());//单位名称
                        et_ein_name.setText(listApiResponse.getData().getSh());//税号
                        et_email.setText(listApiResponse.getData().getYx());//收件人邮箱

                        try {
                            p = listApiResponse.getData().getP();
                            c = listApiResponse.getData().getC();
                            a = listApiResponse.getData().getA();
                            et_paper_area.setText(p+c+a);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (listApiResponse.getData().getLx().equals("纸质发票")) {
                            spinner_way.setSelection(1);
                        } else {
                            spinner_way.setSelection(0);
                        }
                        et_paper_name.setText(listApiResponse.getData().getLxr());//收件人姓名
                        et_paper_phone.setText(listApiResponse.getData().getDh());//收件人电话
                        et_paper_address.setText(listApiResponse.getData().getDz());//收件人地址
                    } else {
                        InvoiceSetActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getDatas(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (listApiResponse.getErrorCode() == 0 && tag == 1) {
                        EventBus.getDefault().post(EventBusService.ORDER_INVOICE_REFRESH);

                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, msg, InvoiceSetActivity.this, dialog -> {
                            dialog.dismiss();

                            AppManager.getAppManager().killActivity(mWeakReference);
                        });//弹出提示
                    } else if (listApiResponse.getErrorCode() == 0 && tag == 2) {
                        InvoiceSetActivity.this.getErrorNews(msg);
                        EventBus.getDefault().post(EventBusService.ORDER_INVOICE_REFRESH);
                        AppManager.getAppManager().killActivity(mWeakReference);
                    } else {
                        InvoiceSetActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mInvoiceEntity = (InvoiceEntity) getIntent().getSerializableExtra("data");

        if (mInvoiceEntity != null) {

            p = mInvoiceEntity.getP();
            c = mInvoiceEntity.getC();
            a = mInvoiceEntity.getA();
            et_paper_area.setText(p+c+a);

            mOrderPresenter.getUser_Invoice_Get_detail(mInvoiceEntity.getId());
            setTopRightButton("删除", () -> {

                errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, "确定删除该发票信息", InvoiceSetActivity.this, dialog -> {
                    tag = 2;
                    mOrderPresenter.getUser_Invoice_Get_del(mInvoiceEntity.getId());
                });
            });
        } else {
            mOrderPresenter.getUser_Invoice_Get_detail("");
        }
    }

    private InvoiceEntity mInvoiceEntity;

    @OnClick({R.id.btn_sure, R.id.ll_paper_area})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_sure:
                //确定发票
                tag = 1;
                if (mInvoiceEntity != null) {
                    mOrderPresenter.getUser_Invoice_Set(mInvoiceEntity.getId(), et_unit_name, et_ein_name, spinner_way.getSelectedItem().toString(),
                            et_paper_name, et_paper_phone, et_paper_address,
                            et_email,p,c,a);
                } else {
                    mOrderPresenter.getUser_Invoice_Set("", et_unit_name, et_ein_name, spinner_way.getSelectedItem().toString(),
                            et_paper_name, et_paper_phone, et_paper_address,
                            et_email,p,c,a);
                }

                break;
            case R.id.ll_paper_area:

                Log.d("dyfp", "onViewClicked: -------sa");
                onAddress3Picker(this, new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {

                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        p = province.getName();
                        c = city.getName();
                        a = county.getName();
                        et_paper_area.setText(p+c+a);
                    }
                });
                break;
        }
    }

    private String p = "" ;//收件人所在省
    private String c ="";//收件人所在市
    private String a ;//收件人所在区县

    public void onAddress3Picker(Activity activity, AddressPickTask.Callback callback) {
        AddressPickTask task = new AddressPickTask(activity);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(callback);
        task.execute("四川省", "成都市", "青羊区");
    }

//    User_Invoice_Set接口新增参数：
//    p：收件人所在省
//    c：收件人所在市
//    a：收件人所在区县
//    江波  15:15:15
//    User_Invoice_List接口新增返回字段：
//    p：收件人所在省
//    c：收件人所在市
//    a：收件人所在区县
//    江波  15:18:55
//    User_Invoice_Get接口新增返回字段：
//    p：收件人所在省
//    c：收件人所在市
//    a：收件人所在区县
}

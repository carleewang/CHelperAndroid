package com.cpigeon.cpigeonhelper.modular.flyarea.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.SelectPlaceEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.presenter.FlyingPresenter;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.viewdao.FlyingView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.cpigeon.cpigeonhelper.utils.service.RequestCodeService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加司放地页面
 * Created by Administrator on 2017/9/25.
 */

public class AddFlyingAreaActivity extends ToolbarBaseActivity implements FlyingView {

    @BindView(R.id.et_flying_numbering)
    TextView etFlyingNumbering;//编号
    @BindView(R.id.et_flying_place)
    EditText etFlyingPlace;//地点
    @BindView(R.id.imgbtn_place)
    ImageButton imgbtnPlace;//选择地点的imgBtn
    @BindView(R.id.et_flying_lo)
    EditText etFlyingLo;//经度
    @BindView(R.id.et_flying_la)
    EditText etFlyingLa;//纬度
    @BindView(R.id.et_flying_remark)
    EditText etFlyingRemark;//备注
//    @BindView(R.id.hint_tv)
//    TextView hint_tv;//备注


    private FlyingPresenter presenter;//控制层
    private SelectPlaceEntity selectPlaceEntity;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_flying_area;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String listRefresh) {
        //结束比赛成功返回
        if (listRefresh.equals(EventBusService.FLYING_LIST_REFRESH)) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            setTitle("添加司放地");
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            setTitle("添加训放地");
        }

        setTopLeftButton(R.drawable.ic_back, this::finish);

        etFlyingNumbering.setText(getIntent().getIntExtra("faid", 0) + 1 + "");//设置编号

        //设置经纬度输入文本监听
        GPSFormatUtils.textChangedListener(this, etFlyingLo, etFlyingLa);

        setTopRightButton("完成", new OnClickListener() {
            @Override
            public void onClick() {

                if (etFlyingLo.getText().toString().equals("") || etFlyingLa.getText().toString().equals("") || etFlyingPlace.getText().toString().equals("")) {
                    CommonUitls.showToast(AddFlyingAreaActivity.this, "输入内容不能为空");
                    return;
                }

                double lo = Double.valueOf(etFlyingLo.getText().toString().trim());
                double la = Double.valueOf(etFlyingLa.getText().toString().trim());
                Log.d(TAG, "onClick: lo-->" + lo + "    la-->" + la);

                presenter.addFlyingArea(etFlyingRemark.getText().toString(),//备注（别名）
                        etFlyingPlace.getText().toString(),//地点
                        Double.valueOf(lo),//经度（安捷格式）
                        Double.valueOf(la));//纬度（安捷格式）
//                        Double.valueOf(CommonUitls.GPS2AjLocation(lo)),//经度（安捷格式）
//                        Double.valueOf(CommonUitls.GPS2AjLocation(la)));//纬度（安捷格式）
            }
        });

        presenter = new FlyingPresenter(this);
    }


    /**
     * 点击选择地点
     */
    @OnClick(R.id.imgbtn_place)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, SelectPlaceActivity.class), 0x0084);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //设置内容
            if (requestCode == 0x0084 && data.getExtras().getParcelable("selectPlaceEntity") != null) {
                selectPlaceEntity = data.getExtras().getParcelable("selectPlaceEntity");
                etFlyingPlace.setText(selectPlaceEntity.getLocation() + "");//设置地名
                etFlyingLo.setText(CommonUitls.GPS2AjLocation(selectPlaceEntity.getLongitude()) + "");//设置经度
                etFlyingLa.setText(CommonUitls.GPS2AjLocation(selectPlaceEntity.getLatitude()) + "");//设置纬度
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFlyingFlyingData(ApiResponse<Object> listApiResponse, String msg, Throwable throwable) {
        try {
            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, this);//弹出提示
        } catch (Exception e) {
            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "网络异常，请检查网络连接", this);//弹出提示
        }
    }

    @Override
    public void getFlyingData(ApiResponse<List<FlyingAreaEntity>> listApiResponse, String msg, Throwable throwable) {

    }

    /**
     * 添加司放地成功后回调
     *
     * @param msg
     */
    @Override
    public void getFlyingDataNull(String msg) {
        try {
            EventBus.getDefault().post(EventBusService.FLYING_LIST_REFRESH);
            if (selectPlaceEntity != null) {
                Intent intent = new Intent();

                FlyingAreaEntity mFlyingAreaEntity = new FlyingAreaEntity();

                mFlyingAreaEntity.setLatitude(Double.valueOf(CommonUitls.GPS2AjLocation(selectPlaceEntity.getLatitude())));
                mFlyingAreaEntity.setLongitude(Double.valueOf(CommonUitls.GPS2AjLocation(selectPlaceEntity.getLongitude())));
                mFlyingAreaEntity.setArea(selectPlaceEntity.getLocation());

                intent.putExtra("FlyingAreaEntity", mFlyingAreaEntity);
                setResult(RequestCodeService.ADD_SFD_XFD, intent);

            }

            CommonUitls.showSweetDialog1(this, msg, dialog -> {
                AppManager.getAppManager().killActivity(AddFlyingAreaActivity.this.mWeakReference);//关闭当前页面
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }
}

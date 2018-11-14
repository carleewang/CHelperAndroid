package com.cpigeon.cpigeonhelper.modular.flyarea.view.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.presenter.FlyingPresenter;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.viewdao.FlyingView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑司放地页面 （编辑司放地界面）
 * Created by Administrator on 2017/9/25.
 */

public class EditFlyingActivity extends ToolbarBaseActivity implements FlyingView {

    @BindView(R.id.et_flying_place)
    EditText etChangeDomainName;//地点
    @BindView(R.id.et_flying_longitude)
    EditText etFlyingLongitude;//经度
    @BindView(R.id.et_flying_latitude)
    EditText etFlyingLatitude;//纬度
    @BindView(R.id.et_flying_remark)
    EditText etFlyingRemark;//备注
    @BindView(R.id.et_flying_del)
    Button etFlyingDel;//按钮删除

    private FlyingAreaEntity entity;//司放地单个item的实体类

    private FlyingPresenter presenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_flying;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("编辑");
        //点击左上角关闭当前页面
        setTopLeftButton(R.drawable.ic_back, this::finish);

        //点击右上角完成按钮
        setTopRightButton("完成", () -> {
                    try {
                        presenter.modifyFlyingArea(entity.getFaid(),//需要修改的司放地坐标
                                etFlyingRemark.getText().toString(),//备注
                                etChangeDomainName.getText().toString(),//司放地（地点）
                                etFlyingLongitude.getText().toString(),//经度
                                etFlyingLatitude.getText().toString());//纬度
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        entity = getIntent().getParcelableExtra("flyingAreaEntity");
        etChangeDomainName.setText(entity.getArea());//设置司放地
        etFlyingLongitude.setText(entity.getLongitude() + "");//设置经度
        etFlyingLatitude.setText(entity.getLatitude() + "");//设置纬度
        etFlyingRemark.setText(entity.getAlias());//设置备注（别名）

        presenter = new FlyingPresenter(this);//初始化控制层
    }

    @OnClick(R.id.et_flying_del)//点击删除司放地
    public void onViewClicked() {
        CommonUitls.showSweetDialog(this, "是否删除该司放地", dialog -> {
            dialog.dismiss();
            presenter.delFlyingArea(entity.getFaid());//删除司放地
        });
    }

    @Override
    public void addFlyingFlyingData(ApiResponse<Object> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getFlyingData(ApiResponse<List<FlyingAreaEntity>> listApiResponse, String msg, Throwable throwable) {

    }

    /**
     * 删除，修改成功后回调
     *
     * @param msg（消息）
     */
    @Override
    public void getFlyingDataNull(String msg) {
        try {
            EventBus.getDefault().post(EventBusService.FLYING_LIST_REFRESH);

            CommonUitls.showSweetDialog1(this, msg, dialog -> {
                dialog.dismiss();
                AppManager.getAppManager().killActivity(mWeakReference);//关闭当前的Activity
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

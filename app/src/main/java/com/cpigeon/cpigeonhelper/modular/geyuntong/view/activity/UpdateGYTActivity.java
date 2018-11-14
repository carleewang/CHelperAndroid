
package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.activity.SelectFlyingActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTListPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTListView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cpigeon.cpigeonhelper.R.id.add_play_location;

/**
 * 编辑鸽运通比赛
 * Created by Administrator on 2017/10/9.
 */
public class UpdateGYTActivity extends ToolbarBaseActivity implements GYTListView {

    @BindView(R.id.et_play_name)
    EditText etPlayName;//比赛名称
    @BindView(R.id.ll_fly_location)
    LinearLayout llFlyLocation;//司放地地点item
    @BindView(add_play_location)
    TextView playLocation;//地点
    @BindView(R.id.et_lo)
    EditText etLo;//经度
    @BindView(R.id.et_la)
    EditText etLa;//纬度
    @BindView(R.id.imtbtn_determine)
    Button imtbtnDetermine;//确定按钮
    @BindView(R.id.name_hint)
    TextView nameHint;//名称提示
    @BindView(R.id.location_hint)
    TextView locationHint;//地点提示
    @BindView(R.id.lola_hint)
    TextView lolaHint;//坐标提示

    private GYTListPresenter presenter;//添加比赛控制层

    private int rid = -1;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_gyt_play;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTopLeftButton(R.drawable.ic_back, UpdateGYTActivity.this::finish);

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            setTitle("编辑比赛");
            playLocation.setText(R.string.str_gyt_add_play_fly_hint_location);//输入司放地点
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            setTitle("编辑训放");
            etPlayName.setFocusable(false);//设置不能点击
            etPlayName.setFocusableInTouchMode(false);//设置不能弹出软键盘
            playLocation.setText(R.string.str_gyt_add_fly_hint_location);//输入训放地点
            nameHint.setText(R.string.str_gyt_add_fly_play_name);//训放名称
            locationHint.setText(R.string.str_gyt_add_fly_location);//训放地点
            playLocation.setText(R.string.str_gyt_add_fly_hint_location);//请输入训放地点
            lolaHint.setText(R.string.str_gyt_add_fly_coordinate);//训放坐标
        }

        //上一个页面传送过来的值设置

        etLo.setText(getIntent().getStringExtra("gyt_lo"));//经度
        etLa.setText(getIntent().getStringExtra("gyt_la"));//纬度
        playLocation.setText(getIntent().getStringExtra("gyt_area"));//地点
        etPlayName.setText(getIntent().getStringExtra("gyt_name"));//竞赛名称

        rid = getIntent().getIntExtra("gyt_rid", -1);

        presenter = new GYTListPresenter(this);
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
    }

    @OnClick({R.id.ll_fly_location, R.id.imtbtn_determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_fly_location://选择司放地
                Intent intent = new Intent(this, SelectFlyingActivity.class);
                startActivityForResult(intent, 0x0034);
                break;

            case R.id.imtbtn_determine://确定添加
                presenter.updataGytPlay(etPlayName,//比赛名称
                        playLocation.getText().toString(),//司放地
                        etLo,//经度
                        etLa,//纬度
                        String.valueOf(rid));//常用司放地 ID
                break;
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(FlyingAreaEntity item) {
        etLo.setText(String.valueOf(item.getLongitude()));//经度
        etLa.setText(String.valueOf(item.getLatitude()));//纬度
        playLocation.setText(item.getArea());//地点
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    /**
     * 页面返回方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getGYTRaceList(ApiResponse<List<GeYunTong>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getGYTRaceLists(List<GeYunTong> geYunTongDatas) {

    }

    @Override
    public void getReturnMsg(String msg) {

    }


    /**
     * 添加比赛成功后回调
     */
    @Override
    public void addPlaySuccess() {

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("温馨提示");
        dialog.setContentText("修改比赛成功");
        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //发布事件
                EventBus.getDefault().post("playListRefresh");
                EventBus.getDefault().post("playListRefreshs");
                //关闭当前页面
                AppManager.getAppManager().killActivity(mWeakReference);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
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

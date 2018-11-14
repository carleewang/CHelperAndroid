package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.activity.SelectFlyingActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTListPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTListViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加比赛页面
 * Created by Administrator on 2017/9/28.
 */

public class AddGytPlayActivity extends ToolbarBaseActivity {

    @BindView(R.id.et_play_name)
    EditText etPlayName;//比赛名称
    @BindView(R.id.ll_fly_location)
    LinearLayout llFlyLocation;//司放地地点item
    @BindView(R.id.add_play_location)
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
//    @BindView(R.id.hint_tv)
//    MyTextView hintTv;

    @BindView(R.id.ll_gk)
    LinearLayout ll_gk;//
    @BindView(R.id.btn_cb)
    CheckBox btnCb;
    private int show = 0;//是否公开

    private GYTListPresenter presenter;//添加比赛控制层

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

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            setTitle("添加比赛");
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            setTitle("添加训放");
            ll_gk.setVisibility(View.VISIBLE);
            etPlayName.setFocusable(false);//设置不能点击
            etPlayName.setFocusableInTouchMode(false);//设置不能弹出软键盘
            etPlayName.setText(DateUtils.getStringDateNow() + " 训放");
            nameHint.setText(R.string.str_gyt_add_fly_play_name);//训放名称
            locationHint.setText(R.string.str_gyt_add_fly_location);//训放地点
            playLocation.setHint(R.string.str_gyt_add_fly_hint_location);//请输入训放地点
            lolaHint.setText(R.string.str_gyt_add_fly_coordinate);//训放坐标
        }

        setTopLeftButton(R.drawable.ic_back, AddGytPlayActivity.this::finish);

        //设置经纬度输入文本监听
        GPSFormatUtils.textChangedListener(this, etLo, etLa);

        presenter = new GYTListPresenter(new GYTListViewImpl() {
            /**
             * 添加比赛成功后回调
             */
            @Override
            public void addPlaySuccess() {
                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "添加成功", AddGytPlayActivity.this, dialog -> {
                        dialog.dismiss();
                        //发布事件
                        EventBus.getDefault().post("playListRefresh");
                        //关闭当前页面
                        AppManager.getAppManager().killActivity(mWeakReference);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {

                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, str, AddGytPlayActivity.this);//弹出提示
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
    }

    @OnClick({R.id.ll_fly_location, R.id.imtbtn_determine, R.id.btn_cb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_fly_location://选择司放地
                Intent intent = new Intent(this, SelectFlyingActivity.class);
//                startActivityForResult(intent, RequestCodeService.ADD_SFD_XFD);
                startActivity(intent);
                break;
            case R.id.imtbtn_determine://确定添加

                presenter.addGytPlay(etPlayName,//比赛名称
                        playLocation.getText().toString(),//司放地
                        etLo,//经度
                        etLa,//纬度
                        null,//常用司放地 ID
                        show, mLoadDataDialog);
                break;

            case R.id.btn_cb:
                if (btnCb.isChecked()) {
                    show = 1;//公开
                } else {
                    show = 0;//不公开
                }
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
}

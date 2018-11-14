package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.PlayAdminPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdaoimpl.PlayAdminViewImpl;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 比赛管理列表详情
 * Created by Administrator on 2017/12/18.
 */

public class DetailsPlayActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_play_name)
    TextView tvPlayName;//项目名称
    @BindView(R.id.tv_paly_flay_location)
    TextView tvPalyFlayLocation;//司放地点
    @BindView(R.id.tv_flay_time)
    TextView tvFlayTime;//司放时间
    @BindView(R.id.ll_state)
    LinearLayout llState;
    @BindView(R.id.img_del_hint)
    ImageView imgDelHint;//
    @BindView(R.id.tv_del_hint)
    TextView tvDelHint;
    private PlayListEntity playListEntity;//当前item数据详情

    private PlayAdminPrensenter mPlayAdminPrensenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_play_admin_xh;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("比赛详情");
        setTopLeftButton(R.drawable.ic_back, DetailsPlayActivity.this::finish);
        playListEntity = (PlayListEntity) getIntent().getSerializableExtra("PlayListEntity");

        if (playListEntity != null) {
            tvPlayName.setText(playListEntity.getXmmc());//项目名称
            tvPalyFlayLocation.setText(playListEntity.getSfdd());//司放地点
            tvFlayTime.setText(playListEntity.getSfsj());//司放时间
            if (playListEntity.getSczt().equals("0")) {//sczt;//申请删除状态：0正常，1删除确认中
                llState.setClickable(true);//设置不可点击
                imgDelHint.setVisibility(View.VISIBLE);
                tvDelHint.setText("申请删除比赛");
//                setTopRightButton("短信设置", () -> {
//                    Intent intent = new Intent(this, PlaySmsSetActivity.class);
//                    intent.putExtra("PlayListEntity", playListEntity);
//                    startActivity(intent);
//                });
            } else if (playListEntity.getSczt().equals("1")) {
                llState.setClickable(false);//设置不可点击
                imgDelHint.setVisibility(View.GONE);
                tvDelHint.setText("删除审核中");
            }
        }

        mPlayAdminPrensenter = new PlayAdminPrensenter(new PlayAdminViewImpl() {
            /**
             * 删除比赛申请结果
             *
             * @param dxsetApiResponse
             * @param msg
             */
            @Override
            public void subBsdxData(ApiResponse<Object> dxsetApiResponse, String msg) {
                try {
                    if (dxsetApiResponse.getErrorCode() == 0) {
                        llState.setClickable(false);//设置不可点击
                        imgDelHint.setVisibility(View.GONE);
                        tvDelHint.setText("删除审核中");

                        //发布事件（通知比赛列表刷新数据）
                        EventBus.getDefault().post("playAdminDataRefresh");
                        CommonUitls.showSweetAlertDialog(DetailsPlayActivity.this, "温馨提示", msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.ll_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_state:
                try {
                    SweetAlertDialog dialog = new SweetAlertDialog(DetailsPlayActivity.this, SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("温馨提示");
                    dialog.setContentText("是否删除比赛");
                    dialog.setCancelText("取消");
                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setConfirmText("确定");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.dismiss();
                            //删除比赛申请
                            if (playListEntity != null) {
                                mPlayAdminPrensenter.subDelPlayApply(playListEntity.getBsid());
                            }
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

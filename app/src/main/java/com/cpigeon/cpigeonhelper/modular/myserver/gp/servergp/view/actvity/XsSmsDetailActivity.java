package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsListEntity;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;

/**
 * 训赛短信详情
 * Created by Administrator on 2017/12/25.
 */

public class XsSmsDetailActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_xmmc)
    TextView tvXmmc;//项目名称
    @BindView(R.id.tv_sfsj)
    TextView tvSfsj;//司放时间
    @BindView(R.id.tv_sfdd)
    TextView tvSfdd;//司放地点
    @BindView(R.id.tv_slys)
    TextView tvSlys;//上笼羽数
    @BindView(R.id.tv_xsys)
    TextView tvXsys;//显示羽数
    private XsListEntity mXsListEntity;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_xs_sms_detail;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("训赛短信详情");
        setTopLeftButton(R.drawable.ic_back, XsSmsDetailActivity.this::finish);

        mXsListEntity = (XsListEntity) getIntent().getSerializableExtra("XsListEntity");

        if (mXsListEntity != null) {
            tvXmmc.setText(mXsListEntity.getXmmc());//项目名称
            tvSfsj.setText(mXsListEntity.getSfsj());//司放时间
            tvSfdd.setText(mXsListEntity.getSfdd());//司放地点
            tvSlys.setText("" + mXsListEntity.getSlys());//上笼羽数
            tvXsys.setText(mXsListEntity.getXsys());//显示羽数
        }

        setTopRightButton("设置", () -> {
            if (mXsListEntity != null) {
                Intent intent = new Intent(this, XsSmsSetActivity.class);
                intent.putExtra("XsListEntity", mXsListEntity);
                startActivity(intent);
            }
        });
    }
}

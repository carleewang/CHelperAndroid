package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SGTInfoActivity;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/15.
 */

public class GpsgHomeActivity extends ToolbarBaseActivity {

    @BindView(R.id.gpsg_btn1)
    Button gpsg_btn1;//授权管理
    @BindView(R.id.gpsg_btn3)
    Button gpsg_btn3;//错误补拍
    @BindView(R.id.gpsg_btn9)
    Button gpsg_btn9;//查看赛鸽

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gpsg_home;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);

        setTitle("公棚赛鸽");

        setTopLeftButton(R.drawable.ic_back, this::finish);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        try {
            if (getIntent().getIntExtra("sqpzuid", -1) != -1) {
                gpsg_btn1.setVisibility(View.GONE);
                gpsg_btn3.setVisibility(View.GONE);
                gpsg_btn9.setVisibility(View.GONE);
                SGTPresenter.uids = getIntent().getIntExtra("sqpzuid", -1);
                SGTPresenter.isAuth = true;
            } else {
                SGTPresenter.isAuth = false;
                SGTPresenter.uids = AssociationData.getUserId();
                setTopRightButton("信息", new OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(GpsgHomeActivity.this, SGTInfoActivity.class);
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent intent;

    @OnClick({R.id.gpsg_btn1, R.id.gpsg_btn2, R.id.gpsg_btn3, R.id.gpsg_btn4, R.id.gpsg_btn5,
            R.id.gpsg_btn6, R.id.gpsg_btn7, R.id.gpsg_btn8, R.id.gpsg_btn9})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.gpsg_btn1://授权管理
                IntentBuilder.Builder().startParentActivity(this, CancelPhotoPermissionsFragment.class);

                break;
            case R.id.gpsg_btn2://未用

                break;
            case R.id.gpsg_btn3://错误补拍
                intent = new Intent(this, GpsgTagSearchActivity.class);
                intent.putExtra("title", "错误补拍");
                intent.putExtra("tagid", 111);
                startActivity(intent);

                break;
            case R.id.gpsg_btn4://入棚拍照
                intent = new Intent(this, SGTHomeActivity3.class);
                startActivity(intent);
                break;
            case R.id.gpsg_btn5://日常随拍
                intent = new Intent(this, GpsgTagSearchActivity.class);
                intent.putExtra("title", "日常随拍");
                intent.putExtra("tagid", 8);
                startActivity(intent);
                break;
            case R.id.gpsg_btn6://查棚收费
                intent = new Intent(this, GpsgTagSearchActivity.class);
                intent.putExtra("title", "查棚收费");
                intent.putExtra("tagid", 9);
                startActivity(intent);
                break;
            case R.id.gpsg_btn7://比赛上笼
                intent = new Intent(this, GpsgTagSearchActivity.class);
                intent.putExtra("title", "比赛上笼");
                intent.putExtra("tagid", 10);
                startActivity(intent);
                break;
            case R.id.gpsg_btn8://获奖荣誉
                intent = new Intent(this, GpsgTagSearchActivity.class);
                intent.putExtra("title", "获奖荣誉");
                intent.putExtra("tagid", 11);
                startActivity(intent);
                break;
            case R.id.gpsg_btn9://查看赛鸽
                intent = new Intent(this, SGTHomeActivity3.class);
                SGTHomeActivity3.isShowPhone = 1;
                startActivity(intent);
                break;
        }
    }
}

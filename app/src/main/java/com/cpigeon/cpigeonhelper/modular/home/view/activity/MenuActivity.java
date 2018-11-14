package com.cpigeon.cpigeonhelper.modular.home.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.GYTHomeActivity;
import com.cpigeon.cpigeonhelper.modular.menu.view.activity.BulletinActivity;
import com.cpigeon.cpigeonhelper.modular.menu.view.activity.LogbookActivity;
import com.cpigeon.cpigeonhelper.modular.menu.view.activity.SettingActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.activity.OrgInforActivity;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * 菜单页  舍弃 第一个版本
 * Created by Administrator on 2017/9/12.
 */
public class MenuActivity extends BaseActivity {
    @BindView(R.id.menu_close)
    ImageButton menuClose;
    @BindView(R.id.menu_user_head_img)
    CircleImageView menuUserHeadImg;
    @BindView(R.id.menu_userName)
    TextView menuUserName;
    @BindView(R.id.menu_userphone)
    TextView menuUserphone;
    @BindView(R.id.menu_information)
    TextView menuInformation;
    @BindView(R.id.ac_menu_information)
    LinearLayout acMenuInformation;
    @BindView(R.id.ac_menu_mine)
    LinearLayout acMenuMine;
    @BindView(R.id.ac_menu_setting)
    LinearLayout acMenuSetting;
    @BindView(R.id.ac_menu_announcement)
    LinearLayout acMenuAnnouncement;
    @BindView(R.id.ac_menu_logbook)
    LinearLayout acMenuJournal;//操作日志
    @BindView(R.id.ac_menu_helping)
    LinearLayout acMenuHelping;

    @Override
    protected void swipeBack() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void setStatusBar() {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        //初始化用户头像
        Glide.with(this).load(TextUtils.isEmpty(AssociationData.getUserImgUrl()) ? null : AssociationData.getUserImgUrl())
                .placeholder(R.mipmap.head)
                .error(R.mipmap.head)
                .into(menuUserHeadImg);

        //设置用户名字
        menuUserName.setText(AssociationData.getUserName());
        //设置用户签名
        menuUserphone.setText(AssociationData.getUserSign());
        //设置组织信息
        menuInformation.setText(AssociationData.getUserAccountTypeString());
    }


    @OnClick({R.id.menu_close, R.id.ac_menu_information, R.id.ac_menu_mine, R.id.ac_menu_setting, R.id.ac_menu_announcement, R.id.ac_menu_logbook, R.id.ac_menu_helping})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_close://关闭
                AppManager.getAppManager().killActivity(mWeakReference);
                break;
            case R.id.ac_menu_information://协会，公棚信息
                queryInformation();
                break;
            case R.id.ac_menu_mine://我的鸽运通
                startActivity(new Intent(MenuActivity.this, GYTHomeActivity.class));
                break;
            case R.id.ac_menu_setting://设置
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.ac_menu_announcement://公告通知
                startActivity(new Intent(MenuActivity.this, BulletinActivity.class));
                break;
            case R.id.ac_menu_logbook://操作日志
                startActivity(new Intent(this, LogbookActivity.class));
                break;
            case R.id.ac_menu_helping://帮助
                Intent intent = new Intent(MenuActivity.this, WebViewActivity.class);
                intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.HELP_TYPE);
                startActivity(intent);
                break;
        }
    }

    /**
     * 没有查看组织信息的权限提示框
     */
    private void queryInformation() {
        if (menuInformation.getText().toString().equals("组织信息")) {
            CommonUitls.showSweetAlertDialog(this, "温馨提示", "你没有查看权限");
        } else {
            startActivity(new Intent(this, OrgInforActivity.class));
        }
    }
}

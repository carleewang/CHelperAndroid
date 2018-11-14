package com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.base.BaseInputDialog;
import com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.base.PictureSelectUtil;
import com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.entity.ShootInfoEntity;
import com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.viewmodel.ShootViewModel;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.StringUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 拍摄视频  设置页面
 * Created by Administrator on 2018/9/30 0030.
 */

public class ShootVideoSettingActivity extends ToolbarBaseActivity {


    @BindView(R.id.tv_name)
    TextView tv_name;//
    @BindView(R.id.img_logo)
    ImageView img_logo;//
    @BindView(R.id.ll_upload_logo)
    LinearLayout ll_upload_logo;//
    @BindView(R.id.rl_upload_logo)
    RelativeLayout rlUploadLogo;

    private String[] chooseWays;

    private ShootViewModel mShootViewModel;


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_shoot_video_setting;
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, ShootVideoSettingActivity.this::finish);
        setTitle("信鸽随拍");
        chooseWays = new String[]{"打开相机", "打开相册"};
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mShootViewModel = new ShootViewModel();

        mShootViewModel.getTXGP_GetTouXiangGeSheMingChengData(datas -> {
            initDatas(datas);
        });
    }

    private BaseInputDialog mBaseInputDialog;

    @OnClick({R.id.btn_shoot_video, R.id.rl_upload_logo, R.id.ll_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_shoot_video:
                //拍摄视频
                Intent intent = new Intent(this, AtAnyTimeShootingActivity.class);
                intent.putExtra(IntentBuilder.KEY_DATA, mShootViewModel.mShootInfoEntity);
                intent.putExtra("type", "video");
                startActivity(intent);
                break;
            case R.id.rl_upload_logo:
                //上传logo
                new SaActionSheetDialog(this)
                        .builder()
                        .addSheetItem("相册选取", OnSheetItemClickListener)
                        .addSheetItem("拍一张", OnSheetItemClickListener)
                        .show();

                break;
            case R.id.ll_name:
                //鸽舍名称
                mBaseInputDialog = BaseInputDialog.show(this.getFragmentManager()
                        , "请输入", tv_name.getText().toString(), 50, InputType.TYPE_NUMBER_FLAG_DECIMAL, content -> {
                            tv_name.setText(content);
                            mBaseInputDialog.hide();
                            mShootViewModel.mShootInfoEntity.setSszz(content);
                            mShootViewModel.getTXGP_SetTouXiangGeSheMingChengData(o -> {
                                mShootViewModel.getTXGP_GetTouXiangGeSheMingChengData(datas -> {
                                    initDatas(datas);
                                });
                            });
                        }, null);

                break;
        }
    }

    private void initDatas(ApiResponse<ShootInfoEntity> datas) {
        mShootViewModel.mShootInfoEntity = datas.getData();

        if (StringUtil.isStringValid(mShootViewModel.mShootInfoEntity.getImgurl())) {
            ll_upload_logo.setVisibility(View.GONE);
            img_logo.setVisibility(View.VISIBLE);
            Glide.with(this).load(mShootViewModel.mShootInfoEntity.getImgurl()).into(img_logo);
        } else {
            img_logo.setVisibility(View.GONE);
            ll_upload_logo.setVisibility(View.VISIBLE);
        }

        tv_name.setText(mShootViewModel.mShootInfoEntity.getSszz());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode != Activity.RESULT_OK) return;
            if (requestCode == PictureMimeType.ofImage()) {

                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                mShootViewModel.mShootInfoEntity.setImgurl(selectList.get(0).getCutPath());

                mShootViewModel.getTXGP_SetTouXiangGeSheMingChengData(o -> {
                    mShootViewModel.getTXGP_GetTouXiangGeSheMingChengData(datas -> {
                        initDatas(datas);
                    });
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            switch (which) {
                case 1:
                    //跳转到相册
                    PictureSelectUtil.showChooseHeadImage(ShootVideoSettingActivity.this);
                    break;
                case 2:
                    //跳转到相机
                    PictureSelectUtil.openCamera(ShootVideoSettingActivity.this, true);
                    break;
            }
        }
    };
}

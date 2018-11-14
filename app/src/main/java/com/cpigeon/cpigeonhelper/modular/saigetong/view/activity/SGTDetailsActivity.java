package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter.SGTInfoFgmtAdapter;
import com.cpigeon.cpigeonhelper.ui.CustomViewPager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;

/**
 * 赛鸽通详情页
 * Created by Administrator on 2017/12/5.
 */

public class SGTDetailsActivity extends ToolbarBaseActivity {
//
//    @BindView(R.id.btn_zh_num)
//    TextView btnZhNum;
//    @BindView(R.id.btn_gz_name)
//    TextView btnGzName;
//    @BindView(R.id.tb_btn)
//    ToggleButton btnTb;

    @BindView(R.id.viewPager)
    CustomViewPager mViewPager;


    private SGTHomeListEntity.DataBean dataBean;
    private SGTHomeListEntity sgtHomeListEntity;


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sgt_details;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        dataBean = (SGTHomeListEntity.DataBean) getIntent().getSerializableExtra("DataBean");
        sgtHomeListEntity = (SGTHomeListEntity) getIntent().getSerializableExtra("SGTHomeListEntity");

        setTitle(dataBean.getFoot());
//        setTopRightButton(R.mipmap.photo_img_w, new OnClickListener() {
//            @Override
//            public void onClick() {
////                startPhoto();//开始跳转拍照
//            }
//        });

//        btnTb.setChecked(true);//默认设置点击

        setTopLeftButton(R.drawable.ic_back, this::finish);
        initViewPager();//初始化ViewPager


        mViewPager.setCurrentItem(0);//view默认显示第一个页面

    }

    private void initViewPager() {
        mViewPager.setScanScroll(false);//禁止滑动
        mViewPager.setOffscreenPageLimit(2);//预加载
        mViewPager.setAdapter(new SGTInfoFgmtAdapter(SGTDetailsActivity.this.getSupportFragmentManager(), SGTDetailsActivity.this));
        mViewPager.setCurrentItem(0);//view默认显示第一个页面
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}


//    @OnClick({R.id.btn_zh_num, R.id.btn_gz_name, R.id.tb_btn})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_zh_num:
//                btnTb.setChecked(true);
//                mViewPager.setCurrentItem(0);//view默认显示第一个页面
//                btnGzName.setTextColor(getResources().getColor(R.color.color_999999));
//                btnZhNum.setTextColor(getResources().getColor(R.color.color_sgt_search));
//                setTitle(dataBean.getFoot());
//                setTopRightButton(R.mipmap.photo_img_w, new OnClickListener() {
//                    @Override
//                    public void onClick() {
//                        startPhoto();
//                    }
//                });
//                break;
//            case R.id.btn_gz_name:
//                btnTb.setChecked(false);
//                btnGzName.setTextColor(getResources().getColor(R.color.color_sgt_search));
//                btnZhNum.setTextColor(getResources().getColor(R.color.color_999999));
//                setTitle(sgtHomeListEntity.getXingming());
//                setTopRightButton(0);
//                mViewPager.setCurrentItem(1);//view默认显示第一个页面
//                break;
//            case R.id.tb_btn:
//                if (btnTb.isChecked()) {
////                    btnGzName.setText("鸽主名称");
//                    btnGzName.setTextColor(getResources().getColor(R.color.color_999999));
//                    btnZhNum.setTextColor(getResources().getColor(R.color.color_sgt_search));
//
//                    mViewPager.setCurrentItem(0);//view默认显示第一个页面
//                    setTitle(dataBean.getFoot());
//                    setTopRightButton(R.mipmap.photo_img_w, new OnClickListener() {
//                        @Override
//                        public void onClick() {
//                            startPhoto();
//                        }
//                    });
//                } else {
//                    btnZhNum.setTextColor(getResources().getColor(R.color.color_999999));
//                    btnGzName.setTextColor(getResources().getColor(R.color.color_sgt_search));
//
////                    btnZhNum.setText("足环号码");
//                    setTitle(sgtHomeListEntity.getXingming());
//                    setTopRightButton(0);
//                    mViewPager.setCurrentItem(1);//view默认显示第一个页面
//                }
//
//                break;
//        }
//    }
//
//    /**
//     * 跳转到照相机拍照
//     */
//    private Intent intent;
//
//    private void startPhoto() {
////        if (dataBean != null) {
////            Intent intent = new Intent(SGTDetailsActivity.this, AtAnyTimeShootingActivity.class);
////            intent.putExtra("type", "sgt");
////            intent.putExtra("DataBean", dataBean);
////            SGTDetailsActivity.this.startActivity(intent);
////        }
//
//        ImgChooseDialogUtil.createImgChonseDialog(SGTDetailsActivity.this, new SaActionSheetDialog.OnSheetItemClickListener() {
//            @Override
//            public void onClick(int which) {
//                switch (which) {
//                    case 1:
//                        //跳转到相册
//                        ImgChooseDialogUtil.jumpAlbum(SGTDetailsActivity.this, PictureMimeType.ofImage(), 1, PictureConfig.LUBAN_COMPRESS_MODE, RequestCodeService.SGT_IMG_CHOOSE_DETAILS);
//                        break;
//                    case 2:
//                        //跳转到相机
//                        intent = new Intent(SGTDetailsActivity.this, AtAnyTimeShootingActivity.class);
//                        intent.putExtra("type", "sgt");
//                        intent.putExtra("DataBean", dataBean);
//                        intent.putExtra("uplodType", 1);//1：单个足环  2：鸽主
//                        intent.putExtra("SGTHomeListEntity", getIntent().getSerializableExtra("SGTHomeListEntity"));
//                        SGTDetailsActivity.this.startActivity(intent);
//                        break;
//                }
//            }
//        });
//    }

//    private List<LocalMedia> list = new ArrayList<>();//图片选择返回

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case RequestCodeService.SGT_IMG_CHOOSE_DETAILS:
//                    // 图片选择结果回调
//                    list = PictureSelector.obtainMultipleResult(data);
//                    if (list.size() == 1) {
//                        String imgPath = list.get(0).getCompressPath();
//                        Intent intent = new Intent(this, SGTImgUploadActivity.class);
//                        intent.putExtra("img_path", imgPath);
//                        intent.putExtra("code", 1);//1：图片选择  2：拍摄图片
//                        intent.putExtra("uplodType", 1);//1：单个足环  2：鸽主
//                        intent.putExtra("SGTHomeListEntity", getIntent().getSerializableExtra("SGTHomeListEntity"));
//                        intent.putExtra("DataBean", dataBean);
//                        startActivity(intent);
//                    }
//                    break;
//            }
//        }
//    }
//}

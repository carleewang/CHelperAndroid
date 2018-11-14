package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.camera.util.BitmapUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.GameGcPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.XhdtPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameDtView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameGcView;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.imgupload.AddImgView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DpSpUtil;
import com.cpigeon.cpigeonhelper.utils.NetStateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 协会  ---》赛事规程修改
 * Created by Administrator on 2017/12/12.
 */

public class ReviseGameGcActivity extends ToolbarBaseActivity implements GameGcView, GameDtView {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_centent)
    EditText etCentent;
    //    @BindView(R.id.btn_switch)
//    Switch mSwitch;
    @BindView(R.id.tb_pl)
    ToggleButton tb_pl;
    @BindView(R.id.img_ll)
    LinearLayout imgLl;
    @BindView(R.id.imgbtn_add)
    ImageButton imgbtnAdd;
    @BindView(R.id.tv_pl_hint)
    TextView plHint;//评论提示信息
    @BindView(R.id.et_dtly)
    EditText etDtly;//动态来源
    @BindView(R.id.dtly_ll)
    LinearLayout dtlyLl;//动态来源布局

    private int switchTag = 0;
    private GameGcPrensenter mGameGcPrensenter;//赛事规程控制层
    private GcItemEntity mGcItemEntity;//赛事规程

    private XhdtPrensenter mXhdtPrensenter;//协会动态控制层
    private DtItemEntity mDtItemEntity;

    private SweetAlertDialog mSweetAlertDialog;//上传过程提示框

    private boolean uploadNetworkTag = false;//上传网络状态保存
    private SweetAlertDialog hintDialog;//网络状态提示框

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_game_gc_add;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (GameGcActivity.type.equals("ssgc")) {
            //赛事规程
            initSsgc();
        } else if (GameGcActivity.type.equals("xhdt")) {
            //协会动态
            initxhdt();
        }
        setTopLeftButton(R.drawable.ic_back, ReviseGameGcActivity.this::finish);

        //提示框
        mSweetAlertDialog = new SweetAlertDialog(ReviseGameGcActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        setTopRightButton("保存", () -> {
            try {
                if (GameGcActivity.type.equals("ssgc") && mGcItemEntity != null) {
                    //赛事规程
                    if (imgLl.getChildCount() > 0) {
                        if (!uploadNetworkTag) {
                            //上传时网络判断
                            if (NetStateUtils.getAPNType(ReviseGameGcActivity.this) != 1) {
                                hintDialog = CommonUitls.showSweetDialog(ReviseGameGcActivity.this, "您正在使用手机流量进行上传", "开始上传", "停止上传", dia -> {
                                    uploadNetworkTag = true;//确定为流量上传
                                    hintDialog.dismiss();//当前提示框关闭
                                    mGameGcPrensenter.editGuiCheng(etTitle, etCentent, switchTag, mGcItemEntity.getGcid(), imgLl, mSweetAlertDialog);
                                });
                                return;
                            }
                        }
                    }
                    mGameGcPrensenter.editGuiCheng(etTitle, etCentent, switchTag, mGcItemEntity.getGcid(), imgLl, mSweetAlertDialog);
                } else if (GameGcActivity.type.equals("xhdt") && mDtItemEntity != null) {
                    //协会动态
                    if (imgLl.getChildCount() > 0) {
                        if (!uploadNetworkTag) {
                            //上传时网络判断
                            if (NetStateUtils.getAPNType(ReviseGameGcActivity.this) != 1) {
                                hintDialog = CommonUitls.showSweetDialog(ReviseGameGcActivity.this, "您正在使用手机流量进行上传", "开始上传", "停止上传", dia -> {
                                    uploadNetworkTag = true;//确定为流量上传
                                    hintDialog.dismiss();//当前提示框关闭
                                    mXhdtPrensenter.editXhdt(etTitle, etCentent, etDtly, switchTag, mDtItemEntity.getDtid(), imgLl, mSweetAlertDialog);
                                });
                                return;
                            }
                        }
                    }


                    mXhdtPrensenter.editXhdt(etTitle, etCentent, etDtly, switchTag, mDtItemEntity.getDtid(), imgLl, mSweetAlertDialog);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 添加监听
        tb_pl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchTag = 1;//开启评论
                } else {
                    switchTag = 0;//关闭评论
                }
            }
        });
    }

    /**
     * 赛事规程数据初始化
     */
    private void initSsgc() {
        setTitle("赛事规程修改");
        plHint.setText(R.string.tv_ssgc_hint);
        etTitle.setHint(R.string.et_hint_title_ssgc);
        etCentent.setHint(R.string.et_hint_con_ssgc);
        mGcItemEntity = (GcItemEntity) getIntent().getSerializableExtra("GcItemEntity");
        if (mGcItemEntity != null) {
            etTitle.setText(mGcItemEntity.getTitle()); //设置赛事标题
            etCentent.setText(mGcItemEntity.getContent());//设置赛事内容
            switchTag = mGcItemEntity.getPl();

            AddImgView view;
            if (mGcItemEntity.getTplist() != null) {
                for (int x = 0; x < mGcItemEntity.getTplist().size(); x++) {
                    if (DetailsGameGcActivity.getImgList().size() == 0 && DetailsGameGcActivity.getImgList().size() != mGcItemEntity.getTplist().size()) {
                        break;
                    }
                    view = new AddImgView(this);

//                view.setEmptyImage(list.get(x).getCompressPath());
//                view.setEmptyImageUrl(mGcItemEntity.getTplist().get(x).getImgurl());
                    view.setAddImage(DetailsGameGcActivity.getImgList().get(x));
                    view.setIndex(x);
                    view.mGetClickIndex = new AddImgView.GetClickIndex() {
                        @Override
                        public void getViewClickIndex(int index) {
//                            Log.d(TAG, "onClick: 点击了-->" + index);
                            imgLl.removeView(imgLl.getChildAt(index));
                            for (int i = 0; i < imgLl.getChildCount(); i++) {
                                AddImgView newView = (AddImgView) imgLl.getChildAt(i);
                                newView.setIndex(i);
                            }
                        }
                    };
//                    imgLl.addView(view,new LinearLayout.LayoutParams(R.dimen.add_img_hint_clo,R.dimen.add_img_hint_clo));
                    imgLl.addView(view, DpSpUtil.dip2px(this, 80), DpSpUtil.dip2px(this, 80));

                }
            }

            if (mGcItemEntity.getPl() == 1) {
                tb_pl.setChecked(true);
            } else {
                tb_pl.setChecked(false);
            }
        }

        mGameGcPrensenter = new GameGcPrensenter(this);
    }

    /**
     * 初始化协会动态
     */
    private void initxhdt() {
        setTitle("协会动态修改");
        etTitle.setHint(R.string.et_hint_title_xhdt);
        etCentent.setHint(R.string.et_hint_con_xhdt);
        dtlyLl.setVisibility(View.VISIBLE);
        plHint.setText(R.string.tv_xhdt_hint);
        mDtItemEntity = (DtItemEntity) getIntent().getSerializableExtra("DtItemEntity");
        if (mDtItemEntity != null) {
            etTitle.setText(mDtItemEntity.getTitle()); //设置协会标题
            etCentent.setText(mDtItemEntity.getContent());//设置协会内容内容
            switchTag = mDtItemEntity.getPl();//评论
            etDtly.setText(mDtItemEntity.getSource());//来源
            AddImgView view;
            if (mDtItemEntity.getTplist() != null) {
                for (int x = 0; x < mDtItemEntity.getTplist().size(); x++) {
                    if (DetailsGameGcActivity.getImgList().size() == 0 && DetailsGameGcActivity.getImgList().size() != mDtItemEntity.getTplist().size()) {
                        break;
                    }
                    view = new AddImgView(this);
                    view.setAddImage(DetailsGameGcActivity.getImgList().get(x));
                    view.setIndex(x);
                    view.mGetClickIndex = new AddImgView.GetClickIndex() {
                        @Override
                        public void getViewClickIndex(int index) {
//                            Log.d(TAG, "onClick: 点击了-->" + index);
                            imgLl.removeView(imgLl.getChildAt(index));
                            for (int i = 0; i < imgLl.getChildCount(); i++) {
                                AddImgView newView = (AddImgView) imgLl.getChildAt(i);
                                newView.setIndex(i);
                            }
                        }
                    };
//                    imgLl.addView(view,new LinearLayout.LayoutParams(R.dimen.add_img_hint_clo,R.dimen.add_img_hint_clo));
                    imgLl.addView(view, DpSpUtil.dip2px(this, 80), DpSpUtil.dip2px(this, 80));
                }
            }
            if (mDtItemEntity.getPl() == 1) {
                tb_pl.setChecked(true);
            } else {
                tb_pl.setChecked(false);
            }
        }
        mXhdtPrensenter = new XhdtPrensenter(this);
    }

    @OnClick({R.id.tb_pl, R.id.imgbtn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_pl:
                break;
            case R.id.imgbtn_add:
                if (imgLl.getChildCount() == 6) {
                    CommonUitls.showSweetAlertDialog(ReviseGameGcActivity.this, "温馨提示", "图片最多为6张");
                    return;
                }
                new SaActionSheetDialog(ReviseGameGcActivity.this)
                        .builder()
                        .addSheetItem("相册选取", OnSheetItemClickListener)
                        .addSheetItem("拍一张", OnSheetItemClickListener)
                        .show();
                break;
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

    @Override
    public void getGCList(List<GcListEntity> datas, String msg) {

    }

    /**
     * 修改赛事规程回调  (修改协会动态)
     *
     * @param listApiResponse
     * @param msg
     */
    @Override
    public void addResults(ApiResponse<Object> listApiResponse, String msg) {

        try {
            if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {
                mSweetAlertDialog.dismiss();
            }
            Log.d(TAG, "addResults: cod" + listApiResponse.getErrorCode() + "  msg" + msg);
            if (listApiResponse.getErrorCode() == 0) {
                //发布事件（通知比赛列表刷新数据）
                EventBus.getDefault().post("xhgcListRefresh");

                CommonUitls.showSweetDialog1(this, listApiResponse.getMsg(), dialog -> {
                    dialog.dismiss();
                    AppManager.getAppManager().killActivity(mWeakReference);
                });

            } else {
                CommonUitls.showSweetAlertDialog(this, "温馨提示", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delResults_gc(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void delResults_dt(ApiResponse<Object> listApiResponse, String msg) {

    }


    @Override
    public void getDtItmeInfo(ApiResponse<DtItemEntity> listApiResponse, String msg) {

    }

    @Override
    public void getItmeInfo(ApiResponse<GcItemEntity> listApiResponse, String msg) {

    }

    //选择相片
    private List<LocalMedia> list = new ArrayList<>();
    private int chooseMode = PictureMimeType.ofImage();//设置选择的模式
    private int maxSelectNum = 6;// 最大图片选择数量
    private int compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式

    private File compressimg = new File("");//图片路径
    private final int albumRequestCode = 0x0029;//相册,相册返回码
    /**
     * 弹出选择相册还是照相机
     */
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //跳转到相册
                    maxSelectNum = 6 - imgLl.getChildCount();
                    OrgInforPresenter.jumpAlbum(ReviseGameGcActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
                case 2:
                    //跳转到相机
                    OrgInforPresenter.jumpCamera(ReviseGameGcActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
            }
        }
    };

    private Handler mHandler = new Handler();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case albumRequestCode:
                    // 图片选择结果回调
                    list = PictureSelector.obtainMultipleResult(data);
                    compressimg = new File(list.get(0).getCompressPath());
                    if (imgLl.getChildCount() + list.size() > 6) {
                        CommonUitls.showSweetAlertDialog(this, "温馨提示", "图片最多为6张");
                        return;
                    }

                    //图片打水印
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int x = 0; x < list.size(); x++) {
                                String img_path = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() + "xh_img" + x + ".jpeg";

                                Bitmap bitmap = BitmapUtils.createBitmapCenter(BitmapFactory.decodeFile(list.get(x).getCompressPath()), BitmapFactory.decodeResource(getResources(), R.mipmap.wartermark));

                                BitmapUtils.saveJPGE_After(bitmap, img_path, 80);

                                int finalX = x;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AddImgView view;
                                        view = new AddImgView(ReviseGameGcActivity.this);
                                        view.setAddImage(img_path);
                                        view.setIndex(finalX);
                                        view.mGetClickIndex = new AddImgView.GetClickIndex() {
                                            @Override
                                            public void getViewClickIndex(int index) {
                                                imgLl.removeView(imgLl.getChildAt(index));
                                                for (int i = 0; i < imgLl.getChildCount(); i++) {
                                                    AddImgView newView = (AddImgView) imgLl.getChildAt(i);
                                                    newView.setIndex(i);
                                                }
                                            }
                                        };
                                        imgLl.addView(view, DpSpUtil.dip2px(ReviseGameGcActivity.this, 80), DpSpUtil.dip2px(ReviseGameGcActivity.this, 80));
                                    }
                                });
                            }
                        }
                    });
                    break;
            }
        }
    }

    //===================================协会动态=============================================
    @Override
    public void getDtList(List<DtListEntity> datas, String msg) {

    }
}

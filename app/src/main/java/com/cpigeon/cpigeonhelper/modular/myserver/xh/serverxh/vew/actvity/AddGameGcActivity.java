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

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 协会  ---》赛事规程添加（协会动态添加）
 * Created by Administrator on 2017/12/12.
 */

public class AddGameGcActivity extends ToolbarBaseActivity implements GameGcView, GameDtView {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_centent)
    EditText etCentent;
    @BindView(R.id.tb_pl)
    ToggleButton tb_pl;
    //    @BindView(R.id.btn_switch)
//    Switch mSwitch;
    //    @BindView(R.id.gridview)
//    GridView mGridView;
    @BindView(R.id.img_ll)
    LinearLayout imgLl;
    @BindView(R.id.imgbtn_add)
    ImageButton addImg;
    @BindView(R.id.dtly_ll)
    LinearLayout dtlyLl;//动态来源编辑
    @BindView(R.id.et_dtly)
    EditText etDtly;//动态来源
    @BindView(R.id.tv_pl_hint)
    TextView plHint;//评论提示

    private int switchTag = 1;

    private GameGcPrensenter mGameGcPrensenter;//赛事规程控制层
    private XhdtPrensenter mXhdtPrensenter;//协会动态控制层

    private SweetAlertDialog mSweetAlertDialog;//上传过程提示框

    private boolean uploadNetworkTag = false;//上传网络状态保存
    private SweetAlertDialog hintDialog;//网络状态提示框
//    private List<String> imgList;

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
        //提示框
        mSweetAlertDialog = new SweetAlertDialog(AddGameGcActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        if (GameGcActivity.type.equals("ssgc")) {
            //赛事规程
            setTitle("赛事规程添加");
            etTitle.setHint(R.string.et_hint_title_ssgc);
            etCentent.setHint(R.string.et_hint_con_ssgc);
            plHint.setText(R.string.tv_ssgc_hint);
            mGameGcPrensenter = new GameGcPrensenter(this);//初始化赛事规程控制层
        } else if (GameGcActivity.type.equals("xhdt")) {
            //协会动态
            setTitle("协会动态添加");
            etTitle.setHint(R.string.et_hint_title_xhdt);
            etCentent.setHint(R.string.et_hint_con_xhdt);
            dtlyLl.setVisibility(View.VISIBLE);
            plHint.setText(R.string.tv_xhdt_hint);
            mXhdtPrensenter = new XhdtPrensenter(this);
        }

        setTopLeftButton(R.drawable.ic_back, AddGameGcActivity.this::finish);
        setTopRightButton("保存", () -> {
            try {
                if (GameGcActivity.type.equals("ssgc")) {
                    //赛事规程
                    //上传图片
                    if (imgLl.getChildCount() > 0) {
                        if (!uploadNetworkTag) {
                            //上传时网络判断
                            if (NetStateUtils.getAPNType(AddGameGcActivity.this) != 1) {
                                hintDialog = CommonUitls.showSweetDialog(AddGameGcActivity.this, "您正在使用手机流量进行上传", "开始上传", "停止上传", dia -> {
                                    uploadNetworkTag = true;//确定为流量上传
                                    hintDialog.dismiss();//当前提示框关闭
                                    mGameGcPrensenter.addGuiCheng(etTitle, etCentent, switchTag, imgLl, mSweetAlertDialog);//添加赛事规程
                                });
                                return;
                            }
                        }
                    }
                    mGameGcPrensenter.addGuiCheng(etTitle, etCentent, switchTag, imgLl, mSweetAlertDialog);//添加赛事规程
                } else if (GameGcActivity.type.equals("xhdt")) {
                    //协会动态
                    if (imgLl.getChildCount() > 0) {
                        if (!uploadNetworkTag) {
                            //上传时网络判断
                            if (NetStateUtils.getAPNType(AddGameGcActivity.this) != 1) {
                                hintDialog = CommonUitls.showSweetDialog(AddGameGcActivity.this, "您正在使用手机流量进行上传", "开始上传", "停止上传", dia -> {
                                    uploadNetworkTag = true;//确定为流量上传
                                    hintDialog.dismiss();//当前提示框关闭
                                    mXhdtPrensenter.addXhdt(etTitle, etCentent, etDtly, switchTag, imgLl, mSweetAlertDialog);//添加赛事规程
                                });
                                return;
                            }
                        }
                    }
                    mXhdtPrensenter.addXhdt(etTitle, etCentent, etDtly, switchTag, imgLl, mSweetAlertDialog);//添加赛事规程
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 添加监听
        tb_pl.setChecked(true);
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

    @OnClick({R.id.tb_pl, R.id.imgbtn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_pl:
                break;
            case R.id.imgbtn_add://添加图片

                if (imgLl.getChildCount() == 6) {
                    CommonUitls.showSweetAlertDialog(AddGameGcActivity.this, "温馨提示", "图片最多为6张");
                    return;
                }
                new SaActionSheetDialog(AddGameGcActivity.this)
                        .builder()
                        .addSheetItem("相册选取", OnSheetItemClickListener)
                        .addSheetItem("拍一张", OnSheetItemClickListener)
                        .show();

                break;
        }
    }

    /**
     * 添加赛事规程结果 （添加协会动态回调）
     *
     * @param msg
     */
    @Override
    public void addResults(ApiResponse<Object> listApiResponse, String msg) {

        try {
            //关闭提示框
            if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {
                mSweetAlertDialog.dismiss();
            }

            Log.d(TAG, "addResults: cod" + listApiResponse.getErrorCode() + "  msg" + msg);
            if (listApiResponse.getErrorCode() == 0) {

                //发布事件（通知比赛列表刷新数据）
                EventBus.getDefault().post("xhgcListRefresh");

                CommonUitls.showSweetDialog1(this, listApiResponse.getMsg(), dialog -> {
                    dialog.dismiss();
                    //关闭当前页面
                    AppManager.getAppManager().killActivity(mWeakReference);
                });

            } else {
                CommonUitls.showSweetDialog(this, msg);
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
    public void getItmeInfo(ApiResponse<GcItemEntity> listApiResponse, String msg) {

    }


    @Override
    public void getGCList(List<GcListEntity> datas, String msg) {

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
//            Logger.e(which + "");
            switch (which) {
                case 1:
                    //跳转到相册
                    maxSelectNum = 6 - imgLl.getChildCount();
                    OrgInforPresenter.jumpAlbum(AddGameGcActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
                case 2:
                    //跳转到相机
                    OrgInforPresenter.jumpCamera(AddGameGcActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
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
                                            AddImgView view;  view = new AddImgView(AddGameGcActivity.this);
                                            view.setAddImage(img_path);
    //                                        Log.d("xiaohl", "run: "+img_path);
                                            view.setIndex(finalX);
                                            view.mGetClickIndex = new AddImgView.GetClickIndex() {
                                                @Override
                                                public void getViewClickIndex(int index) {
    //                                Log.d(TAG, "onClick: 点击了-->" + index);
                                                    imgLl.removeView(imgLl.getChildAt(index));
                                                    for (int i = 0; i < imgLl.getChildCount(); i++) {
                                                        AddImgView newView = (AddImgView) imgLl.getChildAt(i);
                                                        newView.setIndex(i);
                                                    }
                                                }
                                            };
    //                        imgLl.addView(view,new LinearLayout.LayoutParams(R.dimen.add_img_hint_clo,R.dimen.add_img_hint_clo));
                                            imgLl.addView(view, DpSpUtil.dip2px(AddGameGcActivity.this, 80), DpSpUtil.dip2px(AddGameGcActivity.this, 80));
                                        }
                                    });
                                }
                            }
                        });

                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler();

    //==================================================================================
    @Override
    public void getDtList(List<DtListEntity> datas, String msg) {

    }

    @Override
    public void getDtItmeInfo(ApiResponse<DtItemEntity> listApiResponse, String msg) {

    }


}

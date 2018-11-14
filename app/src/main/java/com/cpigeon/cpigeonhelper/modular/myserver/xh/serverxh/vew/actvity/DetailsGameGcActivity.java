package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
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
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DpSpUtil;
import com.cpigeon.cpigeonhelper.utils.SDFileHelper;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.video.utils.DensityUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 协会  ---》赛事规程详情(协会动态详情)
 * Created by Administrator on 2017/12/12.
 */

public class DetailsGameGcActivity extends ToolbarBaseActivity implements GameGcView, GameDtView {

    @BindView(R.id.et_title)
    TextView etTitle;
    @BindView(R.id.et_centent)
    WebView etCentent;
    @BindView(R.id.tv_switch)
    TextView tvSwitch;

    @BindView(R.id.img_ll)
    LinearLayout imgLl;
    @BindView(R.id.pl_ll)
    LinearLayout plLl;

    @BindView(R.id.dtly_ll)
    LinearLayout dtlyLl;
    @BindView(R.id.tv_dtly)
    TextView tvDtly;
    private int imgSpacing = 16;

    private WebSettings mWebSettings;

    private GameGcPrensenter mGameGcPrensenter;//赛事规程控制层
    private GcItemEntity mGcItemEntity;

    private XhdtPrensenter mXhdtPrensenter;//协会动态

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_game_gc_details;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mWebSettings = etCentent.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setTextSize(WebSettings.TextSize.NORMAL);


        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        if (GameGcActivity.type.equals("ssgc")) {
            //赛事规程
            setTitle("赛事规程详情");
            mGameGcPrensenter = new GameGcPrensenter(this);
            mGameGcPrensenter.getGuiChengItemInfo(getIntent().getIntExtra("gcid", -1));
        } else if (GameGcActivity.type.equals("xhdt")) {
            //协会动态
            setTitle("协会动态详情");
            mXhdtPrensenter = new XhdtPrensenter(this);
            mXhdtPrensenter.getXhdtItemInfo(getIntent().getIntExtra("dtid", -1));
            dtlyLl.setVisibility(View.VISIBLE);
        }

        setTopLeftButton(R.drawable.ic_back, DetailsGameGcActivity.this::finish);

    }


    private void setTopRight() {
        setTopRightButton("修改", () -> {
            try {
                Log.d(TAG, "setTopRight: 0");
                if (GameGcActivity.type.equals("ssgc")) {
                    Log.d(TAG, "setTopRight: 1");
                    //赛事规程
                    if (mGcItemEntity != null) {

                        Log.d(TAG, "setTopRight: 2");
                        if (mGcItemEntity.getTplist() != null) {
                            if (fileList.size() != mGcItemEntity.getTplist().size()) {
                                Log.d(TAG, "setTopRight: 3");
                                return;
                            }
                        }

                        Log.d(TAG, "setTopRight: 4");
                        Intent intent = new Intent(this, ReviseGameGcActivity.class);
                        intent.putExtra("GcItemEntity", mGcItemEntity);
                        startActivity(intent);
                    }
                } else if (GameGcActivity.type.equals("xhdt")) {
                    //协会动态
                    if (mDtItemEntity != null) {
                        if (mDtItemEntity.getTplist() != null) {
                            if (fileList.size() != mDtItemEntity.getTplist().size()) {
                                return;
                            }
                        }
                        Intent intent = new Intent(this, ReviseGameGcActivity.class);
                        intent.putExtra("DtItemEntity", mDtItemEntity);
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String xhgcListRefresh) {
        try {
            if (xhgcListRefresh.equals("xhgcListRefresh")) {
                if (GameGcActivity.type.equals("ssgc")) {
                    //刷新赛事规程详情
                    mGameGcPrensenter.getGuiChengItemInfo(getIntent().getIntExtra("gcid", -1));
                } else if (GameGcActivity.type.equals("xhdt")) {
                    //刷新协会动态详情
                    mXhdtPrensenter.getXhdtItemInfo(getIntent().getIntExtra("dtid", -1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getImgList() {
        return fileList;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            fileList.clear();
            imgLl.removeAllViews();//移除所有的view
            EventBus.getDefault().unregister(this);//取消注册
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static List<String> fileList = new ArrayList<>();

    //========================================赛事规程====================================

    /**
     * 获取详情数据成功
     *
     * @param listApiResponse
     * @param msg
     */
    @Override
    public void getItmeInfo(ApiResponse<GcItemEntity> listApiResponse, String msg) {

        try {
            switch (listApiResponse.getErrorCode()) {
                case 0:
                    imgLl.removeAllViews();//移除所有的view
                    fileList.clear();
                    mGcItemEntity = listApiResponse.getData();
                    etTitle.setText(listApiResponse.getData().getTitle());

                    etCentent.loadDataWithBaseURL(null, listApiResponse.getData().getContent(), "text/html", "utf-8", null);

                    //                    etCentent.setText(listApiResponse.getData().getContent());

                    Log.d(TAG, "数据返回: " + listApiResponse.getData().getPl());
                    if (listApiResponse.getData().getPl() == 1) {
                        plLl.setVisibility(View.VISIBLE);
                        tvSwitch.setText("已打开");
                    } else {
                        plLl.setVisibility(View.GONE);
                        //                        tvSwitch.setText("已关闭");
                    }


                    if (mGcItemEntity.isupdate()) {
                        setTopRight();
                    }


                    try {
                        if (listApiResponse.getData() == null || listApiResponse.getData().getTplist() == null || listApiResponse.getData().getTplist().size() == 0) {
                            return;
                        }

                        if (listApiResponse.getData().getTplist() != null) {
                            Log.d(TAG, "getItmeInfo: 图片数量-->" + listApiResponse.getData().getTplist().size());
                        }

                        //下载图片
                        SDFileHelper sdFileHelper = new SDFileHelper(this);
                        sdFileHelper.getDownImg = new SDFileHelper.GetDownImg() {
                            @Override
                            public void getDownImgFilePath(String filePath) {

                                setImgLl(filePath);
                            }
                        };

                        for (int i = 0; i < listApiResponse.getData().getTplist().size(); i++) {
                            Log.d(TAG, "图片url: " + listApiResponse.getData().getTplist().get(i).getImgurl());
                            sdFileHelper.savePicture("img_" + i, listApiResponse.getData().getTplist().get(i).getImgurl());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    CommonUitls.showSweetAlertDialog(this, "温馨提示", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //========================================协会信息====================================
    @Override
    public void getDtList(List<DtListEntity> datas, String msg) {

    }

    @Override
    public void addResults(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void delResults_gc(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void delResults_dt(ApiResponse<Object> listApiResponse, String msg) {

    }


    /**
     * 动态详情
     *
     * @param listApiResponse
     * @param msg
     */
    private DtItemEntity mDtItemEntity;

    @Override
    public void getDtItmeInfo(ApiResponse<DtItemEntity> listApiResponse, String msg) {
        try {
            switch (listApiResponse.getErrorCode()) {
                case 0:
                    if (!this.isDestroyed()) {
                        imgLl.removeAllViews();//移除所有的view
                        fileList.clear();
                        mDtItemEntity = listApiResponse.getData();
                        etTitle.setText(listApiResponse.getData().getTitle());

                        etCentent.loadDataWithBaseURL(null, listApiResponse.getData().getContent(), "text/html", "utf-8", null);
                        tvDtly.setText(listApiResponse.getData().getSource());//动态来源
                        if (listApiResponse.getData().getPl() == 1) {
                            plLl.setVisibility(View.VISIBLE);
                            tvSwitch.setText("已打开");
                        } else {
                            plLl.setVisibility(View.GONE);
                            //                        tvSwitch.setText("已关闭");
                        }


                        if (mDtItemEntity.isupdate()) {
                            setTopRight();
                        }

                        if (listApiResponse.getData() == null || listApiResponse.getData().getTplist() == null || listApiResponse.getData().getTplist().size() == 0) {
                            return;
                        }

                        //下载图片
                        SDFileHelper sdFileHelper = new SDFileHelper(this);
                        sdFileHelper.getDownImg = new SDFileHelper.GetDownImg() {
                            @Override
                            public void getDownImgFilePath(String filePath) {
                                setImgLl(filePath);
                            }
                        };
                        for (int i = 0; i < listApiResponse.getData().getTplist().size(); i++) {
                            sdFileHelper.savePicture("img_" + i, listApiResponse.getData().getTplist().get(i).getImgurl());
                        }

                    }
                    break;
                default:
                    CommonUitls.showSweetAlertDialog(this, "温馨提示", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 图片下载结果回调设置
     *
     * @param filePath
     */
    private void setImgLl(String filePath) {
        try {
            ImageView imageView = new ImageView(DetailsGameGcActivity.this);

            int w = DensityUtils.getScreenWidth(DetailsGameGcActivity.this);//屏幕的宽度

            imageView.setLayoutParams(new LinearLayout.LayoutParams(w, w));
            imageView.setPadding(DpSpUtil.dip2px(DetailsGameGcActivity.this, imgSpacing),
                    DpSpUtil.dip2px(DetailsGameGcActivity.this, imgSpacing),
                    DpSpUtil.dip2px(DetailsGameGcActivity.this, imgSpacing),
                    0);

            imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));

            imgLl.addView(imageView);
//        Log.d(TAG, "下载图片路径: " + filePath);
            fileList.add(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

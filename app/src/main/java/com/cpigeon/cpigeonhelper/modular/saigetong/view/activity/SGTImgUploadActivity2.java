package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.camera.util.BitmapUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.LocationEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.NetStateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.RequestCodeService;
import com.cpigeon.cpigeonhelper.video.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cpigeon.cpigeonhelper.utils.CommonUitls.showLoadSweetAlertDialog;
import static com.cpigeon.cpigeonhelper.utils.CommonUitls.showSweetDialog;

/**
 * 赛鸽通图片上传
 * Created by Administrator on 2017/12/6.
 */

public class SGTImgUploadActivity2 extends ToolbarBaseActivity {


    @BindView(R.id.upload_img)
    ImageView uploadImg;
    @BindView(R.id.upload_ll_tag)
    RelativeLayout uploadLlTag;
    @BindView(R.id.imtbtn_determine)
    Button imtbtnDetermine;

    @BindView(R.id.tv_check)
    TextView tvCheck;//查看已选足环号码

    @BindView(R.id.watermark_tv1)
    TextView watermarkTv1;
    @BindView(R.id.watermark_tv2)
    TextView watermarkTv2;
    @BindView(R.id.watermark_tv3)
    TextView watermarkTv3;
    @BindView(R.id.watermark_tv4)
    TextView watermarkTv4;
    @BindView(R.id.watermark_tv5)
    TextView watermarkTv5;
    @BindView(R.id.watermark_tv6)
    TextView watermarkTv6;
    @BindView(R.id.watermark_llz)
    RelativeLayout watermarkLlz;

//    @BindView(R.id.watermark_z_bg)
//    ImageView watermark_z_bg;//水印背景

    @BindView(R.id.watermark_center_img)
    ImageView watermarkCenterImg;

    @BindView(R.id.watermark_tv_tag)
    TextView watermarkTvTag;

    @BindView(R.id.watermark_rl_tag)
    RelativeLayout watermark_rl_tag;

//    private String img_path;//图片地址

    private SGTPresenter mSGTPresenter;

    private SweetAlertDialog mSweetAlertDialog;//上传提示框
    private SweetAlertDialog hintDialog;//网络状态提示框

    private List<FootSSEntity> listdetail = new ArrayList<FootSSEntity>();

    private String tagStr;
    private String img_path;
    private int tagid = -1;


    public static int uplodResultsTag = -1;// 1  支付成功   -1 未成功

    /**
     * 绘制水印
     */
    private void drawWatermark() {

    }

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.layout_sgt_img_upload2;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        uplodResultsTag = -1;
    }

    private String xcImgPath;//相册选择的图片地址

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTopLeftButton(R.drawable.ic_back, this::finish);

        xcImgPath = getIntent().getStringExtra("img_path");//相册选择图片地址
        tagStr = getIntent().getStringExtra("tagStr");//标签名字
        tagid = getIntent().getIntExtra("tagid", -1);//标签id
        listdetail = getIntent().getParcelableArrayListExtra("listdetail");

        mSGTPresenter = new SGTPresenter(new SGTViewImpl() {

            @Override
            public void uploadResults(ApiResponse<Object> dateApiResponse, String msg) {
                try {
                    switch (dateApiResponse.getErrorCode()) {
                        case 0:
                            uploadSucceed();//上传图片成功
                            break;
                        case 20014:
                            mSweetAlertDialog.dismiss();
                            CommonUitls.showSweetDialog(SGTImgUploadActivity2.this, msg, dialog -> {
                                dialog.dismiss();
                            });
                            break;
                        default:
                            uploadFail(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        startDrawWater();

        determineFootNum1(listdetail);//获取足环号码
    }

    private String strSelectFoot;//上传足环id
    private String strSelectFoot1;//查看足环号码

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && resultCode == RequestCodeService.SGT_SELECT_FOOT) {
            strSelectFoot = data.getStringExtra("strSelectFoot");
            strSelectFoot1 = data.getStringExtra("strSelectFoot1");
        }
    }

    @OnClick({R.id.imtbtn_determine, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imtbtn_determine:

                int APNType = NetStateUtils.getAPNType(SGTImgUploadActivity2.this);
                if (APNType == 0) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "当前暂无网络", SGTImgUploadActivity2.this);
                    return;
                }

                SweetAlertDialogUtil.showSweetDialog(this, "足环号码确认无误", dialog -> {
                    dialog.dismiss();
                    //上传时网络判断
                    if (NetStateUtils.getAPNType(this) != 1) {
                        hintDialog = showSweetDialog(this, "您正在使用手机流量进行上传", "开始上传", "停止上传", dia -> {
                            hintDialog.dismiss();//当前提示框关闭
                            startUpload(); //开始上传
                        });
                        return;
                    }

                    startUpload(); //开始上传
                });

                break;

            case R.id.tv_check://鸽主查看已选足环号码

                //鸽主查看已选足环号码
                if (strSelectFoot1 == null || strSelectFoot1.isEmpty()) {
                    CommonUitls.showSweetDialog(this, "请选择足环号码");
                    return;
                }

                Intent intent = new Intent(this, CheckSelectActivity.class);
                intent.putExtra("strSelectFoot1", strSelectFoot1);
                startActivity(intent);

                break;
        }
    }

    //开始上传
    private void startUpload() {
        //上传提示框
        mSweetAlertDialog = new SweetAlertDialog(SGTImgUploadActivity2.this, SweetAlertDialog.PROGRESS_TYPE);
        //上传图片
        mSweetAlertDialog = showLoadSweetAlertDialog(mSweetAlertDialog);

        if (listdetail.size() == 1) {
            mSGTPresenter.uploadFootImg(String.valueOf(listdetail.get(0).getId()),
                    tagid,
                    new LocationEntity("", "", "", "", ""),
                    img_path,
                    1
                    , getIntent().getStringExtra("reason"));

        } else {
            mSGTPresenter.uploadGZFootImg(strSelectFoot,
                    tagid,
                    new LocationEntity("", "", "", "", ""),
                    img_path,
                    strSelectFoot);
        }

    }

    //上传图片成功
    public void uploadSucceed() {
        //隐藏提示框
        if (mSweetAlertDialog.isShowing()) mSweetAlertDialog.dismiss();
        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "上传图片成功", this, dialog -> {
            dialog.dismiss();
            uplodResultsTag = 1;
            AppManager.getAppManager().killActivity(mWeakReference);//关闭当页面
        });//弹出提示
    }

    /**
     * 上传图片失败
     */
    public void uploadFail(String msg) {
        if (mSweetAlertDialog.isShowing()) mSweetAlertDialog.dismiss();
        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, this);
    }

    /**
     * 开始绘制水印
     */
    private void startDrawWater() {
        try {
            if (this.isDestroyed()) return;
            if (xcImgPath == null) return;

            //标签
            if (tagStr != null) {
                watermarkTvTag.setText(tagStr);
            }

            //左下角水印
            if (listdetail != null && listdetail.size() > 0) {
                if (listdetail.get(0).getTel() != null) {
                    watermarkTv1.setText(listdetail.get(0).getGpmc() + "(" + listdetail.get(0).getTel() + ")");//
                } else {
                    watermarkTv1.setText(listdetail.get(0).getGpmc());
                }
                watermarkTv2.setText(DateUtils.getStringDate());//

                if (listdetail.get(0).getAddress() == null || listdetail.get(0).getAddress().length() == 0) {
                    watermarkTv3.setVisibility(View.GONE);
                } else {
                    watermarkTv3.setText(listdetail.get(0).getAddress());//
                }

            }


            for (FootSSEntity data : listdetail) {
                //            Log.d("shangchuan", "startDrawWater: foot-->" + data.getFoot() + "   color-->" + data.getColor() + "     tel-->" + data.getTel() + "     add-->" + data.getAddress() + "      gpmc-->" + data.getGpmc() + "     sex-->" + data.getSex());
                Log.d("shangchuan", data.toJsonString());
            }


            if (listdetail != null && listdetail.size() == 1) {
                watermarkTv4.setText(setFoot(listdetail.get(0)));
                watermarkTv5.setVisibility(View.GONE);
                watermarkTv6.setVisibility(View.GONE);
            }

            if (listdetail != null && listdetail.size() == 2) {
                watermarkTv4.setText(setFoot(listdetail.get(0)));
                watermarkTv5.setText(setFoot(listdetail.get(1)));
                watermarkTv6.setVisibility(View.GONE);
            }

            if (listdetail != null && listdetail.size() == 3) {
                watermarkTv4.setText(setFoot(listdetail.get(0)));
                watermarkTv5.setText(setFoot(listdetail.get(1)));
                watermarkTv6.setText(setFoot(listdetail.get(2)));
            }

            img_path = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() + "sggp_img" + ".jpeg";

            Bitmap bitmap1 = BitmapUtils.convertViewToBitmap(watermarkCenterImg);//中间
            Bitmap bitmap2 = convertViewToBitmap1(watermarkLlz, this);//左下角
            Bitmap bitmap3 = BitmapUtils.convertViewToBitmap(watermark_rl_tag);//左上角

            Bitmap bitmap4 = createBitmapCenter3(BitmapFactory.decodeFile(xcImgPath), bitmap1, bitmap2, bitmap3);
            BitmapUtils.saveJPGE_After(SGTImgUploadActivity2.this, bitmap4, img_path, 100);

            uploadImg.setImageBitmap(BitmapFactory.decodeFile(img_path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String setFoot(FootSSEntity mFootSSEntity) {

        String str = "";

        str += mFootSSEntity.getFoot();

        if (mFootSSEntity.getColor() != null && mFootSSEntity.getSex() == null) {

            if (mFootSSEntity.getColor().length() > 0) {
                str += "(" + mFootSSEntity.getColor() + ")";
            }
        }


        if (mFootSSEntity.getColor() == null && mFootSSEntity.getSex() != null) {
            if (mFootSSEntity.getSex().length() > 0) {
                str += "(" + mFootSSEntity.getSex() + ")";
            }
        }

        if (mFootSSEntity.getColor() != null && mFootSSEntity.getSex() != null) {

            if (mFootSSEntity.getColor().length() > 0 && mFootSSEntity.getSex().length() > 0) {
                str += "(" + mFootSSEntity.getColor() + "," + mFootSSEntity.getSex() + ")";
            }

            if (mFootSSEntity.getColor().length() > 0 && mFootSSEntity.getSex().length() == 0) {
                str += "(" + mFootSSEntity.getColor() + ")";
            }

            if (mFootSSEntity.getColor().length() == 0 && mFootSSEntity.getSex().length() > 0) {
                str += "(" + mFootSSEntity.getSex() + ")";
            }
        }

        return str;
    }


    /**
     * 图片合成 中心
     *
     * @param src
     * @param watermark1 中心
     * @param watermark2 左下
     * @param watermark3 左上
     * @return
     */
    public static Bitmap createBitmapCenter3(Bitmap src, Bitmap watermark1, Bitmap watermark2, Bitmap watermark3) {

        if (src == null) {
            return null;
        }

        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark1.getWidth();
        int wh = watermark1.getHeight();


        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);

        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src

        cv.drawBitmap(watermark1, w / 2 - ww / 2, h / 2 - wh / 2, null);//中心

        cv.drawBitmap(watermark2, 0, h - watermark2.getHeight() + 5, null);//左下

        cv.drawBitmap(watermark3, 15, 15, null);//左上

        cv.save(Canvas.CLIP_SAVE_FLAG);// 保存

        cv.restore();// 存储
        return newb;
    }

    /**
     * 将View转为Bitmap
     *
     * @param view
     * @return
     */
    private Bitmap convertViewToBitmap1(View view, Context mContext) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, DensityUtils.getScreenWidth(mContext), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    private void determineFootNum1(List<FootSSEntity> listdetail) {
        try {
            strSelectFoot = "";
            strSelectFoot1 = "";

            if (listdetail.size() == 0) {
                CommonUitls.showToast(this, "当前没有数据");
                return;
            }

            for (int i = 0; i < listdetail.size(); i++) {
                strSelectFoot += listdetail.get(i).getId() + ",";
                strSelectFoot1 += listdetail.get(i).getFoot() + "、";
            }

            if (strSelectFoot1 == null || strSelectFoot1.equals("")) {//选择的条数为空
                CommonUitls.showToast(this, "暂无足环号码");
                return;
            } else {
                strSelectFoot = strSelectFoot.substring(0, strSelectFoot.length() - 1);
                strSelectFoot1 = strSelectFoot1.substring(0, strSelectFoot1.length() - 1);
            }

            Log.d(TAG, "determineFootNum1: " + strSelectFoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

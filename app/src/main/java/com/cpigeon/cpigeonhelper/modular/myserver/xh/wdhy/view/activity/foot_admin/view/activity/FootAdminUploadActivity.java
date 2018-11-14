package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.CheckSelectActivity;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.NetStateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.cpigeon.cpigeonhelper.utils.CommonUitls.showLoadSweetAlertDialog;

/**
 * Created by Administrator on 2018/6/19.
 */

public class FootAdminUploadActivity extends ToolbarBaseActivity {
    @BindView(R.id.upload_img)
    ImageView uploadImg;

    @BindView(R.id.edit_video)
    JCVideoPlayerStandard editVideo;
    private String img_path;
    private String video_path;

    private FootAdminEntity data;
    private String type;

    private FootAdminPresenter mFootAdminPresenter;

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
        setTopLeftButton(R.drawable.ic_back, FootAdminUploadActivity.this::finish);
        setTitle("上传");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        type = getIntent().getStringExtra("type");

        data = (FootAdminEntity) getIntent().getSerializableExtra("data");

        img_path = getIntent().getStringExtra("img_path");

        video_path = getIntent().getStringExtra("video_path");

        if (type.equals("photo")) {

            uploadImg.setVisibility(View.VISIBLE);
            editVideo.setVisibility(View.GONE);

            uploadImg.setImageBitmap(BitmapFactory.decodeFile(img_path));

        } else if (type.equals("video")) {

            editVideo.setVisibility(View.VISIBLE);
            uploadImg.setVisibility(View.GONE);

            //返回有视频路径
            editVideo.backButton.setVisibility(View.GONE);
            editVideo.tinyBackImageView.setVisibility(View.GONE);
            editVideo.setUp(video_path, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST);
        }

        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {
            @Override
            public void getFootAdminResultsData(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                if (listApiResponse.getErrorCode() == 0) {

                    EventBus.getDefault().post(EventBusService.FOOT_ADMIN_UPLOAD);
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootAdminUploadActivity.this, dialog -> {
                        dialog.dismiss();
                        AppManager.getAppManager().killActivity(mWeakReference);
                    });//弹出提示

                } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootAdminUploadActivity.this, dialog -> {
                        dialog.dismiss();
                        //跳转到登录页
                        AppManager.getAppManager().startLogin(MyApplication.getContext());
                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                    });
                } else {
                    FootAdminUploadActivity.this.getErrorNews(msg);
                }
            }
        });
    }

    private boolean uploadNetworkTag = false;//上传网络状态保存

    @OnClick({R.id.tv_check, R.id.imtbtn_determine})
    public void onViewClicked(View view) {

        if (AntiShake.getInstance().check()) {
            return;
        }


        switch (view.getId()) {
            case R.id.tv_check:

                try {
                    Intent intent = new Intent(this, CheckSelectActivity.class);
                    intent.putExtra("strSelectFoot1", data.getFoot());
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(TAG, "onViewClicked: " + e.getLocalizedMessage());
                }

                break;
            case R.id.imtbtn_determine:

                int APNType = NetStateUtils.getAPNType(FootAdminUploadActivity.this);
                if (APNType == 0) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "当前暂无网络", FootAdminUploadActivity.this);
                    return;
                }

                if (!uploadNetworkTag) {
                    //上传时网络判断
                    if (NetStateUtils.getAPNType(FootAdminUploadActivity.this) != 1) {
                        errSweetAlertDialog = CommonUitls.showSweetDialog(FootAdminUploadActivity.this, "您正在使用手机流量进行上传", "开始上传", "停止上传", dia -> {
                            uploadNetworkTag = true;//确定为流量上传
                            uploadData();//上传文件
                        });
                        return;
                    }
                }
                uploadData();

                break;
        }
    }

    private void uploadData(){
        try {
            //上传图片
            mLoadDataDialog = showLoadSweetAlertDialog(mLoadDataDialog);
            if (type.equals("photo")) {
                mFootAdminPresenter.getXHHYGL_ZHGL_TBHYG(data.getId(), "image", img_path);
            } else if (type.equals("video")) {
                mFootAdminPresenter.getXHHYGL_ZHGL_TBHYG(data.getId(), "video", video_path);
            }
        } catch (Exception e) {
            if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
            Log.d(TAG, "onViewClicked: " + e.getLocalizedMessage());
        }
    }
}

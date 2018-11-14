package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.mina.ConnectionManager;
import com.cpigeon.cpigeonhelper.mina.SessionManager;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.OfflineFileEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorPresenter2;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.UploadImgVideoPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment.PigeonMonitorFragment;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.ImgVideoViewImpl;
import com.cpigeon.cpigeonhelper.service.DetailsService1;
import com.cpigeon.cpigeonhelper.service.DetailsService2;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.OfflineFileManager;
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.video.RecordedActivity3;
import com.luck.picture.lib.PictureSelector;
import com.umeng.socialize.UMShareAPI;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorPresenter2.REQUEST_IGNORE_BATTERY_CODE;
import static com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment.VideoFragment.smallVideoHelper;
import static com.cpigeon.cpigeonhelper.utils.PermissonUtil.cameraIsCanUse;


/**
 * 鸽车监控
 * Created by Administrator on 2017/10/10.
 */

public class PigeonMonitorActivity extends ToolbarBaseActivity {

    private PigeonMonitorFragment homeFragment;
    private final int AC_CODE = 0x00237;

    private UploadImgVideoPresenter mUploadImgVideoPresenter;//获取标签

    public static boolean isLoadFrist = true;

    @Override
    protected void swipeBack() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pigeon_monitor;
    }

    @Override
    protected void setStatusBar() {

        if (!MonitorPresenter2.isIgnoringBatteryOptimizations(this)) {
            MonitorPresenter2.gotoSettingIgnoringBatteryOptimizations(this);
        }

        //保持屏幕唤醒
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        mUploadImgVideoPresenter = new UploadImgVideoPresenter(new ImgVideoViewImpl() {

        });

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            mUploadImgVideoPresenter.getTag();
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        String string = "showViewTag";
        outState.putString("Activity", string);
    }

    public static int tags = -1;//-1  默认  1:  系统被kill掉

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tags = -1;
        isLoadFrist = true;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            tags = 1;
            Log.d("dingweitianqicx", "finishCreateView: 11");
        } else {
            tags = -1;
            Log.d("dingweitianqicx", "finishCreateView: 2");
        }

        //初始化toobal
        setTitle(MonitorData.getMonitorRaceName());
        setTopLeftButton(R.drawable.ic_back, new OnClickListener() {
            @Override
            public void onClick() {
                blak();//退出
            }
        });
        initFragments();
    }

    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        blak();//退出
    }

    /**
     * 退出
     */
    private SweetAlertDialog dialogZ;

    private void blak() {
        //StateCode【0：未开始监控的；1：正在监控中：2：监控结束 ;3:授权人查看比赛】【默认无筛选】
        if (MonitorData.getMonitorStateCode() == 1 && homeFragment != null && homeFragment.blakTag == true) {//正在监控状态
            dialogZ = SweetAlertDialogUtil.showDialog4(dialogZ, "正在监控，是否暂停后退出", PigeonMonitorActivity.this, dialog -> {
                dialog.dismiss();
                DetailsService1.aMapLocationTag = null;
                DetailsService1.locationTag = null;
                ConnectionManager.isConnect = true;
                SessionManager.isday = false;

                try {

                    DetailsService1.isStartLocate = false;
                    DetailsService1.intTag1 = false;

                    if (DetailsService1.mAMapLocationClient != null) {
                        DetailsService1.mAMapLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                        DetailsService1.mAMapLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                        DetailsService1.mAMapLocationClient = null;
                    }

//                getActivity().stopService(new Intent(getActivity(), CoreService.class));//hl  停止服务
                   this.stopService(new Intent(this, DetailsService1.class));//hl  停止服务
                    this.stopService(new Intent(this, DetailsService2.class));//hl  停止服务
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PigeonMonitorActivity.this.finish();//退出监控
            });
        } else {
//            if (CommonUitls.isServiceRunning(PigeonMonitorActivity.this, CoreService.class.getName())) {
            if (CommonUitls.isServiceRunning(PigeonMonitorActivity.this, DetailsService1.class.getName())) {
//                PigeonMonitorActivity.this.stopService(new Intent(PigeonMonitorActivity.this, CoreService.class));//hl  停止服务
                PigeonMonitorActivity.this.stopService(new Intent(PigeonMonitorActivity.this, DetailsService1.class));//hl  停止服务
            }
            PigeonMonitorActivity.this.finish();//退出监控
        }
    }

    private void initFragments() {
        homeFragment = PigeonMonitorFragment.newInstance();//当前页，总的Fragment

        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, homeFragment)
                .show(homeFragment).commit();
    }


    public boolean showUploadDialog(OfflineFileManager imgManager, OfflineFileManager videoManager) {

        boolean isHaveData = false;

        SaActionSheetDialog dialog = new SaActionSheetDialog(this);
        dialog.builder();

        OfflineFileEntity offlineIMGEntity = RealmUtils.getInstance().findOfflineFileEntity(
                String.valueOf(AssociationData.getUserId())
                , String.valueOf(MonitorData.getMonitorId())
                , OfflineFileManager.TYPE_IMG);

        OfflineFileEntity offlineVIDEOEntity = RealmUtils.getInstance().findOfflineFileEntity(
                String.valueOf(AssociationData.getUserId())
                , String.valueOf(MonitorData.getMonitorId())
                , OfflineFileManager.TYPE_VIDEO);


        if (offlineIMGEntity != null) {
            dialog.addSheetItem("继续上传图片", which -> {
                IntentBuilder.Builder(this, PhotoEditActivity.class)
                        .putExtra(IntentBuilder.KEY_DATA, offlineIMGEntity)
                        .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                        .startActivity();
            });
            isHaveData = true;
        }

        if (offlineVIDEOEntity != null) {
            dialog.addSheetItem("继续上传视频", which -> {
                IntentBuilder.Builder(this, VideoEditActivity.class)
                        .putExtra(IntentBuilder.KEY_DATA, offlineVIDEOEntity)
                        .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                        .startActivity();
            });
            isHaveData = true;
        }

        dialog.addSheetItem("结束比赛", which -> {
            RealmUtils.getInstance().deleteOfflineFileEntity(offlineIMGEntity);
            imgManager.deleteOfflineCache();
            RxUtils.delayed(200, aLong -> {
                RealmUtils.getInstance().deleteOfflineFileEntity(offlineVIDEOEntity);
                videoManager.deleteOfflineCache();
                if (onAllUploadDialogCancelListener != null) {
                    onAllUploadDialogCancelListener.click();
                }
            });
        });

        if (offlineIMGEntity != null || offlineVIDEOEntity != null) {
            dialog.show();
        }

        return isHaveData;
    }

    private OnAllUploadDialogCancelListener onAllUploadDialogCancelListener;

    public void setOnAllUploadDialogCancelListener(OnAllUploadDialogCancelListener onAllUploadDialogCancelListener) {
        this.onAllUploadDialogCancelListener = onAllUploadDialogCancelListener;
    }

    public interface OnAllUploadDialogCancelListener {
        void click();
    }

    /**
     * 继续上传的dialog
     */

    public void showUploadDialog(OfflineFileManager offlineFileManager) {

        SaActionSheetDialog dialog = new SaActionSheetDialog(this);
        dialog.builder();

        SaActionSheetDialog dialog2 = new SaActionSheetDialog(this);
        dialog2.builder();

        OfflineFileEntity offlineFileEntity = RealmUtils.getInstance().findOfflineFileEntity(
                String.valueOf(AssociationData.getUserId())
                , String.valueOf(MonitorData.getMonitorId())
                , offlineFileManager.getFileType());

        if (offlineFileManager.getFileType().equals(OfflineFileManager.TYPE_IMG)) {
            if (offlineFileEntity != null && offlineFileManager.getCache(true) != null) {
                dialog.addSheetItem("继续上传照片", which -> {
                    IntentBuilder.Builder(this, PhotoEditActivity.class)
                            .putExtra(IntentBuilder.KEY_DATA, offlineFileEntity)
                            .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                            .startActivity();
                });
                dialog.addSheetItem("重新上传照片", which -> {
                    if (cameraIsCanUse()) {
                        RealmUtils.getInstance().deleteOfflineFileEntity(offlineFileEntity);
                        offlineFileManager.deleteOfflineCache();
                        Intent intent = new Intent(this, RecordedActivity3.class);
                        intent.putExtra("type", "photo");
                        startActivity(intent);
                    }

                });
                dialog.show();
            } else {
                if (cameraIsCanUse()) {
                    Intent intent = new Intent(this, RecordedActivity3.class);
                    intent.putExtra("type", "photo");
                    startActivity(intent);
                }
            }
        } else if (offlineFileManager.getFileType().equals(OfflineFileManager.TYPE_VIDEO)) {
            if (offlineFileEntity != null && offlineFileManager.getCache(true) != null) {
                dialog.addSheetItem("继续上传视频", which -> {
                    IntentBuilder.Builder(this, VideoEditActivity.class)
                            .putExtra(IntentBuilder.KEY_DATA, offlineFileEntity)
                            .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                            .startActivity();

                });
                dialog.addSheetItem("重新上传视频", which -> {

                    //弹出标签
                    mUploadImgVideoPresenter.showTagDialog(dialog2, PigeonMonitorActivity.this, mUploadImgVideoPresenter);

                    RealmUtils.getInstance().deleteOfflineFileEntity(offlineFileEntity);
                    offlineFileManager.deleteOfflineCache();


                });
                dialog.show();
            } else {
                //弹出标签
                mUploadImgVideoPresenter.showTagDialog(dialog, PigeonMonitorActivity.this, mUploadImgVideoPresenter);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (smallVideoHelper != null && smallVideoHelper.backFromFull()) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AC_CODE:
                    // 图片选择结果回调
                    Intent intent1 = new Intent(this, PhotoEditActivity.class);
                    intent1.putExtra("img_path", PictureSelector.obtainMultipleResult(data).get(0).getCompressPath());
                    startActivity(intent1);
                    break;
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == MonitorPresenter2.REQUEST_IGNORE_BATTERY_CODE) {
                Log.d("Hello World!", "开启省电模式成功");
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == REQUEST_IGNORE_BATTERY_CODE) {

                if (!MonitorPresenter2.isIgnoringBatteryOptimizations(this)) {
                    MonitorPresenter2.gotoSettingIgnoringBatteryOptimizations(this);
                }
                Toast.makeText(this, "请用户开启忽略电池优化~", Toast.LENGTH_LONG).show();
            }
        }
    }
}

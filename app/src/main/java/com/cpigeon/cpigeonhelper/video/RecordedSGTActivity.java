package com.cpigeon.cpigeonhelper.video;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.camera.util.BitmapUtils;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SGTImgUploadActivity2;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.video.camera.SensorControler;
import com.cpigeon.cpigeonhelper.video.widget.CameraView;
import com.cpigeon.cpigeonhelper.video.widget.CircularProgressView;
import com.cpigeon.cpigeonhelper.video.widget.FocusImageView;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by cj on 2017/7/25.
 * 公棚赛鸽拍照
 */

public class RecordedSGTActivity extends Activity implements View.OnTouchListener, SensorControler.CameraFocusListener {

    @BindView(R.id.camera_view)
    CameraView mCameraView;
    @BindView(R.id.id_img_gzlk)
    ImageView imgGzlk;//鸽子轮廓
    @BindView(R.id.imgbtn_ture)
    ImageButton imgbtn_ture;//拍照确定
    @BindView(R.id.imgbtn_false)
    ImageButton imgbtn_false;//拍照取消
    @BindView(R.id.btn_flash_lamp)
    ImageView flash_light;//闪光灯
    @BindView(R.id.btn_paizhao)
    FrameLayout btn_paizhao;//拍照

    private CircularProgressView mCapture;
    private FocusImageView mFocus;
    private String img_path;//图片保存地址
    private boolean pausing = false;
    private boolean recordFlag = false;//是否正在录制
    private boolean autoPausing = false;
    ExecutorService executorService;
    private SensorControler mSensorControler;
    private Unbinder mUnbinder;
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    public static int light_num = 2;
    public static int light_num_zd = 2;
    Intent intent1 = null;
    public static int IMG_NUM_TAG = -1;  //1   多张图片   2   足环在左脚   3 足环在右脚
    public static int SHOW_FLASH_TAG = -1;  //是否打开闪光灯

    private Timer mTimer;
//    private TimerTask mTimerTask = new TimerTask() {
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mCameraView.mCameraDrawer.getBitmap.setBitmap(bitmapNull, cameraTag);
//                }
//            });
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgt_recorde);
        mUnbinder = ButterKnife.bind(this);

        executorService = Executors.newSingleThreadExecutor();
        mSensorControler = SensorControler.getInstance();
        mSensorControler.setCameraFocusListener(this);

        initView();
    }

    private void initView() {
        mCapture = (CircularProgressView) findViewById(R.id.mCapture);
        mFocus = (FocusImageView) findViewById(R.id.focusImageView);
        mCameraView.setOnTouchListener(this);

        IMG_NUM_TAG = getIntent().getIntExtra("IMG_NUM_TAG", -1);

        if (IMG_NUM_TAG == 2) {//左脚
            imgGzlk.setImageResource(R.mipmap.gzlk_l);
        } else if (IMG_NUM_TAG == 3) {//右脚
            imgGzlk.setImageResource(R.mipmap.gzlk_r);
        }

        photoOperation();//拍照

        bitmapNull = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);


        initCameraWatermark();

        if (SHOW_FLASH_TAG == -1) {
            SHOW_FLASH_TAG = 1;
            SweetAlertDialogUtil.showDialog3(mSweetAlertDialog, "拍照时打开闪光灯", this, dialog -> {
                dialog.dismiss();
                light_num_zd = 0;
                showFlash();
            });
        } else {
            showFlash();
        }
    }


    private void initCameraWatermark() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //时间
                        mCameraView.mCameraDrawer.getBitmap.setBitmap(bitmapNull, cameraTag);
                    }
                });
            }
        }, 0, 1000);
    }

    //打开闪光灯
    private void showFlash() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFlashLampZD();
                    }
                });
            }
        }).start();
    }

    private Bitmap bitmapNull;
    private SweetAlertDialog mSweetAlertDialog;


    //-----------------------------------------------------生命周期（不动）------------------------------------------------------------------------
    @Override
    protected void onResume() {
        try {
            mCameraView.onResume();
            mCameraView.resume(true);
            cameraTag = 1;

            if (recordFlag && autoPausing) {
                mCameraView.resume(true);
                autoPausing = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            cameraTag = 2;
            if (recordFlag && !pausing) {
                mCameraView.pause(true);
                autoPausing = true;
            }
            mCameraView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mTimer.cancel();
            if (mCameraView.mCamera != null) {
                mCameraView.mCamera.close();
            }
            mCameraView.destroyDrawingCache();
            mUnbinder.unbind();//解除奶油刀绑定
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------事件处理（操作）------------------------------------------------------------------------
    @OnClick({R.id.imgbtn_ture, R.id.imgbtn_false, R.id.btn_flash_lamp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_ture://拍照确定
                imgbtn_ture();
                break;
            case R.id.imgbtn_false://拍照取消
                imgbtn_false();
                break;
            case R.id.btn_flash_lamp://闪光灯
                setFlashLamp();
                break;
        }
    }

    /**
     * 打开闪光灯
     * tag ==1  自动   tag  ==2  手动打开关闭闪光灯
     */
    private void setFlashLamp() {
        try {
            Camera.Parameters parameters = mCameraView.mCamera.mCamera.getParameters();
            switch (light_num) {
                case 0:
                    //打开
                    light_num = 2;
                    light_num_zd = 0;
                    flash_light.setImageResource(R.drawable.btn_camera_flash_on);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                    mCameraView.mCamera.mCamera.setParameters(parameters);
                    break;
                case 2:
                    //关闭
                    light_num = 0;
                    light_num_zd = 2;
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCameraView.mCamera.mCamera.setParameters(parameters);
                    flash_light.setImageResource(R.drawable.btn_camera_flash_off);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开闪光灯
     */
    private void setFlashLampZD() {
        try {
            Camera.Parameters parameters = mCameraView.mCamera.mCamera.getParameters();
            switch (light_num_zd) {
                case 0:
                    //打开
                    flash_light.setImageResource(R.drawable.btn_camera_flash_on);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                    mCameraView.mCamera.mCamera.setParameters(parameters);
                    break;
                case 2:
                    //关闭
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCameraView.mCamera.mCamera.setParameters(parameters);
                    flash_light.setImageResource(R.drawable.btn_camera_flash_off);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照确定
     */
    private void imgbtn_ture() {
        recordFlag = false;
        intent1 = new Intent(RecordedSGTActivity.this, SGTImgUploadActivity2.class);
        intent1.putExtra("img_path", img_path);
        intent1.putExtra("tagStr", getIntent().getStringExtra("tagStr"));//标签名称
        intent1.putExtra("tagid", getIntent().getIntExtra("tagid", -1));//标签id
        intent1.putExtra("listdetail", getIntent().getParcelableArrayListExtra("listdetail"));
        intent1.putExtra("reason", getIntent().getStringExtra("reason"));

        startActivity(intent1);
        RecordedSGTActivity.this.finish();
    }

    //    拍照取消
    private void imgbtn_false() {

        if (mTimer != null) {
            mTimer.cancel();
        }


        btn_paizhao.setVisibility(View.VISIBLE);//拍照显示
        imgbtn_false.setVisibility(View.GONE);//取消按钮隐藏
        imgbtn_ture.setVisibility(View.GONE);//确定按钮隐藏

        mCameraView.onResume();
        mCameraView.resume(true);

        cameraTag = 1;

        initCameraWatermark();
    }

    private int cameraTag = 1;

//-----------------------------------------------------线程相关------------------------------------------------------------------------

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mCameraView.getCameraId() == 1) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float sRawX = event.getRawX();
                float sRawY = event.getRawY();
                float rawY = sRawY * MyApplication.screenWidth / MyApplication.screenHeight;
                float temp = sRawX;
                float rawX = rawY;
                rawY = (MyApplication.screenWidth - temp) * MyApplication.screenHeight / MyApplication.screenWidth;

                Point point = new Point((int) rawX, (int) rawY);
                mCameraView.onFocus(point, callback);
                mFocus.startFocus(new Point((int) sRawX, (int) sRawY));
        }
        return true;
    }

    Camera.AutoFocusCallback callback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //聚焦之后根据结果修改图片
            if (success) {
                mFocus.onFocusSuccess();
            } else {
                //聚焦失败显示的图片
                mFocus.onFocusFailed();
            }
        }
    };

    @Override
    public void onFocus() {
        if (mCameraView.getCameraId() == 1) {
            return;
        }
        Point point = new Point(MyApplication.screenWidth / 2, MyApplication.screenHeight / 2);
        mCameraView.onFocus(point, callback);
    }

//-----------------------------------------------------图片相关（操作）------------------------------------------------------------------------

    /**
     * 当前页面用于拍照
     */
    private void photoOperation() {
        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraView.mCamera.mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                        //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cameraTag = 2;
                                imgbtn_false.setVisibility(View.VISIBLE);//取消显示
                                imgbtn_ture.setVisibility(View.VISIBLE);//确定显示
                                btn_paizhao.setVisibility(View.INVISIBLE);//拍照隐藏

                                img_path = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() + File.separator + System.currentTimeMillis() + ".jpeg";
                                BitmapUtils.saveJPGE_After(RecordedSGTActivity.this, BitmapUtils.rotaingImageView(90, BitmapFactory.decodeByteArray(data, 0, data.length)), img_path, 100);
                            }
                        });
                    }
                });
            }
        });
    }
}

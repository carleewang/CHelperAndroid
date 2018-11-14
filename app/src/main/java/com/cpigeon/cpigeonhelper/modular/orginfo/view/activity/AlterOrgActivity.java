package com.cpigeon.cpigeonhelper.modular.orginfo.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.OrgInforViewImpl;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
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

/**
 * 修改组织信息界面
 * Created by Administrator on 2017/9/16.
 */

public class AlterOrgActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_before_name)
    TextView tvBeforeName;//原名称
    @BindView(R.id.et_change_name)
    EditText etChangeName;//变更名称
    @BindView(R.id.et_change_cause)
    EditText etChangeCause;//变更原因
    @BindView(R.id.alter_img)
    ImageButton alterImg;//上传证明
    @BindView(R.id.alter_org_submit)
    Button alterOrgSubmit;//提交

    private OrgInforPresenter presenter;//控制层

    private List<LocalMedia> list = new ArrayList<>();
    private int chooseMode = PictureMimeType.ofImage();//设置选择的模式
    private int maxSelectNum = 1;// 最大图片选择数量
    private int compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式

    private File compressimg = new File("");//图片路径
    private final int albumRequestCode = 0x0021;//相册,相册返回码

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_alter_org;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("变更名称申请");
        setTopLeftButton(R.drawable.ic_back, AlterOrgActivity.this::finish);

        tvBeforeName.setText(getIntent().getStringExtra("orgName"));//设置原名称

        presenter = new OrgInforPresenter(new OrgInforViewImpl(){

            /**
             * 成功后回调
             */
            @Override
            public void validationSucceed() {

                //发布事件（刷新数据）
                EventBus.getDefault().post(EventBusService.INFO_ORG_REFRESH);

                CommonUitls.showSweetDialog(AlterOrgActivity.this, "申请成功，请等待处理结果", dialog -> {
                    dialog.dismiss();
                    AppManager.getAppManager().killActivity(mWeakReference);
                });
            }
            @Override
            public void getErrorNews(String str) {
                AlterOrgActivity.this.getErrorNews(str);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                AlterOrgActivity.this.getThrowable(throwable);
            }

        });//初始化控制层
    }

    @OnClick({R.id.alter_img, R.id.alter_org_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.alter_img://选择证明图片
                new SaActionSheetDialog(AlterOrgActivity.this)
                        .builder()
                        .addSheetItem("相册选取", OnSheetItemClickListener)
                        .addSheetItem("拍一张", OnSheetItemClickListener)
                        .show();
                break;
            case R.id.alter_org_submit://开始提交
                presenter.changeOrgName(compressimg//证明文件路径
                        , etChangeName.getText().toString()//新的组织名称
                        , etChangeCause.getText().toString());//变更原因

                break;
        }
    }

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
                    presenter.jumpAlbum(AlterOrgActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
                case 2:
                    //跳转到相机
                    presenter.jumpCamera(AlterOrgActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
            }
        }
    };

    /**
     * 跳转到相册
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case albumRequestCode:
                    // 图片选择结果回调
                    list = PictureSelector.obtainMultipleResult(data);
                    compressimg = new File(list.get(0).getCompressPath());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bm = BitmapFactory.decodeFile(list.get(0).getCompressPath(), options);
                    alterImg.setImageBitmap(bm);
                    break;
            }
        }
    }



    //    /**
//     * 选择相册
//     */
//    private void choseHeadImageFromGallry() {
//        if (ContextCompat.checkSelfPermission(AlterOrgActivity.this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(AlterOrgActivity.this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        } else {
//            openAlbum();
//        }
//    }
//
//    private static final int CHOOSE_PHPOTO = 2;
//
//    private void openAlbum() {
//
//        //保存图片文件路径
//        outputImage = new File(getExternalCacheDir(), "output_images.jpg");
//        try {
//            if (outputImage.exists()) {
//                outputImage.delete();
//            }
//            outputImage.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(intent, CHOOSE_PHPOTO);
//    }
//
//
//    /**
//     * 选择相机的
//     */
//    private void choseHeadImageFromCamera() {
//        //环境兼容.自我检查权限()
//        if (ContextCompat.checkSelfPermission(AlterOrgActivity.this,
//                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//
//            ActivityCompat.requestPermissions(AlterOrgActivity.this,
//                    new String[]{Manifest.permission.CAMERA}, 2);
//        } else {
//            openCamera();
//        }
//
//    }
//
//    private Uri imageUri;
//    private static final int TAKE_PHOTO = 4;
//    private File outputImage;
//
//    private void openCamera() {
//        //保存图片文件路径
//        outputImage = new File(getExternalCacheDir(), "output_images.jpg");
//        try {
//            if (outputImage.exists()) {
//                outputImage.delete();
//            }
//            outputImage.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //版本小于7。0
//        if (Build.VERSION.SDK_INT < 24) {
//            imageUri = Uri.fromFile(outputImage);
//        } else {
//            //7.0以上权限路径
//            Log.d(TAG, "openCamera: 测试1");
//            imageUri = FileProvider.getUriForFile(AlterOrgActivity.this, "com.cpigeon.cpigeonhelper.fileprovider", outputImage);
//            Log.d(TAG, "openCamera: 测试2");
//        }
//
//
//        // 启动相机程序
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, TAKE_PHOTO);
//    }
//
//
//    private String mUserHeadImageLocalPath;
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != RESULT_OK) return;
//        switch (requestCode) {
//            case CHOOSE_PHPOTO://选择相册后返回
//                Log.d(TAG, "onActivityResult: 选择相册后返回");
//                handleImageOnKikat(data);
//
//                break;
//
//            case TAKE_PHOTO://启动相机后返回
//                Log.d(TAG, "onActivityResult: 启动相机后返回");
//                crop(imageUri);
//                break;
//            case PHOTO_REQUEST_CUT://图片剪切后设置图片
//                Log.d(TAG, "测试: ---》4");
//                Log.d(TAG, "onActivityResult: 图片剪切后设置图片");
////                Bitmap bitmap = data.getParcelableExtra("data");
////                String filePath = PictureCutUtil.cutPictureQuality(bitmap, CpigeonConfig.CACHE_FOLDER);
////                filePath = CpigeonConfig.CACHE_FOLDER + filePath;
//////                Picasso.with(mContext).load(filePath).into(ivUserHeadImg);
////                Logger.d(filePath);
////                mUserHeadImageLocalPath = filePath;
////                BitmapFactory.Options options = new BitmapFactory.Options();
////                options.inSampleSize = 2;
////                Bitmap bm = BitmapFactory.decodeFile(filePath, options);
////                Log.d(TAG, "测试: ---》5");
////                alterImg.setImageBitmap(bm);
//                break;
//        }
//    }
//
//    private void handleImageOnKikat(Intent data) {
//        Uri uri = data.getData();
////        data.getExtras().get("data");
//
//        Log.d(TAG, "测试: ---》1" + uri.getLastPathSegment());
//        crop(uri);
//    }
//
//    /**
//     * 剪切图片
//     *
//     * @param uri
//     */
//    private static final int PHOTO_REQUEST_CUT = 3;
//
//    private void crop(Uri uri) {
//        // 裁剪图片意图
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // crop为true是设置在开启的intent中设置显示的view可以剪裁
//        intent.putExtra("crop", "true");
//
//
//        // 裁剪框的比例，1：1
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // 裁剪后输出图片的尺寸大小
//        // outputX,outputY 是剪裁图片的宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        // 图片格式
//        intent.putExtra("outputFormat", "JPG");
//        intent.putExtra("noFaceDetection", true);// 取消人脸识别
//        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
//
//        /**
//         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
//         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
//         */
//        //intent.putExtra("return-data", true);
//
//        //uritempFile为Uri类变量，实例化uritempFile
////        Uri uritempFile = Uri.parse(outputImage.getAbsolutePath());
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
////        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//
//
//        File file = new File(CpigeonConfig.CACHE_FOLDER + "/ResultImg.jpg");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//获取当前手机版本>=24  (7.0)
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
////            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////
//            Uri uriForFile = FileProvider.getUriForFile(AlterOrgActivity.this, "com.cpigeon.cpigeonhelper.fileprovider", file);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
//
//
//            Uri outPutUri = Uri.fromFile(file);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
//
//
//        Log.d(TAG, "测试: ---》2");
//        startActivityForResult(intent, PHOTO_REQUEST_CUT);//同样的在onActivityResult中处理剪裁好的图片
//        Log.d(TAG, "测试: ---》3");
//    }
//
//
//    /**
//     * 调用系统剪裁功能
//     */
//    public void cropPicture(Activity activity, String path)
//    {
//        File file = new File(path);
//        if (!file.getParentFile().exists())
//        {
//            file.getParentFile().mkdirs();
//        }
//        Uri imageUri;
//        Uri outputUri;
//       String crop_image = createImagePath(APP_NAME + "_crop_" + DATE);
//
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            //通过FileProvider创建一个content类型的Uri
//            imageUri = FileProvider.getUriForFile(activity, Constants.FILE_CONTENT_FILEPROVIDER, file);
//            outputUri = Uri.fromFile(new File(crop_image));
//            //TODO:outputUri不需要ContentUri,否则失败
//            //outputUri = FileProvider.getUriForFile(activity, "com.solux.furniture.fileprovider", new File(crop_image));
//        } else {
//            imageUri = Uri.fromFile(file);
//            outputUri = Uri.fromFile(new File(crop_image));
//        }
//        intent.setDataAndType(imageUri, "image/*");
//        intent.putExtra("crop", "true");
//        //设置宽高比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        //设置裁剪图片宽高
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 300);
//        intent.putExtra("scale", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true);
//        activity.startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
//    }
}

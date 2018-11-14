package com.cpigeon.cpigeonhelper.utils;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;

/**
 * 图片选择提示框
 * Created by Administrator on 2018/1/6.
 */

public class ImgChooseDialogUtil {


    public static SaActionSheetDialog createImgChonseDialog(Activity activity, SaActionSheetDialog.OnSheetItemClickListener itemClickListener) {

        SaActionSheetDialog mSaActionSheetDialog = new SaActionSheetDialog(activity)
                .builder()
                .addSheetItem("相册选取", itemClickListener)
                .addSheetItem("拍一张", itemClickListener)
                .show();

        return mSaActionSheetDialog;
    }

    /**
     * 跳转到相册
     * chooseMode = PictureMimeType.ofImage();//设置选择的模式
     * maxSelectNum = 1;// 最大图片选择数量
     * compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式
     * requesCode  返回Activity返回码
     */
    public static void jumpAlbum(Activity activity, int chooseMode, int maxSelectNum, int compressMode, int requesCode) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(false)// 是否显示拍照按钮
                .compress(true)// 是否压缩
                .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .forResult(requesCode);//结果回调onActivityResult code
    }


    /**
     * 跳转到相机
     * chooseMode = PictureMimeType.ofImage();//设置选择的模式
     * maxSelectNum = 1;// 最大图片选择数量
     * compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式
     * requesCode  返回Activity返回码
     */
    public static void jumpCamera(Activity activity, int chooseMode, int maxSelectNum, int compressMode, int requesCode) {
        // 进入相机 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openCamera(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .compress(true)// 是否压缩
                .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .forResult(requesCode);//结果回调
    }

}

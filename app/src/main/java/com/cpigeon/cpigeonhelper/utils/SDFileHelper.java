package com.cpigeon.cpigeonhelper.utils;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2017/12/15.
 */

public class SDFileHelper {

    private Context context;

    public SDFileHelper() {
    }

    public SDFileHelper(Context context) {
        super();
        this.context = context;
    }

    //Glide保存图片
    public void savePicture(final String fileName, String url) {
        Glide.with(context).load(url).asBitmap().toBytes().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                try {
                    savaFileToSD(fileName, bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    //往SD卡写入文件的方法
    public String savaFileToSD(String filename, byte[] bytes) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/wdfw";
            File dir1 = new File(filePath);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
            filename = filePath + "/" + filename + ".jpg";
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            //将bytes写入到输出流中
            output.close();
            //关闭输出流
            getDownImg.getDownImgFilePath(filename);//用接口返回图片保存路径
//            Toast.makeText(context, "图片已成功保存到" + filename, Toast.LENGTH_SHORT).show();
            return filename;
        } else {
            CommonUitls.showSweetAlertDialog(context, "温馨提示", "SD卡不存在或者不可读写");
            return null;
        }
    }

    //用接口返回图片保存路径
    public static GetDownImg getDownImg;

    public interface GetDownImg {
        void getDownImgFilePath(String filePath);
    }


    //=========================================用缓存下载图片(未测试)=================================================


    //用缓存下载图片
    public void saveHcPicture(final String fileName, String imgUrl) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    copyFile(Glide.with(context).load(imgUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get().getAbsolutePath(),
                            fileName);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * oldPath: 图片缓存的路径
     * newPath: 图片缓存copy的路径
     */
    public static void copyFile(String oldPath, String newPath) {

        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                newPath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/wdfw/" + newPath;
                try {
                    int byteRead;
                    File oldFile = new File(oldPath);
                    if (oldFile.exists()) {
                        InputStream inStream = new FileInputStream(oldPath);
                        FileOutputStream fs = new FileOutputStream(newPath);
                        byte[] buffer = new byte[1024];
                        while ((byteRead = inStream.read(buffer)) != -1) {
                            fs.write(buffer, 0, byteRead);
                        }
                        inStream.close();
                        getDownImg.getDownImgFilePath(newPath);//用接口返回图片保存路径
                    }
                } catch (Exception e) {
                    System.out.println("复制文件操作出错");
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
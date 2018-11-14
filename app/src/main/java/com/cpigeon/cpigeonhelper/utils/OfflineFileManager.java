package com.cpigeon.cpigeonhelper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.cpigeon.cpigeonhelper.camera.util.BitmapUtils;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;

import java.io.File;

/**
 * Created by Zhu TingYu on 2018/2/27.
 */

public class OfflineFileManager {

    private File cacheDir;
    private String sceneType;
    private String saveCachePath;

    Context context;
    String userId;
    String fileType;


    public static String TYPE_IMG = "image";
    public static String TYPE_VIDEO = "video";
    public static String OFFLINE = "OFFLINE";
    private static String OFFLINE_FILE = "offline_cache";

    public static String SUFFIX_IMG = ".jpeg";
    public static String SUFFIX_MP4 = ".mp4";


    public OfflineFileManager(Context context, String sceneType, String fileType){
        this.context = context;
        this.sceneType = sceneType;
        this.fileType = fileType;
        cacheDir = new File(context.getExternalCacheDir() + File.separator + OFFLINE);
        if(!cacheDir.exists()){
            cacheDir.mkdir();
        }
        userId = String.valueOf(AssociationData.getUserId());
    }

    /**
     * 保存bitmap
     * @param bitmap
     * @return
     */

    public String saveCache(Bitmap bitmap){

        String imgPath = getCachePath();

        BitmapUtils.saveJPGE_After(context, bitmap, imgPath, 100);//图片保存

        return imgPath;
    }

    /**
     * 保存需要转换的图片
     * @param filePath
     */

    public void saveCache(String filePath){
        this.saveCachePath = filePath;
    }

    /**
     * 获取缓存
     * @param isOffline 是否是离线缓存
     * @return
     */
    public String getCache(boolean isOffline){
        File file;
        if(isOffline){
            file = new File(getOfflineCachePath());
        }else file = new File(getCachePath());

        if(file.exists()){
            return file.getPath();
        }else return null;
    }

    /**
     * 转换成离线缓存
     * @return
     */

    public boolean convertToOffline(){
        File file = new File(saveCachePath);
        return file.exists()
                && file.renameTo(new File(getOfflineCachePath()));
    }

    /**
     * 检查缓存是否存在
     * @param isOffline 是否是离线缓存
     * @return
     */
    public boolean checkCache(boolean isOffline){
        if(StringValid.isStringValid(getCache(isOffline))){
            File file = new File(getCache(isOffline));
            return file.exists();
        }else return false;
    }

    /**
     * 删除离线缓存
     * @return
     */

    public boolean deleteOfflineCache(){
        File file = new File(getOfflineCachePath());
        return file.exists() && file.delete();
    }

    private String getCachePath(){
        if(TYPE_IMG.equals(fileType)){
            return getBasePath().append(SUFFIX_IMG).toString();
        }else {
            return getBasePath().append(SUFFIX_MP4).toString();
        }
    }

    private String getOfflineCachePath(){
        LogUtil.print("offline_cache :" + getBasePath().append(OFFLINE).append(SUFFIX_IMG).toString());
        if(TYPE_IMG.equals(fileType)){
            return getBasePath().append(OFFLINE).append(SUFFIX_IMG).toString();
        }else {
            return getBasePath().append(OFFLINE).append(SUFFIX_MP4).toString();
        }
    }

    private StringBuffer getBasePath(){
        StringBuffer filePath = new StringBuffer();

        filePath.append(cacheDir.getPath())
                .append(File.separator)
                .append(sceneType)
                .append("_")
                .append(userId);
        return filePath;

    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }
}

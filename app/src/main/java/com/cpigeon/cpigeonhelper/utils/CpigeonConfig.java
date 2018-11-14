package com.cpigeon.cpigeonhelper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.cpigeon.cpigeonhelper.MyApplication;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;


/**
 * Created by Administrator on 2016-10-20.
 */

public class CpigeonConfig {

    /**
     * 外部文件存储跟路径
     */
    public static final String FILE_PATH_ROOT = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + MyApplication.getInstance().getPackageName();
    /**
     * 更新APP安装包存放文件夹
     */
    public static final String UPDATE_SAVE_FOLDER = FILE_PATH_ROOT + "/update/";


    /**
     * 缓存路径
     */
    public static final String CACHE_FOLDER = FILE_PATH_ROOT + "/cache/";

    static {
    }

    /**
     * 获取默认Db
     *
     * @return
     */
    public static DbManager.DaoConfig getDataDb() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        daoConfig.setDbName("data.db");
        daoConfig.setDbVersion(7);
        daoConfig.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                try {
                    db.dropDb();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
        daoConfig.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {
                db.getDatabase().enableWriteAheadLogging();
            }
        });

        daoConfig.setAllowTransaction(true);
        daoConfig.setTableCreateListener(new DbManager.TableCreateListener() {
            @Override
            public void onTableCreated(DbManager db, TableEntity<?> table) {

            }
        });
        return daoConfig;
    }

    /**
     * 获取资讯用DB
     *
     * @return
     */
    public static DbManager.DaoConfig getDataDbforNews() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        daoConfig.setDbName("news.db");
        daoConfig.setDbVersion(1);
        daoConfig.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
            }
        });
        daoConfig.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {
                db.getDatabase().enableWriteAheadLogging();
            }
        });

        daoConfig.setAllowTransaction(true);
        daoConfig.setTableCreateListener(new DbManager.TableCreateListener() {
            @Override
            public void onTableCreated(DbManager db, TableEntity<?> table) {

            }
        });
        return daoConfig;
    }


    /**
     * 给出url，获取视频的第一帧
     *
     * @param url
     * @return
     */
    public static Bitmap getVideoThumbnail(String url) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(url, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 获取本地视频的第一帧
     *
     * @param filePath
     * @return
     */
    public static Bitmap getLocalVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }
    public static Bitmap createVideoThumbnail(String filePath) {
        // MediaMetadataRetriever is available on API Level 8
        // but is hidden until API Level 10
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();

            Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, filePath);

            // The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= 9) {
                return (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null) return bitmap;
                }
                return (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } catch (InstantiationException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (InvocationTargetException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (ClassNotFoundException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (NoSuchMethodException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (IllegalAccessException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

}

package com.cpigeon.cpigeonhelper.mina;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.ApiConstants;

/**
 * 长连接
 */
public class CoreService extends Service {
    public static final String TAG = "CoreServices";
    private ConnectionThread thread;
    public static ConnectionManager mManager;

    public static boolean isconcatenon = false;

    public CoreService() {
    }

    @Override
    public void onCreate() {
        ConnectionConfig config = new ConnectionConfig.Builder(getApplicationContext())
                .setIp(ApiConstants.BASE_IP)//连接的IP地址
                .setPort(5555)//连接的端口号
                .setReadBufferSize(1024)
                .setConnectionTimeout(10000).builder();
        mManager = new ConnectionManager(config);
//        mManager = ConnectionManager.getInstance(config);
        thread = new ConnectionThread();
        thread.start();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY_COMPATIBILITY;
    }

    public class ConnectionThread extends Thread {

        @Override
        public void run() {
            for (; ; ) {
                CoreService.isconcatenon = mManager.connect();
                if (isconcatenon) {
                    Log.d(TAG, "连接成功跳出循环");
                    break;
                }
                try {
                    Log.d(TAG, "尝试重新连接");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void disConnect() {
        mManager.disConnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disConnect();
//        Log.d("CoreService", "onDestroy: 结束长连接服务 ");
        thread = null;
    }
}

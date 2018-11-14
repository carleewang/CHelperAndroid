package com.cpigeon.cpigeonhelper.mina;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.MyLocation;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.greenrobot.eventbus.EventBus;

import io.realm.RealmResults;

/**
 * Session管理类
 * Created by Administrator on 2017/7/4.
 */

public class SessionManager {
    private static SessionManager mInstance = null;

    private IoSession mSession;


    public static SessionManager getInstance() {
        if (mInstance == null) {
            synchronized (SessionManager.class) {
                if (mInstance == null) {
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }

    private SessionManager() {
    }

    public void setSession(IoSession session) {
        this.mSession = session;
    }

    private String TAG = "asdfsa";

//    private String sss = "30.673792772412|104.03025399355919|18.0|1523156254|晴|西|5|2018-04-08 10:00:00|17|17586.199999999993,30.673792772412|104.03025399355919|18.0|1523156259|晴|西|5|2018-04-08 10:00:00|17|17870.299999999992,30.673792772412|104.03025399355919|18.0|1523156264|晴|西|5|2018-04-08 10:00:00|17|18154.39999999999,30.673792772412|104.03025399355919|18.0|1523156269|晴|西|5|2018-04-08 10:00:00|17|18438.49999999999,30.673792772412|104.03025399355919|18.0|1523156274|晴|西|5|2018-04-08 10:00:00|17|18722.599999999988,30.673792772412|104.03025399355919|18.0|1523156279|晴|西|5|2018-04-08 10:00:00|17|19006.699999999986,30.673792772412|104.03025399355919|18.0|1523156284|晴|西|5|2018-04-08 10:00:00|17|19290.799999999985,30.673792772412|104.03025399355919|18.0|1523156289|晴|西|5|2018-04-08 10:00:00|17|19574.899999999983,30.673792772412|104.03025399355919|18.0|1523156294|晴|西|5|2018-04-08 10:00:00|17|19858.99999999998,30.673792772412|104.03025399355919|18.0|1523156299|晴|西|5|2018-04-08 10:00:00|17|20143.09999999998,30.673792772412|104.03025399355919|18.0|1523156304|晴|西|5|2018-04-08 10:00:00|17|20427.19999999998,30.673792772412|104.03025399355919|18.0|1523156309|晴|西|5|2018-04-08 10:00:00|17|20711.299999999977,30.673792772412|104.03025399355919|18.0|1523156314|晴|西|5|2018-04-08 10:00:00|17|20995.399999999976,30.673792772412|104.03025399355919|18.0|1523156319|晴|西|5|2018-04-08 10:00:00|17|21279.499999999975,30.673792772412|104.03025399355919|18.0|1523156324|晴|西|5|2018-04-08 10:00:00|17|21563.599999999973,30.673792772412|104.03025399355919|18.0|1523156329|晴|西|5|2018-04-08 10:00:00|17|21847.69999999997,30.673792772412|104.03025399355919|18.0|1523156334|晴|西|5|2018-04-08 10:00:00|17|22131.79999999997,30.673792772412|104.03025399355919|18.0|1523156339|晴|西|5|2018-04-08 10:00:00|17|22415.89999999997";


    private RealmResults<MyLocation> results;

    public static String contents = "";

    public static boolean isday = false;

    public void writeToServer(IoBuffer buffer, String msg) {

        Log.d(TAG, "writeToServer: 3");
        results = RealmUtils.getInstance().queryLocation();

        if (mSession != null) {
            if (ConnectionManager.isConnect) {
                //长连接连接成功
                if (results != null && results.size() > 0) {
                    Log.d(TAG, "writeToServer: ---1");

                    if (isday) {
                        Log.d(TAG, "writeToServer: 2");
                        return;
                    }

                    isday = true;

                    EventBus.getDefault().post(results);
//                    for (int i = results.size() - 1; i >= 0; i--) {
//                        if (!contents.isEmpty()) {
//                            contents = contents + "," + results.get(i).getMsg();
//                        } else {
//                            contents = results.get(i).getMsg();
//                        }
//                    }
//                    Log.d(TAG, "writeToServer: "+contents);
//
//                    mGYTMonitorPresenters.gytOfflineUpload(contents);

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }).start();

//                    Log.d(TAG, "writeToServer: 3--》" + results.size());
//
//                    int tag = 0;
//                    String s = EncryptionTool.encryptAES(AssociationData.getUserToken(), CommonUitls.KEY_SERVER_PWD);
//                    String sign = EncryptionHeader(s) + s;//hl  加密
//
//                    for (int i = results.size() - 1; i >= 0; i--) {
//                        if (!content.isEmpty()) {
//                            content = content + "," + results.get(i).getMsg();
//                        } else {
//                            content = results.get(i).getMsg();
//                        }
//
//                        if (tag == 100 || i == 0) {
//
//                            try {
//                                Thread.sleep(1000);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            content = EncryptionContent(content) + content;//加密的内容
//                            IoBuffer buffers = IoBuffer.allocate(100000000);
//                            buffers.put(sign.getBytes());
//                            buffers.put(content.getBytes());
//                            Log.d("asdfsa", "writeToServer 1: " + content);
//
//                            buffers.flip();
//                            mSession.write(buffers);
//
//                            content = "";
//                            tag = 0;
//                        }
//                        tag++;
//                    }
//                    RealmUtils.getInstance().deleteLocation();//删除所有点坐标
                }

                Log.d(TAG, "writeToServer: 4");
                buffer.flip();
                mSession.write(buffer);

            } else {
                //长连接连接失败
                Log.d(TAG, "writeToServer: 5 -->" + RealmUtils.getInstance().queryLocation().size());

                RealmUtils.getInstance().insertLocation(new MyLocation(RealmUtils.getInstance().queryLocation().size() + 1, msg, MonitorData.getMonitorId()));

//                for (int x = 0; x < 500; x++) {
//                    int realmSize = RealmUtils.getInstance().queryLocation().size();
//
//                    if (realmSize == 0) {
//                        RealmUtils.getInstance().insertLocation(new MyLocation(1, msg, mMonitorId));
//                    } else if (results.get(realmSize).getId() % 100 == 0) {
//                        RealmUtils.getInstance().insertLocation(new MyLocation(realmSize + 1, msg, mMonitorId));
//                    } else {
//                        RealmUtils.getInstance().insertLocation(new MyLocation(realmSize, results.get(realmSize).getMsg() + "," + msg, mMonitorId));
//                    }
//                }
            }
        }
    }

    public void closeSession() {
        if (mSession != null) {
            mSession.closeOnFlush();
        }
    }

    public void removeSession() {
        this.mSession = null;
    }
}
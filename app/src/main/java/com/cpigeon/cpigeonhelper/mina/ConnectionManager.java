package com.cpigeon.cpigeonhelper.mina;

/**
 * 长连接
 * Created by Administrator on 2017/7/4.
 */

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

public class ConnectionManager {

    public String TAG = "myPrint";
    private ConnectionConfig mConfig;
    private WeakReference<Context> mContext;
    public NioSocketConnector mConnection;
    private IoSession mSession;
    private InetSocketAddress mAddress;
    private Handler mHandler = new Handler();

    public ConnectionManager(ConnectionConfig config) {
        this.mConfig = config;
        this.mContext = new WeakReference<Context>(config.getContext());
        init();
    }


//    public void setConnectionConfig(ConnectionConfig config) {
//        this.mConfig = config;
//    }
//
//    private static ConnectionManager mInstance = null;
//
//    public static ConnectionManager getInstance(ConnectionConfig config) {
//        if (mInstance == null) {
//            synchronized (ConnectionManager.class) {
//                if (mInstance == null) {
//                    mInstance = new ConnectionManager(config);
//                }
//            }
//        }
//        return mInstance;
//    }


    private void init() {
        try {
            this.userToken = AssociationData.getUserToken();
            mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
            mConnection = new NioSocketConnector();
            mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
            //设置多长时间没有进行读写操作进入空闲状态，会调用sessionIdle方法，单位（秒）
            mConnection.getSessionConfig().setReaderIdleTime(60 * 5);
            mConnection.getSessionConfig().setWriterIdleTime(60 * 5);
            mConnection.getSessionConfig().setBothIdleTime(60 * 5);
            mConnection.getFilterChain().addFirst("reconnection", new MyIoFilterAdapter());
            //自定义编解码器
            mConnection.getFilterChain().addLast("mycoder", new ProtocolCodecFilter(new MyCodecFactory()));
            //添加消息处理器
            mConnection.setHandler(new DefaultHandler(mContext.get()));
            mConnection.setDefaultRemoteAddress(mAddress);
        } catch (Exception e) {

        }
    }

    /**
     * 与服务器连接
     *
     * @return true连接成功，false连接失败
     */
    private ConnectFuture future;

    public static boolean isConnect = true;//长连接是否连接成功

    public boolean connect() {
        try {
            future = mConnection.connect();
            future.awaitUninterruptibly();
            mSession = future.getSession();
            if (mSession != null && mSession.isConnected()) {
                SessionManager.getInstance().setSession(mSession);
            } else {
                ConnectionManager.isConnect = false;
                return false;
            }
        } catch (Exception e) {
            Log.d(TAG, "connect:异常 " + e.getLocalizedMessage());
            e.printStackTrace();
            ConnectionManager.isConnect = false;
            return false;
        }
        ConnectionManager.isConnect = true;
        return true;
    }

    /**
     * 断开连接
     */
    public void disConnect() {
        mConnection.dispose();
        mConnection = null;
        mSession = null;
        mAddress = null;
        mContext = null;
    }

    private class DefaultHandler extends IoHandlerAdapter {

        private Context mContext;

        private DefaultHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);

            Log.d(TAG, "sessionOpened: 连接打开");
        }

        private IoBuffer buf;

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            Log.d(TAG, "messageReceived: 收到数据，接下来你要怎么解析数据就是你的事了");
            buf = (IoBuffer) message;
            HandlerEvent.getInstance().handle(buf);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            super.sessionIdle(session, status);
            Log.d(TAG, "sessionIdle: 客户端与服务端连接空闲");
            //进入空闲状态我们把会话关闭，接着会调用MyIoFilterAdapter的sessionClosed方法，进行重新连接
            if (session != null) {
                session.closeOnFlush();
            }
        }
    }

    private String s = "";
    private String header = "";
    private String result = "";
    private IoBuffer buffer;
    private String userToken;

    private class MyIoFilterAdapter extends IoFilterAdapter {
        @Override
        public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
            Log.d(TAG, "sessionClosed: 连接关闭，每隔5秒进行重新连接");
            for (; ; ) {
                try {
                    if (mConnection == null) {
                        break;
                    }

                    if (ConnectionManager.this.connect()) {
                        isConnect = true;
                        Log.d(TAG, "sessionClosed: 555---" + "断线重连[" + mConnection.getDefaultRemoteAddress().getHostName() + ":" + mConnection.getDefaultRemoteAddress().getPort() + "]成功");
//                        s = EncryptionTool.encryptAES(userToken, KEY_SERVER_PWD);
//                        header = "[len=" + s.length() + "&typ=2&sign=" + EncryptionTool.MD5(
//                                "len=" + s.length() + "&typ=2&" + s + "&soiDuo3inKjSdi") + "]";
//                        result = header + s;
//                        buffer = IoBuffer.allocate(100000);
//                        buffer.put(result.getBytes());
//                        SessionManager.getInstance().writeToServer(buffer,"");

                        Log.d(TAG, "sessionClosed: 888---" + "断线重连[" + mConnection.getDefaultRemoteAddress().getHostName() + ":" + mConnection.getDefaultRemoteAddress().getPort() + "]成功");
                        break;
                    } else {
                        isConnect = false;
                        Log.d(TAG, "sessionClosed:999-> 连接断开" + session.getHandler());
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                    Log.d(TAG, "sessionClosed: 3333---" + e.getLocalizedMessage());
                }
            }
        }
    }

}

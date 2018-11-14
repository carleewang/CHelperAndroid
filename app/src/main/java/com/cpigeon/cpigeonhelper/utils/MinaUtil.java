package com.cpigeon.cpigeonhelper.utils;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.mina.ConnectionManager;
import com.cpigeon.cpigeonhelper.mina.SessionManager;
import com.cpigeon.cpigeonhelper.service.DetailsService1;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 长连接的工具类
 * Created by Administrator on 2017/11/9.
 */

public class MinaUtil {
    /**
     * 给服务器发送一条消息 （数据格式为:里程）
     */
    private static String userToken = AssociationData.getUserToken();
    private static String s;
    private static String sign;
    private static String content;
    private static IoBuffer buffer;
    private static String sss = "30.668479|104.032259|0.0|1523099431|多云|西|5|2018-04-07 19:00:00|19|290.9,30.668479|104.032259|0.0|1523099431|多云|西|5|2018-04-07 19:00:00|19|290.9,30.668479|104.032259|0.0|1523099431|多云|西|5|2018-04-07 19:00:00|19|290.9";

    public static void sendMsg(String msg) {
        Log.d("sendMsg", "----------上传数据1: " + msg);
        Log.d("sendMsg", "----------上传数据2: " + ConnectionManager.isConnect);
        Log.d("sendMsg", "----------上传数据4: " + DetailsService1.intTag1);
        s = EncryptionTool.encryptAES(userToken, CommonUitls.KEY_SERVER_PWD);
        sign = EncryptionHeader(s) + s;//hl  加密头
        content = EncryptionContent(msg) + msg;//加密的内容
        buffer = IoBuffer.allocate(100000);
        buffer.put(sign.getBytes());
        buffer.put(content.getBytes());
        SessionManager.getInstance().writeToServer(buffer, msg);// hl  发送给服务器
        Log.d("sendMsg", "----------上传数据3: ");
    }

    /**
     * 加密头
     *
     * @param msg
     * @return
     */
    public static String EncryptionHeader(String msg) {
        return "[len=" + msg.length() + "&typ=1&sign=" + EncryptionTool.MD5(
                "len=" + msg.length() + "&typ=1&" + msg + "&soiDuo3inKjSdi") + "]";
    }

    /**
     * 加密内容
     *
     * @param msg
     * @return
     */
    public static String EncryptionContent(String msg) {
        return "[len=" + msg.length() + "&typ=2&sign=" + EncryptionTool.MD5(
                "len=" + msg.length() + "&typ=2&" + msg + "&soiDuo3inKjSdi") + "]";
    }
}

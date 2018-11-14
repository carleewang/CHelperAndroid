package com.cpigeon.cpigeonhelper.common.network;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址
 * Created by Administrator on 2017/5/25.
 */

public class ApiConstants {

    private static ApiConstants instance;

    public static String BASE_URL = ApiConstants.getInstance().getBaseUrl();//服务器地址

    public String BASE_URL1 = "http://118.123.244.89:818/";//服务器地址
    public String BASE_URL2 = "http://221.236.20.76:818/";//服务器地址
    public String BASE_URL3 = "http://114.141.132.146:818/";

    public List<String> FWQDZSJ = new ArrayList<>();

    private String getBaseUrl() {

        if (FWQDZSJ == null || FWQDZSJ.size() == 0) {
            FWQDZSJ.add(BASE_URL1);
            FWQDZSJ.add(BASE_URL2);
            FWQDZSJ.add(BASE_URL3);
        }

        try {
            Log.d("fuwqd", "getApi:1  " + (int) (Math.random() * 3));
            return FWQDZSJ.get((int) (Math.random() * 3));
        } catch (Exception e) {
            return FWQDZSJ.get(0);
        }
    }

    public static ApiConstants getInstance() {
        if (instance == null) {
            synchronized (ApiConstants.class) {
                if (instance == null)
                    instance = new ApiConstants();
            }
        }
        return instance;
    }

    //        public static final String BASE_IP = "118.123.244.89";//服务器地址
    public static final String BASE_IP = "221.236.20.76";//服务器地址

    //    public static final String BASE_URL = "http://192.168.0.5:8888/";
    public static final String PLAY_TYPE = "APP/Protocol?type=pay";//支付协议
    public static final String HELP_TYPE = "APP/Help?type=help&appType=cpigeonhelper";//支付协议
    public static final String USER_TYPE = "APP/Protocol?type=zhushou";//用户协议
    public static final String FUNCTION_TYPE = "APP/Intro?type=zhushouandroid";//功能介绍
    public static final String WHAT_PLAY_PAS = "APP/Help?type=help&id=172";//什么是支付密码
    public static final String PIGEON_SPECTRUM = "http://www.cpigeon.com/uploadfiles/ad/txgp/";//天下鸽谱
    public static final String PLAY_SMS = "APP/Protocol?type=bsdxxy";//比赛短信协议URL
    public static final String MONITOR_HELP = "APP/Help?type=gyt";//鸽车监控使用帮助

    public static final String APP_HELP_URL = "APP/Help?type=help";// App帮助

    public static final String APP_SIGN_URL = "APP/UserSign?uid=";//App签到链接

    //    public static final String VIDEO_SHARE = "http://118.123.244.89:818/Share?id=";//视频分享的Bas url  视频分享：http://118.123.244.89:818/Share?id=视频的ID（fid）
    public static final String VIDEO_SHARE = "http://www.cpigeon.com:818/Share/VideoZGZS?id=";//视频分享的Bas url  视频分享：http://118.123.244.89:818/Share?id=视频的ID（fid）

    //个人服务推荐URL
    public static final String GRFW_ZGW_APP = "Share/zgw";//中鸽网App
    public static final String GRFW_ZHCX = "Share/zhcx";//足环查询
    public static final String GRFW_TXGP = "Share/txgp";//天下鸽谱

    //公棚协议
    public static final String GP_SERVICE_RP = "APP/Protocol?type=rpdxxy";//入棚短信协议
    public static final String GP_SERVICE_XS = "APP/Protocol?type=xsdxxy";//训赛短信协议
    public static final String GP_SERVICE_SL = "APP/Protocol?type=sldxxy";//上笼短信协议

    public static final String SHARE_FRIND = "/Share/zgzs?yqm=";//分享中鸽助手
}

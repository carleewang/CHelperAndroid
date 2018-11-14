package com.cpigeon.cpigeonhelper.utils.version;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.ArrayList;
import java.util.List;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Created by Administrator on 2018/3/9.
 */

public class VersionUpdateUtil {

    //http://blog.csdn.net/qq_31344287/article/details/52399812
    private String TXYYB = "com.tencent.android.qqdownloader";//腾讯应用宝
    private String SJZS = "com.qihoo.appstore";//360手机助手
    private String BDSJZS = "com.baidu.appsearch";//百度手机助手
    private String XMSJSD = "com.xiaomi.market";//小米手机商店
    private String HWSHJD = "com.huawei.appmarket";//华为手机商店
    private String WDJ = "com.wandoujia.phoenix2";//豌豆荚
    private String OPYYSD = "com.oppo.market";//op应用商店

    //跳转到当前app应用商城
    public void startDq(Activity mContent) {
        try {
            String mAddress = "market://details?id=" + getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            mContent.startActivity(marketIntent);
        } catch (Exception e) {
            CommonUitls.showSweetDialog1(mContent, "未找到应用商城", dialog -> {
                dialog.dismiss();
            });
        }
    }

    //跳转到应用宝
    public void startYYB(Activity mContent) {
        try {
            String mAddress = "market://details?id=" + getPackageName();
            Intent marketIntent = new Intent("com.tencent.android.qqdownloader");
            marketIntent.setData(Uri.parse(mAddress));
            mContent.startActivity(marketIntent);
        } catch (Exception e) {
            CommonUitls.showSweetDialog1(mContent, "未找到应用商城", dialog -> {
                dialog.dismiss();
            });
        }
    }

    private void intit_getClick(Activity mContent, String yysc) {
        if (isAvilible(mContent, yysc)) {
            // 市场存在
            launchAppDetail(mContent, getPackageName(), yysc);
        } else {
            Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=" + getPackageName());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            mContent.startActivity(it);
        }
    }

    /**
     * 启动到app详情界面
     *
     * @param appPkg    App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 判断市场是否存在的方法
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }
}

package com.cpigeon.cpigeonhelper.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;
import com.cpigeon.cpigeonhelper.ui.button.CircularProgressButton;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cpigeon.cpigeonhelper.utils.CommonUitls.OnWxPayListener.ERR_OK;
import static com.cpigeon.cpigeonhelper.utils.CommonUitls.OnWxPayListener.ERR_USER_CANCEL;


/**
 * 中鸽网公共类
 * Created by Administrator on 2017/5/25.
 */
public class CommonUitls {
    private static final String KEY_API_SIGN = "iy087234ho78fuqy49TR23jk4h";    //只有url参数的签名算法

    public static final String KEY_SERVER_PWD = "Je9Ol174MbsWaq2K";
    private static String DeviceID;
    private static Toast toast;
    private volatile static CommonUitls mCommonUitls;
    private List<OnWxPayListener> onWxPayListenerList;

    private CommonUitls() {
    }

    public static CommonUitls getInstance() {
        if (mCommonUitls == null) {
            synchronized (CommonUitls.class) {
                if (mCommonUitls == null) {
                    mCommonUitls = new CommonUitls();
                }
            }
        }
        return mCommonUitls;
    }

    public static String getApiSign(Map<String, Object> urlParams) {
        Map<String, String> map = new TreeMap<>();

        if (urlParams != null && urlParams.size() > 0) {
            for (String key : urlParams.keySet()) {
                if (key != null && ("sign".equals(key.toLowerCase()) || TextUtils.isEmpty(urlParams.get(key).toString())))
                    continue;
                if (!map.containsKey("get_" + key)) {
                    map.put("get_" + key, urlParams.get(key).toString());
                }

            }
        }


        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            stringBuilder.append(key + "=" + map.get(key) + "&");
        }
        stringBuilder.append("key=" + KEY_API_SIGN);
        String result = stringBuilder.toString();
        Logger.d(result);
        result = EncryptionTool.MD5(result);
        return result;
    }

    //只有timestamp为url参数的签名算法
    public static String getApiSign(long timestamp, Map<String, Object> postParams) {
        String result = null;
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("get_timestamp", timestamp);
            if (postParams != null && postParams.size() > 0) {
                for (String key : postParams.keySet()) {
                    if (!map.containsKey("post_" + key) && postParams.get(key) != null && !TextUtils.isEmpty(postParams.get(key).toString())) {
                        map.put("post_" + key, postParams.get(key).toString());
                    }

                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : map.keySet()) {
                stringBuilder.append(key + "=" + map.get(key) + "&");
            }
            stringBuilder.append("key=" + KEY_API_SIGN);
            result = stringBuilder.toString();
            result = EncryptionTool.MD5(result);
        } catch (Exception e) {
            return "";
        }
        return result;
    }

    //包含urlParams和postParams
    public static String getApiSign(Map<String, Object> urlParams, Map<String, Object> postParams) {
        Map<String, String> map = new TreeMap<>();

        if (urlParams != null && urlParams.size() > 0) {
            for (String key : urlParams.keySet()) {
                if (key != null && ("sign".equals(key.toLowerCase()) || TextUtils.isEmpty(urlParams.get(key).toString())))
                    continue;
                if (!map.containsKey("get_" + key)) {
                    map.put("get_" + key, urlParams.get(key).toString());
                }

            }
        }

        if (postParams != null && postParams.size() > 0) {
            for (String key : postParams.keySet()) {
                if (!map.containsKey("post_" + key) && postParams.get(key) != null && !TextUtils.isEmpty(postParams.get(key).toString())) {
                    map.put("post_" + key, postParams.get(key).toString());
                }

            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            stringBuilder.append(key + "=" + map.get(key) + "&");
        }
        stringBuilder.append("key=" + KEY_API_SIGN);
        String result = stringBuilder.toString();
        Logger.d(result);
        result = EncryptionTool.MD5(result);
        return result;
    }

    /**
     * 获得当前应用的版本号名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获得当前应用的版本号数字
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            verCode = 0;
        }
        return verCode;
    }

    /**
     * 获取设备IMEI号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        return IMEI;
    }

    /**
     * 安装应用
     *
     * @param context
     * @param filePath 文件路径
     */
    public static void installApp(Context context, String filePath) {
        Logger.d(filePath);
        File _file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", _file);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            fileUri = Uri.fromFile(_file);
        }
        Logger.d(fileUri);
        intent.setDataAndType(fileUri,
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    public static void hideIME(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }


    /**
     * 通用正则表达式调用方法
     *
     * @param Content    需要检查的字符串
     * @param PatternStr 正则表达式
     * @return
     */
    public static boolean Compile(String Content, String PatternStr) {
        Pattern p = Pattern.compile(PatternStr);
        Matcher m = p.matcher(Content);
        return m.matches();
    }

    /**
     * 结合多种方式计算设备唯一码
     *
     * @param context
     * @return
     */
    public static String getCombinedDeviceID(Context context) {
        if (!TextUtils.isEmpty(DeviceID)) return DeviceID;
        //IMEI
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String m_szImei = null;
        try {
            m_szImei = TelephonyMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            m_szImei = "";
        }

        //Pseudo-Unique ID
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits

        //Android ID
        String m_szAndroidID = null;
        try {
            m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            m_szAndroidID = "";
        }

        //WIFI MAC Address string

//        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        String m_szWLANMAC = getMacAddr();
        //BT MAC Address string
        String m_szBTMAC = Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");

        String m_szLongID = m_szImei + m_szDevIDShort
                + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
        //compute md5
        DeviceID = EncryptionTool.MD5(m_szLongID).toLowerCase();
        return DeviceID;
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 判断是否是合法手机号
     *
     * @param name
     * @return
     */
    public static boolean isAccountValid(String name) {
        Pattern pattern = Pattern.compile("^(13[0-9]|14[5|7]|15\\d|17[6|7]|18[\\d])\\d{8}$");
        return pattern.matcher(name).matches();
    }


    /**
     * 判断密码是否大于6位
     *
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        return password.length() > 6;
    }


    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }


    /**
     * hl 抛出异常
     *
     * @param context
     * @param throwable
     */
    public static void exceptionHandling(Context context, Throwable throwable) {

        try {
            if (throwable instanceof SocketTimeoutException) {
                CommonUitls.showSweetDialog1(context, "连接超时", dialog -> {
                    dialog.dismiss();
                });
            } else if (throwable instanceof ConnectException) {
                CommonUitls.showSweetDialog1(context, "无法连接到服务器，请检查连接", dialog -> {
                    dialog.dismiss();
                });
            } else if (throwable instanceof RuntimeException) {
                CommonUitls.showSweetDialog1(context, "发生错误:" + throwable.getMessage(), dialog -> {
                    dialog.dismiss();
                });
            }
        } catch (Exception e) {

        }
    }

    /**
     * hl 第三方提示框
     */
    public static void showSweetAlertDialog(Context context, String title, String content) {
        try {
            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText(title);
            dialog.setContentText(content);
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {

        }

    }

    /**
     * hl 第三方提示框
     */
    public static SweetAlertDialog showSweetDialog(Context context, String content, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {
            if (context == null) return null;

            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelText("取消");
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * hl 第三方提示框
     */
    public static SweetAlertDialog showSweetDialog(Context context, String content,
                                                   SweetAlertDialog.OnSweetClickListener confirmListener,
                                                   SweetAlertDialog.OnSweetClickListener cancelListener) {

        try {

            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelText("取消");
            dialog.setCancelClickListener(cancelListener);
            dialog.setCancelable(true);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }

        return null;

    }

    /**
     * hl 第三方提示框
     */
    public static SweetAlertDialog showSweetDialog(Context context, String content, String confirmText, String cancelText, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {

            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示:");
            dialog.setContentText(content);
            dialog.setConfirmText(confirmText);
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelText(cancelText);
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }
        return null;

    }

    public static SweetAlertDialog showSweetDialog(Context context, String content) {
        try {
            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }

        return null;

    }


    public static SweetAlertDialog showSweetDialog1(Context context, String content, SweetAlertDialog.OnSweetClickListener confirmListener) {
        try {
            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelable(true);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }
        return null;
    }


    public static SweetAlertDialog showSweetDialog2(Context context, String content, SweetAlertDialog.OnSweetClickListener confirmListener) {
        try {
            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelable(false);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }

        return null;
    }

    public static SweetAlertDialog showSweetDialogOffline(Context context, String content, SweetAlertDialog.OnSweetClickListener cancelListener, SweetAlertDialog.OnSweetClickListener confirmListener) {
        try {
            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setContentText(content);
            dialog.setTitleText("下线通知");
            dialog.setCancelText("退出应用");
            dialog.setConfirmText("重新登陆");
            dialog.setConfirmClickListener(confirmListener);//点击确定
            dialog.setCancelClickListener(cancelListener);//点击取消
            dialog.setCancelable(false);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }

        return null;
    }


    /**
     * hl第三方提示框加载
     */
    public static SweetAlertDialog showLoadSweetAlertDialog(SweetAlertDialog mSweetAlertDialog) {

        try {

            mSweetAlertDialog.setTitleText("正在努力上传");
            mSweetAlertDialog.show();

            return mSweetAlertDialog;
        } catch (Exception e) {

        }

        return null;
    }


    /**
     * hl第三方提示框加载
     */
    public static SweetAlertDialog showLoadSweetAlertDialog2(Activity mActivity) {

        try {
            SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(mActivity, SweetAlertDialog.PROGRESS_TYPE);

            mSweetAlertDialog.setTitleText("正在加载数据");
            mSweetAlertDialog.setCancelable(false);

            return mSweetAlertDialog;
        } catch (Exception e) {

        }

        return null;
    }


    /**
     * hl第三方提示框加载
     */
    public static SweetAlertDialog showLoadSweetAlertDialog3(Activity mActivity) {

        try {
            SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(mActivity, SweetAlertDialog.PROGRESS_TYPE);

            mSweetAlertDialog.setTitleText("正在导入数据，需要一定时间，请耐心等待");
            mSweetAlertDialog.setCancelable(false);

            return mSweetAlertDialog;
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * 显示隐藏密码
     *
     * @param editText
     * @param imageButton
     */
    public static void setPasHint(EditText editText, ImageButton imageButton) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageButton.setImageResource(R.mipmap.hide2x);//设置图片
        } else {
            //选择状态 显示明文--设置为可见的密码
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageButton.setImageResource(R.mipmap.show2x);//设置图片
        }

        editText.setSelection(editText.getText().length());//光标移动到文本框末尾
    }

    public static void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            button.setProgress(value);
        });
        widthAnimation.start();
    }

    public static void simulateErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            button.setProgress(value);
            if (value == 99) {
                button.setProgress(-1);
            }
        });
        widthAnimation.start();
    }

    /**
     * Gps转化为安捷格式
     *
     * @param gpsLocationValue
     * @return
     */
    public static String GPS2AjLocation(double gpsLocationValue) {
        int du = (int) gpsLocationValue;
        double temp = (gpsLocationValue - du) * 60;//需要转换的部分（分）
        int fen = (int) temp;
        temp = (temp - fen) * 60;//秒
        return doubleformat(du + (double) fen / 100 + temp / 10000, 6);
    }

    /**
     * 安捷格式转经纬度
     *
     * @param ajLocationValue
     * @return
     */
    public static double Aj2GPSLocation(double ajLocationValue) {
        int du = (int) ajLocationValue;
        double temp = (ajLocationValue - du) * 100;//需要转换的部分（分）
        int fen = (int) temp;
        temp = (temp - fen) * 100;//秒
        return du + (double) fen / 60 + temp / 3600;
    }

    public static boolean isAjLocation(double ajLocationValue) {
        String value = String.valueOf(ajLocationValue);
        if (value.length() - value.indexOf(".") - 1 > 6) return false;
        int temp = ((int) (ajLocationValue * 100)) % 100;
        if (temp >= 0 && temp < 60) {
            temp = ((int) (ajLocationValue * 10000)) % 100;
            if (temp >= 0 && temp < 60) {
                return true;
            }
        }
        return false;

    }

    public static String doubleformat(double d, int cout) {
        NumberFormat nf = NumberFormat.getNumberInstance();


        // 保留两位小数
        nf.setMaximumFractionDigits(cout);


        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);


        return nf.format(d);
    }


    /**
     * 初始化微信支付回调引用
     */
    private void initWxPayListenerListRef() {
        if (this.onWxPayListenerList == null) {
            synchronized (CommonUitls.class) {
                this.onWxPayListenerList = new ArrayList<>();
            }
        }
        //清理为空的引用
        ListIterator<OnWxPayListener> iterator = onWxPayListenerList.listIterator();
        while (iterator.hasNext()) {
            OnWxPayListener ref = iterator.next();
            if (ref == null)
                iterator.remove();
        }
    }


    /**
     * 添加微信支付回调
     */
    public void addOnWxPayListener(OnWxPayListener onWxPayListener) {
        initWxPayListenerListRef();
        synchronized (this) {
            this.onWxPayListenerList.add(onWxPayListener);
        }
    }

    /**
     * 移除微信支付回调
     */
    public void removeOnWxPayListener(OnWxPayListener onWxPayListener) {
        initWxPayListenerListRef();
        synchronized (this) {
            this.onWxPayListenerList.remove(onWxPayListener);
        }
    }

    /**
     * * 微信支付回调监听
     *
     * @param mContext
     * @param tag      :1  微信余额充值  2,订单微信充值
     * @return
     */

    public static CommonUitls.OnWxPayListener onWxPayListenerWeakReference(Activity mContext, int tag) {

        return wxPayReturnCode -> {
            SweetAlertDialog dialog;
            switch (wxPayReturnCode) {
                case ERR_OK:
                    playCompleteJump(mContext, tag);
                    break;
                case ERR_USER_CANCEL:
                    dialog = CommonUitls.showSweetDialog(mContext, "支付取消");
                    break;
                default:
                    dialog = CommonUitls.showSweetDialog(mContext, "支付错误");
            }
        };
    }

    /**
     * 微信支付完成跳转
     *
     * @param tag :1  微信余额充值  2,订单微信充值
     */
    private static SweetAlertDialog playCompleteJump(Activity mContext, int tag) {

        switch (tag) {
            case 1://余额充值
                OrderPlayActivity.playResultsTag = 1;
                //发布事件（刷新数据）
                EventBus.getDefault().post(EventBusService.INFO_REFRESH);
                return CommonUitls.showSweetDialog2(mContext, "支付成功", dialog -> {
                    dialog.dismiss();
                    mContext.finish();
                });

            default:
                OrderPlayActivity.playResultsTag = 1;
                //发布事件（订单列表刷新数据）
                EventBus.getDefault().post(EventBusService.ORDER_REFRESH);
                return CommonUitls.showSweetDialog2(mContext, "支付成功", dialog -> {
                    dialog.dismiss();
                    mContext.finish();
                });
        }
    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 触发微信回调
     */
    public void onWxPay(Context context, int wxPayReturnCode) {
        if (context instanceof IWXAPIEventHandler) {
            if (this.onWxPayListenerList == null || onWxPayListenerList.size() == 0) return;
            for (OnWxPayListener ref : this.onWxPayListenerList) {
                if (ref != null) {
                    ref.onPayFinished(wxPayReturnCode);
                }
            }
        } else {
            Log.e("ERROR", "onWxPay called error");
        }
    }


    public interface OnWxPayListener {
        int ERR_OK = 0;
        int ERR_COMM = -1;
        int ERR_USER_CANCEL = -2;
        int ERR_SENT_FAILED = -3;
        int ERR_AUTH_DENIED = -4;
        int ERR_UNSUPPORT = -5;
        int ERR_BAN = -6;

        /**
         *
         */
        void onPayFinished(int wxPayReturnCode);
    }


    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }


    /**
     * 保留三位小数
     */
    public static String strThree(double num) {
        return new DecimalFormat("0.000").format(num);
    }


    /**
     * 保留两位小数
     */
    public static String strTwo(double num) {
        return new DecimalFormat("0.00").format(num);
    }


    /**
     * 方法描述：判断某一应用是否正在运行
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法描述：判断某一Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service的全路径： 包名 + service的类名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
            if (runningServiceInfos.size() <= 0) {
                return false;
            }
            for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
                if (serviceInfo.service.getClassName().equals(serviceName)) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }
    }
}




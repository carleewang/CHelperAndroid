package com.cpigeon.cpigeonhelper.commonstandard.view.activity;

/**
 * 基类View
 * Created by Administrator on 2017/4/5.
 */

public interface IView {
    enum TipType {
        View,
        ViewSuccess,
        ViewError,
        Dialog,
        DialogSuccess,
        DialogError,
        LoadingShow,
        LoadingHide,
        ToastLong,
        ToastShort,
        SnackbarShort,
        SnackbarLong
    }

    /**
     * 检查是否登录
     *
     * @return true:已登录；false：未登录
     */
    boolean checkLogin();

    /**
     * 显示提示(错误、正确、加载中)
     *
     * @param tip     提示内容
     * @param tipType 提示类型
     */
    boolean showTips(String tip, TipType tipType);

    /**
     * 显示提示(错误、正确、加载中)
     *
     * @param tip     提示内容
     * @param tipType 提示类型
     * @param tag     标签（识别用）
     * @return
     */
    boolean showTips(String tip, TipType tipType, int tag);

    void getErrorNews(String str);//获取错误消息


    void getThrowable(Throwable throwable);//抛出异常


}
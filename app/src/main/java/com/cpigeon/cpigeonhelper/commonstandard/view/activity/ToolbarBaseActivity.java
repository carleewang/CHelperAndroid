package com.cpigeon.cpigeonhelper.commonstandard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;
import com.cpigeon.cpigeonhelper.utils.throwable.ThrowableUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 带有Toolbar的Activity
 * Created by Administrator on 2017/5/26.
 */

public abstract class ToolbarBaseActivity extends RxAppCompatActivity {

    protected Toolbar toolbar;
    private FrameLayout viewContent;
    protected TextView tvTitle;
    private Unbinder mUnbinder;
    public Context mContext;
    public WeakReference<AppCompatActivity> mWeakReference;
    public int mColor;
    private OnClickListener onClickListenerTopLeft, onClickListenerTopRight;
    private int menuResId;//右边图标
    private int menuLeftResId;//边图标
    private String menuStr;//名称
    protected String TAG = getClass().getSimpleName();

    protected SweetAlertDialog mLoadDataDialog;//数据加载转圈提示框
    protected SweetAlertDialog mLoadDataDialog2;//数据加载转圈提示框
    protected SweetAlertDialog errSweetAlertDialog;//异常dialog提示

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        swipeBack();
        setContentView(R.layout.activity_base_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewContent = (FrameLayout) findViewById(R.id.viewContent);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        //设置mContext
        mContext = MyApplication.getInstance();
        //初始化设置StatusBar
        setStatusBar();
        //初始化ToolBar
        setSupportActionBar(toolbar);

        //设置不显示自带的title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater.from(ToolbarBaseActivity.this).inflate(getContentView(), viewContent);

        //初始化数据加载dialog
        mLoadDataDialog = CommonUitls.showLoadSweetAlertDialog2(this);

        mLoadDataDialog2 = CommonUitls.showLoadSweetAlertDialog3(this);

        mUnbinder = ButterKnife.bind(this);

        //
        initViews(savedInstanceState);
        LogUtil.print("onCreate");
    }

    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    protected void setTopLeftButton() {
        invalidateOptionsMenu();
        setTopLeftButton(R.drawable.ic_back, null);
    }

    protected void setTopLeftButton(int iconResId, OnClickListener onClickListener) {
        invalidateOptionsMenu();
        this.menuLeftResId = iconResId;
        toolbar.setNavigationIcon(iconResId);
        this.onClickListenerTopLeft = onClickListener;
    }

    protected void setTopRightButton(String menuStr, OnClickListener onClickListener) {
        invalidateOptionsMenu();
        this.onClickListenerTopRight = onClickListener;
        this.menuStr = menuStr;
    }

    protected void setTopRightButton(String menuStr) {
        invalidateOptionsMenu();
        this.menuStr = menuStr;
    }

    protected void setTopRightButton(String menuStr, int menuResId, OnClickListener onClickListener) {
        invalidateOptionsMenu();
        this.menuResId = menuResId;
        this.menuStr = menuStr;
        this.onClickListenerTopRight = onClickListener;
    }

    protected void setTopRightButton(int menuResId, OnClickListener onClickListener) {
        invalidateOptionsMenu();
        this.menuResId = menuResId;
        this.onClickListenerTopRight = onClickListener;
    }

    protected void setTopRightButton(int menuResId) {
        invalidateOptionsMenu();
        this.menuResId = menuResId;
    }


    protected void setTopLeftButton(int menuResId) {
        invalidateOptionsMenu();
        toolbar.setNavigationIcon(menuResId);
        this.menuLeftResId = menuResId;
    }

    protected int getTopRightButton() {
        return menuResId;
    }


    protected String getTopRightButtonStr() {
        return menuStr;
    }

    protected int getTopLeftButton() {
        return menuLeftResId;
    }


    protected abstract void swipeBack();

    protected abstract int getContentView();

    protected abstract void setStatusBar();

    protected abstract void initViews(Bundle savedInstanceState);

    public void loadData() {
    }

    /**
     * 显示Loading
     */
    public void showProgressBar() {
    }

    /**
     * 隐藏Loading
     */
    public void hideProgressBar() {
    }

    /**
     * 初始化Recyclerview
     */
    public void initRecyclerView() {
    }

    /**
     * 刷新
     */
    public void initRefreshLayout() {
    }

    /**
     * 完成数据加载过后的
     */
    public void finishTask() {

    }

    /**
     * 初始化之前
     */
    private void doBeforeSetcontentView() {
        mWeakReference = new WeakReference<AppCompatActivity>(this);
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(mWeakReference);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        AppManager.getAppManager().removeActivity(mWeakReference);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onClickListenerTopLeft.onClick();
        } else if (item.getItemId() == R.id.menu_function) {
            onClickListenerTopRight.onClick();
        }

        return true; // true 告诉系统我们自己处理了点击事件
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuResId != 0 || !TextUtils.isEmpty(menuStr)) {
            getMenuInflater().inflate(R.menu.menu_activity_base_toolbar, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuResId != 0) {
            menu.findItem(R.id.menu_function).setIcon(menuResId);
        }
        if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(R.id.menu_function).setTitle(menuStr);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_left);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
    }

    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    protected boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom);
        }
        return false;
    }

    // 隐藏软键盘
    protected void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public interface OnClickListener {
        void onClick();
    }

    /**
     * 获取错误消息
     *
     * @param str
     */
    public void getErrorNews(String str) {
        try {
            if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
            if (mLoadDataDialog2.isShowing()) mLoadDataDialog2.dismiss();
            Log.d("myerr", "myerr: bas---" + getClass().getSimpleName() + str);
            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, str, this, dialog -> {
                dialog.dismiss();
            });//弹出提示
        } catch (Exception e) {

        }
    }

    /**
     * 异常相关
     *
     * @param throwable
     */
    public void getThrowable(Throwable throwable) {

        try {
            Log.d("mythr", "getThrowable: bas---" + getClass().getSimpleName() + "-----" + throwable.getLocalizedMessage());
            if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
            if (mLoadDataDialog2.isShowing()) mLoadDataDialog2.dismiss();
            String s = ThrowableUtil.exceptionHandling(getApplicationContext(), throwable);
//            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, s, this);
            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "网络异常，请检查网络连接", this);
        } catch (Exception e) {

        }
    }
}

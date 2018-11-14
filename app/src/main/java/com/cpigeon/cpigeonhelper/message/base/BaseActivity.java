package com.cpigeon.cpigeonhelper.message.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.idcard.utils.ToastUtil;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonTool;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/4/5.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements IView {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public Context mContext;
    private Unbinder mUnbinder;
    private WeakReference<AppCompatActivity> weakReference;
    protected SweetAlertDialog dialogPrompt;
    protected T mPresenter;


    protected Toolbar toolbar;
    //AppBarLayout appBarLayout;
    /**
     * 加载中--对话框
     */
    protected SweetAlertDialog mLoadingSweetAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setOnCreateBefore();
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();
        mPresenter = this.initPresenter();
        initView(savedInstanceState);

    }

    protected  void setOnCreateBefore(){

    }

    public void setToolbar() {
        //appBarLayout = findViewById(R.id.appbar);
        if (null != toolbar) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

    }

    //获取布局文件
    @LayoutRes
    public abstract int getLayoutId();

    public abstract T initPresenter();

    public abstract void initView(Bundle savedInstanceState);



    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 设置layout前配置
     */
    protected void doBeforeSetcontentView() {
        weakReference = new WeakReference<AppCompatActivity>(this);
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(weakReference);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null && !mPresenter.isDetached()) {
            mPresenter.onDestroy();
            mPresenter.detach();
        }

        CommonTool.hideIME(this);
        mUnbinder.unbind();
        AppManager.getAppManager().removeActivity(weakReference);
    }

    @Override
    public void finish() {
        if (mPresenter != null) mPresenter.detach();
        super.finish();
        //overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        //overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public boolean showTips(String tip, TipType tipType) {

        switch (tipType) {
            case Dialog:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText(getString(R.string.prompt))
                        .setContentText(tip)
                        .setConfirmText(getString(R.string.confirm)).show();
                return true;
            case DialogSuccess:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText("成功")
                        .setContentText(tip)
                        .setConfirmText(getString(R.string.confirm)).show();
                return true;
            case DialogError:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText("失败")
                        .setContentText(tip).
                        setConfirmText(getString(R.string.confirm)).show();
                return true;
            case View:
            case ViewSuccess:
            case ViewError:
                return false;
            case LoadingShow:
                if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                    mLoadingSweetAlertDialog.dismiss();
                mLoadingSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                mLoadingSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mLoadingSweetAlertDialog.setCancelable(true);
                mLoadingSweetAlertDialog.setTitleText(tip);
                mLoadingSweetAlertDialog.show();
                return true;
            case LoadingHide:
                if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                    mLoadingSweetAlertDialog.dismiss();
                return true;
            case ToastLong:
                ToastUtil.showToast(this, tip, Toast.LENGTH_LONG);
                return true;
            case ToastShort:
                ToastUtil.showToast(this, tip, Toast.LENGTH_SHORT);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        if (tag == 0)
            return showTips(tip, tipType);
        return false;
    }

    /**
     * 检查是否登录
     */
    @Override
    public boolean checkLogin() {
        return true;
    }

    protected void showLoading(){
        showTips("正在拼命加载", TipType.LoadingShow);
    }

    protected void hideLoading(){
        showTips("", TipType.LoadingHide);
    }

    public Activity getActivity() {
        return this;
    }

    protected void addItemDecorationLine(RecyclerView recyclerView){
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(recyclerView.getContext())
                .colorResId(R.color.line_color).size(1)
                .showLastDivider().build());
    }
}

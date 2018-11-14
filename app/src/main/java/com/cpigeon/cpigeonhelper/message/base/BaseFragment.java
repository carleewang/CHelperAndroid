package com.cpigeon.cpigeonhelper.message.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.utils.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class BaseFragment extends Fragment implements IView {
    private View parentView;

    private FragmentActivity activity;

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;

    private Unbinder bind;
    /**
     * 加载中--对话框
     */
    protected SweetAlertDialog mLoadingSweetAlertDialog;

    protected Toolbar toolbar;

    protected TextView toolbarTitle;

    protected final CompositeDisposable composite = new CompositeDisposable();

    protected SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = inflater.inflate(getLayoutResource(), container, false);
            activity = getSupportActivity();
        }
        ViewGroup parent = (ViewGroup) parentView.getParent();
        if (parent != null) {
            parent.removeView(parentView);
        }
        return parentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        if (toolbar==null){
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            if(toolbar != null){
                toolbar.setNavigationOnClickListener(v -> getActivity().finish());
            }
        }
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        if(refreshLayout != null){
            refreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
            refreshLayout.setEnabled(false);
        }
        finishCreateView(savedInstanceState);
    }

    public void setTitle(@StringRes int resId) {
        if (null != toolbar)
            toolbarTitle.setText(resId);
    }

    public void setTitle(String resId) {
        if (null != toolbar)
            toolbarTitle.setText(resId);
    }

    public abstract void finishCreateView(Bundle state);

    public Context getApplicationContext() {

        return this.activity == null
                ? (getActivity() == null ? null :
                getActivity().getApplicationContext())
                : this.activity.getApplicationContext();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {

        lazyLoad();
    }

    protected void lazyLoad() {}


    protected void onInvisible() {}


    protected void loadData() {}


    //获取布局文件
    @LayoutRes
    protected abstract int getLayoutResource();

    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isAvailable();
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if(getActivity() != null){
            SweetAlertDialog dialogPrompt;
            switch (tipType) {
                case Dialog:
                    dialogPrompt = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                    dialogPrompt.setCancelable(false);
                    dialogPrompt.setTitleText(getString(R.string.prompt))
                            .setContentText(tip)
                            .setConfirmText(getString(R.string.confirm)).show();
                    return true;
                case DialogSuccess:
                    dialogPrompt = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    dialogPrompt.setCancelable(false);
                    dialogPrompt.setTitleText("成功")
                            .setContentText(tip)
                            .setConfirmText(getString(R.string.confirm)).show();
                    return true;
                case DialogError:
                    dialogPrompt = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    dialogPrompt.setTitleText("失败")
                            .setContentText(tip)
                            .setConfirmText(getString(R.string.confirm)).show();
                    return true;
                case View:
                case ViewSuccess:
                case ViewError:
                    return false;
                /*case HINT:

                    return false;*/
                case LoadingShow:
                    if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                        mLoadingSweetAlertDialog.dismiss();
                    mLoadingSweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                    mLoadingSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    mLoadingSweetAlertDialog.setTitleText(tip);
                    mLoadingSweetAlertDialog.setCancelable(false);
                    mLoadingSweetAlertDialog.show();
                    return true;
                case LoadingHide:
                    if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                        mLoadingSweetAlertDialog.dismiss();
                    return true;
                case ToastLong:
                    Toast.makeText(getActivity(), tip, Toast.LENGTH_LONG).show();
                    return true;
                case ToastShort:
                    Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        }
        return false;

    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return tag == 0 && showTips(tip, tipType);
    }

    @Override
    public boolean checkLogin() {
        /*try {
            boolean res = (boolean) SharedPreferencesTool.Get(getActivity(), "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);
            res &= SharedPreferencesTool.Get(getActivity(), "userid", 0, SharedPreferencesTool.SP_FILE_LOGIN).equals(
                    Integer.valueOf(EncryptionTool.decryptAES(SharedPreferencesTool.Get(getActivity(), "token", "", SharedPreferencesTool.SP_FILE_LOGIN).toString()).split("\\|")[0]));
            return res;
        } catch (Exception e) {
            return false;
        }*/
        return true;
    }

    /*public Map<String, Object> getLoginUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", getString(R.string.user_name));
        map.put("touxiang", "");
        map.put("touxiangurl", "");
        map.put("nicheng", "");
        map.put("userid", 0);
        map.put("phone", "");
        return SharedPreferencesTool.Get(getActivity(), map, SharedPreferencesTool.SP_FILE_LOGIN);
    }*/


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
        composite.clear();
    }

    protected <T extends View> T findViewById(@NonNull View view, @IdRes int resId) {
        T t = null;
        if (view != null)
            t = (T) view.findViewById(resId);
        if (t == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(resId)
                    + " doesn't exist");
        }
        return t;
    }

    protected <T extends View> T findViewById(@IdRes int resId) {
        T t = null;
        if (getView() != null)
            t = (T) getView().findViewById(resId);
        if (t == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(resId)
                    + " doesn't exist");
        }
        return t;
    }

    public FragmentActivity getSupportActivity() {
        return super.getActivity();
    }

    protected void addItemDecorationLine(RecyclerView recyclerView){
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(recyclerView.getContext())
                .colorResId(R.color.line_color).size(1)
                .showLastDivider().build());
    }

    protected <T> void bindUi(Observable<T> observable, Consumer<? super T> onNext) {
        composite.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable -> {
                            ToastUtil.showLongToast(getActivity(), throwable.getMessage());
                        }
                ));
    }

    protected void showLoading(){
        if(refreshLayout != null){
            refreshLayout.setRefreshing(true);
        }else {
            showLoading("请稍后...");
        }
    }

    protected void showDialogLoading(){
        showLoading("请稍后...");
    }

    protected void showLoading(String message){
        showTips(message, TipType.LoadingShow);
    }

    protected void hideLoading(){

        showTips("", TipType.LoadingHide);

        if(refreshLayout != null){
            refreshLayout.setRefreshing(false);
        }
    }

    protected void finish(){
        getActivity().finish();
    }

    protected void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){
        if(refreshLayout != null){
            refreshLayout.setEnabled(true);
            refreshLayout.setOnRefreshListener(listener);
        }
    }


    // 隐藏软键盘
    protected void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    //edittext默认不显示软键盘，只有edittext被点击时，软键盘才弹出

    protected void hideSoftInput(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}

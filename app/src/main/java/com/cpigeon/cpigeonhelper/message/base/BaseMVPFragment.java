package com.cpigeon.cpigeonhelper.message.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.utils.DialogUtils;
import com.cpigeon.cpigeonhelper.utils.DpSpUtil;
import com.cpigeon.cpigeonhelper.utils.ToastUtil;
import com.cpigeon.cpigeonhelper.utils.http.RestErrorInfo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenshuai on 2017/4/15.
 */

public abstract class BaseMVPFragment<Pre extends BasePresenter> extends BaseFragment {

    protected Pre mPresenter;

    protected final CompositeDisposable composite = new CompositeDisposable();

    private SweetAlertDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = this.initPresenter();
        bindError();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract Pre initPresenter();

    protected void bindError(){
        if(mPresenter != null){
            bindData(mPresenter.getError(), o -> {
                RestErrorInfo error = (RestErrorInfo) o;
                if (error!=null) {
                    error(error.code,error.message);
                }
            });

            /**/
        }

    }

    protected void error(String message) {
        hideLoading();
        if(!TextUtils.isEmpty(message)) {
            if(errorDialog == null || !errorDialog.isShowing()){
                errorDialog = DialogUtils.createErrorDialog(getContext(), message);
            }
        }
    }

    public void error(int code, String error) {
        if (code == 2400||code==2401) {
            hideLoading();
            finish();
            return;
        }
        error(error);
    }

    /**
     * 是否可以释放Presenter与View 的绑定
     *
     * @return
     */
    protected abstract boolean isCanDettach();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isCanDettach() && mPresenter != null && mPresenter.isAttached())
            mPresenter.detach();
    }

    protected void addItemDecorationLine(RecyclerView recyclerView){
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(recyclerView.getContext())
                .colorResId(R.color.dividingline).size(DpSpUtil.dip2px(getActivity(),0.5f))
                .build());
    }

    public <T> void bindData(Observable<T> observable, Consumer<? super T> onNext) {
        composite.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,
                        throwable -> {
                                ToastUtil.showLongToast(getActivity(), throwable.getMessage());
                        }
                ));
    }

}

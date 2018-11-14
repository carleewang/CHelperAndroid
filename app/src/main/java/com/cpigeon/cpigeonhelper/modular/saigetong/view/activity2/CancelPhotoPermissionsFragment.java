package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.CancelPhotoPre;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2.CancelPhotoPermissionAdapter;
import com.cpigeon.cpigeonhelper.utils.DialogUtils;
import com.cpigeon.cpigeonhelper.utils.Lists;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class CancelPhotoPermissionsFragment extends BaseMVPFragment<CancelPhotoPre> {

    RecyclerView recyclerView;
    CancelPhotoPermissionAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_with_bottom_layout;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }

    @Override
    protected CancelPhotoPre initPresenter() {
        return new CancelPhotoPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        setTitle("授权列表");

        toolbar.getMenu().clear();
        toolbar.getMenu().add("").setIcon(R.mipmap.top_add).setOnMenuItemClickListener(item -> {

            IntentBuilder.Builder().startParentActivity(getActivity(), EmpowerPhotoFragment.class,
                    IntentBuilder.REQUEST_CODE_RELOAD);
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        findViewById(R.id.rl_btn).setOnClickListener(v -> {
            DialogUtils.createDialogWithLeft(getContext(), "是否撤销所有的授权？",sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
                showDialogLoading();
                mPresenter.cancelAllEmpower(aBoolean -> {
                    hideLoading();
                    DialogUtils.createHintDialog(getContext(),"撤销成功！");
                    adapter.setNewData(Lists.newArrayList());
                });
            });
        });

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CancelPhotoPermissionAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnCancelAuthListener(point -> {
            DialogUtils.createDialogWithLeft(getContext(), "是否要撤销权利", sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
                showDialogLoading();
                mPresenter.cancelUserId = adapter.getData().get(point).authuid;
                mPresenter.cancelEmpower(aBoolean -> {
                    hideLoading();
                    DialogUtils.createHintDialog(getContext(), "撤销授权成功");
                    adapter.remove(point);
                });
            });
        });

        bindData();

        setRefreshListener(() -> {
            bindData();
        });
    }

    private void bindData() {
        showLoading();
        mPresenter.getEmpowerUserList(list -> {
            hideLoading();
            adapter.setNewData(list);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentBuilder.REQUEST_CODE_RELOAD){
            if(data.hasExtra(IntentBuilder.KEY_DATA)){
                bindData();
            }
        }

    }
}

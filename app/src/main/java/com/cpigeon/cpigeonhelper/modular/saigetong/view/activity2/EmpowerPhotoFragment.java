package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.EmpowerPre;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2.EmpowerPhotoAdapter;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.DialogUtils;

/**
 * Created by Zhu TingYu on 2018/3/15.
 * 搜索用户
 */

public class EmpowerPhotoFragment extends BaseMVPFragment<EmpowerPre> {

    RecyclerView recyclerView;
    SearchEditText searchEditText;
    EmpowerPhotoAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recycler_with_search_layout;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }

    @Override
    protected EmpowerPre initPresenter() {
        return new EmpowerPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        setTitle("添加授权");

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EmpowerPhotoAdapter(mPresenter);
        adapter.setAddEmpowerClickListener(phone -> {
            mPresenter.toUserPhone = phone;
            DialogUtils.createDialogWithLeft(getContext(), "是否授权用户" + phone +"公棚赛鸽拍照", sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
                showDialogLoading();
                mPresenter.empowerPhoto(s -> {
                    hideLoading();
                    DialogUtils.createDialogWithLeft(getContext(), s, sweetAlertDialog1 -> {
                        sweetAlertDialog1.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra(IntentBuilder.KEY_DATA,"true");
                        getActivity().setResult(0,intent);
                        finish();
                    });
                });
            });

        });
        adapter.bindToRecyclerView(recyclerView);
        searchEditText = findViewById(R.id.search);
        searchEditText.setHint("请输入被授权的手机号码");
        searchEditText.setOnSearchClickListener((view, keyword) -> {
            showDialogLoading();
            mPresenter.phone = keyword;
            mPresenter.searchPhoto(data -> {
                hideLoading();
                if (data.isEmpty()) {
                    error("没有查询到该用户");
                } else {
                    adapter.setNewData(data);
                }
            });
        });

        searchEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = {new InputFilter.LengthFilter(11)};
        searchEditText.setFilters(filters);

        recyclerView.requestFocus();
    }
}

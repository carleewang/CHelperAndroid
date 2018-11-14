package com.cpigeon.cpigeonhelper.message.ui.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.MultiSelectEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.utils.Lists;

import java.util.ArrayList;


/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class SendMessageContactsListFragment extends BaseContactsListFragment {

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);
        setTitle("选择群组");

        bottomLinearLayout.setVisibility(View.GONE);
        btn.setVisibility(View.VISIBLE);
        btn.setText("确定");
        btn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(IntentBuilder.KEY_DATA, "12312313112312313512341233");
            getActivity().setResult(0, intent);
            finish();
        });

        adapter.setImgChooseVisible(true);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            adapter.setMultiSelectItem(adapter.getItem(position), position);
        });

    }

    @Override
    protected void bindData() {
        ArrayList<MultiSelectEntity> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            data.add(new MultiSelectEntity());
        }
        //adapter.setNewData(data);
        adapter.setImgChooseVisible(true);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

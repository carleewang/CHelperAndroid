package com.cpigeon.cpigeonhelper.message.ui.contacts;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.message.adapter.ContactsListAdapter;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;


/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public abstract class BaseContactsListFragment<Pre extends BasePresenter> extends BaseMVPFragment<Pre> {

    LinearLayout bottomLinearLayout;
    protected TextView btn;
    protected RecyclerView recyclerView;
    protected ContactsListAdapter adapter;
    protected AppCompatImageView icon;
    protected TextView title;


    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_with_button_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initView();
    }

    private void initView() {

        bottomLinearLayout = findViewById(R.id.ll1);
        btn = findViewById(R.id.text_btn);
        title = findViewById(R.id.title);
        icon = findViewById(R.id.icon);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecorationLine(recyclerView);
        adapter = new ContactsListAdapter();
        recyclerView.setAdapter(adapter);
        bindData();
    }

    protected abstract void bindData();


}

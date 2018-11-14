package com.cpigeon.cpigeonhelper.message.ui.contacts;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.event.ContactsEvent;
import com.cpigeon.cpigeonhelper.message.ui.contacts.presenter.TelephoneBookPre;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.RxUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class TelephoneBookFragment extends BaseContactsListFragment<TelephoneBookPre> {

    @Override
    protected TelephoneBookPre initPresenter() {
        return new TelephoneBookPre(this);
    }

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);
        EventBus.getDefault().register(this);
        setTitle("电话薄");

        icon.setBackgroundResource(R.mipmap.ic_contacts_add);
        title.setText("添加联系人");

        bottomLinearLayout.setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_TYPE, ContactsInfoFragment.TYPE_EDIT)
                    .startParentActivity(getActivity(), ContactsInfoFragment.class);
        });

        adapter.addHeaderView(initHeadView());

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_DATA,adapter.getItem(position))
                    .startParentActivity(getActivity(), ContactsListFragment.class);
        });
    }

    @Override
    protected void bindData() {
        showLoading();
        mPresenter.getContactsGroups(data -> {
            hideLoading();
            adapter.setNewData(data);
            adapter.setImgChooseVisible(false);
        });

    }

    private View initHeadView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_telephone_book_fagment_head_layout, recyclerView,false);
        findViewById(view, R.id.rl1).setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_TYPE, SelectContactsFragment.TYPE_PHONE_SELECT)
                    .startParentActivity(getActivity(), SelectContactsFragment.class);
        });

        findViewById(view, R.id.rl2).setOnClickListener(v -> {
            showAddMessageDialog();
        });
        return view;
    }

    private void showAddMessageDialog() {
        AlertDialog dialogPrompt = new AlertDialog.Builder(getContext()).create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_message_layout, null);

        TextView title = findViewById(view, R.id.title);
        EditText content = findViewById(view, R.id.content);

        TextView btnLeft = findViewById(view, R.id.btn_left);
        TextView btnRight = findViewById(view, R.id.btn_right);

        title.setText("新建群组");

        bindUi(RxUtils.textChanges(content), mPresenter.setGroupName());

        btnLeft.setOnClickListener(v -> {
            dialogPrompt.dismiss();
        });

        btnRight.setOnClickListener(v -> {

            if(mPresenter.groupName.length() < 3){
//                ToastUtil.showLongToast(getContext(),"分组名称长度不能小于3位");

                CommonUitls.showSweetDialog1(getActivity(),"分组名称长度不能小于3位", dialog -> {
                    dialog.dismiss();
                });
                return;
            }

            mPresenter.addContactsGroups(r ->{
                dialogPrompt.dismiss();
                if(r.status){
                    bindData();
                    showTips(r.msg, TipType.Dialog);
                }else {
                    error(r.msg);
                }
            });
        });

        dialogPrompt.setView(view);
        dialogPrompt.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ContactsEvent event){
        bindData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

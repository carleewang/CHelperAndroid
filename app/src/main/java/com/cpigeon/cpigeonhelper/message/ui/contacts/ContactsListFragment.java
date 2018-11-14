package com.cpigeon.cpigeonhelper.message.ui.contacts;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.ContactsGroupEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.adapter.ContactsInfoAdapter;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.message.event.ContactsEvent;
import com.cpigeon.cpigeonhelper.message.ui.contacts.presenter.ContactsListPre;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DialogUtils;
import com.cpigeon.cpigeonhelper.utils.RxUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class ContactsListFragment extends BaseMVPFragment<ContactsListPre> {

    RecyclerView recyclerView;
    SearchEditText searchEditText;

    ContactsInfoAdapter adapter;
    ContactsGroupEntity contactsGroupEntity;

    LinearLayout bottomLl;

    LinearLayout btnLeft;
    LinearLayout btnRight;

    AppCompatImageView iconLeft;
    TextView titleLeft;


    @Override
    protected ContactsListPre initPresenter() {
        return new ContactsListPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        contactsGroupEntity = mPresenter.contactsGroupEntity;
        setTitle(contactsGroupEntity.fzmc);
        initView();
    }

    private void initView() {
        bottomLl = findViewById(R.id.ll1);
        btnLeft = findViewById(R.id.ll_left);
        btnRight = findViewById(R.id.ll_right);


        iconLeft = findViewById(R.id.ic_left);
        titleLeft = findViewById(R.id.title_left);


        searchEditText = findViewById(R.id.widget_title_bar_search);
        bindUi(RxUtils.textChanges(searchEditText), mPresenter.setSearchString());
        searchEditText.clearFocus();
        hideSoftInput(searchEditText.getWindowToken());
        searchEditText.setOnSearchClickListener((view, keyword) -> {
            mPresenter.page = 1;
            bindData();
        });

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.requestFocus();
        addItemDecorationLine(recyclerView);
        adapter = new ContactsInfoAdapter();
        adapter.setOnLoadMoreListener(() -> {
            mPresenter.page++;
            mPresenter.getContactsInGroup(contactsEntities -> {
                if (contactsEntities.isEmpty()) {
                    adapter.setLoadMore(true);
                } else {
                    adapter.addData(contactsEntities);
                    adapter.setLoadMore(false);
                }
            });
        }, recyclerView);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_TITLE, contactsGroupEntity.fzmc)
                    .putExtra(IntentBuilder.KEY_DATA, adapter.getData().get(position))
                    .putExtra(IntentBuilder.KEY_TYPE, ContactsInfoFragment.TYPE_LOOK)
                    .startParentActivity(getActivity(), ContactsInfoFragment.class);
        });


        recyclerView.setAdapter(adapter);
        bindData();

        if (!contactsGroupEntity.isSystemGroup()) {
            bottomLl.setVisibility(View.VISIBLE);
            setToolbarChooseMenu();
            setLeftButton(false);
            btnRight.setOnClickListener(v -> {
                DialogUtils.createDialogWithLeft(getContext(), "删除号码分组会同时删除该分组下所有手机号码", sweetAlertDialog -> {
                    mPresenter.deleteGroup(r -> {
                        if (r.status) {
//                            ToastUtil.showLongToast(getContext(), r.msg);

                            EventBus.getDefault().post(new ContactsEvent());

                            CommonUitls.showSweetDialog2(getActivity(), r.msg, dialog -> {
                                dialog.dismiss();
                                finish();
                            });

                        } else {
                            error(r.msg);
                        }
                    });
                });
            });
        }
    }

    private void setToolbarChooseMenu() {
        toolbar.getMenu().clear();
        toolbar.getMenu().add("选择")
                .setOnMenuItemClickListener(item -> {
                    setToolbarCancelMenu();
                    adapter.setImgChooseVisible(true);
                    adapter.setOnItemClickListener((adapter1, view, position) -> {
                        adapter.setMultiSelectItem(adapter.getItem(position), position);
                    });
                    setLeftButton(true);
                    return false;
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }


    private void setToolbarCancelMenu() {
        toolbar.getMenu().clear();
        toolbar.getMenu().add("取消")
                .setOnMenuItemClickListener(item -> {
                    setToolbarChooseMenu();
                    adapter.setImgChooseVisible(false);
                    adapter.setOnItemClickListener((adapter1, view, position) -> {
                        IntentBuilder.Builder()
                                .putExtra(IntentBuilder.KEY_TITLE, contactsGroupEntity.fzmc)
                                .putExtra(IntentBuilder.KEY_DATA, adapter.getData().get(position))
                                .putExtra(IntentBuilder.KEY_TYPE, ContactsInfoFragment.TYPE_LOOK)
                                .startParentActivity(getActivity(), ContactsInfoFragment.class);
                    });
                    setLeftButton(false);
                    return false;
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void setLeftButton(boolean isChoose) {
        if (isChoose) {
            iconLeft.setBackgroundResource(R.drawable.ic_cotants_delete);
            titleLeft.setText("删除选中");
            btnLeft.setOnClickListener(v -> {
                DialogUtils.createDialogWithLeft(getContext(), "确定删除选中联系人"
                        , sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                        }
                        , sweetAlertDialog -> {
                            mPresenter.setSelectIds(adapter);
                            mPresenter.deleteContacts(r -> {
                                sweetAlertDialog.dismiss();
                                if (r.status) {
                                    adapter.deleteChoose();
                                    showTips(r.msg, TipType.Dialog);
                                    EventBus.getDefault().post(new ContactsEvent());
                                } else {
                                    error(r.msg);
                                }
                            });
                        });
            });
        } else {
            iconLeft.setBackgroundResource(R.drawable.ic_contacts_edit_name);
            titleLeft.setText("重新命名");
            btnLeft.setOnClickListener(v -> {
                showAddMessageDialog();
            });
        }
    }

    private void bindData() {
       /* ArrayList<String> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            data.add("");
        }*/
        showLoading();
        mPresenter.getContactsInGroup(contactsEntities -> {
            adapter.setNewData(contactsEntities);
            hideLoading();
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_seach_layout;
    }

    private void showAddMessageDialog() {
        AlertDialog dialogPrompt = new AlertDialog.Builder(getContext()).create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_message_layout, null);

        TextView title = findViewById(view, R.id.title);
        AppCompatEditText content = findViewById(view, R.id.content);

        TextView btnLeft = findViewById(view, R.id.btn_left);
        TextView btnRight = findViewById(view, R.id.btn_right);

        bindUi(RxUtils.textChanges(content), mPresenter.setGroupName());
        content.setText(contactsGroupEntity.fzmc);
        content.setSelection(contactsGroupEntity.fzmc.length());

        title.setText("重新命名");
        btnRight.setOnClickListener(v -> {
            mPresenter.modifyGroupName(r -> {
                dialogPrompt.dismiss();
                if (r.status) {
                    showTips(r.msg, TipType.Dialog);
                    setTitle(mPresenter.groupName);
                    EventBus.getDefault().post(new ContactsEvent());
                } else {
                    error(r.msg);
                }
            });
        });


        btnLeft.setOnClickListener(v -> {
            dialogPrompt.dismiss();
        });


        dialogPrompt.setView(view);
        dialogPrompt.show();

    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

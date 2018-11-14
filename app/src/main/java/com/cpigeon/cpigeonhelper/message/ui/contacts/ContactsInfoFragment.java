package com.cpigeon.cpigeonhelper.message.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.ContactsEntity;
import com.cpigeon.cpigeonhelper.entity.ContactsGroupEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.message.event.ContactsEvent;
import com.cpigeon.cpigeonhelper.message.ui.contacts.presenter.ContactsInfoPre;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.RxUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class ContactsInfoFragment extends BaseMVPFragment<ContactsInfoPre> {

    public static final int CODE_SELECTE = 0x123;

    public static final int TYPE_LOOK = 0;
    public static final int TYPE_EDIT = 1;

    EditText editText1;
    EditText editText2;
    EditText editText3;
    TextView editText4;

    TextView textView4;
    TextView play_phone;

    RelativeLayout relativeLayout;

    TextView btn;

    ContactsEntity contactsEntity;

    String groupName;

    int type;

    RelativeLayout rlSelectGroup;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragent_cantacts_info_layout;
    }

    @Override
    protected ContactsInfoPre initPresenter() {
        return new ContactsInfoPre(this.getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        type = getActivity().getIntent().getIntExtra(IntentBuilder.KEY_TYPE, 0);
        contactsEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        groupName = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TITLE);
        initView();
    }

    private void initView() {
        editText1 = findViewById(R.id.ed1);
        editText2 = findViewById(R.id.ed2);
        editText3 = findViewById(R.id.ed3);
        editText4 = findViewById(R.id.ed4);
        play_phone = findViewById(R.id.play_phone);

        textView4 = findViewById(R.id.text4);

        relativeLayout = findViewById(R.id.rl1);
        rlSelectGroup = findViewById(R.id.rl2);
        btn = findViewById(R.id.text_btn);


        if (TYPE_EDIT == type) {
            setTitle("添加联系人");
            bindUi(RxUtils.textChanges(editText1), mPresenter.setName());
            bindUi(RxUtils.textChanges(editText2), mPresenter.setPhoneNumer());
            bindUi(RxUtils.textChanges(editText3), mPresenter.setRemarks());

            rlSelectGroup.setOnClickListener(v -> {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_TYPE, SelectContactsFragment.TYPE_CONTACTS_ADD)
                        .startParentActivity(getActivity(), SelectContactsFragment.class, CODE_SELECTE);
            });


            btn.setOnClickListener(v -> {
                mPresenter.addContacts(r -> {
                    if (r.status) {
//                        ToastUtil.showLongToast(getContext(), r.msg);
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

        } else {

            setTitle("联系人详情");
            editText1.setFocusable(false);
            editText2.setFocusable(false);
            editText3.setFocusable(false);
            findViewById(R.id.icon).setVisibility(View.GONE);
            play_phone.setVisibility(View.VISIBLE);

            play_phone.setOnClickListener(v -> {
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactsEntity.sjhmbd));//跳转到拨号界面，同时传递电话号码
                startActivity(dialIntent);
            });


            if (contactsEntity != null) {

                editText1.setText(contactsEntity.xingming);
                editText2.setText(contactsEntity.sjhm);
                editText3.setText(contactsEntity.beizhu);
                editText4.setText(groupName);
            }

            textView4.setText("分组");

            relativeLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CODE_SELECTE == requestCode) {
            if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
                ContactsGroupEntity groupEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                editText4.setText(groupEntity.fzmc);
                mPresenter.groupId = groupEntity.fzid;
            }
        }
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

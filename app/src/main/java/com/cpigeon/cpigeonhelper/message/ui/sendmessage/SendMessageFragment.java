package com.cpigeon.cpigeonhelper.message.ui.sendmessage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.ContactsGroupEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.message.ui.common.CommonMessageFragment;
import com.cpigeon.cpigeonhelper.message.ui.contacts.SelectContactsFragment;
import com.cpigeon.cpigeonhelper.message.ui.home.GXTUserInfoEvent;
import com.cpigeon.cpigeonhelper.message.ui.home.PigeonHomePre;
import com.cpigeon.cpigeonhelper.message.ui.modifysign.ModifySignFragment;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.CreateMessageOrderFragment;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.cpigeon.cpigeonhelper.utils.StringUtil;
import com.cpigeon.cpigeonhelper.utils.StringValid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class SendMessageFragment extends BaseMVPFragment<SendMessagePre> {

    public static final int CODE_COMMON_MESSAGE = 0x123;
    public static final int CODE_CONTACTS_LIST = 0x234;

    EditText edPhoneNumbers;
    AppCompatImageView icContactsAdd;
    EditText edContent;
    AppCompatImageView icRight;
    RelativeLayout rl_add;
    TextView btnLeft;
    TextView btnRight;
    TextView btnModifySign;
    TextView contactsNumber;
    TextView tvSign;
    TextView tv_surplus_sms;//剩余短信条数
    String sign;
    ImageView cleanNumber;

    TextView tvContentCount;

    @Override
    protected SendMessagePre initPresenter() {
        return new SendMessagePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setTitle("发送短信");
        toolbar.getMenu().clear();
        toolbar.getMenu().add("充值短信")
                .setOnMenuItemClickListener(item -> {
                    IntentBuilder.Builder().startParentActivity(getActivity(), CreateMessageOrderFragment.class);
                    return true;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        EventBus.getDefault().register(this);
        initView();
        bindData();
    }

    private void bindData() {
        mPresenter.getPersonSignName(s -> {
            sign = "【" + s + "】";
            tvSign.setText(getString(R.string.string_sign_info, s));
        });
    }

    private void initView() {
        edPhoneNumbers = findViewById(R.id.phone_numbers);
        icContactsAdd = (AppCompatImageView) findViewById(R.id.ic_contacts_add);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        edContent = (EditText) findViewById(R.id.message_content);
        icRight = (AppCompatImageView) findViewById(R.id.ic_right);
        btnLeft = (TextView) findViewById(R.id.btn_left);
        btnRight = (TextView) findViewById(R.id.btn_right);
        btnModifySign = (TextView) findViewById(R.id.btn_modify_sign);
        tv_surplus_sms = (TextView) findViewById(R.id.tv_surplus_sms);//剩余短信条数
        contactsNumber = findViewById(R.id.number);
        tvSign = findViewById(R.id.text2);
        cleanNumber = findViewById(R.id.clean);
        tvContentCount = findViewById(R.id.content_count);

        tv_surplus_sms.setText("当前剩余短信条数：" + PigeonHomePre.userGXTEntity.syts + "条");

        bindData(RxUtils.textChanges(edContent), s -> {
            mPresenter.messageContent = StringUtil.removeAllSpace(s);
            tvContentCount.setText(getString(R.string.string_message_content_count, String.valueOf(s.length())));
        });

//        bindUi(RxUtils.click(tvPhoneNumbers), o -> {
//
//        });

        bindUi(RxUtils.click(rl_add), o -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_TYPE, SelectContactsFragment.TYPE_SEND_MESSAGE)
                    .startParentActivity(getActivity(), SelectContactsFragment.class, CODE_CONTACTS_LIST);
        });

        bindUi(RxUtils.click(icRight), o -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                    .startParentActivity(getActivity(), CommonMessageFragment.class, CODE_COMMON_MESSAGE);
        });

        bindUi(RxUtils.click(btnLeft), o -> {
            mPresenter.addCommonMessage(r -> {
                if (r.status) {
                    showTips(r.msg, TipType.Dialog);
                } else {
                    showTips(r.msg, TipType.DialogError);
                }
            });
        });

        bindUi(RxUtils.click(btnRight), o -> {
            showLoading();
            if (!StringValid.isStringValid(mPresenter.groupId)){
                mPresenter.phoneNumber = edPhoneNumbers.getText().toString();
            }
            mPresenter.sendMessage(r -> {
                hideLoading();
                if (r.status) {
//                    ToastUtil.showLongToast(MyApplication.getContext(),r.msg);
//                    finish();


                    mPresenter.cleanData();
                    edPhoneNumbers.setText("");
                    edContent.setText("");
                    contactsNumber.setVisibility(View.GONE);
                    getUserInfo();

                    EventBus.getDefault().post(new GXTUserInfoEvent());

                    CommonUitls.showSweetDialog2(getActivity(), r.msg, dialog -> {
                        dialog.dismiss();
//                        finish();
                    });

                } else {
                    showTips(r.msg, TipType.DialogError);
                }
            });
        });

        bindUi(RxUtils.click(btnModifySign), o -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), ModifySignFragment.class);
        });

        edPhoneNumbers.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        bindUi(RxUtils.click(cleanNumber), o -> {
            mPresenter.groupId = "";
            mPresenter.phoneNumber = "";
            contactsNumber.setVisibility(View.GONE);
            cleanNumber.setVisibility(View.GONE);
            edPhoneNumbers.setText("");
            edPhoneNumbers.setEnabled(true);
            edPhoneNumbers.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        });
    }

    private void getUserInfo() {
        mPresenter.getUserInfo(r -> {
            if (r.isStatus()) {
                tv_surplus_sms.setText("当前剩余短信条数：" + r.getData().syts + "条");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GXTUserInfoEvent event) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserInfo();
            }
        }, 600);    //延时1s执行

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_send_message_layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CODE_COMMON_MESSAGE == requestCode) {
            if (data != null && StringValid.isStringValid(data.getStringExtra(IntentBuilder.KEY_DATA))) {
                String content = data.getStringExtra(IntentBuilder.KEY_DATA);
                edContent.setText(content);
                edContent.setSelection(content.length());
            }
        } else if (CODE_CONTACTS_LIST == requestCode) {
            if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
                List<ContactsGroupEntity> groupEntities = data.getParcelableArrayListExtra(IntentBuilder.KEY_DATA);
                contactsNumber.setVisibility(View.VISIBLE);
                contactsNumber.setText(getString(R.string.string_text_select_contacts_number
                        , String.valueOf(mPresenter.getContactsCount(groupEntities))));

                mPresenter.setGroupIds(groupEntities);
                mPresenter.phoneNumber = "";

                edPhoneNumbers.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});

                mPresenter.getSendNumber(s -> {
                    edPhoneNumbers.setText(s.length() > 40 ? StringUtil.getCutString(s, 0, 40) : s);
                    cleanNumber.setVisibility(View.VISIBLE);
                    edPhoneNumbers.setEnabled(false);
                });

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

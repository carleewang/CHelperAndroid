package com.cpigeon.cpigeonhelper.message.ui.history;

import android.os.Bundle;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.MessageEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;

/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class MessageDetailsFragment extends BaseMVPFragment {

    TextView date;
    TextView number;
    TextView content;

    MessageEntity messageEntity;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_message_details_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setTitle("短信详情");

        messageEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);

        date = findViewById(R.id.date);
        number = findViewById(R.id.number);
        content = findViewById(R.id.content);

        bindDate();
    }

    private void bindDate() {
        if(messageEntity != null){
            date.setText(messageEntity.fssj);
            number.setText(getString(R.string.string_text_message_addressee_number, String.valueOf(messageEntity.fscount)));
            content.setText(messageEntity.dxnr);
        }
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

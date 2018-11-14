package com.cpigeon.cpigeonhelper.message.ui.order.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.GXTMessagePrice;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.presenter.MessageCreateOrderPre;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.RxUtils;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */

public class CreateMessageOrderFragment extends BaseMVPFragment<MessageCreateOrderPre> {

    AppCompatTextView tvExplain;
    AppCompatTextView tvPrice;
    AppCompatButton btnLook;
    AppCompatEditText edCount;

    TextView btn;


    List<AppCompatTextView> selectTvs;

    List<Integer> tvIds;
    GXTMessagePrice price = new GXTMessagePrice();


    @Override
    protected MessageCreateOrderPre initPresenter() {
        return new MessageCreateOrderPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initView();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_creat_message_order_layout;
    }

    private void initView() {

        setTitle("短信充值");

        toolbar.getMenu().add("充值记录")
                .setOnMenuItemClickListener(item -> {
                    IntentBuilder.Builder().startParentActivity(getActivity(), RechargeHistoryFragment.class);
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        tvIds = Lists.newArrayList(R.id.tv_select1, R.id.tv_select2, R.id.tv_select3
                , R.id.tv_select4, R.id.tv_select5);
        selectTvs = Lists.newArrayList();

        tvExplain = findViewById(R.id.tv_explain);
        tvPrice = findViewById(R.id.order_price);

        edCount = findViewById(R.id.ed_count);
        edCount.setEnabled(false);

        btn = findViewById(R.id.text_btn);

        btn.setOnClickListener(v -> {
            if (mPresenter.messageCount < 1) {
//                ToastUtil.showLongToast(getContext(), "请确认充值的数量");

                CommonUitls.showSweetDialog1(getActivity(), "请确认充值的数量", dialog -> {
                    dialog.dismiss();
                });
                return;
            }
            showLoading("正在创建订单");
            mPresenter.createGXTMessageOrder(r -> {
                hideLoading();
                if (r.status) {
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, r.data)
                            .startParentActivity(getActivity(), OrderPayFragment.class);
                    finish();
                } else {
                    error(r.msg);
                }
            });
        });


        for (int i = 0, len = tvIds.size(); i < len; i++) {
            AppCompatTextView textView = findViewById(tvIds.get(i));
            selectTvs.add(textView);
        }

        for (int i = 0, len = selectTvs.size(); i < len; i++) {
            int finalI = i;
            selectTvs.get(i).setOnClickListener(v -> {
                setTvExplainListener(finalI);
            });
        }


        mPresenter.getGXTMessagePrice(r -> {
            if (r.status) {
                price = r.data;
                tvExplain.setText(getString(R.string.string_text_create_message_order_explain, String.valueOf(price.money)));
                bindUi(RxUtils.textChanges(edCount), mPresenter.setMessageCount(integer -> {
                    mPresenter.price = price.money * (double) integer;
                    tvPrice.setText(String.valueOf(mPresenter.price) + "元");
                }));
            } else {
                error(r.msg);
            }
        });

    }

    private void setTvExplainListener(int position) {

        if (position == 4) {
            edCount.setEnabled(true);
            tvPrice.setText(String.valueOf(0.0) + "元");
            mPresenter.price = 0;
            mPresenter.messageCount = 0;
        } else edCount.setEnabled(false);


        for (int i = 0, len = selectTvs.size(); i < len; i++) {
            AppCompatTextView textView = selectTvs.get(i);
            if (position == i) {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_blue_hook);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(drawable, null, null, null);
                textView.setSelected(true);
            } else {
                textView.setCompoundDrawables(null, null, null, null);
                textView.setSelected(false);
            }
        }

        if (position != selectTvs.size() - 1) {
            mPresenter.messageCount = position + 1;
            mPresenter.price = price.money * (double) (position + 1);
            tvPrice.setText(String.valueOf(mPresenter.price) + "元");
        }
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

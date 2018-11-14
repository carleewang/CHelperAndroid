package com.cpigeon.cpigeonhelper.message.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.entity.UserGXTEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.adapter.PigeonMessageHomeAdapter;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.message.ui.BaseWebViewActivity;
import com.cpigeon.cpigeonhelper.message.ui.common.CommonMessageFragment;
import com.cpigeon.cpigeonhelper.message.ui.contacts.TelephoneBookFragment;
import com.cpigeon.cpigeonhelper.message.ui.history.MessageHistoryFragment;
import com.cpigeon.cpigeonhelper.message.ui.modifysign.ModifySignFragment;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.CreateMessageOrderFragment;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.OrderPayFragment;
import com.cpigeon.cpigeonhelper.message.ui.person.PersonInfoFragment;
import com.cpigeon.cpigeonhelper.message.ui.sendmessage.SendMessageFragment;
import com.cpigeon.cpigeonhelper.message.ui.userAgreement.UserAgreementActivity;
import com.cpigeon.cpigeonhelper.utils.DialogUtils;
import com.cpigeon.cpigeonhelper.utils.Lists;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Zhu TingYu on 2017/11/17.
 */

public class PigeonMessageHomeFragment extends BaseMVPFragment<PigeonHomePre> {

    private static final int CODE_AGREEMENT = 0x123;

    RecyclerView recyclerView;

    PigeonMessageHomeAdapter adapter;

    private List<String> titleList;
    UserGXTEntity userGXTEntity;
    SweetAlertDialog dialogAgreement;

    public static void startPigeonMessageHome(Activity activity) {
        IntentBuilder.Builder().startParentActivity(activity, PigeonMessageHomeFragment.class);
    }


    @Override
    public PigeonHomePre initPresenter() {
        return new PigeonHomePre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }


    private void initView() {
        toolbar.getMenu().clear();
        toolbar.getMenu().add("充值短信")
                .setOnMenuItemClickListener(item -> {
                    IntentBuilder.Builder().startParentActivity(getActivity(), CreateMessageOrderFragment.class);
                    return true;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new PigeonMessageHomeAdapter(getActivity(), titleList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (0 == position) {
                IntentBuilder.Builder().startParentActivity(getActivity(), SendMessageFragment.class);
            } else if (1 == position) {
                IntentBuilder.Builder().startParentActivity(getActivity(), TelephoneBookFragment.class);
            } else if (2 == position) {
                IntentBuilder.Builder().startParentActivity(getActivity(), CommonMessageFragment.class);
            } else if (3 == position) {
                IntentBuilder.Builder().startParentActivity(getActivity(), MessageHistoryFragment.class);
            } else if (4 == position) {
                IntentBuilder.Builder().startParentActivity(getActivity(), ModifySignFragment.class);
            } else if (5 == position) {
                //使用帮助
                IntentBuilder.Builder(getSupportActivity(), BaseWebViewActivity.class)
                        .putExtra(IntentBuilder.KEY_TITLE, "使用帮助")
                        .putExtra(IntentBuilder.KEY_DATA, ApiConstants.BASE_URL + getString(R.string.api_user_help))
                        .startActivity();
            } else if (6 == position) {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_LOOK)
                        .startParentActivity(getActivity(), PersonInfoFragment.class);
            } else if (7 == position) {
                //用户协议
                UserAgreementActivity.startActivity(getSupportActivity(), true);
            }
        });

    }

    @Override
    public void finishCreateView(Bundle state) {
        titleList = Lists.newArrayList("发送短信", "电话薄", "短语库", "发送记录"
                , "修改签名", "使用帮助", "个人信息", "用户协议");

        EventBus.getDefault().register(this);

        userGXTEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);


        setTitle("鸽信通");

        mPresenter.userId = AssociationData.getUserId();

        getUserData();

        /*if (userGXTEntity.tyxy == 0) { //为0是未同意协议
            DialogUtils.createDialogWithLeft(getActivity(), "你已经开通鸽信通，阅读并同意后即可使用", sweetAlertDialog -> {
                UserAgreementActivity.startActivity(getActivity(), false, CODE_AGREEMENT);
                sweetAlertDialog.dismiss();
            });
        } else {
            if (userGXTEntity.syts < 1000) {
                showTips(getString(R.string.message_pigeon_message_count_not_enough), TipType.Dialog);
            }
            initView();
        }*/

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_layout;
    }


    public void getUserData() {
        showLoading();
        mPresenter.getUserInfo(r -> {
            hideLoading();
            if (r.status) {
                userGXTEntity = r.data;
                if (userGXTEntity.tyxy == 0) { //为0是未同意协议
                    DialogUtils.createDialogWithLeft(getActivity(), "你已经开通鸽信通，阅读并同意后即可使用"
                            , sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                            , sweetAlertDialog -> {
                                dialogAgreement = sweetAlertDialog;
                                UserAgreementActivity.startActivity(getActivity(), false, CODE_AGREEMENT);
                            });
                } else {
                    if (userGXTEntity.syts < 1000) {
//                        showTips(getString(R.string.message_pigeon_message_count_not_enough), TipType.Dialog);
                        DialogUtils.createDialog(getContext(), getString(R.string.message_pigeon_message_count_not_enough), sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            IntentBuilder.Builder().startParentActivity(getActivity(), CreateMessageOrderFragment.class);
                        });
                    }
                    initView();
                }
            } else {
                if (r.errorCode == PigeonHomePre.STATE_NO_OPEN) {
                    DialogUtils.createDialogWithLeft(getContext()
                            , getString(R.string.message_not_open_pigeon_message)
                            , sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                            , sweetAlertDialog -> {
                                IntentBuilder.Builder()
                                        .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_UPLOAD_INFO)
                                        .startParentActivity(getActivity(), PersonInfoFragment.class);
                                sweetAlertDialog.dismiss();
                                finish();
                            });

                } else {
                    if (r.errorCode == PigeonHomePre.STATE_ID_CARD_NOT_NORMAL ||
                            r.errorCode == PigeonHomePre.STATE_PERSON_INFO_NOT_NORMAL) {

                        DialogUtils.createDialogWithLeft(getActivity(), r.msg
                                , sweetAlertDialog -> {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }
                                , sweetAlertDialog -> {
                                    IntentBuilder.Builder()
                                            .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_UPLOAD_INFO)
                                            .putExtra(PersonInfoFragment.TYPE_UPLOAD_INFO_HAVE_DATE, true)
                                            .putExtra(IntentBuilder.KEY_DATA, r.msg)
                                            .startParentActivity(getActivity(), PersonInfoFragment.class);
                                    sweetAlertDialog.dismiss();
                                    finish();
                                });

                    } else if (r.errorCode == PigeonHomePre.STATE_NOT_PAY) {

                        DialogUtils.createDialogWithLeft(getContext(), r.msg
                                , sweetAlertDialog -> {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }
                                , sweetAlertDialog -> {
                                    showLoading("正在创建订单");
                                    mPresenter.getGXTOrder(order -> {
                                        hideLoading();
                                        if (order.status) {
                                            IntentBuilder.Builder()
                                                    .putExtra(IntentBuilder.KEY_DATA, order.data)
                                                    .startParentActivity(getActivity(), OrderPayFragment.class);
                                            sweetAlertDialog.dismiss();
                                            finish();
                                        } else {
                                            error(order.msg);
                                        }

                                    });
                                });


                    } else {
                        DialogUtils.createDialog(getContext(), r.msg, sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            finish();
                        });
                    }

                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_AGREEMENT) {
            if (data != null) {
                initView();
                dialogAgreement.dismiss();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GXTUserInfoEvent event) {
        getUserData();
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

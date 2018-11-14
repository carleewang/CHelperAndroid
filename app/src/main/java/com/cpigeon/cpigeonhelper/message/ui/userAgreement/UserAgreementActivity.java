package com.cpigeon.cpigeonhelper.message.ui.userAgreement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.ui.BaseWebViewActivity;
import com.cpigeon.cpigeonhelper.utils.DialogUtils;


/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class UserAgreementActivity extends BaseWebViewActivity<UserAgreementPre> {

    boolean agreeState;

    public static void startActivity(Activity activity, boolean agreeState){
        IntentBuilder.Builder(activity, UserAgreementActivity.class)
                .putExtra(IntentBuilder.KEY_BOOLEAN, agreeState)
                .putExtra(IntentBuilder.KEY_TITLE, "用户协议")
                .putExtra(IntentBuilder.KEY_DATA, ApiConstants.BASE_URL
                        + "/APP/Protocol?type=gxt").startActivity();
    }

    public static void startActivity(Activity activity, boolean agreeState, int requestCode){
        IntentBuilder.Builder(activity, UserAgreementActivity.class)
                .putExtra(IntentBuilder.KEY_BOOLEAN, agreeState)
                .putExtra(IntentBuilder.KEY_TITLE, "用户协议")
                .putExtra(IntentBuilder.KEY_DATA, ApiConstants.BASE_URL
                        + "/APP/Protocol?type=gxt").startActivity(activity, requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_agreement_webview_layout;
    }

    @Override
    public UserAgreementPre initPresenter() {
        return new UserAgreementPre(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        agreeState = getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN,false);

        AppCompatCheckBox checkBox = (AppCompatCheckBox) findViewById(R.id.checkbox);
        TextView btn = (TextView) findViewById(R.id.text_btn);

        if(agreeState){
            checkBox.setVisibility(View.GONE);
            btn.setOnClickListener(v -> {
                finish();
            });
        }else {
            btn.setOnClickListener(v -> {
                if(checkBox.isChecked()){
                    mPresenter.setUserAgreement(apiResponse -> {
                        if(apiResponse.status){
                            DialogUtils.createDialog(this,"提示",apiResponse.msg,"确定", sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra(IntentBuilder.KEY_BOOLEAN, true);
                                getActivity().setResult(0,intent);
                                finish();
                            });
                        }else {
                            showTips(apiResponse.msg, TipType.DialogError);
                        }
                    });
                }else {
                    showTips("请点击同意", TipType.DialogError);
                }
            });
        }


    }
}

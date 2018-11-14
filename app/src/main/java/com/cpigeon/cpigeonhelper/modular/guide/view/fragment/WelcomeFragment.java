package com.cpigeon.cpigeonhelper.modular.guide.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.guide.model.bean.IsLoginAppBean;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 欢迎页fragment
 * Created by Administrator on 2018/2/5.
 */

public class WelcomeFragment extends BaseFragment {


    @BindView(R.id.img_welcome)
    ImageView imageView;
    @BindView(R.id.btn)
    TextView btn;
    private int tag;

    public WelcomeFragment() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_welcome;
    }

    @Override
    public void finishCreateView(Bundle state) {
        tag = getArguments().getInt("tag");

        switch (tag) {
            case 1:
                imageView.setImageResource(R.mipmap.guide1);
                break;
            case 2:
                imageView.setImageResource(R.mipmap.guide2);
                break;
            case 3:
                imageView.setImageResource(R.mipmap.guide3);
                break;
            case 4:
                imageView.setImageResource(R.mipmap.guide4);
                btn.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        RealmUtils.getInstance().insertIsLoginAppEntity(new IsLoginAppBean());

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}

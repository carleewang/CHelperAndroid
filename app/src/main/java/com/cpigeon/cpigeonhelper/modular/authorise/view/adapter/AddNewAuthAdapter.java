package com.cpigeon.cpigeonhelper.modular.authorise.view.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AuthRaceListPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTongs;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.List;

/**
 * 添加授权的适配器
 * Created by Administrator on 2017/9/21.
 */

public class AddNewAuthAdapter extends BaseQuickAdapter<AddAuthEntity, BaseViewHolder> {

    private ImageView imageView;//用户圆形头像
    private ImageButton imageButton;//添加授权按钮

    private AuthRaceListPresenter presenter;//控制层
    private GeYunTongs mGeYunTongs;
    private EditText mEditText;
    private Activity mActivity;

    public AddNewAuthAdapter(List<AddAuthEntity> data, AuthRaceListPresenter presenter, GeYunTongs mGeYunTongs, EditText mEditText, Activity mActivity) {
        super(R.layout.item_addauth_search_succeed, data);
        this.presenter = presenter;
        this.mGeYunTongs = mGeYunTongs;
        this.mEditText = mEditText;
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, AddAuthEntity item) {

        helper.setImageResource(R.id.imgbtn, R.mipmap.add_auth_img);

        helper.setText(R.id.add_auth_tv_userName, item.getAuthUserInfo().getName());//设置用户名称
        helper.setText(R.id.add_auth_tv_phone, item.getAuthUserInfo().getPhone());//设置用户电话

        imageView = helper.getView(R.id.add_it_img_userheads);//获取圆形头像
        imageButton = helper.getView(R.id.imgbtn);//添加授权按钮


        //设置圆形头像
        Glide.with(mContext).load(TextUtils.isEmpty(item.getAuthUserInfo().getHeadimgUrl()) ? null : item.getAuthUserInfo().getHeadimgUrl())
                .placeholder(R.mipmap.head)
                .error(R.mipmap.head)
//                .resizeDimen(R.dimen.image_width_headicon, R.dimen.image_height_headicon)
//                .config(Bitmap.Config.RGB_565)
//                .onlyScaleDown()
                .into(imageView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到需要接受授权的鸽运通比赛列表
                CommonUitls.showSweetDialog(mContext, String.valueOf("是否将比赛\"" + mGeYunTongs.getRaceName() + "\"授权给用户\n" + mEditText.getText().toString() + "监控？"), dialog -> {
                    dialog.dismiss();
                    //提交添加授权申请
                    if (mGeYunTongs.getId() != -1) {
                        if (mActivity.getIntent().getStringExtra("title").equals("变更授权")) {
                            presenter.rquestGYTRaceAuth(item.getAuthUserInfo().getUid(), mGeYunTongs.getId(), 1);
                        } else {
                            presenter.rquestGYTRaceAuth(item.getAuthUserInfo().getUid(), mGeYunTongs.getId(), 0);
                        }

                    }
                    dialog.dismiss();//隐藏提示框
                });
            }
        });
    }
}

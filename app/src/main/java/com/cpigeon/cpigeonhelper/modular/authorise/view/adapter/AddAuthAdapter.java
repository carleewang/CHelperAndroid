package com.cpigeon.cpigeonhelper.modular.authorise.view.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AddAuthPresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.activity.GYTAuthRaceListActivity;

import java.util.List;

/**
 * 添加授权的适配器
 * Created by Administrator on 2017/9/21.
 */

public class AddAuthAdapter extends BaseQuickAdapter<AddAuthEntity, BaseViewHolder> {

    private ImageView imageView;//用户圆形头像
    private ImageButton imageButton;//添加授权按钮

    private AddAuthPresenter presenter;//控制层

    public AddAuthAdapter(List<AddAuthEntity> data, AddAuthPresenter presenter) {
        super(R.layout.item_addauth_search_succeed, data);
        this.presenter = presenter;
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
                Intent intent = new Intent(mContext, GYTAuthRaceListActivity.class);
                intent.putExtra("auid", item.getAuthUserInfo().getUid());//授权用户id
                mContext.startActivity(intent);
            }
        });
    }
}

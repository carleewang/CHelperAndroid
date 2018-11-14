package com.cpigeon.cpigeonhelper.modular.authorise.view.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AuthHomeEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AuthHomePresenter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 授权主页列表适配器
 * Created by Administrator on 2017/9/20.
 */

public class AuthHomeAdapter extends BaseQuickAdapter<AuthHomeEntity, BaseViewHolder> {

    private ImageView imageView;
    private ImageButton imageButton;

    private AuthHomePresenter presenter;//授权列表的控制层

    public AuthHomeAdapter(List<AuthHomeEntity> data, AuthHomePresenter presenter) {
        super(R.layout.item_auth_home, data);
        this.presenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, AuthHomeEntity item) {


        imageView = helper.getView(R.id.it_img_userheads);//获取圆形头像
        imageButton = helper.getView(R.id.img_cancel_auth);//取消授权按钮
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("print", "onClick: 当前按钮被点击--》" + item.getRaceId());

                SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("取消授权");
                dialog.setContentText("是否取消用户" + item.getAuthUserInfo().getName() + "对比赛" + item.getRaceName() + "的授权");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d("print", "onClick: --->1");
                        //确定取消授权
                        dialog.dismiss();//隐藏提示框
                        presenter.getRemoveGYTRaceAuth(item.getRaceId());//提交取消授权申请
                    }
                });
                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d("print", "onClick: --->2");
                        //取消取消授权
                        dialog.dismiss();//隐藏提示框
                    }
                });
                dialog.setConfirmText("确定");
                dialog.setCancelText("取消");
                dialog.setCancelable(true);
                dialog.show();
            }
        });


        //设置圆形头像
        Glide.with(mContext).load(TextUtils.isEmpty(item.getAuthUserInfo().getHeadimgUrl()) ? null : item.getAuthUserInfo().getHeadimgUrl())
                .placeholder(R.mipmap.head)
                .error(R.mipmap.head)
//                .resizeDimen(R.dimen.image_width_headicon, R.dimen.image_height_headicon)
//                .config(Bitmap.Config.RGB_565)
//                .onlyScaleDown()
                .into(imageView);


        helper.setText(R.id.it_tv_userName, item.getAuthUserInfo().getName());//设置用户名称
        helper.setText(R.id.it_tv_userPhone, item.getAuthUserInfo().getPhone());//设置用户电话
        helper.setText(R.id.it_auth_center, "赛事：" + item.getRaceName());//设置赛事名称
    }
}

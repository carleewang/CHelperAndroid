package com.cpigeon.cpigeonhelper.modular.authorise.view.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AuthRaceListPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 授权比赛选择适配器
 * Created by Administrator on 2017/9/22.
 */

public class AuthRaceListAdapter extends BaseQuickAdapter<GeYunTong, BaseViewHolder> {

    private ImageView imageView;//比赛图片
    private LinearLayout linearLayout;//点击item布局
    private AuthRaceListPresenter presenter;//控制层
    private String addUserName;//添加用户名字
    private int auid;//授权用户id

    public AuthRaceListAdapter(List<GeYunTong> data, AuthRaceListPresenter presenter, String addUserName, int auid) {
        super(R.layout.item_geyuntong, data);
        this.presenter = presenter;
        this.addUserName = addUserName;
        this.auid = auid;//需要授权用户id
    }

    @Override
    protected void convert(BaseViewHolder helper, GeYunTong item) {

        imageView = helper.getView(R.id.iv_geyuntong_img);//获取圆形头像
        //设置圆形头像
        Glide.with(mContext).load(TextUtils.isEmpty(item.getRaceImage()) ? null : item.getRaceImage())
                .placeholder(R.mipmap.default_geyuntong)
                .error(R.mipmap.default_geyuntong)
//                .config(Bitmap.Config.RGB_565)

                .into(imageView);

        helper.getView(R.id.it_gyt_r_img).setVisibility(View.GONE);//隐藏item右边的img

        helper.setText(R.id.tv_geyuntong_name, item.getRaceName());//设置比赛图片
        helper.setText(R.id.tv_geyuntong_time, item.getCreateTime());//设置时间
        helper.setText(R.id.tv_geyuntong_place, item.getFlyingArea());//设置地点
        helper.setText(R.id.tv_geyuntong_status, item.getState());//设置监控状态


        //点击item开始弹出提示框（是否进行授权）
        linearLayout = helper.getView(R.id.item_gyt_ll);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("添加授权");
                dialog.setContentText("是否添加用户" + addUserName + "对比赛" + item.getRaceName() + "的授权");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d("print", "onClick: --->1");
                        //确定取消授权
                        //提交添加授权申请
                        if (auid != -1) {
                            presenter.rquestGYTRaceAuth(auid, item.getId(), 0);
                        }
                        dialog.dismiss();//隐藏提示框
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
    }
}

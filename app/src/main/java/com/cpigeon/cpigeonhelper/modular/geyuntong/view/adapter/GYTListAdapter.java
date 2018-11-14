package com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTongs;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTListPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.PigeonMonitorActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTListView;
import com.cpigeon.cpigeonhelper.ui.AlwaysMarqueeTextView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Administrator on 2017/9/27.
 */

public class GYTListAdapter extends BaseQuickAdapter<GeYunTong, BaseViewHolder> implements GYTListView {

    private Intent intent;

    private GYTListPresenter presenter;//鸽运通比赛列表控制层

    public GYTListAdapter(List<GeYunTong> data, GYTListPresenter presenter) {
        super(R.layout.item_geyuntong, data);
        this.presenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, GeYunTong item) {
        //StateCode【0：未开始监控的；1：正在监控中：2：监控结束 3：授权人的比赛】【默认无筛选】

        helper.setText(R.id.tv_geyuntong_zt, "");
        helper.setText(R.id.tv_geyuntong_status, "");
        ((TextView) helper.getView(R.id.tv_geyuntong_zt)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_end));
        ((TextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_end));
        helper.setText(R.id.tv_geyuntong_name, item.getRaceName());
        helper.setText(R.id.tv_geyuntong_time, item.getCreateTime());
        if (item.getFlyingArea().isEmpty()) {

            if (RealmUtils.getServiceType().equals("geyuntong")) {
                helper.setText(R.id.tv_geyuntong_place, "暂无司放地");
            } else {
                helper.setText(R.id.tv_geyuntong_place, "暂无训放地");
            }

        } else {
            helper.setText(R.id.tv_geyuntong_place, item.getFlyingArea());
        }

        helper.setText(R.id.tv_geyuntong_status, item.getState());

        Glide.with(mContext)
                .load(TextUtils.isEmpty(item.getRaceImage()) ? "1" : item.getRaceImage())
                .placeholder(R.mipmap.default_geyuntong)
                .error(R.mipmap.default_geyuntong)
//                .resize(mContext.getResources().getDimensionPixelSize(R.dimen.gyt_list_img_wh), mContext.getResources().getDimensionPixelSize(R.dimen.gyt_list_img_wh))
                .centerCrop()
                .into((ImageView) helper.getView(R.id.iv_geyuntong_img));


        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_end));

        switch (item.getStateCode()) {
            case 0://未开始
                ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_not_at_the));
                break;
            case 1://正在监控
                ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_under_way));
                break;
        }

        //该场比赛是否是别人授权过来的
        if (item.getRaceUserInfo() != null && item.getAuthUserInfo() != null) {
            if (item.getRaceUserInfo().getUid() != AssociationData.getUserId()) {//没有接受授权比赛状态，授权人的，被授权人不一样

                if (item.getAuthStatus() == 0) {//待授权状态  被授权方

                    if (item.getStateCode() == 0) {//未开始监控 被授权方
                        helper.setText(R.id.tv_geyuntong_status, item.getRaceUserInfo().getPhone() + "授权您监控比赛");
                        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                    } else if (item.getStateCode() == 1) {//监控中 被授权方
                        helper.setText(R.id.tv_geyuntong_zt, item.getRaceUserInfo().getPhone() + "授权");
                        ((TextView) helper.getView(R.id.tv_geyuntong_zt)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                        helper.setText(R.id.tv_geyuntong_status, "监控中");
                        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                    }

                } else if (item.getAuthStatus() == 1) {//接受比赛  被授权方
                    if (item.getStateCode() == 0) {//未开始监控
                        helper.setText(R.id.tv_geyuntong_status, item.getRaceUserInfo().getPhone() + "授权您监控比赛");
                        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                    } else if (item.getStateCode() == 1) {//监控中
                        helper.setText(R.id.tv_geyuntong_zt, item.getRaceUserInfo().getPhone() + "授权");
                        ((TextView) helper.getView(R.id.tv_geyuntong_zt)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_not_at_the));

                        helper.setText(R.id.tv_geyuntong_status, "监控中");
                        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_not_at_the));
                    }
                }
            } else if (item.getRaceUserInfo().getUid() == AssociationData.getUserId()) {
                if (item.getAuthStatus() == 0) {//待授权状态   授权方

                    if (item.getStateCode() == 0) {//未开始监控 授权方
                        helper.setText(R.id.tv_geyuntong_status, "您授权" + item.getAuthUserInfo().getPhone() + "监控比赛");
                        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                    } else if (item.getStateCode() == 1) {

                        helper.setText(R.id.tv_geyuntong_zt, item.getAuthUserInfo().getPhone() + "未接受");
                        ((TextView) helper.getView(R.id.tv_geyuntong_zt)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                        helper.setText(R.id.tv_geyuntong_status, "监控中");
                        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                    }
                } else if (item.getAuthStatus() == 1) {//接受授权状态   授权方
                    if (item.getStateCode() == 0) {//未开始监控 授权方
                        helper.setText(R.id.tv_geyuntong_zt, item.getAuthUserInfo().getPhone() + "接受");
                        ((TextView) helper.getView(R.id.tv_geyuntong_zt)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                        helper.setText(R.id.tv_geyuntong_status, "未开启");
                        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.colorRed));

                    } else if (item.getStateCode() == 1) {//开始监控   授权方
                        helper.setText(R.id.tv_geyuntong_zt, item.getAuthUserInfo().getPhone() + "接受");
                        ((TextView) helper.getView(R.id.tv_geyuntong_zt)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_not_at_the));

                        helper.setText(R.id.tv_geyuntong_status, "监控中");
                        ((AlwaysMarqueeTextView) helper.getView(R.id.tv_geyuntong_status)).setTextColor(mContext.getResources().getColor(R.color.color_gytlist_not_at_the));
                    } else if (item.getStateCode() == 2) {
                        helper.setText(R.id.tv_geyuntong_zt, item.getAuthUserInfo().getPhone() + "监控");

                        helper.setText(R.id.tv_geyuntong_status, "监控结束");
                    }
                }
            }
        } else {
            try {
                //自己开启的监控，自己结束
                if (item.getStateCode() == 2) {
                    helper.setText(R.id.tv_geyuntong_zt, item.getRaceUserInfo().getPhone() + "监控");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        helper.getView(R.id.rardView).setOnClickListener(new View.OnClickListener() {
            private GeYunTongs geYunTongs;

            @Override
            public void onClick(View v) {
                if (item.getRaceUserInfo() != null && item.getAuthUserInfo() != null) {
                    if (item.getAuthStatus() == 0 && item.getRaceUserInfo().getUid() == AssociationData.getUserId()) {
//                        CommonUitls.showSweetAlertDialog(mContext, "温馨提示", "正在等待用户接受授权");
//                        return;
                    }
                    Log.d("GYTListAdapter", "该比赛为授权比赛: 授权人" + item.getRaceUserInfo().getPhone() + "  被授权人" + item.getAuthUserInfo().getPhone());
                    Log.d("GYTListAdapter", "该比赛为授权比赛: 开启比赛的用户id" + item.getMuid());
                    Log.d("GYTListAdapter", "该比赛为授权比赛: 授权人" + item.getRaceUserInfo().getUid() + "  被授权人" + item.getAuthUserInfo().getUid());
                }

                intent = new Intent(mContext, PigeonMonitorActivity.class);

                //保存点击item的比赛id
                geYunTongs = new GeYunTongs();
                geYunTongs.setId(item.getId());//比赛id
                geYunTongs.setStateCode(item.getStateCode());//状态码
                geYunTongs.setmEndTime(item.getMEndTime());//结束时间
                geYunTongs.setmTime(item.getMTime());//监控启动时间
                geYunTongs.setLongitude(item.getLongitude());//经度
                geYunTongs.setLatitude(item.getLatitude());//纬度
                geYunTongs.setState(item.getState());//监控状态
                geYunTongs.setRaceName(item.getRaceName());//设置赛事名称
                geYunTongs.setFlyingArea(item.getFlyingArea());

                //该场比赛是否是别人授权过来的
                //比赛未开启
                if (item.getStateCode() != 2) {
                    if (item.getRaceUserInfo() != null && item.getAuthUserInfo() != null) {
                        if (item.getAuthStatus() == 0) {//没有接受授权比赛状态
                            if (AssociationData.getUserId() == item.getRaceUserInfo().getUid()) {
                                //授权人的比赛
                                Log.d(TAG, "onClick: //授权人的比赛");
                            } else {
                                //被授权人的比赛
                                authConfirm(item);
                                return;
                            }

                        } else if (item.getAuthStatus() == 1) {//接受授权比赛状态

                            if (AssociationData.getUserId() == item.getRaceUserInfo().getUid()) {
                                //授权人的比赛
                                geYunTongs.setStateCode(3);//状态码
                            } else {
                                //被授权人的比赛
                            }

                            if (item.getStateCode() == 0) {
                                //授权的比赛,未监控
                                geYunTongs.setStateCode(item.getStateCode());//状态码
                            } else if (item.getStateCode() == 1) {
                                //授权的比赛,监控中

                            } else if (item.getStateCode() == 2) {
                                geYunTongs.setStateCode(2);//状态码
                                //授权的比赛,监控结束
                            }
                        }
                    }

                    Log.d("sqjkl", "onClick: 2");
                    //比赛已开启
                    if (item.getRaceUserInfo() != null && item.getAuthUserInfo() != null) {

                        if (item.getMuid() == item.getRaceUserInfo().getUid() && item.getMuid() == AssociationData.getUserId()) {
                            //开启比赛的用户id == 授权人id  && 开启比赛的用户id == 当前用户的id
                            geYunTongs.setStateCode(item.getStateCode());//状态码
                        } else if (item.getMuid() == item.getRaceUserInfo().getUid() && item.getAuthUserInfo().getUid() == AssociationData.getUserId()) {
                            CommonUitls.showSweetAlertDialog(mContext, "温馨提示", "该场比赛正在被他人监控");
                            return;
                        } else if (item.getMuid() == item.getAuthUserInfo().getUid() && AssociationData.getUserId() == item.getRaceUserInfo().getUid()) {
                            //开始比赛的id  == 授权用户的id  &&当前用户的id  ！= 开启比赛的id
                            geYunTongs.setStateCode(3);//授权人查看比赛
                        }
                    }

                }

                if (item.getRaceUserInfo() != null && item.getAuthUserInfo() != null) {
                    if (item.getRaceUserInfo().getUid() != AssociationData.getUserId()) {//没有接受授权比赛状态，授权人的，被授权人不一样

                        if (item.getAuthStatus() == 0) {//待授权状态  被授权方

                            if (item.getStateCode() == 0) {//未开始监控

                            } else if (item.getStateCode() == 1) {//监控中

                            }

                        } else if (item.getAuthStatus() == 1) {//接受比赛  被授权方
                            if (item.getStateCode() == 0) {//未开始监控
                                Log.d("sqState", "onClick: " + 3);
                                geYunTongs.setStateSq(9);
                            } else if (item.getStateCode() == 1) {//监控中
                                Log.d("sqState", "onClick: " + 4);
                                geYunTongs.setStateSq(10);
                            }
                        }
                    } else if (item.getRaceUserInfo().getUid() == AssociationData.getUserId()) {
                        if (item.getAuthStatus() == 0) {//待授权状态   授权方

                            if (item.getStateCode() == 0) {//未开始监控
                                geYunTongs.setStateSq(5);
                            }

                        } else if (item.getAuthStatus() == 1) {//接受授权状态   授权方
                            if (item.getStateCode() == 0) {//未开始监控 授权方
                                Log.d("sqState", "onClick: " + 5);
                                geYunTongs.setStateSq(3);
                            } else if (item.getStateCode() == 1) {//开始监控   授权方
                                Log.d("sqState", "onClick: " + 6);
                                geYunTongs.setStateSq(4);
                            }
                        }
                    }
                } else {
                    //自己开启的监控，自己结束
                    if (item.getStateCode() == 0) {//未开始监控 授权方
                        Log.d("sqState", "onClick: " + 1);
                        geYunTongs.setStateSq(1);
                    } else if (item.getStateCode() == 1) {//开始监控   授权方
                        Log.d("sqState", "onClick: " + 2);
                        geYunTongs.setStateSq(2);
                    }
                }

                if (!RealmUtils.getInstance().existGYTBeanInfo()) {
                    RealmUtils.getInstance().insertGYTBeanInfo(geYunTongs);//添加用户数据
                } else {
                    RealmUtils.getInstance().deleteGYTBeanInfo();//删除数据
                    RealmUtils.getInstance().insertGYTBeanInfo(geYunTongs);//添加用户数据
                }
                mContext.startActivity(intent);
            }
        });
    }

    private void authConfirm(GeYunTong item) {

        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("授权确认");
        dialog.setContentText("是否接受" + item.getRaceUserInfo().getPhone() + "的比赛");
        dialog.setCancelText("拒绝");
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //拒绝授权的比赛
                presenter.getGYTAuthConfirm(item.getId(), 0);//ar接收或拒绝授权【0：拒绝；1：接受】
                dialog.cancel();
            }
        });
        dialog.setConfirmText("接受");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //接受授权的比赛
                presenter.getGYTAuthConfirm(item.getId(), 1);//ar接收或拒绝授权【0：拒绝；1：接受】
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();

    }

    /**
     * 接受，拒绝授权比赛请求成功后回调
     */
    @Override
    public void addPlaySuccess() {
//        Log.d("GYTListAdapter", "addPlaySuccess: 接受，拒绝授权请求成功");
        presenter.getGYTRaceList(10, 1, "", "", "");//开始获取数据
        this.notifyDataSetChanged();
    }

    @Override
    public void getGYTRaceList(ApiResponse<List<GeYunTong>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getGYTRaceLists(List<GeYunTong> geYunTongDatas) {
//        Log.d("GYTListAdapter", "getGYTRaceLists: 适配器里面获取数据");
    }

    @Override
    public void getReturnMsg(String msg) {

    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

package com.cpigeon.cpigeonhelper.modular.authorise.presenter;

import android.content.Context;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.daoimpl.AddAuthImpl;
import com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao.AddAuthView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 添加授权用户的控制层
 * Created by Administrator on 2017/9/21.
 */

public class AddAuthPresenter extends BasePresenter<AddAuthView, AddAuthImpl> {


    private Map<String, Object> params = new HashMap<>();
    private long timestamp;

    public AddAuthPresenter(AddAuthView mView) {
        super(mView);
    }

    @Override
    protected AddAuthImpl initDao() {
        return new AddAuthImpl();
    }


    /**
     * 通过手机号搜索授权用户（存在显示，不存在提示）
     *
     * @param phone
     */
    public void startRquestAddAuthData(String phone) {

        if (phone.isEmpty()) {
            mView.getErrorNews("输入手机号码不能为空");
            return;
        }

        mDao.requestAddAuthData(AssociationData.getUserToken(),
                phone);
        mDao.getServerAddAuthData = new IBaseDao.GetServerData<List<AddAuthEntity>>() {
            @Override
            public void getdata(ApiResponse<List<AddAuthEntity>> listApiResponse) {

//                Log.d(TAG, "getdata: 请求成功--错误码---》" + listApiResponse.getErrorCode());
                switch (listApiResponse.getErrorCode()) {
                    case 0://请求成功
                        if (listApiResponse.getData() == null) {
                            mView.getDataIsNull();//获取数据为空
                        } else if (listApiResponse.getData().size() > 0) {//请求数据成功有值后返回
                            mView.getAddAuthDatas(listApiResponse.getData());
                        }
                        break;
                    case 20001://手机号格式错误
                        mView.getErrorNews("手机号格式错误");
//                        Log.d(TAG, "getdata: 手机号格式错误");
                        break;
                    default:
                        mView.getErrorNews("获取数据失败，错误码--》" + listApiResponse.getErrorCode());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 短信通知用户
     */
    public void smsCallUser(EditText phone, Context mContext) {

        if (phone.getText().toString().isEmpty()) {
            mView.getErrorNews("输入内容不能为空");
            return;
        }


        //时间戳
        timestamp = System.currentTimeMillis() / 1000;

        params.clear();//清除集合中之前保存的数据
        params.put("uid", AssociationData.getUserId());//用户id
        params.put("tel", phone.getText().toString());//手机号码

        mDao.requestSMSCall(AssociationData.getUserToken(),
                params,//参数
                timestamp,//时间戳
                CommonUitls.getApiSign(timestamp, params));//签名检查
        mDao.getSMSData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> objApiResponse) {

//                Log.d(TAG, "getdata: 短信通知请求成功，错误码----》" + objApiResponse.getErrorCode());

                switch (objApiResponse.getErrorCode()) {
                    case 0://请求成功
                        //弹出提示框通知
                        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("短信通知");
                        dialog.setContentText(objApiResponse.getMsg());
                        dialog.setConfirmText("确定");
                        dialog.setCancelable(true);
                        dialog.show();

                        break;
                    default://其他错误
//                        Log.d(TAG, "getdata: 其他错误");
                        mView.getErrorNews("" + objApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

}

package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardNInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardPInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTOpenAndRenewEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl.XGTImpl;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.xgtactivity.ApplyProbationActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.XGTView;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OpenXGTServiceActivity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 训鸽通控制层
 * Created by Administrator on 2017/12/7.
 */

public class XGTPresenter extends BasePresenter<XGTView, XGTImpl> {

    public XGTPresenter(XGTView mView) {
        super(mView);
    }

    @Override
    protected XGTImpl initDao() {
        return new XGTImpl();
    }

    /**
     * 训鸽通申请试用
     */
    public void openXGT(EditText etName, IdCardPInfoEntity idCardInfoEntity, IdCardNInfoEntity idCardNInfoEntity) {


        try {
            Log.d("ssssssssa", "openXGT1: "+etName.getText().toString());
            Log.d("ssssssssa", "openXGT2: "+idCardInfoEntity.name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (idCardInfoEntity == null) {
            mView.getErrorNews("请上传身份证正面");
            return;
        }

        if (idCardNInfoEntity == null) {
            mView.getErrorNews("请上传身份证背面");
            return;
        }

        if (etName.getText().toString().isEmpty()) {
            mView.getErrorNews("请输入申请人名字");
            return;
        }

//        if (!etName.getText().toString().equals(idCardInfoEntity.name)) {
//            mView.getErrorNews("输入名字与身份证名字不符");
//            return;
//        }


        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("xingming", idCardInfoEntity.name);//身份证信息：姓名
        postParams.put("xingbie", idCardInfoEntity.sex);//身份证信息：性别
        postParams.put("minzu", idCardInfoEntity.nation);//身份证信息：民族
        postParams.put("zhuzhi", idCardInfoEntity.address);//身份证信息：住址
        postParams.put("haoma", idCardInfoEntity.id);//身份证信息：身份证号码
        postParams.put("qianfajiguan", idCardNInfoEntity.authority);//身份证信息：签发机关
        postParams.put("youxiaoqi", idCardNInfoEntity.valid_date);//身份证信息：有效期


        // 创建 RequestBody，用于封装构建RequestBody (身份证正面)
        RequestBody requestFileZm = RequestBody.create(MediaType.parse("image/jpeg"), new File(idCardInfoEntity.frontimage));
        MultipartBody.Part bodyZm = MultipartBody.Part.createFormData("sfzzm", idCardInfoEntity.frontimage, requestFileZm);

        // 创建 RequestBody，用于封装构建RequestBody (身份证背面)
        RequestBody requestFileFm = RequestBody.create(MediaType.parse("image/jpeg"), new File(idCardNInfoEntity.backimage));
        MultipartBody.Part bodyFm = MultipartBody.Part.createFormData("sfzbm", idCardNInfoEntity.backimage, requestFileFm);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("xingming", idCardInfoEntity.name)//身份证信息：姓名
                .addFormDataPart("xingbie", idCardInfoEntity.sex)//身份证信息：性别
                .addFormDataPart("minzu", idCardInfoEntity.nation)//身份证信息：民族
                .addFormDataPart("zhuzhi", idCardInfoEntity.address)//身份证信息：住址
                .addFormDataPart("haoma", idCardInfoEntity.id)//身份证信息：身份证号码
                .addFormDataPart("qianfajiguan", idCardNInfoEntity.authority)//身份证信息：签发机关
                .addFormDataPart("youxiaoqi", idCardNInfoEntity.valid_date)//身份证信息：有效期
                .addPart(bodyZm)//身份证正面
                .addPart(bodyFm);//身份证反面面

        RequestBody requestBody = builder.build();

        mDao.uploadIdCardInfo(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServerData = new IBaseDao.GetServerData() {
            @Override
            public void getdata(ApiResponse apiResponse) {
//                Log.d("xiaohl", "getdata: " + apiResponse.getErrorCode() + "    msg-->" + apiResponse.getData());
                mView.isUploadIdCardInfo(apiResponse);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 获取训鸽通信息
     */
    public void getXGTInfo() {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getXGTInfoData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getXGTEntity = new IBaseDao.GetServerData<XGTEntity>() {
            @Override
            public void getdata(ApiResponse<XGTEntity> apiResponse) {
//                Log.d(TAG, "getdata: 获取训鸽通信息cod--> " + apiResponse.getErrorCode() + "   msg-->" + apiResponse.getMsg());
                mView.isXGTInfo(apiResponse, apiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 训鸽通开通信息
     */
    public void getXTGOpenInfo() {

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id


        mDao.getXGTInfoOpenData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getXGTOpenAndRenewEntity = new IBaseDao.GetServerData<List<XGTOpenAndRenewEntity>>() {
            @Override
            public void getdata(ApiResponse<List<XGTOpenAndRenewEntity>> listApiResponse) {
//                Log.d(TAG, "getdata1: " + "训鸽通开通信息：" + listApiResponse.getMsg() + "  " + listApiResponse.getErrorCode());
                switch (listApiResponse.getErrorCode()) {
                    case 0:
                        mView.getXTGOpenAndRenewInfo(listApiResponse.getData(), null, null);
                        break;
                    default:
                        mView.getErrorNews("获取信息失败：" + listApiResponse.getMsg() + "  " + listApiResponse.getErrorCode());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 训鸽通续费信息
     */
    public void getXTGRenewInfo() {

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getXGTInfoRenewData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getXGTOpenAndRenewEntity = new IBaseDao.GetServerData<List<XGTOpenAndRenewEntity>>() {
            @Override
            public void getdata(ApiResponse<List<XGTOpenAndRenewEntity>> listApiResponse) {
//                Log.d(TAG, "getdata2: " + "训鸽通续费信息：" + listApiResponse.getMsg() + "  " + listApiResponse.getErrorCode());
                switch (listApiResponse.getErrorCode()) {
                    case 0:
                        mView.getXTGOpenAndRenewInfo(null, listApiResponse.getData(), null);
                        break;
                    default:
                        mView.getErrorNews("获取信息失败：" + listApiResponse.getMsg() + "  " + listApiResponse.getErrorCode());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 初始化训鸽通页面 (tab :1  训鸽通页面  tab ：2  个币兑换页面)
     */
    public static void initXTGView(XGTEntity myXGTEntity, Context mContext, View imgbtn3, int tag) {

//        Log.d("XGTPresenter", "initXTGView: " + myXGTEntity.getStatuscode());
        if (mContext == null) {
            return;
        }
        if (myXGTEntity != null) {
            if (myXGTEntity.getStatuscode() == 0) {
                //开通训鸽通
                if (tag == 1) {
                    ((ImageButton) imgbtn3).setImageResource(R.mipmap.xufeifw);
                }
                imgbtn3.setOnClickListener(view -> {
                            Intent intent = new Intent(mContext, OpenXGTServiceActivity.class);
                            mContext.startActivity(intent);
                        }
                );
            } else {
                startShiyong(myXGTEntity, mContext, imgbtn3, tag);    //跳转到申请试用页面
            }
        } else {
            startShiyong(myXGTEntity, mContext, imgbtn3, tag);    //跳转到申请试用页面
        }
    }

    //跳转到申请试用页面
    public static void startShiyong(XGTEntity myXGTEntity, Context mContext, View views, int tag) {
        //开通训鸽通
        if (tag == 1) {
            ((ImageButton) views).setImageResource(R.mipmap.shenqingsy);
        }
        views.setOnClickListener(view -> {
                    //申请试用
                    Intent intent = new Intent(mContext, ApplyProbationActivity.class);
                    intent.putExtra("XGTEntity", myXGTEntity);
                    mContext.startActivity(intent);
                }
        );
    }
}

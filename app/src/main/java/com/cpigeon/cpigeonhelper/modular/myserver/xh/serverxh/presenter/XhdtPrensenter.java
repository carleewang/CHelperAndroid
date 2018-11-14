package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter;

import android.widget.EditText;
import android.widget.LinearLayout;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.daoimpl.XhdtImpl;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameDtView;
import com.cpigeon.cpigeonhelper.ui.imgupload.AddImgView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 协会动态控制层
 * Created by Administrator on 2017/12/14.
 */
public class XhdtPrensenter extends BasePresenter<GameDtView, XhdtImpl> {

    public XhdtPrensenter(GameDtView mView) {
        super(mView);
    }

    @Override
    protected XhdtImpl initDao() {
        return new XhdtImpl();
    }

    /**
     * 获取协会动态列表
     */
    public void getXhdtList() {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getDongtaiList_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getTcListEntityData = new IBaseDao.GetServerData<List<DtListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<DtListEntity>> listApiResponse) {
                mView.getDtList(listApiResponse.getData(), listApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 获取协会动态信息详细
     */
    public void getXhdtItemInfo(int dtid) {
        if (dtid == -1) {
            mView.getErrorNews("当前协会动态错误，请重新刷新后查看");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("dtid", dtid);

        mDao.getXhdtItemInfo_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getDtItemEntityData = new IBaseDao.GetServerData<DtItemEntity>() {
            @Override
            public void getdata(ApiResponse<DtItemEntity> gcItemEntityApiResponse) {
                mView.getDtItmeInfo(gcItemEntityApiResponse, gcItemEntityApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 添加协会动态
     * bt：标题，不能为空，最长不超过30个字。
     * nr：内容
     * pl：评论。整数。0为关闭评论；1为开启评论。
     */
    public void addXhdt(EditText bt, EditText nr, EditText ly, int pl, LinearLayout imgLl, SweetAlertDialog mSweetAlertDialog) {

        if (bt == null || bt.getText().toString().isEmpty()) {
            mView.getErrorNews("标题，不能为空，最长不超过30个字。");
            return;
        }

        if (nr.getText().toString().isEmpty() && imgLl.getChildCount() == 0) {
            mView.getErrorNews("请填写内容或者选择照片");
            return;
        }

        if (ly.getText().toString().isEmpty()) {
            mView.getErrorNews("请填写动态来源");
            return;
        }
        //弹出提示框
        CommonUitls.showLoadSweetAlertDialog(mSweetAlertDialog);

        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("bt", bt.getText().toString());//标题，不能为空，最长不超过30个字。
        postParams.put("nr", nr.getText().toString());//内容
        postParams.put("pl", pl);//评论。整数。0为关闭评论；1为开启评论。
        postParams.put("ly", ly.getText().toString());//来源


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("bt", bt.getText().toString())
                .addFormDataPart("nr", nr.getText().toString())
                .addFormDataPart("pl", String.valueOf(pl))
                .addFormDataPart("ly", ly.getText().toString());//来源

        for (int i = 0; i < imgLl.getChildCount(); i++) {
            // 创建 RequestBody，用于封装构建RequestBody (图片)
            AddImgView view = (AddImgView) imgLl.getChildAt(i);

            RequestBody requestFileFm = RequestBody.create(MediaType.parse("image/jpeg"), new File(view.imgFile));
            MultipartBody.Part bodyFm = MultipartBody.Part.createFormData("pic" + (i + 1), view.imgFile, requestFileFm);
            builder.addPart(bodyFm);
        }

        RequestBody requestBody = builder.build();
        mDao.addXhdt_xh(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getAddXhdt = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                mView.addResults(listApiResponse, listApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }




    /**
     * 编辑协会动态
     * bt：标题，不能为空，最长不超过30个字。
     * nr：内容
     * pl：评论。整数。0为关闭评论；1为开启评论。
     */
    public void editXhdt(EditText bt, EditText nr, EditText ly, int pl, int dtid, LinearLayout imgLl, SweetAlertDialog mSweetAlertDialog) {

        if (bt == null || bt.getText().toString().isEmpty()) {
            mView.getErrorNews("标题，不能为空，最长不超过30个字。");
            return;
        }

        if (nr.getText().toString().isEmpty() && imgLl.getChildCount() == 0) {
            mView.getErrorNews("请填写内容或者选择照片");
            return;
        }

        if (ly.getText().toString().isEmpty()) {
            mView.getErrorNews("请填写动态来源");
            return;
        }

        //弹出提示框
        CommonUitls.showLoadSweetAlertDialog(mSweetAlertDialog);

        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("bt", bt.getText().toString());//标题，不能为空，最长不超过30个字。
        postParams.put("nr", nr.getText().toString());//内容
        postParams.put("dtid", dtid);//协会id
        postParams.put("pl", pl);//评论。整数。0为关闭评论；1为开启评论。
        postParams.put("ly", ly.getText().toString());//来源


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("bt", bt.getText().toString())
                .addFormDataPart("nr", nr.getText().toString())
                .addFormDataPart("dtid", String.valueOf(dtid))
                .addFormDataPart("pl", String.valueOf(pl))
                .addFormDataPart("ly", ly.getText().toString());//来源

        for (int i = 0; i < imgLl.getChildCount(); i++) {
            // 创建 RequestBody，用于封装构建RequestBody (图片)
            AddImgView view = (AddImgView) imgLl.getChildAt(i);

            RequestBody requestFileFm = RequestBody.create(MediaType.parse("image/jpeg"), new File(view.imgFile));
            MultipartBody.Part bodyFm = MultipartBody.Part.createFormData("pic" + (i + 1), view.imgFile, requestFileFm);
            builder.addPart(bodyFm);
        }

        RequestBody requestBody = builder.build();
        mDao.editXhdt_xh(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getAddXhdt = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {

                mView.addResults(listApiResponse, listApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 删除协会动态
     */
    public void delDt_XH(String ids) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("ids", ids);//ID，删除多条记录使用英文逗号将规程ID分隔。示例：ids=1,2,3

        mDao.delXhdt_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getDelXhdt = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                mView.delResults_dt(listApiResponse, listApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }



}

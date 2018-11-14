package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter;

import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.daoimpl.GameGcImpl;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameGcView;
import com.cpigeon.cpigeonhelper.ui.imgupload.AddImgView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 赛事规程 控制层
 * Created by Administrator on 2017/12/12.
 */
public class GameGcPrensenter extends BasePresenter<GameGcView, GameGcImpl> {

    public GameGcPrensenter(GameGcView mView) {
        super(mView);
    }

    @Override
    protected GameGcImpl initDao() {
        return new GameGcImpl();
    }


    /**
     * 获取协会规程列表
     */
    public void getXhgcList() {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getGuiChengList_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getGcListEntityData = new IBaseDao.GetServerData<List<GcListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<GcListEntity>> listApiResponse) {
                mView.getGCList(listApiResponse.getData(), listApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 添加协会赛事规程
     * bt：标题，不能为空，最长不超过30个字。
     * nr：内容
     * pl：评论。整数。0为关闭评论；1为开启评论。
     */
    public void addGuiCheng(EditText bt, EditText nr, int pl, LinearLayout imgLl, SweetAlertDialog mSweetAlertDialog) {

        if (bt == null || bt.getText().toString().isEmpty()) {
            mView.getErrorNews("标题，不能为空，最长不超过30个字。");
            return;
        }

        if (nr.getText().toString().isEmpty() && imgLl.getChildCount() == 0) {
            mView.getErrorNews("请填写内容或者选择照片");
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


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("bt", bt.getText().toString())
                .addFormDataPart("nr", nr.getText().toString())
                .addFormDataPart("pl", String.valueOf(pl));

        for (int i = 0; i < imgLl.getChildCount(); i++) {
            // 创建 RequestBody，用于封装构建RequestBody (图片)
            AddImgView view = (AddImgView) imgLl.getChildAt(i);
            RequestBody requestFileFm = RequestBody.create(MediaType.parse("image/jpeg"), new File(view.imgFile));
            MultipartBody.Part bodyFm = MultipartBody.Part.createFormData("pic" + (i + 1), view.imgFile, requestFileFm);
            builder.addPart(bodyFm);
        }

        RequestBody requestBody = builder.build();
        mDao.addSSGC_xh(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getAddSSGC = new IBaseDao.GetServerData<Object>() {
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
     * 获取协会规程信息详细
     */
    public void getGuiChengItemInfo(int gcid) {

        if (gcid == -1) {
            mView.getErrorNews("当前赛事规程错误，请重新刷新后查看");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("gcid", gcid);

        mDao.getGuiChengItemInfo_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getGcItemEntityData = new IBaseDao.GetServerData<GcItemEntity>() {
            @Override
            public void getdata(ApiResponse<GcItemEntity> gcItemEntityApiResponse) {
                mView.getItmeInfo(gcItemEntityApiResponse, gcItemEntityApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 修改协会赛事规程
     * bt：标题，不能为空，最长不超过30个字。
     * nr：内容
     * pl：评论。整数。0为关闭评论；1为开启评论。
     */
    public void editGuiCheng(EditText bt, EditText nr, int pl, int gcid, LinearLayout imgLl, SweetAlertDialog mSweetAlertDialog) {

        if (bt == null || bt.getText().toString().isEmpty()) {
            mView.getErrorNews("标题，不能为空，最长不超过30个字。");
            return;
        }

        if (nr.getText().toString().isEmpty() && imgLl.getChildCount() == 0) {
            mView.getErrorNews("至少填写输入内容，或者上传一张照片");
            return;
        }

        //弹出提示框
        CommonUitls.showLoadSweetAlertDialog(mSweetAlertDialog);

        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("gcid", gcid);
        postParams.put("bt", bt.getText().toString());
        postParams.put("nr", nr.getText().toString());
        postParams.put("pl", pl);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("bt", bt.getText().toString())
                .addFormDataPart("gcid", String.valueOf(gcid))
                .addFormDataPart("nr", nr.getText().toString())
                .addFormDataPart("pl", String.valueOf(pl));

        File file;
        for (int i = 0; i < imgLl.getChildCount(); i++) {
            // 创建 RequestBody，用于封装构建RequestBody (图片)
            AddImgView view = (AddImgView) imgLl.getChildAt(i);
            file = new File(view.imgFile);
//            file = Glide.getPhotoCacheDir(mContext, view.imgFile);
//            Glide.with(mContext).load(view.imgFile).;
            RequestBody requestFileFm = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part bodyFm = MultipartBody.Part.createFormData("pic" + (i + 1), view.imgFile, requestFileFm);
            builder.addPart(bodyFm);
        }

        RequestBody requestBody = builder.build();
        mDao.editSSGC_xh(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getAddSSGC = new IBaseDao.GetServerData<Object>() {
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
     * 删除赛事规程
     */
    public void delGc_XH(String ids) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("ids", ids);//ID，删除多条记录使用英文逗号将规程ID分隔。示例：ids=1,2,3

        mDao.delSsgc_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getDelXhdt = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                mView.delResults_gc(listApiResponse, listApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

}

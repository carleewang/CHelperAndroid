package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl.ImgVideoImpl;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.ImgVideoView;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;
import com.cpigeon.cpigeonhelper.video.RecordedActivity3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.cpigeon.cpigeonhelper.utils.PermissonUtil.cameraIsCanUse;

/**
 * 上传图片，视频控制层
 * Created by Administrator on 2017/10/18.
 */

public class UploadImgVideoPresenter extends BasePresenter<ImgVideoView, ImgVideoImpl> {
    private Map<String, Object> postParams = new HashMap<>();
    private long timestamp;

    public List<TagEntitiy> tagEntitiyList = new ArrayList<>();

    public UploadImgVideoPresenter(ImgVideoView mView) {
        super(mView);
    }

    @Override
    protected ImgVideoImpl initDao() {
        return new ImgVideoImpl();
    }


    /**
     * 开始上传图片  视频
     *
     * @param ft
     * @param tagid
     * @param file
     * @param lo
     * @param la
     * @param we
     * @param t
     * @param wp
     * @param wd    type  1 图片   2   视频
     */
    public void uploadImgVideo(String ft, String tagid, File file, double lo, double la, String we, String t, String wp, String wd, int type, @Nullable String uploadTime, byte[] fileByte) {

        LogUtil.print("赛事id: " + String.valueOf(MonitorData.getMonitorId()));

        RequestBody requestFile = null;
        Log.d("aaasd", "onClick: 333");
        if (type == 1) {
            // 创建 RequestBody，用于封装构建RequestBody
            if (fileByte != null) {
                Log.d("aaasd", "onClick: 444");
                requestFile = RequestBody.create(MediaType.parse("image/jpeg"), fileByte);
            } else {
                Log.d("aaasd", "onClick: 555");
                requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            }
        } else if (type == 2) {
            Log.d("aaasd", "onClick: 666");
            // 创建 RequestBody，用于封装构建RequestBody
            requestFile = RequestBody.create(MediaType.parse("video/mp4"), file);
        }

        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("rid", String.valueOf(MonitorData.getMonitorId()))//鸽运通赛事 ID
                .addFormDataPart("ft", ft)//文件类型【image video】
                .addFormDataPart("tagid", tagid)//标签 ID
                .addFormDataPart("lo", lo + "")//经度
                .addFormDataPart("la", la + "")//维度
                .addFormDataPart("we", we + "")//天气名称
                .addFormDataPart("t", t + "")//温度
                .addFormDataPart("wp", wp + "")//风力
                .addFormDataPart("wd", wd + "")//风向
                .addFormDataPart("wt", uploadTime == null ? "" : uploadTime)
                .addPart(body)//图片、视频【文件表单】
                .build();


        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));//用户 ID
        postParams.put("rid", String.valueOf(MonitorData.getMonitorId()));//鸽运通赛事 ID
        postParams.put("ft", ft);//文件类型【image video】
        postParams.put("tagid", tagid);//标签 ID
        postParams.put("lo", lo + "");//经度
        postParams.put("la", la + "");//维度
        postParams.put("we", we);//天气名称
        postParams.put("t", t);//温度
        postParams.put("wp", wp);//风力
        postParams.put("wd", wd);//风向


        //开始请求
        mDao.uploadImgVideo(AssociationData.getUserToken()//通行验证
                , requestBody//请求体
                , timestamp//时间戳
                , CommonUitls.getApiSign(timestamp, postParams));//签名
        mDao.getServerData = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> dateApiResponse) {
//                Log.d("xiaohl", "上传照片视频错误码: " + dateApiResponse.getErrorCode());
                switch (dateApiResponse.getErrorCode()) {
                    case 0://请求成功
                        mView.uploadSucceed();//上传成功后回调
                        break;

                    default://上传失败
                        mView.uploadFail(dateApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //获取鸽运通表
    public void getTag() {
        mDao.getServerTag("gyt");
        mDao.getServerTag = new IBaseDao.GetServerData<List<TagEntitiy>>() {
            @Override
            public void getdata(ApiResponse<List<TagEntitiy>> listApiResponse) {
                switch (listApiResponse.getErrorCode()) {
                    case 0://请求成功
                        if (listApiResponse.getData().size() > 0) {
                            tagEntitiyList = listApiResponse.getData();
                            mView.getTagData(listApiResponse.getData());
                        }
                        break;
                    default:
                        mView.getErrorNews("获取标签失败：" + listApiResponse.getErrorCode() + "  " + listApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    private Intent intent;

    public void showTagDialog(SaActionSheetDialog dialog, Activity mContext, UploadImgVideoPresenter mUploadImgVideoPresenter) {

        if (!cameraIsCanUse()) {
            return;
        }

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            dialog.clearSheetItem();
            if (mUploadImgVideoPresenter.tagEntitiyList.size() > 0) {
                for (int i = 0; i < mUploadImgVideoPresenter.tagEntitiyList.size(); i++) {
                    dialog.addSheetItem(mUploadImgVideoPresenter.tagEntitiyList.get(i).getName(), which -> {
                        //跳转到拍摄视频页面
                        intent = new Intent(mContext, RecordedActivity3.class);
                        intent.putExtra("type", "video");
                        intent.putExtra("label_tag", mUploadImgVideoPresenter.tagEntitiyList.get(which - 1).getName());
                        if (mUploadImgVideoPresenter.tagEntitiyList.get(which - 1).getName().equals("司放瞬间")) {
                            intent.putExtra("video_time", 20 * 1000);//如果标签为 司放瞬间  录制时间为20s
                        }
                        mContext.startActivity(intent);
                    });
                }

                dialog.show();
            } else {
                CommonUitls.showToast(mContext, "未获取到标签数据");
                mUploadImgVideoPresenter.getTag();
            }

        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            //跳转到拍摄视频页面
            if (cameraIsCanUse()) {
                Intent intent = new Intent(mContext, RecordedActivity3.class);
                intent.putExtra("type", "video");
                mContext.startActivity(intent);
            }
        }
    }
}

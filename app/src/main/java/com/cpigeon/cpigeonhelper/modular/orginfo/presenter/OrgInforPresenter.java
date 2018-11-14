package com.cpigeon.cpigeonhelper.modular.orginfo.presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.AlterEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.daoimpl.OrgInforImpl;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.OrgInforView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 协会信息控制层
 * Created by Administrator on 2017/9/14.
 */
public class OrgInforPresenter extends BasePresenter<OrgInforView, OrgInforImpl> {


    private Map<String, Object> postParams = new HashMap<>();
    private long timestamp;

    public OrgInforPresenter(OrgInforView mView) {
        super(mView);
    }

    @Override
    protected OrgInforImpl initDao() {
        return new OrgInforImpl();
    }

    /**
     * 获取组织信息数据
     */
    public void getOrgInforData() {
        mDao.downOrginforData(AssociationData.getUserToken(), AssociationData.getUserId());//开始下载数据
        mDao.getServerData = new IBaseDao.GetServerData<OrgInfo>() {//数据下载成功后回调
            @Override
            public void getdata(ApiResponse<OrgInfo> orgInfoApiResponse) {

                switch (orgInfoApiResponse.getErrorCode()) {
                    case 0:
                        //保存组织信息
                        RealmUtils.preservationOrgInfo(orgInfoApiResponse.getData());
                        mView.validationSucceed(orgInfoApiResponse.getData());//验证成功后回调
                        break;
                    default:
                        mView.getErrorNews("获取信息错误：错误码--" + orgInfoApiResponse.getErrorCode());
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 修改组织信息
     * etUserinfoName;//联系人
     * etUserphone;//联系电话
     * etUseremail;//邮箱
     * etUseraddress;//详细地址
     * etUsersetup;//建立时间
     */
    public void amendUserInforData(EditText etUserinfoName, EditText etUserphone, EditText etUseremail, EditText etUseraddress) {

        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));
        postParams.put("type", AssociationData.getUserType());
        postParams.put("contacts", etUserinfoName.getText().toString());//联系人
        postParams.put("phone", etUserphone.getText().toString());//联系电话
        postParams.put("email", etUseremail.getText().toString());//邮箱
        postParams.put("addr", etUseraddress.getText().toString());//详细地址
//        postParams.put("setuptime", etUsersetup.getText().toString());//建立时间
        Log.d(TAG, "uid: " + String.valueOf(AssociationData.getUserId()));

        long timestamp = System.currentTimeMillis() / 1000;

        //开始提交用户信息
        mDao.submitUserInfor(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        //提交信息后回调
        mDao.submitServerData = new IBaseDao.GetServerData<OrgInfo>() {
            @Override
            public void getdata(ApiResponse<OrgInfo> orgInfoApiResponse) {
                Log.d(TAG, "提交用户信息错误码: " + orgInfoApiResponse.getErrorCode());
                switch (orgInfoApiResponse.getErrorCode()) {

                    case 0://提交成功后回调
                        //保存组织信息
                        RealmUtils.preservationOrgInfo(orgInfoApiResponse.getData());
                        mView.validationSucceed(orgInfoApiResponse.getData());
                        break;
                    case 20000://修改失败
                        mView.getErrorNews("修改失败");
                        break;
                    case 20001://组织类型错误
                        mView.getErrorNews("组织类型错误");
                        break;
                    case 20002://必须有修改的内容
                        mView.getErrorNews("必须有修改的内容");
                        break;
                    case 20003://没有相关的信息
                        mView.getErrorNews("没有相关的信息");
                        break;
                    case 20004://手机号码格式错误
                        mView.getErrorNews("手机号码格式错误");
                        break;
                    case 20005://邮箱格式错误
                        mView.getErrorNews("邮箱格式错误");
                        break;

                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 跳转到相机
     * chooseMode = PictureMimeType.ofImage();//设置选择的模式
     * maxSelectNum = 1;// 最大图片选择数量
     * compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式
     * requesCode  返回Activity返回码
     */
    public static void jumpCamera(Activity activity, int chooseMode, int maxSelectNum, int compressMode, int requesCode) {
        // 进入相机 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openCamera(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .compress(true)// 是否压缩
                .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .forResult(requesCode);//结果回调
    }

    /**
     * 跳转到相册
     * chooseMode = PictureMimeType.ofImage();//设置选择的模式
     * maxSelectNum = 1;// 最大图片选择数量
     * compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式
     * requesCode  返回Activity返回码
     */
    public static void jumpAlbum(Activity activity, int chooseMode, int maxSelectNum, int compressMode, int requesCode) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(false)// 是否显示拍照按钮
                .compress(true)// 是否压缩
                .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .forResult(requesCode);//结果回调onActivityResult code
    }


    /**
     * 跳转到相机
     * chooseMode = PictureMimeType.ofImage();//设置选择的模式
     * maxSelectNum = 1;// 最大图片选择数量
     * compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式
     * requesCode  返回Activity返回码
     */
    public static void jumpCameraWH(Activity activity, int chooseMode, int maxSelectNum, int compressMode, int requesCode) {
        // 进入相机 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openCamera(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如 16:9 3:2 3:4 1:1 可自定义
                .imageSpanCount(4)// 每行显示个数
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(false)// 是否显示拍照按钮
                .compress(true)// 是否压缩
                .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .forResult(requesCode);//结果回调
    }

    /**
     * 跳转到相册
     * chooseMode = PictureMimeType.ofImage();//设置选择的模式
     * maxSelectNum = 1;// 最大图片选择数量
     * compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式
     * requesCode  返回Activity返回码
     */
    public static void jumpAlbumWH(Activity activity, int chooseMode, int maxSelectNum, int compressMode, int requesCode) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如 16:9 3:2 3:4 1:1 可自定义
                .imageSpanCount(4)// 每行显示个数
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(false)// 是否显示拍照按钮
                .compress(true)// 是否压缩
                .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .forResult(requesCode);//结果回调onActivityResult code
    }

    /**
     * 变更名称申请控制层
     */
    public void changeOrgName(File compressimg, String newXiehuiName, String changeReason) {

        if (newXiehuiName.isEmpty() || changeReason.isEmpty()) {
            mView.getErrorNews("输入新的名称或原因不能为空");
            return;
        }

        String filePath = compressimg.getPath();
        if (filePath.equals("")) {
            mView.getErrorNews("请提交文件证明");
            return;
        }
        Log.d(TAG, "changeOrgName: " + filePath);

        timestamp = System.currentTimeMillis() / 1000;//时间戳
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), compressimg);

        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", compressimg.getName(), requestFile);

        postParams.clear();
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));//用户 ID
        postParams.put("type", AssociationData.getUserType());//组织类型 xiehui gongpeng
        postParams.put("name", newXiehuiName);//新的协会名称
        postParams.put("reason", changeReason);//申请原因

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))
                .addFormDataPart("type", AssociationData.getUserType())
                .addFormDataPart("name", newXiehuiName)
                .addFormDataPart("reason", changeReason)
                .addPart(body)
                .build();

        mDao.submitApplyforName(AssociationData.getUserToken()//通行验证
                , requestBody//请求体
                , timestamp//时间戳
                , CommonUitls.getApiSign(timestamp, postParams));//签名
        mDao.submitApplyforNameData = new IBaseDao.GetServerData<String>() {
            @Override
            public void getdata(ApiResponse<String> stringApiResponse) {
                switch (stringApiResponse.getErrorCode()) {
                    case 0://
                        mView.validationSucceed();//申请成功
                        break;
                    case 1000://用户 ID 校验失败
                        mView.getErrorNews("用户 ID 校验失败");
                        break;
                    case 20000://没有相关的协会（公棚）信息
                        mView.getErrorNews("没有相关的协会（公棚）信息");
                        break;
                    case 20001://组织类型错误
                        mView.getErrorNews("组织类型错误");
                        break;
                    case 20002://请上传申请证明扫描文件
                        mView.getErrorNews("请上传申请证明扫描文件");
                        break;
                    case 20003://申请失败
                        mView.getErrorNews("申请失败");
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 获取二级域名，组织名称修改状态
     * atype：申请类型 【orgname orgdomain】【默认值：orgname】
     */
    public void acquireState(String atype) {

        timestamp = System.currentTimeMillis() / 1000;//时间戳


        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", AssociationData.getUserId());
        postParams.put("type", AssociationData.getUserType());
        postParams.put("atype", atype);

        mDao.requestState(AssociationData.getUserToken(),//通行验证
                postParams,
                timestamp,
                CommonUitls.getApiSign(timestamp, postParams));//参数
        mDao.alterData = new IBaseDao.GetServerData<AlterEntity>() {
            @Override
            public void getdata(ApiResponse<AlterEntity> alterEntityApiResponse) {
                switch (alterEntityApiResponse.getErrorCode()) {
                    case 0://请求成功
                        switch (alterEntityApiResponse.getData().getStatusCode()) {
                            case "0":
                                mView.checkStateFor();//申请当中回调
                                break;
                            case "1"://审核通过
                                mView.getErrorNews("审核通过");
                                if (atype.equals("orgname")) {
                                    mView.checkStateNameNo();//没有修改的组织名称申请信息
                                } else if (atype.equals("orgdomain")) {
                                    mView.checkStateDomainNo();//没有修改的二级域名申请信息
                                }
                                break;

                            case "2"://审核失败
                                mView.getErrorNews("审核失败");
                                break;
                        }
                        break;

                    case 1000://用户 ID 校验失败
                        mView.getErrorNews("用户 ID 校验失败");
                        break;

                    case 20000://没有相关的协会（公棚）信息
                        mView.getErrorNews("没有相关的协会（公棚）信息");
                        break;

                    case 20001://组织类型错误
                        mView.getErrorNews("组织类型错误");
                        break;

                    case 20002://没有修改的申请信息
                        if (atype.equals("orgname")) {
                            mView.checkStateNameNo();//没有修改的组织名称申请信息
                        } else if (atype.equals("orgdomain")) {
                            mView.checkStateDomainNo();//没有修改的二级域名申请信息
                        }

                        break;
                    case 10001://用户不存在
                        mView.getErrorNews("用户不存在");
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 提交组织二级域名修改申请
     */
    public void changeDomain(EditText newName, EditText etReason) {

        if (newName.getText().toString().isEmpty() || etReason.getText().toString().isEmpty()) {
            mView.getErrorNews("输入内容不能为空");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));//用户 ID
        postParams.put("type", AssociationData.getUserAccountTypeStrings());//组织类型 xiehui gongpeng
        postParams.put("domain", "testxh");//：新名称
        postParams.put("reason", "ste");//：申请原因【post 表单参数】


        //开始请求
        mDao.submitDomain(AssociationData.getUserToken()//通行验证
                , postParams//请求参数
                , timestamp//时间戳
                , CommonUitls.getApiSign(timestamp, postParams));//签名

        //请求结果
        mDao.submitApplyforNameData = new IBaseDao.GetServerData<String>() {
            @Override
            public void getdata(ApiResponse<String> stringApiResponse) {
                Log.d(TAG, "getdata: 申请返回错误码----" + stringApiResponse.getErrorCode());
                switch (stringApiResponse.getErrorCode()) {
                    case 0://申请成功
                        mView.validationSucceed();//申请成功
                        break;

                    case 1000://用户 ID 校验失败
                        mView.getErrorNews("用户 ID 校验失败");
                        break;

                    case 20000://没有相关的协会（公棚）信息
                        mView.getErrorNews("没有相关的协会（公棚）信息");
                        break;

                    case 20001://组织类型错误
                        mView.getErrorNews("组织类型错误");
                        break;

                    case 20003://申请失败
                        mView.getErrorNews("申请失败");
                        break;

                    default:
                        mView.getErrorNews("错误码：" + stringApiResponse.getMsg());
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);//抛出异常
            }
        };
    }

    /**
     * 获取用户类型
     */
    public void getServiceUserType() {

        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));//用户 ID

        mDao.requestGetUserType(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.userType = new IBaseDao.GetServerData<UserType>() {
            @Override
            public void getdata(ApiResponse<UserType> userTypeApiResponse) {
                Log.d("getusertype", "getdata: " + userTypeApiResponse.toJsonString());
                mView.getUserTypeSuccend(userTypeApiResponse, userTypeApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getUserTypeSuccend(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }

}

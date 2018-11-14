package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedSingle;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GP_GetChaZuEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetChaZuListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetJiangJinXianShiBiLiEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GpRpdxSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlSmsSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsSmsDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsSmsSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.daoimpl.GpSmsImpl;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao.GpSmsView;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 公棚短信控制层
 * Created by Administrator on 2017/12/22.
 */
public class GpSmsPresenter extends BasePresenter<GpSmsView, GpSmsImpl> {
    public GpSmsPresenter(GpSmsView mView) {
        super(mView);
    }

    @Override
    protected GpSmsImpl initDao() {
        return new GpSmsImpl();
    }

    //====================================入棚短信===================================================

    /**
     * 获取公棚短信设置数据
     */
    public void getGpSmsSetData() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getBsdxSettingData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getGpdxSetData = new IBaseDao.GetServerData<GpRpdxSetEntity>() {
            @Override
            public void getdata(ApiResponse<GpRpdxSetEntity> dataApiResponse) {
                mView.getGpdxSetData_RP(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 提交入棚短信设置信息
     */
    public void subGpSmsSetData(GpRpdxSetEntity mGpRpdxSetEntity, EditText gpjc, ToggleButton tb_fsnr, TextView rpkssj) {

        if (mGpRpdxSetEntity == null) {
            mView.getThrowable(new Exception());
            return;
        }

        if (mGpRpdxSetEntity.isSfkq()) {
            if (gpjc.getText().toString().isEmpty()) {
                mView.getErrorNews("输入公棚简称不能为空");
                return;
            }
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("gpjc", gpjc.getText().toString());//公棚简称，字符串，限定为3到6个字符。
        postParams.put("rpkssj", rpkssj.getText().toString());//rpkssj 本届比赛入棚开始日期，字符串格式：2018-01-01

        if (mGpRpdxSetEntity.isSfkq()) {
            postParams.put("sffs", 1);//是否发送短信，数字：0不发送，1发送。

            if (tb_fsnr.isChecked()) {
                if (mGpRpdxSetEntity.isGzxm()) {
                    postParams.put("gzxm", 1);//是否发送鸽主姓名，数字：0不发送，1发送。
                } else {
                    postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
                }

                if (mGpRpdxSetEntity.isCskh()) {
                    postParams.put("cskh", 1);//是否发送参赛卡号，数字：0不发送，1发送。
                } else {
                    postParams.put("cskh", 0);//是否发送参赛卡号，数字：0不发送，1发送。
                }

                if (mGpRpdxSetEntity.isDzhh()) {
                    postParams.put("dzhh", 1);//是否发送电子环号，数字：0不发送，1发送。
                } else {
                    postParams.put("dzhh", 0);//是否发送电子环号，数字：0不发送，1发送。
                }

            } else {
                postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
                postParams.put("cskh", 0);//是否发送参赛卡号，数字：0不发送，1发送。
                postParams.put("dzhh", 0);//是否发送电子环号，数字：0不发送，1发送。
            }

            if (mGpRpdxSetEntity.isSfxs()) {
                postParams.put("sfxs", 1);//是否在入棚记录网页显示收鸽总数，数字：0不发送，1发送。
            } else {
                postParams.put("sfxs", 0);//是否在入棚记录网页显示收鸽总数，数字：0不发送，1发送。
            }

        } else {
            postParams.put("sffs", 0);//是否发送短信，数字：0不发送，1发送。

            postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
            postParams.put("cskh", 0);//是否发送参赛卡号，数字：0不发送，1发送。
            postParams.put("dzhh", 0);//是否发送电子环号，数字：0不发送，1发送。

            postParams.put("sfxs", 0);//是否在入棚记录网页显示收鸽总数，数字：0不发送，1发送。
        }

        mDao.subBsdxSettingData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.subGpdxSetData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.subGpdxSetData_RP(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //====================================训赛短信===================================================

    /**
     * 训赛短信列表数据(获取公棚训赛项目列表)
     */
    public void getXsSmsListData() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.subGetXsSmsListData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getXsListData = new IBaseDao.GetServerNewData<List<XsListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<XsListEntity>> listApiResponse, Throwable throwable) {
                if (throwable != null) {
                    mView.getSmsListData_XS(null, null, throwable);
                } else {
                    Log.d("ddddcs", "getdata: " + listApiResponse.toJsonString());
                    mView.getSmsListData_XS(listApiResponse, listApiResponse.getMsg(), null);
                }
            }
        };
    }

    /**
     * 获取训赛项目详细
     */
    public void getXsSmsDetail(int tid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//训赛索引ID
        mDao.subGetXsSmsDetailData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getXsSmsDetailEntity = new IBaseDao.GetServerData<XsSmsDetailEntity>() {
            @Override
            public void getdata(ApiResponse<XsSmsDetailEntity> xsSmsDetailEntityApiResponse) {
                mView.getSmsDetail_XS(xsSmsDetailEntityApiResponse, xsSmsDetailEntityApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 获取训赛短信设置信息
     */
    public void getSmsSet_XS(String tid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//训赛索引ID
        mDao.getSmsSetData_XS(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getXsSmsSetEntity = new IBaseDao.GetServerData<XsSmsSetEntity>() {
            @Override
            public void getdata(ApiResponse<XsSmsSetEntity> dataApiResponse) {
                mView.getSmsSet_XS(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 提交训赛短信设置信息
     * tb_qbfs 全部发送
     * tb_fsqng  发送前n个
     */
    public void subSmsSet_XS(String tid, EditText orgName, XsSmsSetEntity mXsSmsSetEntity,
                             ToggleButton tb_qbfs, ToggleButton tb_fsqng, EditText etFsqng,
                             ToggleButton tb_fsnr, EditText lo, EditText la) {

        if (orgName.getText().toString().isEmpty()) {
            mView.getErrorNews("简称不能为空");
            return;
        }

        if (orgName.getText().toString().length() > 6 || orgName.getText().toString().length() < 3) {
            mView.getErrorNews("简称请保持在3到6个字符");
            return;
        }

        if (lo.getText().toString().isEmpty() || lo.getText().toString().length() == 0 || la.getText().toString().isEmpty() || la.getText().toString().length() == 0) {
            mView.getErrorNews("经纬度不能为空");
            return;
        }

        if (mXsSmsSetEntity == null) {
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//训赛索引ID

        postParams.put("gpjc", orgName.getText().toString());//公棚简称，字符串，限定为3到6个字符。
        postParams.put("jd", lo.getText().toString());//经度
        postParams.put("wd", la.getText().toString());//纬度

        //发送短信
        if (mXsSmsSetEntity.isKqzt()) {
            postParams.put("sffs", 1);//是否发送短信，数字：0不发送，1发送。

            if (tb_fsnr.isChecked()) {//发送内容打开
                //鸽主姓名
                if (mXsSmsSetEntity.isGzxm()) {
                    postParams.put("gzxm", 1);//是否发送鸽主姓名，数字：0不发送，1发送。
                } else {
                    postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
                }

                //足环号码
                if (mXsSmsSetEntity.isZhhm()) {
                    postParams.put("zhhm", 1);//是否发送足环号码，数字：0不发送，1发送。
                } else {
                    postParams.put("zhhm", 0);//是否发送足环号码，数字：0不发送，1发送。
                }

                //分速
                if (mXsSmsSetEntity.isFsfs()) {
                    postParams.put("fsfs", 1);//是否发送分速，数字：0不发送，1发送。
                } else {
                    postParams.put("fsfs", 0);//是否发送分速，数字：0不发送，1发送。
                }

                //名次
                if (mXsSmsSetEntity.isFsmc()) {
                    postParams.put("fsmc", 1);//是否发送名次，数字：0不发送，1发送。
                } else {
                    postParams.put("fsmc", 0);//是否发送名次，数字：0不发送，1发送。
                }
            } else {//发送内容关闭
                postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
                postParams.put("zhhm", 0);//是否发送足环号码，数字：0不发送，1发送。
                postParams.put("fsfs", 0);//是否发送分速，数字：0不发送，1发送。
                postParams.put("fsmc", 0);//是否发送名次，数字：0不发送，1发送。
            }

            if (mXsSmsSetEntity.getType().equals("xf")) {
                //发送对象 训放有发送对象，比赛没有发送对象
                if (tb_qbfs.isChecked()) {
                    postParams.put("fsdx", 0);//发送对象。数字。0为全部发送（默认），大于0为发送前n名。
                }

                if (tb_fsqng.isChecked()) {
                    if (Integer.valueOf(etFsqng.getText().toString()) > 0) {
                        postParams.put("fsdx", etFsqng.getText().toString());//发送对象。数字。0为全部发送（默认），大于0为发送前n名。
                    } else {
                        mView.getErrorNews("发送对象个数必须大于0");
                        return;
                    }
                }
            }

        } else {
            postParams.put("sffs", 0);//是否发送短信，数字：0不发送，1发送。

            postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
            postParams.put("zhhm", 0);//是否发送足环号码，数字：0不发送，1发送。
            postParams.put("fsfs", 0);//是否发送分速，数字：0不发送，1发送。
            postParams.put("fsmc", 0);//是否发送名次，数字：0不发送，1发送。
            if (mXsSmsSetEntity.getType().equals("xf")) {
                postParams.put("fsdx", 0);//发送对象。数字。0为全部发送（默认），大于0为发送前n名。
            }

        }

        mDao.subSmsSetData_XS(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getSubXsSmsSet = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.subSmsSet_XS(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //====================================上笼短信===================================================

    /**
     * 获取上笼信息列表
     */
    public void getSmsListData_SL() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getSmsListData_SL(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getListData_SL = new IBaseDao.GetServerData<List<SlListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<SlListEntity>> listApiResponse) {
                Log.d("ddddcs", "getdata: " + listApiResponse.toJsonString());
                mView.getSmsListData_SL(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getSmsListData_SL(null, null, throwable);
            }
        };
    }


    /**
     * 获取上笼短信设置信息
     */
    public void getSmsSet_SL(String tid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//索引ID。

        mDao.getSmsSetData_SL(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getSmsSetData_SL = new IBaseDao.GetServerData<SlSmsSetEntity>() {
            @Override
            public void getdata(ApiResponse<SlSmsSetEntity> dataApiResponse) {
                mView.getSmsSetData_SL(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 提交上笼短信设置信息
     */
    public void getSmsSubSet_SL(String tid, SlSmsSetEntity mSlSmsSetEntity, EditText gpjc,
                                ToggleButton tb_fsnr, EditText txOrgXmmc) {


        if (txOrgXmmc.getText().toString().isEmpty()) {
            mView.getErrorNews("请填写项目名称！");
            return;
        }

        if (txOrgXmmc.getText().toString().length() > 20) {
            mView.getErrorNews("项目名称不能超过20个汉字");
            return;
        }


        if (gpjc.getText().toString().isEmpty()) {
            mView.getErrorNews("请填写公棚简称！");
            return;
        }

        if (gpjc.getText().toString().length() < 3 || gpjc.getText().toString().length() > 6) {
            mView.getErrorNews("简称请保持在3到6个字符");
            return;
        }

        if (mSlSmsSetEntity == null) {
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//索引ID。
        postParams.put("gpjc", gpjc.getText().toString());//公棚简称，字符串，限定为3到6个字符。。
        postParams.put("xmmc", txOrgXmmc.getText().toString());//项目名称不能超过20个汉字。


        if (mSlSmsSetEntity.isSfkq()) {
            postParams.put("sffs", 1);//是否发送短信，数字：0不发送，1发送。。

            if (tb_fsnr.isChecked()) {//发送内容打开
                if (mSlSmsSetEntity.isGzxm()) {
                    postParams.put("gzxm", 1);//是否发送鸽主姓名，数字：0不发送，1发送。
                } else {
                    postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
                }

                if (mSlSmsSetEntity.isZhiding()) {
                    postParams.put("czzd", 1);//是否发送插组指定信息，数字：0不发送，1发送。。
                } else {
                    postParams.put("czzd", 0);//是否发送插组指定信息，数字：0不发送，1发送。。
                }
            } else {//发送内容关闭
                postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
                postParams.put("czzd", 0);//是否发送插组指定信息，数字：0不发送，1发送。。
            }

        } else {
            postParams.put("sffs", 0);//是否发送短信，数字：0不发送，1发送。。

            postParams.put("gzxm", 0);//是否发送鸽主姓名，数字：0不发送，1发送。
            postParams.put("czzd", 0);//是否发送插组指定信息，数字：0不发送，1发送。。
        }

        mDao.subSmsSetData_SL(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.subSmsSetData_SL = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getSmsSubSetData_SL(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 提交发送短信通道测试短信
     */
    public void getSmsSubCeShi_SL(EditText csPhone) {

        if (csPhone.getText().toString().isEmpty()) {
            mView.getErrorNews("输入手机号不能为空");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("sjhm", csPhone.getText().toString());//电话号码

        mDao.subSmsCeShiData_SL(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.ceShiSmsSetData_SL = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getSmsSubCeShiData_SL(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

//------------------------------------------------插组指定-------------------------------------------------------


    /**
     * 获取公棚插组指定  公棚  列表
     */
    public void getDesignatedList_gp(int serviceType) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        if (serviceType == 1) {
            //获取协会插组指定
            mDao.subDesignatedList_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        } else if (serviceType == 2) {
            //获取公棚插组指定
            mDao.subDesignatedList_gp(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        }

        mDao.getDesignatedList_gp = new IBaseDao.GetServerData<List<DesignatedListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<DesignatedListEntity>> listApiResponse) {
                mView.getDesignatedList_gp(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getDesignatedList_gp(null, null, throwable);
            }
        };
    }

    /**
     * 获取公棚插组指定详情  舍弃
     */
    public void getDesignatedDetails_gp(int tid, int serviceType) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", tid);//索引ID。

        if (serviceType == 1) {
            //协会
            mDao.subDesignatedDetails_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        } else if (serviceType == 2) {
            //公棚
            mDao.subDesignatedDetails_gp(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        }


        mDao.getDesignatedDetails_gp = new IBaseDao.GetServerData<DesignatedSetEntity>() {
            @Override
            public void getdata(ApiResponse<DesignatedSetEntity> listApiResponse) {
                mView.getDesignatedDetails_gp(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getDesignatedDetails_gp(null, null, throwable);
            }
        };
    }

    /**
     * 设置公棚插组指定  舍弃
     */
    public void getSetDesignated_gp(int tid, List<DesignatedSingle> data, int serviceType) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", tid);//索引ID。

        postParams.put("c1", data.get(0).getContent());
        postParams.put("c2", data.get(1).getContent());
        postParams.put("c3", data.get(2).getContent());
        postParams.put("c4", data.get(3).getContent());
        postParams.put("c5", data.get(4).getContent());
        postParams.put("c6", data.get(5).getContent());
        postParams.put("c7", data.get(6).getContent());
        postParams.put("c8", data.get(7).getContent());
        postParams.put("c9", data.get(8).getContent());
        postParams.put("c10", data.get(9).getContent());
        postParams.put("c11", data.get(10).getContent());
        postParams.put("c12", data.get(11).getContent());
        postParams.put("c13", data.get(12).getContent());
        postParams.put("c14", data.get(13).getContent());
        postParams.put("c15", data.get(14).getContent());
        postParams.put("c16", data.get(15).getContent());
        postParams.put("c17", data.get(16).getContent());
        postParams.put("c18", data.get(17).getContent());
        postParams.put("c19", data.get(18).getContent());
        postParams.put("c20", data.get(19).getContent());
        postParams.put("c21", data.get(20).getContent());
        postParams.put("c22", data.get(21).getContent());
        postParams.put("c23", data.get(22).getContent());
        postParams.put("c24", data.get(23).getContent());

        if (serviceType == 1) {
            //协会
            mDao.subSetDesignated_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        } else if (serviceType == 2) {
            //公棚
            mDao.subSetDesignated_gp(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        }

        mDao.getSubXsSmsSet = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                mView.getSetDesignated_gp(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getSetDesignated_gp(null, null, throwable);
            }
        };
    }


    /**
     * 获取公棚插组指定列表
     */
    public void getGP_GetChaZuList(int tid, int serviceType) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//索引ID。

        if (serviceType == 1) {
            //协会
            mDao.subDesignatedDetails_xh(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        } else if (serviceType == 2) {
            //公棚
            mDao.subGP_GetChaZuList(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        }

        mDao.getGetChaZuList = new IBaseDao.GetServerData<List<GetChaZuListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<GetChaZuListEntity>> listApiResponse) {
                mView.getGetChaZuList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getGetChaZuList(null, null, throwable);
            }
        };
    }


    /**
     * 获取公棚插组每组设置详细
     */
    public void getGP_GetChaZu(int serviceType, String tid, String c) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//索引ID。
        postParams.put("c", c);//组别：a到x，共24个组别，其他参数无效。

        if (serviceType == 1) {
            //协会
            mDao.subXH_GetChaZu(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        } else {
            //公棚
            mDao.subGP_GetChaZu(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        }

        mDao.getGP_GetChaZuEntity = new IBaseDao.GetServerData<GP_GetChaZuEntity>() {
            @Override
            public void getdata(ApiResponse<GP_GetChaZuEntity> listApiResponse) {
                mView.getGP_GetChaZu(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getGP_GetChaZu(null, null, throwable);
            }
        };
    }


    /**
     * 设置 公棚插组每组设置详细
     */
    public void setGP_SetChaZu(int type, String tid, String c, String cbm, String csf, String gz1, String gz2, String gz3,
                               int cb1, int cb2, int cb3) {


        if (cbm.isEmpty()) {
            mView.getErrorNews("请输入插组别名");
            return;
        }

        try {
            if (Integer.valueOf(csf) > 0) {
                if (cb1 == 2 || cb2 == 2 || cb3 == 2) {
                } else {
                    mView.getErrorNews("请填写插组规则");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//索引ID。
        postParams.put("c", c);//组别：a到x，共24个组别，其他参数无效。
        postParams.put("cbm", cbm);//插组别名，字符串
        postParams.put("csf", csf);//参赛费，只能是数字


        if (csf.isEmpty() || Integer.valueOf(csf) == 0) {
            postParams.put("gz1", "0");//规则一，取n名
            postParams.put("gz2", "0");//规则一，取n名
            postParams.put("gz3", "0");//规则一，取n名
        } else {
            if (cb1 == 2) {

                if (gz1.isEmpty() || Integer.valueOf(gz1) == 0) {
                    mView.getErrorNews("插组规则输入不能为空，并且不能为0");
                    return;
                }

                postParams.put("gz1", gz1);//规则一，取n名
                postParams.put("gz2", "0");//规则一，取n名
                postParams.put("gz3", "0");//规则一，取n名
            }

            if (cb2 == 2) {

                if (gz2.isEmpty() || Integer.valueOf(gz2) == 0) {
                    mView.getErrorNews("插组规则输入不能为空，并且不能为0");
                    return;
                }

                postParams.put("gz1", "0");//规则一，取n名
                postParams.put("gz2", gz2);//规则一，取n名
                postParams.put("gz3", "0");//规则一，取n名
            }

            if (cb3 == 2) {

                if (gz3.isEmpty() || Integer.valueOf(gz3) == 0) {
                    mView.getErrorNews("插组规则输入不能为空，并且不能为0");
                    return;
                }

                postParams.put("gz1", "0");//规则一，取n名
                postParams.put("gz2", "0");//规则一，取n名
                postParams.put("gz3", gz3);//规则一，取n名
            }

        }

        if (type == 1) {
            //协会
            mDao.subXH_SetChaZu(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        } else {
            //公棚
            mDao.subGP_SetChaZu(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        }

        mDao.getGP_SetChaZu = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                mView.setGP_SetChaZu(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.setGP_SetChaZu(null, null, throwable);
            }
        };
    }


    /**
     * 公棚插组奖金显示比例设置
     */
    public void setGP_SetChaZu(String tid, String jjbl, int serviceType) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//索引ID。
        postParams.put("jjbl", jjbl);//奖金显示比例，1到99


        if (serviceType == 1) {
            //协会
            mDao.subXH_SetJiangJinXianShiBiLi(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        } else {
            //公棚
            mDao.subGP_SetJiangJinXianShiBiLi(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        }

        mDao.getGP_SetChaZu = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                mView.setGP_SetChaZu(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.setGP_SetChaZu(null, null, throwable);
            }
        };
    }


    /**
     * 获取 奖金比例
     */
    public void subGP_GetJiangJinXianShiBiLi(String tid, int serviceType) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("tid", tid);//索引ID。

        if (serviceType == 1) {
            //协会
            mDao.subXH_GetJiangJinXianShiBiLi(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        } else {
            //公棚
            mDao.subGP_GetJiangJinXianShiBiLi(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        }

        mDao.getJiangJinXianShiBiLi = new IBaseDao.GetServerData<GetJiangJinXianShiBiLiEntity>() {
            @Override
            public void getdata(ApiResponse<GetJiangJinXianShiBiLiEntity> listApiResponse) {
                mView.setGP_GetChaZu(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.setGP_GetChaZu(null, null, throwable);
            }
        };
    }


//------------------------------------------------ViewControl-------------------------------------------------------


    public static AlertDialog initDialog(Context mContext, GpSmsPresenter mGpSmsPresenter) {
        LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_sms_test_dialog, null);
        EditText et_phone = (EditText) dialogLayout.findViewById(R.id.et_phone);//输入手机号码
        TextView tv_cancel = (TextView) dialogLayout.findViewById(R.id.tv_cancel);//取消

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog dialog = builder.create();

        tv_cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        TextView tv_send = (TextView) dialogLayout.findViewById(R.id.tv_send);//发送
        tv_send.setOnClickListener(view -> {
            mGpSmsPresenter.getSmsSubCeShi_SL(et_phone);
        });


        dialog.setView(dialogLayout);
        //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


    //奖金比例设置提示框
    public static CustomAlertDialog initMoneySetDialog(Activity mContext, String defaultScale, DialogClickListener mDialogClickListener) {

        try {
            CustomAlertDialog dialog = new CustomAlertDialog(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_designated_money_dialog, null);
            dialogLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


            TextView tv_sure = (TextView) dialogLayout.findViewById(R.id.tv_sure);
            EditText et_input = (EditText) dialogLayout.findViewById(R.id.et_input);


            LinearLayout ll_1 = (LinearLayout) dialogLayout.findViewById(R.id.ll_1);
            LinearLayout ll_2 = (LinearLayout) dialogLayout.findViewById(R.id.ll_2);


            et_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (Integer.valueOf(s.toString()) > 99 || Integer.valueOf(s.toString()) < 0) {


                            et_input.setText(s.toString().substring(0, s.toString().length() - 1));
                            et_input.setSelection(et_input.getText().toString().length());//光标移动到最后的位置

                            SweetAlertDialogUtil.showDialog3(null, "请输入1-99内数字", mContext, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });


            CheckBox cb_checkbox1 = (CheckBox) dialogLayout.findViewById(R.id.cb_checkbox1);
            CheckBox cb_checkbox2 = (CheckBox) dialogLayout.findViewById(R.id.cb_checkbox2);


            TextView tv_checkbox1 = (TextView) dialogLayout.findViewById(R.id.tv_checkbox1);
            TextView tv_checkbox2 = (TextView) dialogLayout.findViewById(R.id.tv_checkbox2);

            if (Integer.valueOf(defaultScale) == 100) {
                cb_checkbox2.setChecked(true);
                tv_checkbox1.setTextColor(mContext.getResources().getColor(R.color.color_b3b3b3));
                tv_checkbox2.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
                et_input.setText(defaultScale);
                cb_checkbox1.setChecked(true);

                tv_checkbox1.setTextColor(mContext.getResources().getColor(R.color.black));
                tv_checkbox2.setTextColor(mContext.getResources().getColor(R.color.color_b3b3b3));
            }

            ll_1.setOnClickListener(view -> {
                if (!cb_checkbox1.isChecked()) {
                    cb_checkbox1.setChecked(true);
                    cb1Click(mContext, et_input, cb_checkbox1, cb_checkbox2, tv_checkbox1, tv_checkbox2);
                }
            });

            ll_2.setOnClickListener(view -> {
                if (!cb_checkbox2.isChecked()) {
                    cb_checkbox2.setChecked(true);
                    cb2Click(mContext, et_input, cb_checkbox1, cb_checkbox2, tv_checkbox1, tv_checkbox2);
                }
            });

            cb_checkbox1.setOnClickListener(view -> {
                cb1Click(mContext, et_input, cb_checkbox1, cb_checkbox2, tv_checkbox1, tv_checkbox2);
            });
            cb_checkbox2.setOnClickListener(view -> {
                cb2Click(mContext, et_input, cb_checkbox1, cb_checkbox2, tv_checkbox1, tv_checkbox2);
            });


            tv_sure.setOnClickListener(view -> {

                if (cb_checkbox1.isChecked()) {
                    if (et_input.getText().toString().isEmpty() || Integer.valueOf(et_input.getText().toString()) <= 0 || Integer.valueOf(et_input.getText().toString()) >= 100) {
                        CommonUitls.showToast(mContext, "请输入1-99内数字");

                        try {
                            et_input.setText(et_input.getText().toString().substring(0, et_input.getText().toString().length() - 1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        et_input.setSelection(et_input.getText().toString().length());//光标移动到最后的位置
                        return;
                    }
                }

                mDialogClickListener.onDialogClickListener(view, dialog, cb_checkbox1, cb_checkbox2, et_input);
            });

            dialog.setContentView(dialogLayout);

            //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
//            toggleInput(mContext);
            return dialog;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void cb2Click(Activity mContext, EditText et_input, CheckBox cb_checkbox1, CheckBox cb_checkbox2, TextView tv_checkbox1, TextView tv_checkbox2) {
        if (cb_checkbox2.isChecked()) {
            cb_checkbox1.setChecked(false);
            tv_checkbox2.setTextColor(mContext.getResources().getColor(R.color.black));
            tv_checkbox1.setTextColor(mContext.getResources().getColor(R.color.color_b3b3b3));

            et_input.requestFocus();//获取焦点
        } else {

        }
    }

    private static void cb1Click(Activity mContext, EditText et_input, CheckBox cb_checkbox1, CheckBox cb_checkbox2, TextView tv_checkbox1, TextView tv_checkbox2) {
        if (cb_checkbox1.isChecked()) {
            cb_checkbox2.setChecked(false);

            tv_checkbox2.setTextColor(mContext.getResources().getColor(R.color.color_b3b3b3));
            tv_checkbox1.setTextColor(mContext.getResources().getColor(R.color.black));

            et_input.clearFocus();//失去焦点

        } else {
        }
    }


    public interface DialogClickListener {
        void onDialogClickListener(View viewSure, CustomAlertDialog dialog, CheckBox cb_checkbox1, CheckBox cb_checkbox2, EditText et_input);
    }


}

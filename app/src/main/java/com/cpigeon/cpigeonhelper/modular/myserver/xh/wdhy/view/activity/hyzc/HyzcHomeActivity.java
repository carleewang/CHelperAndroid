package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hyzc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.idcard.IdCardCameraActivity;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardNInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardPInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AddRegisterEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.picker.PickerAdmin2;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * 会员注册首页
 * Created by Administrator on 2018/3/23.
 */
public class HyzcHomeActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_hyxm)
    TextView tvHyxm;//会员姓名
    @BindView(R.id.tv_sfzhm)
    TextView tvSfzhm;//身份证号码
    @BindView(R.id.tv_sjhm)
    TextView tvSjhm;//手机号码
    @BindView(R.id.tv_zcph)
    TextView tvZcph;//注册棚号
    @BindView(R.id.tv_gsmc)
    TextView tvGsmc;//鸽舍名称
    @BindView(R.id.tv_gsdz)
    TextView tvGsdz;//鸽舍地址
    @BindView(R.id.tv_gszb)
    TextView tvGszb;//鸽舍坐标
    @BindView(R.id.tv_csbh)
    TextView tvCsbh;//参赛编号
    @BindView(R.id.tv_zcrhsj)
    TextView tvZcrhsj;//注册入会时间

    @BindView(R.id.img_Id_card_z)
    ImageView img_Id_card_z;//身份证正面
    @BindView(R.id.img_Id_card_f)
    ImageView img_Id_card_f;//身份证反面

    @BindView(R.id.img_head)
    ImageView img_head;//用户头像

    private AddRegisterEntity mAddRegisterEntity;
    private MemberPresenter mMemberPresenter;

    @Override

    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hyzc_home;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, HyzcHomeActivity.this::finish);
        setTitle("会员注册");
        mAddRegisterEntity = new AddRegisterEntity();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {

            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                super.getServiceResults(listApiResponse, msg, mThrowable);
                try {

                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                    if (listApiResponse.getErrorCode() == 0) {
                        //发布事件（刷新数据）
                        EventBus.getDefault().post(EventBusService.HYGL_LIST_REFRESH);
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HyzcHomeActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HyzcHomeActivity.this, dialog -> {
                        dialog.dismiss();
                        if (listApiResponse.getErrorCode() == 0) {
                            AppManager.getAppManager().killActivity(mWeakReference);
                        }
                    });//弹出提示

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                super.getErrorNews(str);
                try {
                    HyzcHomeActivity.this.getErrorNews(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                super.getThrowable(throwable);
                try {
                    HyzcHomeActivity.this.getErrorNews(throwable.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Intent intent;

    @OnClick({R.id.ll_hytx, R.id.ll_hyxm, R.id.ll_sfzhm, R.id.ll_sfzzm, R.id.llsfzfm, R.id.ll_sjhm, R.id.ll_zcph, R.id.ll_gsmc, R.id.ll_gsdz, R.id.ll_gszb, R.id.ll_csbh, R.id.ll_zcrhsj, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit://提交
                mLoadDataDialog.show();
                mAddRegisterEntity.setRhsj(tvZcrhsj.getText().toString());
                mMemberPresenter.getXHHYGL_JBXX_Add(mAddRegisterEntity, idCardInfoEntity, idCardNInfoEntity, tvHyxm, tvSfzhm, compressimg);
                break;

            case R.id.ll_hytx://会员头像
                new SaActionSheetDialog(HyzcHomeActivity.this)
                        .builder()
                        .addSheetItem("相册选取", OnSheetItemClickListener)
                        .addSheetItem("拍一张", OnSheetItemClickListener)
                        .show();
                break;
            case R.id.ll_sfzzm://身份证正面
                intent = new Intent(this, IdCardCameraActivity.class);
                intent.putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_P);
                startActivityForResult(intent, IdCardCameraActivity.CODE_ID_CARD_P);
                break;
            case R.id.llsfzfm://身份证反面
                intent = new Intent(this, IdCardCameraActivity.class);
                intent.putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_N);
                startActivityForResult(intent, IdCardCameraActivity.CODE_ID_CARD_N);
                break;

            case R.id.ll_zcrhsj://注册入会时间
                PickerAdmin2.showPicker2(this, 1, new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        tvZcrhsj.setText(year + "-" + month + "-" + day);
                    }
                });
                break;

            case R.id.ll_hyxm://会员姓名
                MyMemberDialogUtil.initInputDialog(HyzcHomeActivity.this, tvHyxm.getText().toString(), "请输入会员姓名", "请如实按照身份证进行填写!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvHyxm.setText(etStr);
                                mAddRegisterEntity.setXm(etStr);
                            }
                        });
                break;
            case R.id.ll_sfzhm://身份证号码
                MyMemberDialogUtil.initInputDialog(HyzcHomeActivity.this, tvSfzhm.getText().toString(), "请输入会员身份证号码", "请如实按照身份证进行填写!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvSfzhm.setText(etStr);
                                mAddRegisterEntity.setHaoma(etStr);
                            }
                        });
                break;

            case R.id.ll_sjhm://手机号码
                MyMemberDialogUtil.initInputDialog(this, tvSjhm.getText().toString(), "请输入会员手机号码", "请填写正确的手机号码！", InputType.TYPE_CLASS_NUMBER,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvSjhm.setText(etStr);
                                mAddRegisterEntity.setSjhm(etStr);
                            }
                        });
                break;
            case R.id.ll_zcph://注册棚号
                MyMemberDialogUtil.initInputDialog(this, tvZcph.getText().toString(), "请输入注册棚号", "请如实填写棚号！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvZcph.setText(etStr);
                                mAddRegisterEntity.setPh(etStr);
                            }
                        });
                break;
            case R.id.ll_gsmc://鸽舍名称
                MyMemberDialogUtil.initInputDialog(this, mMemberPresenter.getHintString(tvGsmc, "请如实填写名称"), "请输入鸽舍名称", "请如实填写名称！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvGsmc.setText(etStr);
                                mAddRegisterEntity.setGsmc(etStr);
                            }
                        });
                break;
            case R.id.ll_gsdz://鸽舍地址
                MyMemberDialogUtil.initInputDialog(this, tvGsdz.getText().toString(), "请输入鸽舍地址", "请如实填写地址！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvGsdz.setText(etStr);
                                mAddRegisterEntity.setGsdz(etStr);
                            }
                        });
                break;
            case R.id.ll_gszb://鸽舍坐标
                MyMemberDialogUtil.initInputDialogLola(this, mMemberPresenter.getHintString(tvGszb, ""), "请输入鸽舍坐标", "请如实填写坐标！", InputType.TYPE_CLASS_TEXT,
                        errSweetAlertDialog,
                        new MyMemberDialogUtil.DialogClickListenerLoLa() {
                            @Override
                            public void onDialogClickListenerLoLa(View viewSure, CustomAlertDialog dialog, String lo, String la) {
                                dialog.dismiss();
                                if (lo.isEmpty() || lo.length() == 0) return;
                                if (la.isEmpty() || la.length() == 0) return;
                                tvGszb.setText(String.valueOf("东经：" + lo + " 北纬：" + la));
                                mAddRegisterEntity.setGsjd(lo);
                                mAddRegisterEntity.setGswd(la);
                            }
                        });
                break;
            case R.id.ll_csbh://参赛编号
                MyMemberDialogUtil.initInputDialog(this, tvCsbh.getText().toString(), "请输入参赛编号", "请如实参赛编号！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvCsbh.setText(etStr);
                                mAddRegisterEntity.setCsbh(etStr);
                            }
                        });
                break;
        }
    }

    private IdCardPInfoEntity idCardInfoEntity;
    private IdCardNInfoEntity idCardNInfoEntity;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
                if (IdCardCameraActivity.CODE_ID_CARD_P == requestCode) {
                    //正面
                    idCardInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                    if (idCardInfoEntity != null) {
                        img_Id_card_z.setImageBitmap(BitmapFactory.decodeFile(idCardInfoEntity.frontimage));//身份证正面照片

                        tvHyxm.setText(idCardInfoEntity.name);
                        tvSfzhm.setText(idCardInfoEntity.id);
                    }
                } else if (IdCardCameraActivity.CODE_ID_CARD_N == requestCode) {
                    //反面
                    idCardNInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                    if (idCardNInfoEntity != null) {
                        //身份证反面照片
                        img_Id_card_f.setImageBitmap(BitmapFactory.decodeFile(idCardNInfoEntity.backimage));
                    }
                }
            }

            switch (requestCode) {
                case albumRequestCode:
                    // 图片选择结果回调
                    list = PictureSelector.obtainMultipleResult(data);
                    compressimg = new File(list.get(0).getCompressPath());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bm = BitmapFactory.decodeFile(list.get(0).getCompressPath(), options);
                    img_head.setImageBitmap(bm);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<LocalMedia> list = new ArrayList<>();
    private int chooseMode = PictureMimeType.ofImage();//设置选择的模式
    private int maxSelectNum = 1;// 最大图片选择数量
    private int compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式
    private OrgInforPresenter mOrgInforPresenter;//控制层
    private final int albumRequestCode = 0x0021;//相册,相册返回码
    private File compressimg;//图片路径

    /**
     * 弹出选择相册还是照相机
     */
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //跳转到相册
                    mOrgInforPresenter.jumpAlbumWH(HyzcHomeActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
                case 2:
                    //跳转到相机
                    mOrgInforPresenter.jumpCameraWH(HyzcHomeActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
            }
        }
    };
}

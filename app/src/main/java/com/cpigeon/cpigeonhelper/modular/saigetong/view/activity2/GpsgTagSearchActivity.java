package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SearchFootEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl.OrgItem;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl.RaceItem;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter2;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SGTDetailsActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.fragment.ChangeFootDetailsFragment;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.video.RecordedSGTActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cpigeon.cpigeonhelper.utils.PermissonUtil.cameraIsCanUse;

/**
 * 根据标签 拍摄赛鸽照片
 * Created by Administrator on 2018/3/15.
 */

public class GpsgTagSearchActivity extends ToolbarBaseActivity {

    @BindView(R.id.ac_search_input)
    AppCompatEditText acSearchInput;
    @BindView(R.id.imgbtn_start_search)
    ImageButton imgbtnStartSearch;
    @BindView(R.id.gpsg_sg_foot_hint)
    TextView gpsgSgFootHint;//足环号码提示
    @BindView(R.id.gpsg_tv_ser_gz)
    TextView gpsgTvSerGz;//鸽主
    @BindView(R.id.gpsg_tv_ser_ys)
    TextView gpsgTvSerYs;//羽色
    @BindView(R.id.gpsg_tv_ser_dq)
    TextView gpsgTvSerDq;//地区

    @BindView(R.id.ll_btn_ckxq)
    LinearLayout ll_btn_ckxq;//查看详情

    @BindView(R.id.ll_search_results)
    LinearLayout ll_search_results;//搜索结果显示回调

    @BindView(R.id.ll_btn_pz)
    LinearLayout ll_btn_pz;//进入拍照

    @BindView(R.id.img_btn_ypz)
    ImageButton img_btn_ypz;//已拍照

    @BindView(R.id.ll_djpz_z)
    LinearLayout ll_djpz_z;//点击拍照总


    //    错误补拍控件
    @BindView(R.id.ll_cwbp_z)
    LinearLayout ll_cwbp_z;//错误补拍
    @BindView(R.id.tv_gpsg_tag1)
    TextView tv_gpsg_tag1;//
    @BindView(R.id.tv_gpsg_tag2)
    TextView tv_gpsg_tag2;//
    @BindView(R.id.tv_gpsg_tag3)
    TextView tv_gpsg_tag3;//
    @BindView(R.id.tv_gpsg_tag4)
    TextView tv_gpsg_tag4;//
    @BindView(R.id.tv_gpsg_tag5)
    TextView tv_gpsg_tag5;//
    @BindView(R.id.tv_btn_bp)
    TextView tv_btn_bp;//错误补拍

    @BindView(R.id.tv_gpst_bpyy)
    TextView tv_gpst_bpyy;//补拍原因


    private SGTPresenter mSGTPresenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tag_search;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        chooseLabelTag = -1;

        try {
            setTitle(getIntent().getStringExtra("title"));
        } catch (Exception e) {
            setTitle("公棚赛鸽");
        }

        if (getIntent().getStringExtra("title").equals("错误补拍")) {
            initShowErrWhy();
        }
        setTopLeftButton(R.drawable.ic_back, this::finish);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            chooseLabelTag = -1;
            tv_gpst_bpyy.setText("");
            chooseLabel(null);
            if (getIntent().getStringExtra("title").equals("错误补拍")) {
                mSGTPresenter.getSearchBuPaiFootInfo(gpsgSgFootHint.getText().toString());
            } else {
                mSGTPresenter.getSearchFootInfo(gpsgSgFootHint.getText().toString(), getIntent().getIntExtra("tagid", -1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        mSGTPresenter = new SGTPresenter(new SGTViewImpl() {
            @Override
            public void getSearchFootInfoData(ApiResponse<SearchFootEntity> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    ll_btn_pz.setVisibility(View.GONE);
                    img_btn_ypz.setVisibility(View.GONE);
                    ll_djpz_z.setVisibility(View.GONE);
                    ll_search_results.setVisibility(View.GONE);
                    ll_cwbp_z.setVisibility(View.GONE);


                    if (mThrowable != null) return;
                    if (listApiResponse != null && listApiResponse.getErrorCode() == 0 && listApiResponse.getData() != null) {
                        acSearchInput.setText("");
                        ll_search_results.setVisibility(View.VISIBLE);

                        if (getIntent().getStringExtra("title").equals("错误补拍")) {
                            ll_cwbp_z.setVisibility(View.VISIBLE);
                            chooseLabelTag = -1;
                            tv_gpst_bpyy.setText("");
                            chooseLabel(null);
                        } else {
                            ll_djpz_z.setVisibility(View.VISIBLE);
                            if (listApiResponse.getData().getIspic() == 0) {
                                ll_btn_pz.setVisibility(View.VISIBLE);
                            } else {
                                img_btn_ypz.setVisibility(View.VISIBLE);
                            }
                        }

                        //请求数据成功
                        gpsgSgFootHint.setText(listApiResponse.getData().getFoot());//足环号码提示
                        gpsgTvSerGz.setText(listApiResponse.getData().getXingming());//设置鸽主姓名
                        gpsgTvSerYs.setText(listApiResponse.getData().getColor());//羽色
                        gpsgTvSerDq.setText(listApiResponse.getData().getDiqu());//地区


                        listdetail.clear();

                        listdetail.add(new FootSSEntity(listApiResponse.getData().getId(),
                                listApiResponse.getData().getColor(),
                                listApiResponse.getData().getSex(),
                                listApiResponse.getData().getAddress(),//地址
                                listApiResponse.getData().getFoot(),
                                listApiResponse.getData().getTel(),//电话
                                listApiResponse.getData().getEye(),
                                listApiResponse.getData().getSjhm(),
                                listApiResponse.getData().getXingming(),
                                listApiResponse.getData().getCskh(),
                                listApiResponse.getData().getGpmc()));//公棚名称

                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, GpsgTagSearchActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, GpsgTagSearchActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        acSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        Log.d("dianji", "onEditorAction: dianji");
                        if (gpsgSgFootHint.getText().toString().length() == 0) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "输入内容不能为空", GpsgTagSearchActivity.this);
                            return true;
                        }

                        if (getIntent().getStringExtra("title").equals("错误补拍")) {
                            mSGTPresenter.getSearchBuPaiFootInfo(acSearchInput.getText().toString());
                        } else {
                            mSGTPresenter.getSearchFootInfo(acSearchInput.getText().toString(), getIntent().getIntExtra("tagid", -1));
                        }
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @OnClick({R.id.imgbtn_start_search, R.id.ll_btn_ckxq, R.id.ll_btn_pz, R.id.tv_btn_bp, R.id.ll_btn_bpyy
            , R.id.tv_gpsg_tag1, R.id.tv_gpsg_tag2, R.id.tv_gpsg_tag3, R.id.tv_gpsg_tag4, R.id.tv_gpsg_tag5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_start_search://点击搜索
                try {

                    if (gpsgSgFootHint.getText().toString().length() == 0) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "输入内容不能为空", GpsgTagSearchActivity.this);
                        return;
                    }

                    ll_search_results.setVisibility(View.GONE);
                    ll_btn_pz.setVisibility(View.GONE);
                    img_btn_ypz.setVisibility(View.GONE);

                    if (getIntent().getStringExtra("title").equals("错误补拍")) {
                        mSGTPresenter.getSearchBuPaiFootInfo(acSearchInput.getText().toString());
                    } else {
                        mSGTPresenter.getSearchFootInfo(acSearchInput.getText().toString(), getIntent().getIntExtra("tagid", -1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_btn_ckxq:
                //查看详情
                if (SGTPresenter.searchFootInfoData != null && SGTPresenter.searchFootInfoData.getData() != null) {
                    Intent intent = new Intent(GpsgTagSearchActivity.this, SGTDetailsActivity.class);
                    RaceItem raceItem = new RaceItem(new SGTHomeListEntity.DataBean(SGTPresenter.searchFootInfoData.getData().getFoot(), "" + SGTPresenter.searchFootInfoData.getData().getId()));
                    OrgItem orgItem = new OrgItem(new SGTHomeListEntity());
                    intent.putExtra("DataBean", raceItem.getRace());
                    intent.putExtra("SGTHomeListEntity", orgItem.getOrgInfo());
                    startActivity(intent);
                }
                break;

            case R.id.ll_btn_pz://进入拍照

                try {
                    if (!cameraIsCanUse()) {
                        return;
                    }
                    mIntent = new Intent(this, RecordedSGTActivity.class);
                    mIntent.putExtra("tagStr", getIntent().getStringExtra("title"));//标签名称
                    mIntent.putExtra("tagid", getIntent().getIntExtra("tagid", -1));//标签id
                    mIntent.putParcelableArrayListExtra("listdetail", (ArrayList<? extends Parcelable>) listdetail);
                    if (mSaActionSheetDialog == null) {
                        mSaActionSheetDialog = SGTPresenter2.selectFootDialog2(mSaActionSheetDialog, this, new SaActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (which == 1) {
                                    mIntent.putExtra("IMG_NUM_TAG", 2);//左脚
                                } else {
                                    mIntent.putExtra("IMG_NUM_TAG", 3);//右脚
                                }

                                startActivity(mIntent);
                            }
                        });
                    }

                    mSaActionSheetDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.tv_btn_bp:
                try {
                    //错误补拍  跳转
                    if (chooseLabelTag == -1) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "请选择标签", GpsgTagSearchActivity.this);
                        return;
                    }

                    if (!StringValid.isStringValid(tv_gpst_bpyy.getText().toString())) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "请选择错误原因", GpsgTagSearchActivity.this);
                        return;
                    }


                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, SGTPresenter.searchFootInfoData.getData())
                            .putExtra(IntentBuilder.KEY_TYPE, String.valueOf(chooseLabelTag))
                            .putExtra(IntentBuilder.KEY_CONTENT, String.valueOf(tv_gpst_bpyy.getText().toString()))
                            .putExtra("tagStr", tagString)
                            .startParentActivity(this, ChangeFootDetailsFragment.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.ll_btn_bpyy:
                //错误补拍原因
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
                break;

            case R.id.tv_gpsg_tag1:
                chooseLabelTag = 7;
                chooseLabel(tv_gpsg_tag1);
                tagString = "入棚拍照";
                break;

            case R.id.tv_gpsg_tag2:
                chooseLabelTag = 8;
                chooseLabel(tv_gpsg_tag2);
                tagString = "日常随拍";
                break;

            case R.id.tv_gpsg_tag3:
                chooseLabelTag = 9;
                chooseLabel(tv_gpsg_tag3);
                tagString = "查棚收费";
                break;

            case R.id.tv_gpsg_tag4:
                chooseLabelTag = 10;
                chooseLabel(tv_gpsg_tag4);
                tagString = "比赛上笼";
                break;

            case R.id.tv_gpsg_tag5:
                chooseLabelTag = 11;
                chooseLabel(tv_gpsg_tag5);
                tagString = "获奖荣誉";
                break;
        }
    }

    private int chooseLabelTag = -1;//标记   选择的是那个标签
    private String tagString;

    private void chooseLabel(TextView clickTv) {
        tv_gpsg_tag1.setBackground(getResources().getDrawable(R.drawable.gpsg_cwbp_tag1));
        tv_gpsg_tag2.setBackground(getResources().getDrawable(R.drawable.gpsg_cwbp_tag1));
        tv_gpsg_tag3.setBackground(getResources().getDrawable(R.drawable.gpsg_cwbp_tag1));
        tv_gpsg_tag4.setBackground(getResources().getDrawable(R.drawable.gpsg_cwbp_tag1));
        tv_gpsg_tag5.setBackground(getResources().getDrawable(R.drawable.gpsg_cwbp_tag1));

        tv_gpsg_tag1.setTextColor(getResources().getColor(R.color.color_262626));
        tv_gpsg_tag2.setTextColor(getResources().getColor(R.color.color_262626));
        tv_gpsg_tag3.setTextColor(getResources().getColor(R.color.color_262626));
        tv_gpsg_tag4.setTextColor(getResources().getColor(R.color.color_262626));
        tv_gpsg_tag5.setTextColor(getResources().getColor(R.color.color_262626));


        if (clickTv != null) {
            clickTv.setBackground(getResources().getDrawable(R.drawable.gpsg_cwbp_tag2));
            clickTv.setTextColor(getResources().getColor(R.color.color_ffffff));
        }
    }

    private List<FootSSEntity> listdetail = new ArrayList<FootSSEntity>();//搜索标签传递数据 拍照时打水印数据
    Intent mIntent;
    SaActionSheetDialog mSaActionSheetDialog;
    AlertDialog dialog;//错误补拍原因dialog
    CheckBox checkBox1;//足环号码错误
    CheckBox checkBox2;//赛鸽羽色错误

    private String strErrWhy;//错误补拍原因

    //错误补拍原因dialog
    private void initShowErrWhy() {
        try {
            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(GpsgTagSearchActivity.this).inflate(R.layout.layout_pgsg_err_why_dialog, null);
            checkBox1 = (CheckBox) dialogLayout.findViewById(R.id.err_pb_cb1);
            checkBox2 = (CheckBox) dialogLayout.findViewById(R.id.err_pb_cb2);
            LinearLayout ll_dialog_qd = (LinearLayout) dialogLayout.findViewById(R.id.ll_dialog_qd);

            dialogLayout.findViewById(R.id.ll_1).setOnClickListener(v -> {
                checkBox1.setChecked(!checkBox1.isChecked());
            });

            dialogLayout.findViewById(R.id.ll_2).setOnClickListener(v -> {
                checkBox2.setChecked(!checkBox2.isChecked());
            });

            ll_dialog_qd.setOnClickListener(view -> {
                //点击确定
                if (checkBox1.isChecked() && !checkBox2.isChecked()) {
                    tv_gpst_bpyy.setText("足环错误");
                } else if (!checkBox1.isChecked() && checkBox2.isChecked()) {
                    tv_gpst_bpyy.setText("羽色错误");
                } else if (checkBox1.isChecked() && checkBox2.isChecked()) {
                    tv_gpst_bpyy.setText("足环错误,羽色错误");
                } else {
                    tv_gpst_bpyy.setText("");
                }

                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                dialog.dismiss();
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(GpsgTagSearchActivity.this);
            dialog = builder.create();
            dialog.setView(dialogLayout);

            //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
            dialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

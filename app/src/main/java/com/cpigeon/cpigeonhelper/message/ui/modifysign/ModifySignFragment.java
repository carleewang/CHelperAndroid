package com.cpigeon.cpigeonhelper.message.ui.modifysign;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.PersonInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.IdCardCameraActivity;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardNInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardPInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.adapter.PersonImageInfoAdapter;
import com.cpigeon.cpigeonhelper.message.base.BaseActivity;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.FileTool;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.PermissonUtil;
import com.cpigeon.cpigeonhelper.utils.PhotoUtil;
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class ModifySignFragment extends BaseMVPFragment<PersonSignPre> {


    RecyclerView recyclerView;
    PersonImageInfoAdapter adapter;

    TextView btn;

    EditText edSign;
    private int PHOTO_SUCCESS_REQUEST = 2083;

    PersonInfoEntity entity;

    List<String> imgs;


    @Override
    protected PersonSignPre initPresenter() {
        return new PersonSignPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        PermissonUtil.getAppDetailSettingIntent(getActivity());//权限检查（添加权限）
        setTitle("修改签名");
        imgs = Lists.newArrayList("idCard_P", "idCard_N", "license");
        hideSoftInput();
        initView();
        getData();
    }

    private void getData() {
        showLoading();
        mPresenter.getPersonSignInfo(r -> {
            hideLoading();
            if (r.status) {
                entity = r.data;
                FileTool.byte2File(entity.sfzzm, getContext().getCacheDir().getPath(), imgs.get(0));
                FileTool.byte2File(entity.sfzbm, getContext().getCacheDir().getPath(), imgs.get(1));
                FileTool.byte2File(entity.yyzz, getContext().getCacheDir().getPath(), imgs.get(2));
                hideLoading();
                bindData();
            } else {
                error(r.msg);
            }
        });
    }

    private void bindData() {
        adapter.setNewData(imgs);
//        if () {
//
//        }
        if (!entity.isExamine()) {
            edSign.setText(entity.shenqingqm);
            btn.setText("签名正在审核中");
            btn.setEnabled(false);
            adapter.setOnItemClickListener(null);
        } else {
            setBtn();
            edSign.setText(entity.qianming);
            adapter.setOnItemClickListener((adapter1, view, position) -> {
                if (position == 0) {//身份证正面
                    IntentBuilder.Builder(getActivity(), IdCardCameraActivity.class)
                            .putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_P)
                            .startActivity(getActivity(), IdCardCameraActivity.CODE_ID_CARD_P);
                } else if (position == 1) {//身份中反面
                    IntentBuilder.Builder(getActivity(), IdCardCameraActivity.class)
                            .putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_N)
                            .startActivity(getActivity(), IdCardCameraActivity.CODE_ID_CARD_N);
                } else if (position == 2) {//营业执照
                    new SaActionSheetDialog(getContext())
                            .builder()
                            .addSheetItem("相册选取", OnSheetItemClickListener)
                            .addSheetItem("拍一张", OnSheetItemClickListener)
                            .show();
                }
            });
        }
    }

    protected void initView() {
        findViewById(R.id.ll1).setVisibility(View.GONE);
        btn = findViewById(R.id.text_btn);
        btn.setVisibility(View.VISIBLE);
        btn.setText("提交签名");
        btn.setEnabled(false);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new PersonImageInfoAdapter(getContext());
        adapter.bindToRecyclerView(recyclerView);
        adapter.addHeaderView(initHeadView());
        adapter.setNewData(Lists.newArrayList("", "", ""));
        recyclerView.requestFocus();


    }

    private void setBtn() {
        btn.setEnabled(true);
        btn.setOnClickListener(v -> {
            showTips("正在修改", TipType.LoadingShow);
            mPresenter.modifySign(r -> {
                showTips("", TipType.LoadingHide);
                if (r.status) {
//                    ToastUtil.showLongToast(getContext(),"修改成功");
//                    finish();

                    CommonUitls.showSweetDialog2(getActivity(), "修改成功", dialog -> {
                        dialog.dismiss();

                        finish();
                    });

                } else {
                    error(r.msg);
                }
            });
        });
    }

    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 2:
                    goCamera();//相机
                    break;
                case 1:
                    goGallry();//相册
                    break;
            }
        }
    };

    private void goGallry() {
        BaseActivity baseActivity = (BaseActivity) getContext();
        MultiImageSelector.create()
                .showCamera(true)
                .single()
                .start(baseActivity, PHOTO_SUCCESS_REQUEST);
    }

    private void goCamera() {
        PhotoUtil.photo(this, uri -> {
            mPresenter.license = PhotoUtil.getPath(getActivity(), uri);
        });
    }

    protected View initHeadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_modify_sign_head_layout, recyclerView, false);
        edSign = findViewById(view, R.id.sign);
        bindUi(RxUtils.textChanges(edSign), mPresenter.setSign());
        edSign.clearFocus();
        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_with_button_layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
            if (IdCardCameraActivity.CODE_ID_CARD_P == requestCode) {
                IdCardPInfoEntity idCardInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                mPresenter.name = idCardInfoEntity.name;
                mPresenter.sex = idCardInfoEntity.sex;
                mPresenter.address = idCardInfoEntity.address;
                mPresenter.familyName = idCardInfoEntity.nation;
                mPresenter.idCardNumber = idCardInfoEntity.id;
                AppCompatImageView imageView = (AppCompatImageView) adapter.getViewByPosition(recyclerView, 1, R.id.icon);
                imageView.setImageBitmap(BitmapFactory.decodeFile(idCardInfoEntity.frontimage));
                mPresenter.idCardP = idCardInfoEntity.frontimage;
            } else if (IdCardCameraActivity.CODE_ID_CARD_N == requestCode) {
                IdCardNInfoEntity idCardNInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                mPresenter.organization = idCardNInfoEntity.authority;
                mPresenter.idCardDate = idCardNInfoEntity.valid_date;
                AppCompatImageView imageView = (AppCompatImageView) adapter.getViewByPosition(recyclerView, 2, R.id.icon);
                imageView.setImageBitmap(BitmapFactory.decodeFile(idCardNInfoEntity.backimage));
                mPresenter.idCardN = idCardNInfoEntity.backimage;
            }

        }

        if (requestCode == PHOTO_SUCCESS_REQUEST) {
            if (data != null && data.hasExtra(MultiImageSelectorActivity.EXTRA_RESULT)) {
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // do your logic ....
                mPresenter.license = path.get(0);
                AppCompatImageView imageView = (AppCompatImageView) adapter.getViewByPosition(recyclerView, 3, R.id.icon);
                imageView.setImageBitmap(BitmapFactory.decodeFile(path.get(0)));
            }
        }

        if (requestCode == PhotoUtil.CAMERA_SUCCESS && resultCode == -1) {
            AppCompatImageView imageView = (AppCompatImageView) adapter.getViewByPosition(recyclerView, 3, R.id.icon);
            imageView.setImageBitmap(BitmapFactory.decodeFile(mPresenter.license));
        }
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

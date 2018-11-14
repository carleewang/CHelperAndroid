package com.cpigeon.cpigeonhelper.modular.home.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.home.presenter.UlageToolPresenter;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.daima.PickerChooseUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 空距测量小工具
 * Created by Administrator on 2018/1/22.
 */

public class UllageToolActivity extends ToolbarBaseActivity {

    @BindView(R.id.et_ullage1_lo1)
    EditText etUllage1Lo1;
    @BindView(R.id.et_ullage1_lo2)
    EditText etUllage1Lo2;
    @BindView(R.id.et_ullage1_lo3)
    EditText etUllage1Lo3;
    @BindView(R.id.et_ullage1_la1)
    EditText etUllage1La1;
    @BindView(R.id.et_ullage1_la2)
    EditText etUllage1La2;
    @BindView(R.id.et_ullage1_la3)
    EditText etUllage1La3;
    @BindView(R.id.et_ullage2_lo1)
    EditText etUllage2Lo1;
    @BindView(R.id.et_ullage2_lo2)
    EditText etUllage2Lo2;
    @BindView(R.id.et_ullage2_lo3)
    EditText etUllage2Lo3;
    @BindView(R.id.et_ullage2_la1)
    EditText etUllage2La1;
    @BindView(R.id.et_ullage2_la2)
    EditText etUllage2La2;
    @BindView(R.id.et_ullage2_la3)
    EditText etUllage2La3;
    @BindView(R.id.tv_djjs)
    TextView tvDjjs;
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.tv_time2)
    TextView tvTime2;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_ullage_tool;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTitle(getString(R.string.str_ullage));
        setTopLeftButton(R.drawable.ic_back, this::finish);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        etUllage1Lo1.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage1Lo1, 1, etUllage1Lo2));
        etUllage1Lo2.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage1Lo2, 3, etUllage1Lo3));
        etUllage1Lo3.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage1Lo3, 4, etUllage1La1));

        etUllage1La1.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage1La1, 2, etUllage1La2));
        etUllage1La2.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage1La2, 3, etUllage1La3));
        etUllage1La3.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage1La3, 4, etUllage2Lo1));

        etUllage2Lo1.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage2Lo1, 1, etUllage2Lo2));
        etUllage2Lo2.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage2Lo2, 3, etUllage2Lo3));
        etUllage2Lo3.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage2Lo3, 4, etUllage2La1));

        etUllage2La1.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage2La1, 2, etUllage2La2));
        etUllage2La2.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage2La2, 3, etUllage2La3));
        etUllage2La3.addTextChangedListener(UlageToolPresenter.setLoLaSListener(this, etUllage2La3, 4, etUllage2La3));
    }

    @OnClick({R.id.ll_xzsj1, R.id.ll_xzsj2, R.id.tv_djjs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_xzsj1://司放地经纬度选择时间
                PickerChooseUtil.showTimePickerChooseSMF(this, tvTime1);
                break;
            case R.id.ll_xzsj2://鸽舍经纬度选择时间
                PickerChooseUtil.showTimePickerChooseSMF(this, tvTime2);
                break;
            case R.id.tv_djjs://点击计算
                uploadIdCardInfo();
                break;
        }
    }

    public void uploadIdCardInfo() {

        Map<String, Object> postParams = new HashMap<>();//存放参数
        long timestamp;//时间搓
        timestamp = System.currentTimeMillis() / 1000;

        if (errotToast(etUllage1Lo1)) return;
        if (errotToast(etUllage1Lo2)) return;
        if (errotToast(etUllage1Lo3)) return;
        if (errotToast(etUllage1La1)) return;
        if (errotToast(etUllage1La2)) return;
        if (errotToast(etUllage1La3)) return;

        if (errotToast(etUllage2Lo1)) return;
        if (errotToast(etUllage2Lo2)) return;
        if (errotToast(etUllage2Lo3)) return;
        if (errotToast(etUllage2La1)) return;
        if (errotToast(etUllage2La2)) return;
        if (errotToast(etUllage2La3)) return;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("fangfeijingdu_du", etUllage1Lo1.getText().toString());//
        postParams.put("fangfeijingdu_fen", etUllage1Lo2.getText().toString());//
        postParams.put("fangfeijingdu_miao", etUllage1Lo3.getText().toString());//
        postParams.put("fangfeiweidu_du", etUllage1La1.getText().toString());//
        postParams.put("fangfeiweidu_fen", etUllage1La2.getText().toString());//
        postParams.put("fangfeiweidu_miao", etUllage1La3.getText().toString());//


        postParams.put("guichaojingdu_du", etUllage2Lo1.getText().toString());//
        postParams.put("guichaojingdu_fen", etUllage2Lo2.getText().toString());//
        postParams.put("guichaojingdu_miao", etUllage2Lo3.getText().toString());//
        postParams.put("guichaoweidu_du", etUllage2La1.getText().toString());//
        postParams.put("guichaoweidu_fen", etUllage2La2.getText().toString());//
        postParams.put("guichaoweidu_miao", etUllage2La3.getText().toString());//

        if (!tvTime1.getText().toString().equals("请选择时间")) {
            Log.d(TAG, "时间: " + DateUtils.hmsTolong(tvTime1.getText().toString()));
        }

        if (!tvTime2.getText().toString().equals("请选择时间")) {
            Log.d(TAG, "时间: " + DateUtils.hmsTolong(tvTime2.getText().toString()));
        }

        RetrofitHelper.getApi()
                .getKongju(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {

                    try {
                        if (data.getErrorCode() == 0) {
                            if (data.getData() != null) {
                                double result = data.getData().getResult();

                                Intent intent = new Intent(UllageToolActivity.this, UllageToolDetailsActivity.class);
                                intent.putExtra("sfd_lo", CommonUitls.Aj2GPSLocation(UlageToolPresenter.strPj(etUllage1Lo1, etUllage1Lo2, etUllage1Lo3)));
                                intent.putExtra("sfd_la", CommonUitls.Aj2GPSLocation(UlageToolPresenter.strPj(etUllage1La1, etUllage1La2, etUllage1La3)));
                                intent.putExtra("gc_lo", CommonUitls.Aj2GPSLocation(UlageToolPresenter.strPj(etUllage2Lo1, etUllage2Lo2, etUllage2Lo3)));
                                intent.putExtra("gc_la", CommonUitls.Aj2GPSLocation(UlageToolPresenter.strPj(etUllage2La1, etUllage2La2, etUllage2La3)));
                                intent.putExtra("result", String.valueOf(result));

                                if (!tvTime1.getText().toString().equals("请选择时间") && !tvTime2.getText().toString().equals("请选择时间")) {
                                    double time1 = DateUtils.hmsTolong(tvTime1.getText().toString());
                                    double time2 = DateUtils.hmsTolong(tvTime2.getText().toString());
                                    if (time2 < time1) {
                                        CommonUitls.showSweetDialog(UllageToolActivity.this, "归巢时间小于司放时间");
                                        return;
                                    }
                                    intent.putExtra("time", String.valueOf(CommonUitls.doubleformat(result * 1000 / (time2 - time1), 2) + "米/秒"));
                                }

                                startActivity(intent);
                            }
                        } else {
                            CommonUitls.showSweetDialog(UllageToolActivity.this, "输入经纬度有误");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, throwable -> {
                    try {
                        CommonUitls.showSweetDialog(UllageToolActivity.this, throwable.getLocalizedMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    //判读输入文本输入是否为空
    public boolean errotToast(EditText et) {
        if (et.getText().toString().equals("")) {
            CommonUitls.showToast(UllageToolActivity.this, "输入内容不能为空");
            return true;
        } else {
            return false;
        }
    }
}

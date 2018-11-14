package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.content.Intent;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearSelectionEntitiy;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl.YearSelectionAddActivity;
import com.cpigeon.cpigeonhelper.utils.string.MyString;

import java.util.List;

/**
 * 年度评选
 * Created by Administrator on 2018/4/4.
 */

public class YearSelectionAdapter extends BaseQuickAdapter<YearSelectionEntitiy, BaseViewHolder> {

    private Intent intent;
    private HyUserDetailEntity mHyUserDetailEntity;
    private String type = "myself";//look :上级协会查看   myself：自己查看

    public YearSelectionAdapter(List<YearSelectionEntitiy> data,HyUserDetailEntity mHyUserDetailEntity,String type) {
        super(R.layout.item_year_selection, data);
        this.mHyUserDetailEntity = mHyUserDetailEntity;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, YearSelectionEntitiy item) {

        helper.setText(R.id.tv_pxresult, item.getPxresult());
        helper.setText(R.id.tv_time, item.getPxsj());//时间
        helper.setText(R.id.tv_pxcomment, "评语：" + item.getPxcomment());//评语


        ImageView img_pxresult = helper.getView(R.id.img_pxresult);
        switch (item.getPxresult()) {

            case MyString.str_ndpy_pxresult_1:

                img_pxresult.setImageResource(R.mipmap.hygl_pxresult_zjsfy);

                break;
            case MyString.str_ndpy_pxresult_2:
                img_pxresult.setImageResource(R.mipmap.hygl_pxresult_zjgp);
                break;

            case MyString.str_ndpy_pxresult_3:
                img_pxresult.setImageResource(R.mipmap.hygl_pxresult_zjcp);
                break;
            case MyString.str_ndpy_pxresult_4:
                img_pxresult.setImageResource(R.mipmap.hygl_pxresult_zjhy);
                break;
        }

        if (type.equals("myself") && !mHyUserDetailEntity.getZhuangtai().equals("除名")) {
            helper.getView(R.id.ll_itemZ).setOnClickListener(view -> {
                intent = new Intent(mContext, YearSelectionAddActivity.class);
                intent.putExtra("mYearSelectionEntitiy",item);
                intent.putExtra("data", mHyUserDetailEntity);
                mContext.startActivity(intent);
            });
        }
    }
}

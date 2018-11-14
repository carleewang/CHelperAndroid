package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl.OrgItem;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl.RaceItem;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SelectZhActivity2;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2.SGTHomeActivity3;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛格通主页列表的adapter
 * Created by Administrator on 2017/12/1.
 */
public class SGTHomeListAdapter3 extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_ORG = 1;
    public static final int TYPE_RACE = 2;

    private Intent intent;

    public SGTHomeListAdapter3(List<MultiItemEntity> data) {
        super(data);

        addItemType(TYPE_ORG, R.layout.item_list_title);
        addItemType(TYPE_RACE, R.layout.item_list_con_content2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_ORG:

                SGTHomeListEntity mSGTHomeListEntity = ((OrgItem) item).getOrgInfo();

                if (mSGTHomeListEntity.getTag() == 1) {
                    ((ImageButton) helper.getView(R.id.it_sgt_z_l_btn)).setImageResource(R.mipmap.right_img);
                    helper.getView(R.id.item_dividerline).setVisibility(View.VISIBLE);
                } else {
                    ((ImageButton) helper.getView(R.id.it_sgt_z_l_btn)).setImageResource(R.mipmap.zhankai_img);
                    helper.getView(R.id.item_dividerline).setVisibility(View.GONE);
                }
                helper.setText(R.id.it_sgt_z_name, mSGTHomeListEntity.getXingming());
                helper.setText(R.id.it_sgt_z_num, mSGTHomeListEntity.getCount() + "羽");

                if (SGTHomeActivity3.isShowPhone == 1) {
                    helper.getView(R.id.it_sgt_z_r_btn).setVisibility(View.GONE);
                }

                helper.getView(R.id.it_sgt_z_r_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(mContext, SelectZhActivity2.class);
                        intent.putExtra("id", mSGTHomeListEntity.getId());
                        intent.putExtra("tagid", 7);
                        intent.putExtra("tagStr", "入棚拍照");

                        mContext.startActivity(intent);
                    }
                });

                break;
            case TYPE_RACE:

                SGTHomeListEntity.DataBean mDataBean = ((RaceItem) item).getRace();
                helper.getView(R.id.ll_z).setClickable(false);

                helper.setText(R.id.it_sgt_name, mDataBean.getFoot());
                helper.setText(R.id.it_sgt_num, mDataBean.getColor());
                helper.setText(R.id.tv_img_num, mDataBean.getPic());
                break;
        }
    }

    public static List<MultiItemEntity> setSGTHomeData(List<SGTHomeListEntity> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null) {
            return result;
        }
        OrgItem orgItem;
        RaceItem raceItem;
        if (data.size() > 0) {
            for (SGTHomeListEntity orginfo : data) {
                orgItem = new OrgItem(orginfo);
                if (orginfo.getData() != null)
                    for (SGTHomeListEntity.DataBean race : orginfo.getData()) {
                        raceItem = new RaceItem(race);
                        orgItem.addSubItem(raceItem);
                    }
                result.add(orgItem);
            }
        }
        return result;
    }
}

package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SGTDetailsActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2.SGTHomeActivity3;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.video.RecordedSGTActivity;

import java.util.ArrayList;
import java.util.List;

import static com.cpigeon.cpigeonhelper.utils.PermissonUtil.cameraIsCanUse;

/**
 * 赛格通页面适配器
 * Created by Administrator on 2017/12/4.
 */
public class SGTSearchAdapter3 extends BaseQuickAdapter<FootSSEntity, BaseViewHolder> {

    private Intent intent;
    private List<TagEntitiy> tagDatas = new ArrayList<>();
    private SaActionSheetDialog dialog;
    private SaActionSheetDialog dialogFoot;

    private SGTPresenter mSGTPresenter;

    private String tagStr;// 选择的标签名称
    private int tagid;// 选择的标签名称
    private int id;


    private List<FootSSEntity> listdetail = new ArrayList<FootSSEntity>();

    public SGTSearchAdapter3(Context myContext, List<FootSSEntity> data, SGTPresenter mSGTPresenter) {
        super(R.layout.item_list_con_content, data);
        dialog = new SaActionSheetDialog(myContext)
                .builder();
        dialogFoot = new SaActionSheetDialog(myContext).builder();

        dialogFoot.addSheetItem("足环在左脚", onSheetItemClickListener2);
        dialogFoot.addSheetItem("足环在右脚", onSheetItemClickListener2);

        this.mSGTPresenter = mSGTPresenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, FootSSEntity item) {
        helper.setText(R.id.it_sgt_name, item.getFoot());
        helper.setTextColor(R.id.it_sgt_name, R.color.color_262626);

        helper.setText(R.id.it_sgt_num, item.getXingming());
        helper.setTextColor(R.id.it_sgt_num, R.color.color_262626);

        ImageButton imageButton = helper.getView(R.id.it_sgt_r_btn);

        if (SGTHomeActivity3.isShowPhone == 1) {
            imageButton.setVisibility(View.GONE);
        } else {
            imageButton.setVisibility(View.VISIBLE);
        }

        imageButton.setOnClickListener(view -> {

                    id = item.getId();
                    listdetail.add(item);//保存数据，跳转页面传递
                    dialogFoot.show();
//                    listdetail.clear();
//                    tagDatas.clear();

//                    mSGTPresenter.getSGTTag(String.valueOf(item.getId()));//获取赛鸽通标签
                }
        );

        helper.getView(R.id.ll_z).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, SGTDetailsActivity.class);
            SGTHomeListEntity.DataBean dataBean = new SGTHomeListEntity.DataBean();
            dataBean.setId(String.valueOf(item.getId()));
            dataBean.setFoot(item.getFoot());
            dataBean.setCskh(item.getCskh());
            intent.putExtra("DataBean", dataBean);


            SGTHomeListEntity sgtHomeListEntity = new SGTHomeListEntity();
            sgtHomeListEntity.setXingming(item.getXingming());
            sgtHomeListEntity.setCskh(item.getCskh());
            intent.putExtra("SGTHomeListEntity", sgtHomeListEntity);
            mContext.startActivity(intent);
        });
    }


    public void setSgtTAG(List<TagEntitiy> tagDatas) {
        this.tagDatas = tagDatas;
        dialog.clearSheetItem();//清空dialog数据

        //上传标签判断
        if (tagDatas.size() > 0) {
            for (int i = 0; i < tagDatas.size(); i++) {
                dialog.addSheetItem(tagDatas.get(i).getName(), onSheetItemClickListener);
            }
        }

        dialog.show();
    }

    /**
     * 弹出选择标签  足环标签
     */
    private SaActionSheetDialog.OnSheetItemClickListener onSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {

            if (tagDatas.size() > 0) {
                tagid = tagDatas.get(which - 1).getTid();
                tagStr = tagDatas.get(which - 1).getName();
                dialogFoot.show();
            }
        }
    };

    /**
     * 足环左右脚
     */
    private SaActionSheetDialog.OnSheetItemClickListener onSheetItemClickListener2 = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {

            if (!cameraIsCanUse()) {
                return;
            }

            intent = new Intent(mContext, RecordedSGTActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("tagid", 7);
            intent.putExtra("tagStr", "入棚拍照");

            if (which == 1) {
                intent.putExtra("IMG_NUM_TAG", 2);//左脚
            } else {
                intent.putExtra("IMG_NUM_TAG", 3);//右脚
            }

            intent.putParcelableArrayListExtra("listdetail", (ArrayList<? extends Parcelable>) listdetail);

            mContext.startActivity(intent);
            listdetail.clear();
        }
    };
}



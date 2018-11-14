package com.cpigeon.cpigeonhelper.ui.imgupload;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.R;


/**
 * Created by hcc on 16/8/7 21:18
 * 100332338@qq.com
 * <p/>
 * 图片右上角x，点击删除
 */
public class AddImgView extends FrameLayout {

    private ImageView mEmptyImg;//图片展示

    private CheckBox mCheckBox;//右上角的x

    public String imgFile;//图片路径

    private int index = -1;//当前控件在父类控件中的下标


    public AddImgView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init();
    }


    public AddImgView(Context context) {

        this(context, null);
    }


    public AddImgView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }


    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_add_photo_gv_items, this);
        mEmptyImg = (ImageView) view.findViewById(R.id.main_gridView_item_photo);
        mCheckBox = (CheckBox) view.findViewById(R.id.main_gridView_item_cb);

        mCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetClickIndex.getViewClickIndex(index);
            }
        });
    }

    public GetClickIndex mGetClickIndex;

    public interface GetClickIndex {
        void getViewClickIndex(int index);
    }

    public void setEmptyImage(int imgRes) {
        mEmptyImg.setImageResource(imgRes);
    }

    public void setAddImage(String imgFile) {
        this.imgFile = imgFile;
        mEmptyImg.setImageBitmap(BitmapFactory.decodeFile(imgFile));
    }


    public void setEmptyImageUrl(String imgFile) {
        this.imgFile = imgFile;
        Glide.with(getContext()).load(imgFile).into(mEmptyImg);
    }

    public CheckBox getmCheckBox() {
        return this.mCheckBox;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }


}

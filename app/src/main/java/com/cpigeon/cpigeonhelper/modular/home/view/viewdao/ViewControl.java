package com.cpigeon.cpigeonhelper.modular.home.view.viewdao;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;

/**
 * Created by Administrator on 2017/11/27.
 */

public class ViewControl {

    /**
     * 初始化控件
     *
     * @param orgType //1：公棚 2：协会  3：个人
     */
    public static void initView3(int orgType, TextView homeTv1, TextView homeTv3,
                                 ImageButton homeImgbtn1, ImageButton homeImgbtn2,
                                 ImageButton homeImgbtn3, ImageButton homeImgbtn4,
                                 ImageButton homeImgbtn5, ImageButton homeImgbtn6,
                                 ImageButton homeImgbtn7, ImageButton homeImgbtn8,
                                 ImageButton homeImgbtn9, ImageButton homeImgbtn10,
                                 ImageButton homeImgbtn13, ImageButton tv_sggj_tv1,
                                 ImageButton tv_sggj_tv2, ImageButton tv_sggj_tv3,
                                 ImageButton wdfw_btn4, ImageButton wdfw_btn5,
                                 ImageButton wdfw_btn6) {


//      tv_sggj_tv1.setImageResource(R.mipmap.kongjujs);//随拍
        tv_sggj_tv1.setImageResource(R.mipmap.saixiantq);//赛线天气
        tv_sggj_tv2.setImageResource(R.mipmap.kongjujs);//空距小工具

        switch (orgType) {
            case 1:
                //公棚
                homeTv1.setText("公棚管理");
                homeTv3.setText("我的服务");
//                homeImgbtn1.setImageDrawable(getResources().getDrawable(R.mipmap.rupengdx));//入棚短信
                homeImgbtn1.setImageResource(R.mipmap.rupengdx);//入棚短信
                homeImgbtn2.setImageResource(R.mipmap.xunsaidx);//训赛短信
                homeImgbtn3.setImageResource(R.mipmap.shanglongdx);//上笼短信
                homeImgbtn4.setImageResource(R.mipmap.saigetong);//赛鸽通
                homeImgbtn5.setImageResource(R.mipmap.gexintong);//鸽信通
                homeImgbtn6.setImageResource(R.mipmap.geyuntong);//鸽运通


                homeImgbtn7.setImageResource(R.mipmap.chazugl);//插组管理
                homeImgbtn8.setImageBitmap(null);
                homeImgbtn9.setImageBitmap(null);

                //我的服务
                wdfw_btn4.setImageResource(R.mipmap.zhonggew);//中鸽网
                break;
            case 2:
                //协会
                homeTv1.setText("协会管理");
                homeTv3.setText("我的服务");

                homeImgbtn1.setImageResource(R.mipmap.duanxinsz);//短信设置
                homeImgbtn2.setImageResource(R.mipmap.chazugl);//插组指定
                homeImgbtn3.setImageResource(R.mipmap.bisaigl);//比赛管理

                homeImgbtn7.setImageResource(R.mipmap.saishigc);//赛事规程
                homeImgbtn8.setImageResource(R.mipmap.xiehuidt);//协会动态
                homeImgbtn9.setImageResource(R.mipmap.wodehy);//会员管理

                homeImgbtn13.setImageResource(R.mipmap.geyoujl);//鸽友交流

                homeImgbtn4.setImageResource(R.mipmap.geyuntong);//鸽运通
                homeImgbtn5.setImageResource(R.mipmap.gexintong);//鸽信通
                homeImgbtn6.setImageResource(R.mipmap.xungetong);//训鸽通

                //我的服务
                wdfw_btn4.setImageResource(R.mipmap.zhonggew);//中鸽网
                break;
            case 3:
                //个人
                homeTv1.setText("我的服务");
                homeTv3.setText("推荐服务");
                homeImgbtn1.setImageResource(R.mipmap.xungetong);//训鸽通
                homeImgbtn2.setImageResource(R.mipmap.gexintong);//鸽信通
                homeImgbtn3.setImageResource(R.mipmap.geyuntong);//鸽运通
                homeImgbtn4.setImageResource(R.mipmap.zhonggew);//中鸽网
                homeImgbtn5.setImageResource(R.mipmap.zuhuancx);//足环查询
                homeImgbtn6.setImageResource(R.mipmap.tianxiagp);//天下鸽谱
                break;
        }
    }


    public static void initView(
            int orgType, TextView tvHint, TextView glName,
            LinearLayout ll_gl_z, LinearLayout llGlLl1z, LinearLayout llGlLl2z,
            LinearLayout ll_fw_z, LinearLayout llWdfwLl1z, LinearLayout llWdfwLl2z,
            ImageView imgGlLl11, TextView tvGlLl11,
            ImageView imgGlLl12, TextView tvGlLl12,
            ImageView imgGlLl13, TextView tvGlLl13,
            ImageView imgGlLl14, TextView tvGlLl14,

            ImageView imgGlLl21, TextView tvGlLl21,
            ImageView imgGlLl22, TextView tvGlLl22,
            ImageView imgGlLl23, TextView tvGlLl23,
            ImageView imgGlLl24, TextView tvGlLl24,


            ImageView imgWdfwLl11, TextView tvWdfwLl11,
            ImageView imgWdfwLl12, TextView tvWdfwLl12,
            ImageView imgWdfwLl13, TextView tvWdfwLl13,
            ImageView imgWdfwLl14, TextView tvWdfwLl14,

            ImageView imgWdfwLl21, TextView tvWdfwLl21,
            ImageView imgWdfwLl22, TextView tvWdfwLl22,
            ImageView imgWdfwLl23, TextView tvWdfwLl23,
            ImageView imgWdfwLl24, TextView tvWdfwLl24) {


        switch (orgType) {
            case 1:
                //公棚
                tvHint.setVisibility(View.GONE);
                llGlLl2z.setVisibility(View.GONE);
                glName.setText("公棚管理");

                imgGlLl11.setImageResource(R.mipmap.rupengdx1);//入棚短信
                tvGlLl11.setText("入棚短信");
                imgGlLl12.setImageResource(R.mipmap.xunsaidx1);//训赛短信
                tvGlLl12.setText("训赛短信");
                imgGlLl13.setImageResource(R.mipmap.shanglongdx1);//上笼短信
                tvGlLl13.setText("上笼短信");
                imgGlLl14.setImageResource(R.mipmap.chazugl1);//插组管理
                tvGlLl14.setText("插组管理");


                imgWdfwLl11.setImageResource(R.mipmap.saigetong1);//公棚赛鸽
                tvWdfwLl11.setText("公棚赛鸽");
                imgWdfwLl12.setImageResource(R.mipmap.gexintong1);//鸽信通
                tvWdfwLl12.setText(MyApplication.getContext().getString(R.string.str_sms));
                imgWdfwLl13.setImageResource(R.mipmap.geyuntong1);//鸽运通
                tvWdfwLl13.setText(MyApplication.getContext().getString(R.string.str_gyt));
                imgWdfwLl14.setImageResource(R.mipmap.shouquanpz1);//授权拍照
                tvWdfwLl14.setText(MyApplication.getContext().getString(R.string.str_authorization_taking_pictures));

                imgWdfwLl21.setImageResource(R.mipmap.saixiantq1);//赛线天气
                tvWdfwLl21.setText(MyApplication.getContext().getString(R.string.str_line_weather));
                imgWdfwLl22.setImageResource(R.mipmap.kongjujs1);//空距计算
                tvWdfwLl22.setText(MyApplication.getContext().getString(R.string.str_ullage));
                imgWdfwLl23.setImageResource(R.mipmap.zhonggew1);//中鸽网
                tvWdfwLl23.setText(MyApplication.getContext().getString(R.string.str_cpigeon));

                break;
            case 2:
                //协会
                tvHint.setVisibility(View.GONE);
                glName.setText("协会管理");

                imgGlLl11.setImageResource(R.mipmap.duanxinsz2);//短信设置
                tvGlLl11.setText("报到短信");
                imgGlLl12.setImageResource(R.mipmap.chazugl2);//插组管理
                tvGlLl12.setText("插组管理");
                imgGlLl13.setImageResource(R.mipmap.bisaigl2);//比赛管理
                tvGlLl13.setText("比赛管理");
                imgGlLl14.setImageResource(R.mipmap.saishigc2);//赛事规程
                tvGlLl14.setText("赛事规程");


                imgGlLl21.setImageResource(R.mipmap.xiehuidt2);//协会动态
                tvGlLl21.setText("协会动态");
                imgGlLl22.setImageBitmap(null);
                tvGlLl22.setText("");
                imgGlLl23.setImageBitmap(null);
                tvGlLl23.setText("");
                imgGlLl24.setImageBitmap(null);
                tvGlLl24.setText("");


                imgWdfwLl11.setImageResource(R.mipmap.geyuntong2);//鸽运通
                tvWdfwLl11.setText(MyApplication.getContext().getString(R.string.str_gyt));
                imgWdfwLl12.setImageResource(R.mipmap.gexintong2);//鸽信通
                tvWdfwLl12.setText(MyApplication.getContext().getString(R.string.str_sms));
                imgWdfwLl13.setImageResource(R.mipmap.xungetong2);//训鸽通
                tvWdfwLl13.setText("训鸽通");
                imgWdfwLl14.setImageResource(R.mipmap.saixiantq2);//赛线天气
                tvWdfwLl14.setText(MyApplication.getContext().getString(R.string.str_line_weather));


                imgWdfwLl21.setImageResource(R.mipmap.kongjujs2);//空距计算
                tvWdfwLl21.setText(MyApplication.getContext().getString(R.string.str_ullage));
                imgWdfwLl22.setImageResource(R.mipmap.zhonggew2);//中鸽网
                tvWdfwLl22.setText(MyApplication.getContext().getString(R.string.str_cpigeon));
                imgWdfwLl23.setImageResource(R.mipmap.shouquanpz2);//授权拍照
                tvWdfwLl23.setText(MyApplication.getContext().getString(R.string.str_authorization_taking_pictures));
                break;

            case 3:
                //个人

                tvHint.setVisibility(View.VISIBLE);
                ll_gl_z.setVisibility(View.GONE);

                imgWdfwLl11.setImageResource(R.mipmap.xungetong3);//训鸽通
                tvWdfwLl11.setText("训鸽通");
                imgWdfwLl12.setImageResource(R.mipmap.gexintong3);//鸽信通
                tvWdfwLl12.setText(MyApplication.getContext().getString(R.string.str_sms));
                imgWdfwLl13.setImageResource(R.mipmap.geyuntong3);//鸽运通
                tvWdfwLl13.setText(MyApplication.getContext().getString(R.string.str_gyt));
                imgWdfwLl14.setImageResource(R.mipmap.saixiantq3);//赛线天气
                tvWdfwLl14.setText(MyApplication.getContext().getString(R.string.str_line_weather));

                imgWdfwLl21.setImageResource(R.mipmap.kongjujs3);//空距计算
                tvWdfwLl21.setText(MyApplication.getContext().getString(R.string.str_ullage));
                imgWdfwLl22.setImageResource(R.mipmap.zhonggew3);//中鸽网
                tvWdfwLl22.setText(MyApplication.getContext().getString(R.string.str_cpigeon));
                imgWdfwLl23.setImageResource(R.mipmap.tianxiagp3);//天下鸽谱
                tvWdfwLl23.setText(MyApplication.getContext().getString(R.string.str_book));
                imgWdfwLl24.setImageResource(R.mipmap.shouquanpz3);//授权拍照
                tvWdfwLl24.setText(MyApplication.getContext().getString(R.string.str_authorization_taking_pictures));

                break;
        }
    }
}

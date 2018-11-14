package com.cpigeon.cpigeonhelper.utils.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.LinkagePicker;
import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by Administrator on 2018/6/27.
 */

public class MyAddressPicker2 extends LinkagePicker<Province, City, County> {
    private MyAddressPicker2.OnAddressPickListener onAddressPickListener;
    private MyAddressPicker2.OnWheelListener onWheelListener;
    //只显示地市及区县
    private boolean hideProvince = false;
    //只显示省份及地市
    private boolean hideCounty = false;
    //省市区数据
    private ArrayList<Province> provinces = new ArrayList<>();

    public MyAddressPicker2(Activity activity, ArrayList<Province> provinces,String mCity) {
        super(activity, new MyAddressPicker2.AddressProvider2(provinces,mCity));
        this.provinces = provinces;
    }

    /**
     * 设置默认选中的省市县
     */
    public void setSelectedItem(Province province, City city, County county) {
        super.setSelectedItem(province, city, county);
    }

    public void setSelectedItem(String province, String city, String county) {
        setSelectedItem(new Province(province), new City(city), new County(county));
    }

    public Province getSelectedProvince() {
        return provinces.get(selectedFirstIndex);
    }

    public City getSelectedCity() {
        List<City> cities = getSelectedProvince().getCities();
        if (cities.size() == 0) {
            return null;//可能没有第二级数据
        }
        return cities.get(selectedSecondIndex);
    }

    public County getSelectedCounty() {
        List<County> counties = getSelectedCity().getCounties();
        if (counties.size() == 0) {
            return null;//可能没有第三级数据
        }
        return counties.get(selectedThirdIndex);
    }

    /**
     * 隐藏省级行政区，只显示地市级和区县级。
     * 设置为true的话，地址数据中只需要某个省份的即可
     * 参见示例中的“assets/city2.json”
     */
    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    /**
     * 隐藏县级行政区，只显示省级和市级。
     * 设置为true的话，hideProvince将强制为false
     * 数据源依然使用“assets/city.json” 仅在逻辑上隐藏县级选择框，实际项目中应该去掉县级数据。
     */
    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    /**
     * 设置滑动监听器
     */
    public void setOnWheelListener(MyAddressPicker2.OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setOnAddressPickListener(MyAddressPicker2.OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }

    /**
     * @deprecated use {@link #setOnAddressPickListener(MyAddressPicker2.OnAddressPickListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnLinkageListener(OnLinkageListener onLinkageListener) {
        throw new UnsupportedOperationException("Please use setOnAddressPickListener instead.");
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        if (null == provider) {
            throw new IllegalArgumentException("please set address provider before make view");
        }
        float provinceWeight = firstColumnWeight;
        float cityWeight = secondColumnWeight;
        float countyWeight = thirdColumnWeight;
        if (hideCounty) {
            hideProvince = false;
        }
        if (hideProvince) {
            provinceWeight = 0;
            cityWeight = firstColumnWeight;
            countyWeight = secondColumnWeight;
        }
        dividerConfig.setRatio(WheelView.DividerConfig.FILL);

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        final WheelView provinceView = createWheelView();
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, provinceWeight));
        layout.addView(provinceView);
        if (hideProvince) {
            provinceView.setVisibility(View.GONE);
        }

        final WheelView cityView = createWheelView();
        cityView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, cityWeight));
        layout.addView(cityView);

        final WheelView countyView = createWheelView();
        countyView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, countyWeight));
        layout.addView(countyView);
        if (hideCounty) {
            countyView.setVisibility(View.GONE);
        }

        provinceView.setItems(provider.initFirstData(), selectedFirstIndex);
        provinceView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedFirstIndex = index;
                selectedFirstItem = getSelectedProvince();
                if (onWheelListener != null) {
                    onWheelListener.onProvinceWheeled(selectedFirstIndex, selectedFirstItem);
                }
                LogUtils.verbose(this, "change cities after province wheeled: index=" + index);
                selectedSecondIndex = 0;//重置地级索引
                selectedThirdIndex = 0;//重置县级索引
                //根据省份获取地市
                //noinspection unchecked
                List<City> cities = provider.linkageSecondData(selectedFirstIndex);
                if (cities.size() > 0) {
                    selectedSecondItem = cities.get(selectedSecondIndex);
                    cityView.setItems(cities, selectedSecondIndex);
                } else {
                    selectedSecondItem = null;
                    cityView.setItems(new ArrayList<String>());
                }
                //根据地市获取区县
                //noinspection unchecked
                List<County> counties = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
                if (counties.size() > 0) {
                    selectedThirdItem = counties.get(selectedThirdIndex);
                    countyView.setItems(counties, selectedThirdIndex);
                } else {
                    selectedThirdItem = null;
                    countyView.setItems(new ArrayList<String>());
                }
            }
        });

        cityView.setItems(provider.linkageSecondData(selectedFirstIndex), selectedSecondIndex);
        cityView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedSecondIndex = index;
                selectedSecondItem = getSelectedCity();
                if (onWheelListener != null) {
                    onWheelListener.onCityWheeled(selectedSecondIndex, selectedSecondItem);
                }
                LogUtils.verbose(this, "change counties after city wheeled: index=" + index);
                selectedThirdIndex = 0;//重置县级索引
                //根据地市获取区县
                //noinspection unchecked
                List<County> counties = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
                if (counties.size() > 0) {
                    selectedThirdItem = counties.get(selectedThirdIndex);
                    //若不是用户手动滚动，说明联动需要指定默认项
                    countyView.setItems(counties, selectedThirdIndex);
                } else {
                    selectedThirdItem = null;
                    countyView.setItems(new ArrayList<String>());
                }
            }
        });

//        countyView.setItems(provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex), selectedThirdIndex);
        countyView.setItems(provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex));
        countyView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedThirdIndex = index;
                selectedThirdItem = getSelectedCounty();
                if (onWheelListener != null) {
                    onWheelListener.onCountyWheeled(selectedThirdIndex, selectedThirdItem);
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onAddressPickListener != null) {
            Province province = getSelectedProvince();
            City city = getSelectedCity();
            County county = null;
            if (!hideCounty) {
                county = getSelectedCounty();
            }
            onAddressPickListener.onAddressPicked(province, city, county);
        }
    }

    /**
     * 地址选择回调
     */
    public interface OnAddressPickListener {

        void onAddressPicked(Province province, City city, County county);

    }

    /**
     * 滑动回调
     */
    public interface OnWheelListener {

        void onProvinceWheeled(int index, Province province);

        void onCityWheeled(int index, City city);

        void onCountyWheeled(int index, County county);

    }

    /**
     * 地址提供者
     */
    private static class AddressProvider2 implements Provider<Province, City, County> {
        private List<Province> firstList = new ArrayList<>();
        private List<List<City>> secondList = new ArrayList<>();
//        private List<List<List<County>>> thirdList = new ArrayList<>();
        private String mCity;

        public AddressProvider2(List<Province> provinces,String mCity) {
            this.mCity = mCity;
            parseData(provinces,mCity);
        }

        @Override
        public boolean isOnlyTwo() {
//            return thirdList.size() == 0;
            return  true;
        }

        @Override
        @NonNull
        public List<Province> initFirstData() {
            return firstList;
        }

        @Override
        @NonNull
        public List<City> linkageSecondData(int firstIndex) {
            return secondList.get(firstIndex);
        }

        @Override
        @NonNull
        public List<County> linkageThirdData(int firstIndex, int secondIndex) {
//            return thirdList.get(firstIndex).get(secondIndex);
            return null;
        }

        private void parseData(List<Province> data,String  mCity) {
            int provinceSize = data.size();
            Province pro1 = new Province("全部");
            firstList.add(pro1);

            //添加省
            for (int x = 0; x < provinceSize; x++) {

                Province pro = data.get(x);
                if (pro.getName().indexOf(mCity)!=-1){
                    firstList.add(pro);
                    List<City> cities = pro.getCities();
                    List<City> xCities = new ArrayList<>();
                    int citySize = cities.size();
                    xCities.add(new City("1","全部地区"));
                    //添加地市
                    for (int y = 0; y < citySize; y++) {
                        City cit = cities.get(y);
                        cit.setProvinceId(pro.getAreaId());
                        xCities.add(cit);
                    }
                    secondList.add(xCities);
                }
            }
        }
    }
}

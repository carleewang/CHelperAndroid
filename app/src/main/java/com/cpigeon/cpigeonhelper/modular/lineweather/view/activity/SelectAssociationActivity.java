package com.cpigeon.cpigeonhelper.modular.lineweather.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseActivity;
import com.cpigeon.cpigeonhelper.message.ui.selectPhoneNumber.pinyin.CharacterParser;
import com.cpigeon.cpigeonhelper.message.ui.selectPhoneNumber.pinyin.PinyinComparator3;
import com.cpigeon.cpigeonhelper.modular.lineweather.model.bean.ContactModel2;
import com.cpigeon.cpigeonhelper.modular.lineweather.presenter.LineWeatherPresenter;
import com.cpigeon.cpigeonhelper.modular.lineweather.view.adapter.SelectAssociationAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AssociationListEntity;
import com.cpigeon.cpigeonhelper.ui.AlwaysMarqueeTextView;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.jiang.android.lib.adapter.expand.StickyRecyclerHeadersDecoration;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * 选择协会
 * Created by Administrator on 2018/5/7.
 */
public class SelectAssociationActivity extends BaseActivity<LineWeatherPresenter> {

    private AlwaysMarqueeTextView title;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.side_bar)
    WaveSideBar sideBar;

    @BindView(R.id.et_search)
    EditText et_search;//输入框

    @BindView(R.id.tv_search)
    TextView tv_search;//

    private SelectAssociationAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_shed;
    }

    @Override
    public LineWeatherPresenter initPresenter() {
        return new LineWeatherPresenter(this);
    }

    private String defaultRegion = "";
    private String region = "";

    @Override
    public void initView(Bundle savedInstanceState) {

        region=  getIntent().getStringExtra("region");
        defaultRegion=  getIntent().getStringExtra("parameter");

        et_search.setHint("请输入协会名称快速查找");
        title = (AlwaysMarqueeTextView) findViewById(R.id.toolbar_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        title.setText("选择协会");
        toolbar.setNavigationOnClickListener(v -> finish());

        mAdapter = new SelectAssociationAdapter(this, null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(mAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

        showLoading();
        mPresenter.getAssociationInfo(et_search.getText().toString(),defaultRegion,region, data -> {
            mSwipeLayout.setRefreshing(false);
            hideLoading();
            seperateLists(data);
        });

        //下拉刷新
        setRefreshListener(() -> {
            mPresenter.getAssociationInfo(et_search.getText().toString(),defaultRegion,region, data -> {
                mSwipeLayout.setRefreshing(false);
                hideLoading();
                seperateLists(data);
            });
        });

        sideBar.setIndexItems("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#");

        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {

                for (int i = 0; i < mMembers.size(); i++) {
                    if (index.equals(mMembers.get(i).getSortLetters())) {
                        //或者
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator3();

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //完成自己的事件
                    if (!et_search.getText().toString().isEmpty()) {
                        showLoading();
                        mPresenter.getAssociationInfo(et_search.getText().toString(),defaultRegion,region, data -> {
                            mSwipeLayout.setRefreshing(false);
                            hideLoading();
                            seperateLists(data);
                        });
                    }
                }
                return false;
            }
        });

        tv_search.setOnClickListener(view -> {
            //完成自己的事件
            if (!et_search.getText().toString().isEmpty()) {
                showLoading();
                mPresenter.getAssociationInfo(et_search.getText().toString(),defaultRegion,region, data -> {
                    mSwipeLayout.setRefreshing(false);
                    hideLoading();
                    seperateLists(data);
                });
            }
        });
    }

    private List<ContactModel2.MembersEntity2> mMembers = new ArrayList<>();
    private List<ContactModel2.MembersEntity2> mMembers2 = new ArrayList<>();
    private CharacterParser characterParser;
    private PinyinComparator3 pinyinComparator;

    private List<ContactModel2.MembersEntity2> mAllLists = new ArrayList<>();

    private void seperateLists(List<AssociationListEntity> data) {

        ContactModel2 mModel = new ContactModel2();

        mAllLists.clear();
        for (int i = 0; i < data.size(); i++) {
            ContactModel2.MembersEntity2 myEntity = new ContactModel2.MembersEntity2();
            myEntity.setUsername(data.get(i).getXhname());
            myEntity.setData(data.get(i));
            myEntity.setId(data.get(i).getXhid());
            mAllLists.add(myEntity);
        }

        mModel.setMembers(mAllLists);

        mMembers.clear();
        mMembers2.clear();
        if (mModel.getMembers() != null && mModel.getMembers().size() > 0) {

            List<String> aaa = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                aaa.add(data.get(i).getXhname().trim());
            }

            int dataSeze = aaa.size();

            Collections.sort(aaa, CHINA_COMPARE);

            for (int i = 0; i < dataSeze; i++) {
                for (int y = 0; y < dataSeze; y++) {
                    if (aaa.get(i).equals((mModel.getMembers().get(y).getUsername()))) {
                        ContactModel2.MembersEntity2 temp = mModel.getMembers().get(y);
                        if (StringValid.isStringValid(temp.getUsername())) {
                            ContactModel2.MembersEntity2 entity = temp;
                            String pinyin = characterParser.getSelling(entity.getUsername()).trim();
                            String sortString = pinyin.substring(0, 1).toUpperCase();

                            if (sortString.matches("[A-Z]")) {
                                entity.setSortLetters(sortString.toUpperCase());
                                entity.setId(entity.getId());
                            } else {
                                entity.setSortLetters("#");
                            }
                            mMembers2.add(entity);
                        }
                    }
                }
            }

            for (int i = 0; i < mMembers2.size(); i++) {
                ContactModel2.MembersEntity2 temp = mMembers2.get(i);
                if (StringValid.isStringValid(temp.getUsername())) {
                    ContactModel2.MembersEntity2 entity = temp;
                    String pinyin = characterParser.getSelling(entity.getUsername()).trim();
                    String sortString = pinyin.substring(0, 1).toUpperCase();

                    if (sortString.matches("[A-Z]")) {
                        entity.setSortLetters(sortString.toUpperCase());
                    } else {
                        entity.setSortLetters("#");
                    }
                    mMembers.add(entity);
                }
            }

            Collections.sort(mMembers, pinyinComparator);

            mAdapter.addAll(mMembers);

        }
    }

    private final static Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    private void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setEnabled(true);
            mSwipeLayout.setOnRefreshListener(listener);
        }
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}

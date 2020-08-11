package com.example.ximalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ximalaya.adapter.RecommendListAdapter;
import com.example.ximalaya.adapter.SuggestWordsAdapter;
import com.example.ximalaya.base.BaseApplication;
import com.example.ximalaya.precenter.SearchPrencter;
import com.example.ximalaya.view.FlowTextLayout;
import com.example.ximalaya.view.UILoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchPrencter.ISearchPrencter, SuggestWordsAdapter.OnItemClick{

    private ImageView mBack;
    private EditText mEditText;
    private ImageView mInputDelete;
    private Button mSearch;
    private FrameLayout mFrameLayout;
    private UILoader mUILoader;

    //热词
    private FlowTextLayout mFlowTextLayout;

    //搜索结果列表
    private TwinklingRefreshLayout mTwinklingRefreshLayout;
    private RecyclerView mSearchList;
    private RecommendListAdapter mRecommendListAdapter;

    //搜索提示列表
    private RecyclerView mSuggestList;
    private SuggestWordsAdapter mSuggestWordsAdapter;


    private SearchPrencter mSearchPrencter;

    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mBack = findViewById(R.id.back);
        mEditText = findViewById(R.id.search_input);
        mInputDelete = findViewById(R.id.input_delete);
        mSearch = findViewById(R.id.search);
        mFrameLayout  = findViewById(R.id.search_content);
        mUILoader = findViewById(R.id.ui_load);

        mSearchPrencter = new SearchPrencter(this);


        mTwinklingRefreshLayout  = (TwinklingRefreshLayout) getLayoutInflater().inflate(R.layout.activity_search_seach_list, mFrameLayout, false);
        mSearchList = mTwinklingRefreshLayout.findViewById(R.id.recommend_list);
        mSearchList.setLayoutManager(new LinearLayoutManager(this));
        mRecommendListAdapter = new RecommendListAdapter(this);
        mSearchList.setAdapter(mRecommendListAdapter);


        mSuggestList = (RecyclerView) getLayoutInflater().inflate(R.layout.activity_search_suggest_words, mFrameLayout, false);
        mSuggestList.setLayoutManager(new LinearLayoutManager(this));
        mSuggestWordsAdapter  = new SuggestWordsAdapter();
        mSuggestWordsAdapter.setOnItemClick(this);
        mSuggestList.setAdapter(mSuggestWordsAdapter);


        init();

    }

    public void init(){

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(s.toString())) {

                    mSuggestWordsAdapter.clear();
                    mSearchPrencter.loadSuggestWords(s.toString());

                    mFrameLayout.removeAllViews();
                    mFrameLayout.addView(mSuggestList);
                }else {

                    mFrameLayout.removeAllViews();
                    mFrameLayout.addView(mFlowTextLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                mInputDelete.setVisibility(View.VISIBLE);
            }
        });

        mInputDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);
                mEditText.setText("");
                mInputDelete.setVisibility(View.GONE);

                mFrameLayout.removeAllViews();
                mFrameLayout.addView(mFlowTextLayout);
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mEditText.getText().toString())){

                    Toast.makeText(SearchActivity.this, BaseApplication.getContext().getResources().getString(R.string.search_empty), Toast.LENGTH_SHORT).show();
                }else {

                    mSearchPrencter.searchByKeyword(mEditText.getText().toString(), 1, SearchPrencter.LOAD_NEXT);
                    mPage = 1;
                }
            }
        });

        mFlowTextLayout = (FlowTextLayout) getLayoutInflater().inflate(R.layout.activity_search_hot_words, mFrameLayout, false);
        mFlowTextLayout.setClickListener(new FlowTextLayout.ItemClickListener() {
            @Override
            public void onItemClick(String text) {

                mEditText.setText(text);
                mSearchPrencter.searchByKeyword(text, 1, SearchPrencter.LOAD_NEXT);
                mPage = 1;
            }
        });


        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);

                mSearchPrencter.searchByKeyword(mEditText.getText().toString(), ++mPage, SearchPrencter.LOAD_NEXT);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mSearchPrencter.searchByKeyword(mEditText.getText().toString(), ++mPage, SearchPrencter.LOAD_NEXT);
            }
        });


        mSearchPrencter.loadHotWords();

    }

    @Override
    public void loadHotWordsList(List<HotWord> hotWords) {

        List<String> content = new ArrayList<>();

        for (HotWord hotWord : hotWords) {

            content.add(hotWord.getSearchword());
        }

        mFlowTextLayout.setTextContents(content);

        mFrameLayout.removeAllViews();
        mFrameLayout.addView(mFlowTextLayout);
    }

    @Override
    public void loadSearchByKeyNextPage(List<Album> albumList) {

        mUILoader.switchUILoad(UILoader.UILoadState.LOAD_SUCCESS);
        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);

        mRecommendListAdapter.refreshData(albumList);

        mFrameLayout.removeAllViews();
        mFrameLayout.addView(mTwinklingRefreshLayout);
    }

    @Override
    public void loadSearchByKeyMoreResult(List<Album> albumList) {

        mUILoader.switchUILoad(UILoader.UILoadState.LOAD_SUCCESS);
        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);

        mRecommendListAdapter.refreshMoreData(albumList);

        mFrameLayout.removeAllViews();
        mFrameLayout.addView(mTwinklingRefreshLayout);
    }

    @Override
    public void loadSuggestWords(List<QueryResult> suggestWordsList) {

        mUILoader.switchUILoad(UILoader.UILoadState.LOAD_SUCCESS);
        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);

        List<String> list = new ArrayList<>();
        for (QueryResult queryResult : suggestWordsList) {
            list.add(queryResult.getKeyword());
        }

        mSuggestWordsAdapter.refreshData(list);

        mFrameLayout.removeAllViews();
        mFrameLayout.addView(mSuggestList);

    }

    @Override
    public void loading() {

        mUILoader.switchUILoad(UILoader.UILoadState.LOADING);
    }

    @Override
    public void loadError(String error) {

        mTwinklingRefreshLayout.finishRefreshing();
        mTwinklingRefreshLayout.finishLoadmore();
        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void netError() {

        mTwinklingRefreshLayout.finishRefreshing();
        mTwinklingRefreshLayout.finishLoadmore();
        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);
        Toast.makeText(this, "请检查网络～～～", Toast.LENGTH_LONG).show();
    }

    @Override
    public void dataEmpty() {

        mTwinklingRefreshLayout.finishRefreshing();
        mTwinklingRefreshLayout.finishLoadmore();
        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);

    }

    @Override
    public void onItemClick(String key) {

        mEditText.setText(key);
        mSearchPrencter.searchByKeyword(key, 1, SearchPrencter.LOAD_NEXT);

        mPage = 1;
    }
}

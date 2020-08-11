package com.example.ximalaya.fragment;

import android.app.NotificationChannel;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.ximalaya.R;
import com.example.ximalaya.adapter.RecommendListAdapter;
import com.example.ximalaya.precenter.RecommendPrecenter;
import com.example.ximalaya.view.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecommendFragment extends Fragment implements RecommendPrecenter.IRecommendPrecenter {

    private RecyclerView mRecyclerView;
    private RecommendListAdapter mRecommendListAdapter;
    private UILoader mUILoader;

    private RecommendPrecenter mRecommendPrecenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_recommend, container, false);
        mRecyclerView = frameLayout.findViewById(R.id.recommend_list);
        mUILoader = frameLayout.findViewById(R.id.ui_load);
        mUILoader.setOnRetryClickListener(new UILoader.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                mRecommendPrecenter.loadData();
            }
        });

        mRecommendListAdapter = new RecommendListAdapter(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRecommendListAdapter);

        mRecommendPrecenter = new RecommendPrecenter(this);
        mRecommendPrecenter.loadData();


        return frameLayout;
    }


    @Override
    public void loadRecommendDataSuccess(List<Album> list) {

        mUILoader.switchUILoad(UILoader.UILoadState.LOAD_SUCCESS);
        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);
        mRecommendListAdapter.refreshData(list);
    }


    @Override
    public void loading() {
        mUILoader.switchUILoad(UILoader.UILoadState.LOADING);
    }

    @Override
    public void loadError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        mUILoader.switchUILoad(UILoader.UILoadState.LOAD_ERROR);
    }


    @Override
    public void netError() {

        mUILoader.switchUILoad(UILoader.UILoadState.NET_ERROR);
    }

    @Override
    public void dataEmpty() {
        mUILoader.switchUILoad(UILoader.UILoadState.DATA_EMPTY);
    }
}

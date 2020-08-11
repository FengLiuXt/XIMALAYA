package com.example.ximalaya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ximalaya.adapter.AlbumListAdapter;
import com.example.ximalaya.base.BaseApplication;
import com.example.ximalaya.precenter.AlbumPrencter;
import com.example.ximalaya.util.LogUtils;
import com.example.ximalaya.view.UILoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements AlbumPrencter.IAlbumPrencter, AlbumListAdapter.OnItemClickListener {

    public static final int ID = 2;

    public static final String TAG = "DetailActivity";

    public static final String ALBUM = "ALBUM";


    private ImageView mBgImage;
    private ImageView mCoverImage;
    private ImageView mControlPlayImage;
    private TextView mAuthorTv;
    private TextView mAuthorInfoTv;
    private TextView mCurrenntPlayTv;
    private TwinklingRefreshLayout mTwinklingRefreshLayout;
    private RecyclerView mRecyclerView;
    private UILoader mUILoader;

    private AlbumListAdapter mAlbumListAdapter;

    private Album mAlbum;

    private AlbumPrencter mAlbumPrencter;

    private int currentPage = 1;

    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mAuthorTv = findViewById(R.id.author_tv);
        mAuthorInfoTv = findViewById(R.id.author_info_tv);
        mCurrenntPlayTv = findViewById(R.id.album_title);

        mBgImage = findViewById(R.id.bg_image);
        mCoverImage = findViewById(R.id.cover_image);
        mControlPlayImage  = findViewById(R.id.play_control);

        mTwinklingRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.play_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUILoader = findViewById(R.id.ui_load);

        mAlbumListAdapter = new AlbumListAdapter();

        mAlbumPrencter = new AlbumPrencter(this);

        init();
    }

    private void init(){

        Intent intent = getIntent();

        mAlbum = intent.getParcelableExtra(ALBUM);
        Announcer announcer = mAlbum.getAnnouncer();

        String url = announcer.getAvatarUrl();
        Picasso.with(this).load(url).fit().centerCrop().into(mBgImage);
        Picasso.with(this).load(url).into(mCoverImage);

        mAuthorTv.setText(announcer.getNickname());
        mAuthorInfoTv.setText(announcer.getVdesc());

        mAlbumPrencter.defaultLoad(mAlbum.getId());

        mRecyclerView.setAdapter(mAlbumListAdapter);

//        mUILoader.setOnRetryClickListener(new UILoader.OnRetryClickListener() {
//            @Override
//            public void onRetryClick() {
//                mAlbumPrencter.loadAlbums(mAlbum.getId(), 1);
//            }
//        });

        mControlPlayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mAlbumPrencter.isPlaying()){

                    mAlbumPrencter.pause();

                }else {

                    mAlbumPrencter.play();
                }
            }
        });

        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);

                mAlbumPrencter.loadNextPageAlbums(mAlbum.getId(), ++currentPage);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);

                mAlbumPrencter.loadMoreAlbums(mAlbum.getId(), ++currentPage);
            }
        });

        mAlbumListAdapter.setItemOnclick(this);
    }

    @Override
    public void loading() {

        mUILoader.switchUILoad(UILoader.UILoadState.LOADING);
        LogUtils.d(TAG, "loading");
    }

    @Override
    public void loadError(String error) {

        mTwinklingRefreshLayout.finishRefreshing();
        mTwinklingRefreshLayout.finishLoadmore();
        mUILoader.switchUILoad(UILoader.UILoadState.LOAD_ERROR);
        LogUtils.d(TAG, "error");
    }

    @Override
    public void netError() {

        mTwinklingRefreshLayout.finishRefreshing();
        mTwinklingRefreshLayout.finishLoadmore();
        mUILoader.switchUILoad(UILoader.UILoadState.NET_ERROR);
    }

    @Override
    public void dataEmpty() {

        mTwinklingRefreshLayout.finishRefreshing();
        mTwinklingRefreshLayout.finishLoadmore();
        mUILoader.switchUILoad(UILoader.UILoadState.DATA_EMPTY);
        LogUtils.d(TAG, "empty");
    }

    @Override
    public void loadMoreAlbumDataSuccess(List<Track> data) {

        mTwinklingRefreshLayout.finishLoadmore();

        if(data.size() == 0)
            Toast.makeText(BaseApplication.getContext(), getResources().getString(R.string.already_end), Toast.LENGTH_LONG).show();
        else {
            mAlbumListAdapter.refreshMoreData(data);
            mUILoader.switchUILoad(UILoader.UILoadState.LOAD_SUCCESS);

            List<Track> list = mAlbumListAdapter.getAlbumList();
            mAlbumPrencter.setPlayList(list);
            String albumTitle = list.get(0).getTrackTitle();
            mCurrenntPlayTv.setText(albumTitle);
        }

        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);
    }

    @Override
    public void loadNextPageAlbumsSuccess(List<Track> data) {

        mTwinklingRefreshLayout.finishRefreshing();

        if(data.size() == 0)
            Toast.makeText(BaseApplication.getContext(), getResources().getString(R.string.already_first), Toast.LENGTH_LONG).show();
       else {
            mAlbumListAdapter.refreshNewData(data);
            mUILoader.switchUILoad(UILoader.UILoadState.LOAD_SUCCESS);

            List<Track> list = mAlbumListAdapter.getAlbumList();
            mAlbumPrencter.setPlayList(list);
            mCurrenntPlayTv.setText(list.get(0).getTrackTitle());
        }

        mUILoader.switchUILoad(UILoader.UILoadState.NOROMAL);
    }

    @Override
    public void play() {

        mControlPlayImage.setImageDrawable(getDrawable(R.drawable.bt_play_control_play_selector));

    }

    @Override
    public void pause() {

        mControlPlayImage.setImageDrawable(getDrawable(R.drawable.bt_play_control_pause_selector));

    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.FROME_ACTIVITYID, ID);
        intent.putExtra(PlayerActivity.POSITION, position);
        startActivityForResult(intent, PlayerActivity.ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PlayerActivity.ID && resultCode == RESULT_OK){

            mPosition = data.getIntExtra(PlayerActivity.POSITION, 0);

            String albumTitle = mAlbumPrencter.getAlbumtitle(mPosition);
            mCurrenntPlayTv.setText(albumTitle);


        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(PlayerActivity.POSITION, mPosition);
        setResult(RESULT_OK, intent);
        finish();
    }
}

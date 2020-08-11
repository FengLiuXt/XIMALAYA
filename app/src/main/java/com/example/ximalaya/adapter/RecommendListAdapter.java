package com.example.ximalaya.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ximalaya.DetailActivity;
import com.example.ximalaya.R;
import com.example.ximalaya.util.NumberCover;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.RecommendViewHolder>{

    private List<Album> mAlbumList;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;

    public RecommendListAdapter(Activity activity){

        mAlbumList = new ArrayList<>();
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(mActivity);
    }

    @NonNull
    @Override
    public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.item_recommend_list, parent, false);
        final RecommendViewHolder viewHolder = new RecommendViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = viewHolder.getAdapterPosition();
                Album album = mAlbumList.get(position);
                Intent intent = new Intent(mActivity, DetailActivity.class);
                intent.putExtra(DetailActivity.ALBUM, album);
                mActivity.startActivityForResult(intent, DetailActivity.ID);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendViewHolder holder, int position) {

        Album album = mAlbumList.get(position);
        holder.mTitleTv.setText(album.getAlbumTitle());
        holder.mDescTv.setText(album.getAlbumIntro());
        holder.mViewNumberTv.setText(NumberCover.numberCovertTo(album.getPlayCount()));
        holder.mEpisodeTv.setText(String.valueOf(album.getIncludeTrackCount()));

        Picasso.with(mActivity).
                load(album.getCoverUrlLarge()).
                placeholder(R.drawable.item_bg_recommend_image).
                noFade().
                fit().
                into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    public void refreshData(List<Album> list){

        mAlbumList.clear();
        mAlbumList.addAll(0, list);
        notifyDataSetChanged();
    }

    public void refreshMoreData(List<Album> list){

        mAlbumList.addAll(mAlbumList.size(), list);
        notifyDataSetChanged();
    }



    protected class RecommendViewHolder extends ViewHolder {

        private ImageView mImageView;
        private TextView mTitleTv;
        private TextView mDescTv;
        private TextView mViewNumberTv;
        private TextView mEpisodeTv;

        public RecommendViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image);
            mTitleTv = itemView.findViewById(R.id.title);
            mDescTv = itemView.findViewById(R.id.desc);
            mViewNumberTv = itemView.findViewById(R.id.views_number);
            mEpisodeTv = itemView.findViewById(R.id.episode);
        }
    }

}


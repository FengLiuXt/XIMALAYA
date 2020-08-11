package com.example.ximalaya.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ximalaya.R;
import com.example.ximalaya.util.NumberCover;
import com.example.ximalaya.util.PlayerUtils;
import com.example.ximalaya.util.TimeFormatUtils;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.PlayViewHolder> {

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    //格式化时间
    private SimpleDateFormat mUpdateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");

    private List<Track> mAlbumList;

    private OnItemClickListener mOnItemClickListener;

    public AlbumListAdapter(){

        mAlbumList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlayViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play_list, parent, false);
        final PlayViewHolder playViewHolder = new PlayViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mOnItemClickListener != null){

                    int position = playViewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
        return playViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayViewHolder holder, int position) {

        Track track = mAlbumList.get(position);
        holder.mNumberTv.setText(String.valueOf(position+1));
        holder.mTitleTv.setText(track.getTrackTitle());
        holder.mPlayCountTv.setText(NumberCover.numberCovertTo(track.getPlayCount()));
        holder.mDurationTv.setText(TimeFormatUtils.durationFormat(track.getDuration()*1000));
        holder.mUpdateTimeTv.setText(TimeFormatUtils.updateFormat(track.getCreatedAt()));

    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    public void setItemOnclick(OnItemClickListener onItemClickListener){

        this.mOnItemClickListener = onItemClickListener;
    }


    public void refreshMoreData(List<Track> data){

        mAlbumList.addAll(mAlbumList.size(), data);
        notifyDataSetChanged();
    }

    public void refreshNewData(List<Track> data){

        mAlbumList.clear();
        mAlbumList.addAll(data);
        notifyDataSetChanged();
    }

    public List<Track> getAlbumList(){

        return mAlbumList;
    }

    class PlayViewHolder extends RecyclerView.ViewHolder{

        private TextView mNumberTv;
        private TextView mTitleTv;
        private TextView mPlayCountTv;
        private TextView mDurationTv;
        private TextView mUpdateTimeTv;

        public PlayViewHolder(@NonNull View itemView) {
            super(itemView);

            mNumberTv = itemView.findViewById(R.id.number);
            mTitleTv = itemView.findViewById(R.id.title);
            mPlayCountTv = itemView.findViewById(R.id.play_count);
            mDurationTv = itemView.findViewById(R.id.duration);
            mUpdateTimeTv = itemView.findViewById(R.id.update_time);
        }
    }
}

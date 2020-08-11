package com.example.ximalaya.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ximalaya.R;
import com.example.ximalaya.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SuggestWordsAdapter extends RecyclerView.Adapter<SuggestWordsAdapter.SuggestWordsViewHolder> {

    public interface OnItemClick{

        void onItemClick(String key);
    }

    private List<String> mData = new ArrayList<>();

    private OnItemClick mOnItemClick;

    @NonNull
    @Override
    public SuggestWordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(BaseApplication.getContext());
        View view = layoutInflater.inflate(R.layout.ite_suggest_list, parent, false);
        final SuggestWordsViewHolder suggestWordsViewHolder = new SuggestWordsViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mOnItemClick != null){

                    int position = suggestWordsViewHolder.getAdapterPosition();
                    mOnItemClick.onItemClick(mData.get(position));
                }
            }
        });


        return suggestWordsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestWordsViewHolder holder, int position) {

        TextView view = holder.mTextView;
        view.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void refreshData(List<String> data){

        mData.clear();
        mData.addAll(0, data);
        notifyDataSetChanged();
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick){

        mOnItemClick = onItemClick;
    }

    class SuggestWordsViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;

        public SuggestWordsViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = (TextView) itemView;
        }
    }
}

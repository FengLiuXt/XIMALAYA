package com.example.ximalaya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.ximalaya.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UILoader extends FrameLayout {

    private OnRetryClickListener mOnRetryClickListener;
    private View loaadding, loadError, netError, dataEmpty;

    public UILoader(@NonNull Context context) {
        super(context);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public enum UILoadState{
        LOADING, LOAD_ERROR,NET_ERROR, DATA_EMPTY, LOAD_SUCCESS, NOROMAL
    }



    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener){
        mOnRetryClickListener = onRetryClickListener;
    }

    public void retryLoad(){

        if(mOnRetryClickListener != null)
            mOnRetryClickListener.onRetryClick();
    }

    public void switchUILoad(UILoadState uiLoadState){

        switch (uiLoadState){

            case LOADING:
                if(loaadding == null)
                    loaadding = LayoutInflater.from(getContext()).inflate(R.layout.load_state_loadding, this, false);

                removeAllViews();
                addView(loaadding);
                setVisibility(VISIBLE);
                break;

            case LOAD_ERROR:

                if(loadError == null)
                    loadError = LayoutInflater.from(getContext()).inflate(R.layout.load_state_error, this, false);

                if(!loadError.hasOnClickListeners()){

                    loadError.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            retryLoad();
                        }
                    });
                }

                removeAllViews();
                addView(loadError);
                break;


            case NET_ERROR:

                if(netError == null)
                    netError = LayoutInflater.from(getContext()).inflate(R.layout.load_state_net_error, this, false);

                if(!netError.hasOnClickListeners()){

                    netError.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            retryLoad();
                        }
                    });
                }

                removeAllViews();
                addView(netError);
                setVisibility(VISIBLE);
                break;

            case DATA_EMPTY:

                if(dataEmpty == null)
                    dataEmpty = LayoutInflater.from(getContext()).inflate(R.layout.load_state_data_empty,this, false);

                removeAllViews();
                addView(dataEmpty);
                setVisibility(VISIBLE);
                break;

            case LOAD_SUCCESS:

                removeAllViews();
                Toast.makeText(getContext(), "加载成功", Toast.LENGTH_LONG).show();
                break;

            case NOROMAL:

                removeAllViews();
                setVisibility(GONE);
                break;
        }
    }



    public interface OnRetryClickListener {
        void onRetryClick();
    }
}

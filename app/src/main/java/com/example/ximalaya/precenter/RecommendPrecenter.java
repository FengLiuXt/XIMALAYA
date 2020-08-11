package com.example.ximalaya.precenter;

import com.example.ximalaya.base.IModeListener;
import com.example.ximalaya.base.IPrecenterBase;
import com.example.ximalaya.mode.RecommendContent;
import com.example.ximalaya.util.NetCheckUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

import androidx.core.app.NavUtils;

public class RecommendPrecenter {

    public interface IRecommendPrecenter extends IPrecenterBase {

        void loadRecommendDataSuccess(List<Album> list);
    }

    private IRecommendPrecenter mIRecommendPrecenter;
    private RecommendContent mRecommends;

    public RecommendPrecenter(final IRecommendPrecenter iRecommendPrecenter){

        mIRecommendPrecenter = iRecommendPrecenter;
        mRecommends = new RecommendContent();
    }

    public void loadData(){

        mIRecommendPrecenter.loading();

        if(!NetCheckUtils.isAvaiable()){

            mIRecommendPrecenter.netError();
            return;
        }

        mRecommends.loadRecomends(new IModeListener<Album>() {
            @Override
            public void loadSuccess(List<Album> data) {

                if(data.size() == 0)
                    mIRecommendPrecenter.dataEmpty();
                else
                    mIRecommendPrecenter.loadRecommendDataSuccess(data);

            }

            @Override
            public void loadError(String errorMsg) {

                mIRecommendPrecenter.loadError(errorMsg);
            }
        });
    }
}

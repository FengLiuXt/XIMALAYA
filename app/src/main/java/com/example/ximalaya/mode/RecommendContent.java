package com.example.ximalaya.mode;


import com.example.ximalaya.api.XimalayApi;
import com.example.ximalaya.base.IModeListener;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;


public class RecommendContent {

    public void loadRecomends(final IModeListener<Album> iModeListener){

        XimalayApi.getRecommendList(new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {

                if(gussLikeAlbumList != null)
                    iModeListener.loadSuccess(gussLikeAlbumList.getAlbumList());
                else
                    iModeListener.loadError("服务器出错，请稍后重试");
            }

            @Override
            public void onError(int i, String s) {

                iModeListener.loadError(s);
            }
        });
    }

}

package com.example.ximalaya.mode;

import com.example.ximalaya.api.XimalayApi;
import com.example.ximalaya.base.IModeListener;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;

public class SearchContent {

    public void searchByKeyword(String key, int page, final IModeListener<Album> iModeListener){

        XimalayApi.searchByKeyword(key, page, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {

                iModeListener.loadSuccess(searchAlbumList.getAlbums());
            }

            @Override
            public void onError(int i, String s) {

                iModeListener.loadError(s);
            }
        });
    }
}

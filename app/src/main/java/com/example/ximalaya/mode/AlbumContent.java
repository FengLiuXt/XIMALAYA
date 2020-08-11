package com.example.ximalaya.mode;


import com.example.ximalaya.api.XimalayApi;
import com.example.ximalaya.base.IModeListener;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

public class AlbumContent {


    public void loadAlbums(long anchorId, int startPage, final IModeListener<Track> iModeListener) {

        XimalayApi.getAlbumDetail(anchorId, startPage, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {

                if (iModeListener != null)
                    iModeListener.loadSuccess(trackList.getTracks());
                else
                    iModeListener.loadError("服务器出错，请稍后再试");
            }

            @Override
            public void onError(int i, String s) {

                iModeListener.loadError(s);
            }
        });

    }
}

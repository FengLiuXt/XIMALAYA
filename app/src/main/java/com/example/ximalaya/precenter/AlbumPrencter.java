package com.example.ximalaya.precenter;

import com.example.ximalaya.base.IModeListener;
import com.example.ximalaya.base.IPrecenterBase;
import com.example.ximalaya.mode.AlbumContent;
import com.example.ximalaya.util.PlayerUtils;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;

public class AlbumPrencter {

    public interface IAlbumPrencter extends IPrecenterBase {

        void loadMoreAlbumDataSuccess(List<Track> data);
        void loadNextPageAlbumsSuccess(List<Track> data);
        void play();
        void pause();
    }

    private IAlbumPrencter mIAlbumPrencter;
    private AlbumContent mAlbumContent;

    private XmPlayerManager mXmPlayerManager;


    public AlbumPrencter(IAlbumPrencter iAlbumPrencter){

        mIAlbumPrencter = iAlbumPrencter;
        mAlbumContent = new AlbumContent();

        mXmPlayerManager = PlayerUtils.getsXmPlayerManager();

        mXmPlayerManager.addPlayerStatusListener(new IXmPlayerStatusListener() {
            @Override
            public void onPlayStart() {

                if(mIAlbumPrencter != null)
                    mIAlbumPrencter.play();
            }

            @Override
            public void onPlayPause() {

                if(mIAlbumPrencter != null)
                    mIAlbumPrencter.pause();

            }

            @Override
            public void onPlayStop() {

            }

            @Override
            public void onSoundPlayComplete() {

            }

            @Override
            public void onSoundPrepared() {

            }

            @Override
            public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {

            }

            @Override
            public void onBufferingStart() {

            }

            @Override
            public void onBufferingStop() {

            }

            @Override
            public void onBufferProgress(int i) {

            }

            @Override
            public void onPlayProgress(int i, int i1) {

            }

            @Override
            public boolean onError(XmPlayerException e) {
                return false;
            }
        });

    }

    public void loadNextPageAlbums(long anchorId, int startPage){

        mIAlbumPrencter.loading();
        mAlbumContent.loadAlbums(anchorId, startPage, new IModeListener<Track>() {
            @Override
            public void loadSuccess(List<Track> data) {

               if(mIAlbumPrencter != null)
                    mIAlbumPrencter.loadNextPageAlbumsSuccess(data);
            }

            @Override
            public void loadError(String errorMsg) {

                mIAlbumPrencter.loadError(errorMsg);
            }
        });
    }

    public void loadMoreAlbums(long anchorId, int startPage){

        mIAlbumPrencter.loading();
        mAlbumContent.loadAlbums(anchorId, startPage, new IModeListener<Track>() {
            @Override
            public void loadSuccess(List<Track> data) {

                if(mIAlbumPrencter != null)
                    mIAlbumPrencter.loadMoreAlbumDataSuccess(data);
            }

            @Override
            public void loadError(String errorMsg) {

                mIAlbumPrencter.loadError(errorMsg);
            }
        });
    }

    public void defaultLoad(long anchorId){

        mIAlbumPrencter.loading();
        mAlbumContent.loadAlbums(anchorId, 1, new IModeListener<Track>() {
            @Override
            public void loadSuccess(List<Track> data) {

                if(data.size() == 0)
                    mIAlbumPrencter.dataEmpty();
                else
                    mIAlbumPrencter.loadMoreAlbumDataSuccess(data);
            }

            @Override
            public void loadError(String errorMsg) {

                mIAlbumPrencter.loadError(errorMsg);
            }
        });
    }

    public boolean isPlaying(){

        return mXmPlayerManager.isPlaying();
    }

    public void play(){

        mXmPlayerManager.play();
    }

    public void pause(){

        mXmPlayerManager.pause();
    }

    public String getAlbumtitle(int position){

        return  mXmPlayerManager.getPlayList().get(position).getTrackTitle();
    }

    public void setPlayList(List<Track> playList){

        mXmPlayerManager.setPlayList(playList,0);
    }
}

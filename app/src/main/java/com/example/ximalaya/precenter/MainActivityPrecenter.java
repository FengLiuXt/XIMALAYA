package com.example.ximalaya.precenter;

import com.example.ximalaya.util.PlayerUtils;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;

public class MainActivityPrecenter {

    public interface IMainActivityPrecenter{

        void playStart();
        void playPause();
    }

    private IMainActivityPrecenter mIMainActivityPrecenter;
    private XmPlayerManager mXmPlayerManager;

    public MainActivityPrecenter(IMainActivityPrecenter iMainActivityPrecenter){

        mIMainActivityPrecenter = iMainActivityPrecenter;
        mXmPlayerManager = PlayerUtils.getsXmPlayerManager();
        mXmPlayerManager.addPlayerStatusListener(new IXmPlayerStatusListener() {
            @Override
            public void onPlayStart() {

                if(mIMainActivityPrecenter != null)
                    mIMainActivityPrecenter.playStart();
            }

            @Override
            public void onPlayPause() {

                if(mIMainActivityPrecenter != null)
                    mIMainActivityPrecenter.playPause();

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


    public void play(){

        mXmPlayerManager.play();

        if(mIMainActivityPrecenter != null){

            mXmPlayerManager.play();
        }
    }

    public void pause(){

        mXmPlayerManager.pause();

        if(mIMainActivityPrecenter != null){

            mXmPlayerManager.pause();
        }
    }

    public String getCoverUrl(int position){

        if(!isNullOrEmpty(position)){

            List<Track> list = mXmPlayerManager.getPlayList();
            return list.get(position).getCoverUrlLarge();
        }else {

            return "";
        }
    }

    public String getSongName(int position){

        if(!isNullOrEmpty(position)){

            List<Track> list = mXmPlayerManager.getPlayList();
            return list.get(position).getTrackTitle();
        }
        else {
            return "";
        }
    }

    public String getAuthorNam(int position){

        if(!isNullOrEmpty(position)) {

            List<Track> list = mXmPlayerManager.getPlayList();
            return list.get(position).getAnnouncer().getNickname();
        }
        else {
            return "";
        }

    }

    private boolean isNullOrEmpty(int position){


        List<Track> list = mXmPlayerManager.getPlayList();

        return list == null || list.size() <= position;
    }

    public  boolean isPlaying(){

        return mXmPlayerManager.isPlaying();
    }

}

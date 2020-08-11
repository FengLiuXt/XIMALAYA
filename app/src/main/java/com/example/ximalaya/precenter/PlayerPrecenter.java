package com.example.ximalaya.precenter;

import android.widget.Toast;

import com.example.ximalaya.PlayerActivity;
import com.example.ximalaya.R;
import com.example.ximalaya.base.BaseApplication;
import com.example.ximalaya.util.PlayerUtils;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;

public class PlayerPrecenter {

    public interface IPlayerPrecenter{

        void setAlbumTitle(String str);
        void duration(Object o);
        void playProgress(int currentProgress, int duration);
        void bufferProgress(int bufferProgress);
        void playStart();
        void playPause();
    }

    private  XmPlayerManager xmPlayerManager;

    private IPlayerPrecenter mIPlayerPrecenter;
    private int mPosition;


    public PlayerPrecenter(final IPlayerPrecenter iPlayerPrecenter){

        xmPlayerManager = PlayerUtils.getsXmPlayerManager();
        mIPlayerPrecenter = iPlayerPrecenter;

        xmPlayerManager.addPlayerStatusListener(new IXmPlayerStatusListener() {
            @Override
            public void onPlayStart() {

                if(iPlayerPrecenter != null){
                    iPlayerPrecenter.playStart();
                }
            }

            @Override
            public void onPlayPause() {

                if(iPlayerPrecenter != null){
                    iPlayerPrecenter.playPause();
                }
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

                if(mIPlayerPrecenter != null)
                    mIPlayerPrecenter.bufferProgress(i);
            }

            @Override
            public void onPlayProgress(int i, int i1) {

                if(mIPlayerPrecenter != null)
                    mIPlayerPrecenter.playProgress(i, i1);
            }

            @Override
            public boolean onError(XmPlayerException e) {
                return false;
            }
        });
    }

    public void play(int index){

        mPosition = index;
        xmPlayerManager.play(index);

        if(mIPlayerPrecenter != null){

            List<Track> list = xmPlayerManager.getPlayList();
            Track track = list.get(index);

            mIPlayerPrecenter.setAlbumTitle(track.getTrackTitle());
            mIPlayerPrecenter.duration(track.getDuration()*1000);
        }
    }

    public  void next(){

        List<Track> list = xmPlayerManager.getPlayList();

        if(mPosition+1 == list.size())
            Toast.makeText(BaseApplication.context, BaseApplication.getContext().getResources().getString(R.string.already_end), Toast.LENGTH_LONG).show();
        else {

            Track track = list.get(++mPosition);
            if(mIPlayerPrecenter != null) {
                mIPlayerPrecenter.setAlbumTitle(track.getTrackTitle());
                mIPlayerPrecenter.duration(track.getDuration()*1000);
            }

            xmPlayerManager.playNext();
        }
    }

    public void pre(){

        List<Track> list = xmPlayerManager.getPlayList();

        if(mPosition - 1 == -1)
            Toast.makeText(BaseApplication.context, BaseApplication.context.getResources().getString(R.string.already_first), Toast.LENGTH_LONG).show();
        else {

            Track track = list.get(--mPosition);
            if(mIPlayerPrecenter != null) {
                mIPlayerPrecenter.setAlbumTitle(track.getTrackTitle());
                mIPlayerPrecenter.duration(track.getDuration()*1000);
            }

            xmPlayerManager.playPre();
        }
    }

    public void seekTo(int progress){

        xmPlayerManager.seekTo(progress);
    }

    public int getDuration(){

        return xmPlayerManager.getDuration();
    }


    // PLAY_MODEL_SINGLE 单曲播放
    // PLAY_MODEL_SINGLE_LOOP 单曲循环播放
    // PLAY_MODEL_LIST列表播放
    // PLAY_MODEL_LIST_LOOP列表循环
    // PLAY_MODEL_RANDOM 随机播放
   public void setPlayMode(int mode){

        switch (mode){

            case PlayerActivity.PLAY_MODEL_LIST:

                xmPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_LIST);
                break;

            case PlayerActivity.PLAY_MODEL_SINGLE_LOOP:

                xmPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP);
                break;

            case PlayerActivity.PLAY_MODEL_RANDOM:

                xmPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM);
                break;

            case PlayerActivity.PLAY_MODEL_SINGLE:

                xmPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE);
                break;
        }
   }

   public boolean isPlaying(){

        return xmPlayerManager.isPlaying();
   }

   public void pause(){

        xmPlayerManager.pause();
   }

   public void play(){
        xmPlayerManager.play();
   }

   public int getPosition(){

        return mPosition;
   }

}

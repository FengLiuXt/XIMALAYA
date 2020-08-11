package com.example.ximalaya.util;

import com.example.ximalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import java.util.List;

public class PlayerUtils {

    private static XmPlayerManager sXmPlayerManager;

    static {

        sXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.context);
    }

    private PlayerUtils(){

    }

    public static XmPlayerManager getsXmPlayerManager(){

        return sXmPlayerManager;
    }

    public  static void setPlayerList(List<Track> list, int position){

        sXmPlayerManager.setPlayList(list, position);
    }
}

package com.example.ximalaya.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.example.ximalaya.base.BaseApplication;

public class NetCheckUtils {

    public static boolean isAvaiable(){

        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo  = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null){
            return networkInfo.isAvailable();
        }else{

            return false;
        }
    }
}

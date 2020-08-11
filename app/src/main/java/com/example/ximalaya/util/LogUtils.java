package com.example.ximalaya.util;

import android.util.Log;

import com.example.ximalaya.BuildConfig;

public class LogUtils {

    private LogUtils(){

    }

    public static void d(String TAG, String msg){

        if(BuildConfig.DEBUG) Log.d(TAG, msg);
    }

    public static void w(String TAG, String msg){

        if(BuildConfig.DEBUG) Log.e(TAG, msg);
    }

    public static void e(String TAG, String msg){

        if(BuildConfig.DEBUG) Log.e(TAG, msg);
    }


    public static void i(String TAG, String msg){

        if(BuildConfig.DEBUG) Log.i(TAG, msg);
    }

}

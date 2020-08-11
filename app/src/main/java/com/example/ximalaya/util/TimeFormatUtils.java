package com.example.ximalaya.util;

import java.text.SimpleDateFormat;

public class TimeFormatUtils {

    //格式化时间
    private static SimpleDateFormat mUpdateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");

    public static String updateFormat(Object o){

        return mUpdateDateFormat.format(o);
    }


    public static String durationFormat(Object o){

        return mDurationFormat.format(o);
    }

}

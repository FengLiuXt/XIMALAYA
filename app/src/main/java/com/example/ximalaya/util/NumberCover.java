package com.example.ximalaya.util;

public class NumberCover {

    public static String numberCovertTo(long number){

        if(number > 100000000){
            return String.format("%.2f",number/100000000.0f)+"亿";
        }else if(number > 10000000){
            return String.format("%.2f",number/10000000.0f)+"千万";
        }else if(number > 10000){
            return String.format("%.2f", number/10000.0f)+"万";
        }else {
            return String.valueOf(number);
        }
    }
}

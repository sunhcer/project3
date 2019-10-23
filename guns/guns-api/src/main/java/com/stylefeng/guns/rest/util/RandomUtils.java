package com.stylefeng.guns.rest.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomUtils {
    static String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    public static String getNowDateString(){
        String string = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return string;
    }

    public static String getLenNumRandom(int len){

        int num = (int)(Math.random() * Math.pow(10, len));

        return String.valueOf(num);
    }

    public static String getLenMixRandom(int len){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(getOneString());
        }
        return sb.toString();
    }

    public static String getOneString(){
        int i = (int)(Math.random()*str.length());
        return str.substring(i, i+1);
    }
}

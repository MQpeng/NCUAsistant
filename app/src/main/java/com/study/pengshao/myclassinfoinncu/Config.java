package com.study.pengshao.myclassinfoinncu;

import com.study.pengshao.myclassinfoinncu.utils.LoginEncodeParse;

/**
 * Created by PengShao on 2016/9/26.
 */
public class Config {
    public static String SHARED_TAG = "NCU_CLASS";
    public static String APPLICATION_ID = "02ab28f5218b90729bcf6fe07ad6a954";
    public static String URL_ID = "Ei6b999D";
    public static String FLASH_ID = "qIoe111F";
    public static String VERSION_ID = "L3Z6AAAB";
    public static int APP_VERSION = 2;

    public static String parseEncode(String count,String password){
        String result = "";
        result = LoginEncodeParse.encodeInp(count)+"%%%"+LoginEncodeParse.encodeInp(password);
        return result;
    }
}

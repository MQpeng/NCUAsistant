package com.study.pengshao.myclassinfoinncu.utils;

import java.text.SimpleDateFormat;

/**
 * Created by PengShao on 2016/9/28.
 */
public class GetSystemTime {
    public static String getTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(currentTimeMillis);
    }
}

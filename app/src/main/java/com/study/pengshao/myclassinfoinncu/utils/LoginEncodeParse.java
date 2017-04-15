package com.study.pengshao.myclassinfoinncu.utils;

/**
 * Created by PengShao on 2016/10/17.
 */
public class LoginEncodeParse {
    public static String encodeInp(String input)
    {
        String keyStr="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        String output="";
        int chr1,chr2,chr3;
        int enc1,enc2,enc3,enc4;
        int i=0;
        do{
            chr1=getUnicode(input.charAt(i++));
            enc1=chr1>>2;
            if(i<input.length()){
                chr2=getUnicode(input.charAt(i++));
                enc2=((chr1&3)<<4)|(chr2>>4);

                if(i<input.length()){
                    chr3=getUnicode(input.charAt(i++));
                    enc3=((chr2&15)<<2)|(chr3>>6);
                    enc4=chr3&63;
                }else {
                    enc3=((chr2&15)<<2);
                    enc4=64;
                }
            }else {
                enc2 = (chr1&3)<<4;
                enc3=enc4=64;
            }

            /*if(isNaN(chr2)){
                enc3=enc4=64
            }else if(isNaN(chr3))
            {
                enc4=64
            }*/
            output=output+keyStr.charAt(enc1)+keyStr.charAt(enc2)+keyStr.charAt(enc3)+keyStr.charAt(enc4);
            chr1=chr2=chr3=0;
            enc1=enc2=enc3=enc4=0;
        }while(i<input.length());
        return output;
    }


    //拿到unicode编码
    public static int getUnicode(char c){
        return (int)c;
    }
}

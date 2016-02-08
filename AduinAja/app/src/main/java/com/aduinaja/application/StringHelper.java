package com.aduinaja.application;

/**
 * Created by elmee on 13/11/2015.
 */
public class StringHelper {

    public static String capitalEachWord(String source){
        StringBuffer res = new StringBuffer();

        String[] strArr = source.split(" ");
        for(String str : strArr){
            char[] stringArray = str.trim().toCharArray();
            for(int i = 0; i < stringArray.length; i++){
                if(i == 0){
                    stringArray[0] = Character.toUpperCase(stringArray[0]);
                }else{
                    stringArray[i] = Character.toLowerCase(stringArray[i]);
                }
            }
            str = new String(stringArray);
            res.append(str).append(" ");
        }
        return res.toString().trim();
    }

    public static String upperCase(String source){
        return source.toUpperCase();
    }

    public static String lowerCase(String source){
        return source.toLowerCase();
    }
}

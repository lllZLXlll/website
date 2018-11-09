package com.wchm.website.openapi;

public class UtilDt {

    public static boolean TextIsEmpty(String str) {
        if(str == null || str.trim() == "") {
            return true;
        }
        return false;
    }
}

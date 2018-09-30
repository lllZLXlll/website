package com.wchm.website.util;

import java.util.UUID;

public class UUIDUtil {
    public static String DELIMITER = "@@";
    
    /**
     * 随机获取UUID字符串(无中划线)
     * 
     * @return UUID字符串
     */
    public static String getUUID() {
        return 	UUID.randomUUID().toString().replaceAll("-", "");
    }   
    public static void main(String[] args) {
		System.out.println(getUUID());
	}
}

package com.wchm.website.util;

import java.util.Random;

/**
 * 生成验证码工具类
 */
public class CodeUtis {
    /**
     * 生成6为随机验证码 数字
     */
    public static String getRandomNumCode() {
        String randomStr = "0123456789";
        Random random = new Random();
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(randomStr.length());
            strBuf.append(randomStr.charAt(number));
        }
        return strBuf.toString();

    }

    public static void main(String[] args) {
        System.out.println(getRandomNumCode());
    }

}

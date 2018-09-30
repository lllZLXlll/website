package com.wchm.website.config;

import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptConfig {

    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("G0CvDz7oJn6");
        //要加密的数据（数据库的用户名和密码）
        String username = textEncryptor.encrypt("javaweb");
        String password = textEncryptor.encrypt("2NhSF5LyxwwnyTY4");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
    }
}

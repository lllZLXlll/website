package com.wchm.website.openapi;


import net.sf.json.JSONObject;

import javax.xml.crypto.Data;

public class StartUp {

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String access_key = "81da4211bdb75c59ba7832eba172ed69";
                String secret_key = "d05092aa35685a4080f8da8ce5c38722";
                // get token
                String hasTokenJson = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_TOKEN);

                JSONObject jsonObject = JSONObject.fromObject(hasTokenJson);
               // set token

                String sy = jsonObject.getString("data");

                JSONObject jsonObject1 =JSONObject.fromObject(sy);

                HttpParams.setToken(jsonObject1.getString("token"));


                // get 获取token 状态
                String tokenStatusJson =  HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.CHECK_TOKEN_STATUS);

                System.out.println(tokenStatusJson);
            }
        }).start();

    }
}

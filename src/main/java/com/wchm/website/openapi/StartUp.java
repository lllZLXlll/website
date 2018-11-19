package com.wchm.website.openapi;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

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

                String sy = jsonObject.getString("data");

                JSONObject jsonObject1 = JSONObject.fromObject(sy);

                // set token
                HttpParams.setToken(jsonObject1.getString("token"));
//                HttpParams.setToken("NAetRykz1XmKCWK4OJI533CgW0o=");

                // get 获取token 状态
                String tokenStatusJson = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.CHECK_TOKEN_STATUS);

                String code = null;
                if (tokenStatusJson.equals(null)) {

                }

//                // 获取所有货币
//                String real = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_COIN_ALL);
//
//                JSONObject co = JSONObject.fromObject(real);
//
//                // 打印实时行情
//                System.out.println(co);
//
//                // {"coin_id":210,"code":"pct"}
//
//                // 获取所有交易对
//                String symbol = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_SYMBOL_ALL);
//
//                JSONObject symbolJson = JSONObject.fromObject(symbol);
//
//                // 打印所有交易对 {"symbol":"pct_usdt","symbol_id":210}
//                System.out.println(symbolJson);

                // 获取实时行情
                Map<String, Object> params = new HashMap();
                params.put("symbol_id", "210");
                String paramJson = JSONObject.fromObject(params).toString();
                String realStr = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_MARKET_REAL, paramJson);

                JSONObject realJson = JSONObject.fromObject(realStr);
                String data = realJson.getString("data");
                JSONArray dataJson = JSONArray.fromObject(data);
                String price = JSONObject.fromObject(dataJson.get(0)).getString("close_price");

                // 打印价格
                System.out.println(price);

            }
        }).start();

    }
}

package com.wchm.website.openapi;

public class HostConstant {

    // Api 所提供的接口
    public static String MAIN_HOST = "https://openapi.dragonex.im";

    // 获取请求token，此token有效期24h，有效期过后需要重新获取，每对密钥每个自然日最多可获取100次
    public static String GET_TOKEN = "/api/v1/token/new/";

    // 获取token状态
    public static String CHECK_TOKEN_STATUS = "/api/v1/token/status/";

   // 获取所有货币(GET)
    public static String GET_COIN_ALL ="/api/v1/coin/all/";

    // 获取用户所拥有的货币信息.(POST 只返回用户有的币种)
    public static String GET_USER_COIN ="/api/v1/user/own/";

    //获取实时行情
    public static String GET_MARKET_REAL="/api/v1/market/real/";
}

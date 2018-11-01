package com.wchm.website.util;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.Map;

public class HttpUtils {

    /**
     * get请求
     *
     * @param url <p>请求地址</p>
     * @return <p>返回响应的json字符串</p>
     */
    public static String get(String url) {
        HttpRequest request = HttpRequest.get(url);
        // 信任所有证书
        request.trustAllCerts();
        // 信任所有地址
        request.trustAllHosts();
        // 设置请求超时时间
        request.connectTimeout(5000);
        // 设置读取超时时间
        request.readTimeout(5000);
        return request.body();
    }

    /**
     * post请求
     *
     * @param url  <p>请求地址</p>
     * @param data <p>请求参数</p>
     * @return <p>返回响应的json字符串</p>
     */
    public static String post(String url, Map data) {
        return HttpRequest.post(url)
                .trustAllCerts() // 信任所有证书
                .trustAllHosts() // 信任所有地址
                .form(data)
                .connectTimeout(5000) // 设置请求超时时间
                .readTimeout(5000) // 设置读取超时时间
                .body();
    }


    /**
     * post请求
     *
     * @param url  <p>请求地址</p>
     * @param headerKey <p>请求头key</p>
     * @param headerValue <p>请求头values</p>
     * @return <p>返回响应的json字符串</p>
     */
    public static String post(String url, String headerKey, String headerValue) {
        return HttpRequest.post(url)
                .trustAllCerts() // 信任所有证书
                .trustAllHosts() // 信任所有地址
                .header(headerKey, headerValue)
                .connectTimeout(5000) // 设置请求超时时间
                .readTimeout(5000) // 设置读取超时时间
                .body();
    }

}

package com.wchm.website.util;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信接口工具类
 */
public class SendMsgUtil {

    private static final Logger log = LoggerFactory.getLogger(SendMsgUtil.class);
    private static final String DEF_CHATSET = "UTF-8";
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;
    private static final String APPKEY = "79dc9185c7043e138aab636db4e15707"; // 配置申请的KEY
    private static final String TPL_ID_104137 = "104137"; // 【万呈海盟集团】您的验证码是#code#。如非本人操作，请忽略本短信

    // 调用示例
    public static void main(String[] args) {
        boolean sendStatus = sendCodeMsg("13129524650", "12345");
    }

    /**
     * 发送验证码短信
     *
     * @param mobile 手机号码
     * @param code   验证码
     * @return 是否发送成功
     */
    public static boolean sendCodeMsg(String mobile, String code) {
        Map map = new HashMap();
        map.put("#code#", code);
        return sendMsg(mobile, TPL_ID_104137, urlencode(map));
    }

    /**
     * 屏蔽词检查测
     *
     * @param word 发送的内容
     * @return
     */
    public static boolean checkParam(String word) {
        String url = "http://v.juhe.cn/sms/black"; // 请求接口地址
        Map params = new HashMap(); // 请求参数
        params.put("word", word); // 需要检测的短信内容，需要UTF8 URLENCODE
        params.put("key", APPKEY); // 应用APPKEY(应用详细页查询)
        return sendNet(url, params);
    }

    /**
     * 发送短信
     *
     * @param mobile    手机号码
     * @param tpl_id    模板id
     * @param tpl_value 模板变量key，value
     * @return 是否发送成功
     */
    public static boolean sendMsg(String mobile, String tpl_id, String tpl_value) {
        if (StringUtils.isEmpty(tpl_id)) {
            tpl_id = TPL_ID_104137;
        }
        String url = "http://v.juhe.cn/sms/send"; // 请求接口地址
        Map params = new HashMap(); // 请求参数
        params.put("mobile", mobile); // 接收短信的手机号码
        params.put("tpl_id", tpl_id); // 短信模板ID，请参考个人中心短信模板设置
        params.put("tpl_value", tpl_value); // 变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
        params.put("key", APPKEY); // 应用APPKEY(应用详细页查询)
        params.put("dtype", "json"); // 返回数据的格式,xml或json，默认json
        return sendNet(url, params);
    }

    public static boolean sendNet(String url, Map params) {
        try {
            String result = net(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                log.info(object.toString());
                return true;
            } else {
                log.error(object.get("error_code") + ":" + object.get("reason"));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    // 将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

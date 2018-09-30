package com.wchm.website.util;

import java.util.HashMap;

// 请求返回数据对象
public class Result extends HashMap {

    private Result() {

    }

    // 创建返回对象
    public static Result create() {
        Result result = new Result();
        result.put("code", "00000");
        result.put("status", "success");
        result.put("msg", "操作成功");
        return result;
    }

    // 返回成功状态 消息
    public Result success(String msg) {
        this.put("msg", msg);
        return this;
    }

    // 返回成功状态 消息 数据
    public Result success(String msg, Object data) {
        this.put("code", "00000");
        this.put("msg", msg);
        this.put("data", data);
        return this;
    }

    // 返回成功状态 消息 数据
    public Result success(Object data) {
        this.put("data", data);
        return this;
    }

    // 返回失败状态
    public Result fail() {
        this.put("code", "00001");
        this.put("msg", "操作失败");
        this.put("status", "fail");
        return this;
    }

    // 返回失败状态 消息
    public Result fail(String msg) {
        this.put("code", "00001");
        this.put("msg", msg);
        this.put("status", "fail");
        return this;
    }

    // 返回失败状态 状态 消息
    public Result fail(String code, String msg) {
        this.put("code", code);
        this.put("msg", msg);
        this.put("status", "fail");
        return this;
    }

}

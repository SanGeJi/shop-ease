package com.shop.common;

public class Result {
    private int code;
    private String msg;
    private Object data;

    public Result(int code, String msg, Object data) {
        this.code = code; this.msg = msg; this.data = data;
    }
    public static Result ok(Object data) { return new Result(200, "ok", data); }
    public static Result ok() { return new Result(200, "ok", null); }
    public static Result error(int code, String msg) { return new Result(code, msg, null); }
    public static Result error(String msg) { return new Result(400, msg, null); }

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public Object getData() { return data; }
}
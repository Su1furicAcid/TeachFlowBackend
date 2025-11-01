package com.lss.teachflow.common;

import lombok.Data;

@Data
public class ResponseBody<T> {
    private String code;
    private String msg;
    private String detail;
    private T data;

    public ResponseBody() {
    }

    public ResponseBody(String code, String msg, String detail, T data) {
        this.code = code;
        this.msg = msg;
        this.detail = detail;
        this.data = data;
    }

    public static <T> ResponseBody<T> success(T data, String msg) {
        return new ResponseBody<>("200", msg, null, data);
    }

    public static <T> ResponseBody<T> success(T data) {
        return success(data, "操作成功");
    }

    public static <T> ResponseBody<T> success() {
        return success(null, "操作成功");
    }

    public static <T> ResponseBody<T> error(String code, String msg, String detail) {
        return new ResponseBody<>(code, msg, detail, null);
    }

    public static <T> ResponseBody<T> error(String code, String msg) {
        return error(code, msg, null);
    }
}


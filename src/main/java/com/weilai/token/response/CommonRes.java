package com.weilai.token.response;

import lombok.Data;

/**
 * 通用返回格式
 */
@Data
public class CommonRes {
    public static final int SUCCESS = 0;
    public static final int RELOGIN = 1;
    public static final int FAIL = 2;

    private int code;
    private String message;
    private Object data;

    public CommonRes() {
    }

    public CommonRes(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonRes(Object data) {
        this(SUCCESS, "", data);
    }

}

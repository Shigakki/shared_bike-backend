package com.common;

/**
 * 公共返回对象枚举
 *
 * @author: LC
 * @date 2022/3/2 1:44 下午
 * @ClassName: RespBean
 */

public enum RespEnum {

    //通用
    SUCCESS(200, "成功"),
    ERROR(500, "服务端异常"),

    //登录模块
    LOGIN_ERROR(500210, "用户名或者密码不正确"),
    SESSION_ERROR(500215, "用户SESSION不存在"),
    ;

    private final Integer code;
    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    RespEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

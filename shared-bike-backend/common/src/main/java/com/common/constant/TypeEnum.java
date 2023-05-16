package com.common.constant;

/**
 * @author: swh 20301169@bjtu.edu.cn
 * @description:
 * @date:
 */

public enum TypeEnum {
    USER(0,"普通用户"),
    ADMIN(1,"管理员");
    private int code;

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    private String value;
    TypeEnum(int code, String value) {
        this.value = value;
        this.code = code;
    }
}

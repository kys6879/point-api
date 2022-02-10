package com.musinsa.pointapi.http;

public enum CodeEnum {

    OK(200,"OK"),

    ERROR_MISSING_PARAMETER(-1 ,"ERROR_MISSING_PARAMETER"),
    ERROR_TYPE_MISMATCH(-2 ,"ERROR_TYPE_MISMATCH"),
    ERROR_NOT_FOUND(-3 ,"ERROR_NOT_FOUND"),
    ERROR_ILLEGAL_ARGUMENT(-4 ,"ERROR_ILLEGAL_ARGUMENT"),
    ERROR_NOTENOUGH_POINT(-5 ,"ERROR_NOTENOUGH_POINT");

    private Integer code;
    private String name;

    CodeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

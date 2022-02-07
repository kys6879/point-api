package com.musinsa.pointapi.point;

public enum PointStatusEnum {

    EARN(1,"EARN"), // 적립
    USED(2 ,"USED"); // 사용

    private Integer code;
    private String name;

    PointStatusEnum(Integer code, String name) {
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

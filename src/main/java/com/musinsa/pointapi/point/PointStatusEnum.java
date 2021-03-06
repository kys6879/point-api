package com.musinsa.pointapi.point;

public enum PointStatusEnum {

    EARN(1,"EARN"), // 적립
    USED(2 ,"USED"), // 사용
    CANCEL(3 ,"CANCEL"), // 취소
    EXPIRED(4 ,"EXPIRED"); // 취소

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

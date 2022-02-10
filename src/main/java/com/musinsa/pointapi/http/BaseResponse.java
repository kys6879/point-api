package com.musinsa.pointapi.http;

public class BaseResponse<T> {
    Boolean success;
    CodeEnum code;
    T data;

    public BaseResponse(Boolean success, CodeEnum code, T data) {
        this.success = success;
        this.code = code;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

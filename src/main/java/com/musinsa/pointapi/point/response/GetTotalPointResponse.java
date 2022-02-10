package com.musinsa.pointapi.point.response;

public class GetTotalPointResponse {
    Integer totalPoint;

    public GetTotalPointResponse(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }
}

package com.musinsa.pointapi.point.response;

import com.musinsa.pointapi.point.dto.PointDto;

public class UsePointResponse {
    PointDto pointDto;

    public UsePointResponse() {
    }

    public UsePointResponse(PointDto pointDto) {
        this.pointDto = pointDto;
    }

    public PointDto getPointDto() {
        return pointDto;
    }

    public void setPointDto(PointDto pointDto) {
        this.pointDto = pointDto;
    }
}

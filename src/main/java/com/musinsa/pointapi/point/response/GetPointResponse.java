package com.musinsa.pointapi.point.response;

import com.musinsa.pointapi.point.dto.PointDto;

public class GetPointResponse {
    PointDto pointDto;

    public GetPointResponse() {
    }

    public GetPointResponse(PointDto pointDto) {
        this.pointDto = pointDto;
    }

    public PointDto getPointDto() {
        return pointDto;
    }

    public void setPointDto(PointDto pointDto) {
        this.pointDto = pointDto;
    }
}

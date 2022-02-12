package com.musinsa.pointapi.point.response;

import com.musinsa.pointapi.point.dto.PointDto;

public class GetPointResponse {
    PointDto point;

    public GetPointResponse() {
    }

    public GetPointResponse(PointDto point) {
        this.point = point;
    }

    public PointDto getPoint() {
        return point;
    }

    public void setPoint(PointDto point) {
        this.point = point;
    }
}

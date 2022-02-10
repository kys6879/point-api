package com.musinsa.pointapi.point.response;

import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.dto.PointDto;

import java.util.List;

public class GetPointsResponse {

    private List<PointDto> points;

    private int totalSize;

    public GetPointsResponse(List<PointDto> points, int totalSize) {
        this.points = points;
        this.totalSize = totalSize;
    }

    public List<PointDto> getPoints() {
        return points;
    }

    public void setPoints(List<PointDto> points) {
        this.points = points;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}

package com.musinsa.pointapi.point.response;

import com.musinsa.pointapi.point.PointEntity;

import java.util.List;

public class GetPointsResponse {

    private List<PointEntity> points;

    private int totalSize;

    public GetPointsResponse(List<PointEntity> points, int totalSize) {
        this.points = points;
        this.totalSize = totalSize;
    }

    public List<PointEntity> getPoints() {
        return points;
    }

    public void setPoints(List<PointEntity> points) {
        this.points = points;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}

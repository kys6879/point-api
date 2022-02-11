package com.musinsa.pointapi.point_detail.repository.projection;

import com.musinsa.pointapi.point_detail.PointDetailEntity;

public class AvailablePointDto {
    Integer sum;
    PointDetailEntity pointDetail;

    public AvailablePointDto() {
    }

    public AvailablePointDto(Integer sum, PointDetailEntity pointDetail) {
        this.sum = sum;
        this.pointDetail = pointDetail;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public PointDetailEntity getPointDetail() {
        return pointDetail;
    }

    public void setPointDetail(PointDetailEntity pointDetail) {
        this.pointDetail = pointDetail;
    }
}

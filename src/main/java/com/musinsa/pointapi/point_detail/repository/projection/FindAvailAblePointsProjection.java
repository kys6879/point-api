package com.musinsa.pointapi.point_detail.repository.projection;

import com.musinsa.pointapi.point_detail.QPointDetailEntity;
import com.querydsl.core.annotations.QueryProjection;

public class FindAvailAblePointsProjection {
    private Integer sum;
    private QPointDetailEntity pointDetail;

    public FindAvailAblePointsProjection(Integer sum, QPointDetailEntity pointDetail) {
        this.sum = sum;
        this.pointDetail = pointDetail;
    }
}

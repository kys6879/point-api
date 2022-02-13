package com.musinsa.pointapi.point_detail.dto;

import com.musinsa.pointapi.point.PointStatusEnum;

import java.time.LocalDateTime;

public class PointDetailDto {
    private Long id;
    private Integer amount;
    private PointStatusEnum pointStatusEnum;
    private LocalDateTime actionAt;

    public PointDetailDto(Long id, Integer amount, PointStatusEnum pointStatusEnum, LocalDateTime actionAt) {
        this.id = id;
        this.amount = amount;
        this.pointStatusEnum = pointStatusEnum;
        this.actionAt = actionAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public PointStatusEnum getPointStatusEnum() {
        return pointStatusEnum;
    }

    public void setPointStatusEnum(PointStatusEnum pointStatusEnum) {
        this.pointStatusEnum = pointStatusEnum;
    }

    public LocalDateTime getActionAt() {
        return actionAt;
    }

    public void setActionAt(LocalDateTime actionAt) {
        this.actionAt = actionAt;
    }
}

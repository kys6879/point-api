package com.musinsa.pointapi.point_detail.dto;

import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.PointStatusEnum;
import com.musinsa.pointapi.point_detail.PointDetailEntity;

import java.time.LocalDateTime;

public class SavePointDetailSelfDto {
    private PointStatusEnum status;

    private Integer amount;

    private LocalDateTime actionAt;

    private LocalDateTime expireAt;

    private Long pointId;

    public SavePointDetailSelfDto(PointStatusEnum status, Integer amount, LocalDateTime actionAt, LocalDateTime expireAt, Long pointId) {
        this.status = status;
        this.amount = amount;
        this.actionAt = actionAt;
        this.expireAt = expireAt;
        this.pointId = pointId;
    }

    public PointStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PointStatusEnum status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getActionAt() {
        return actionAt;
    }

    public void setActionAt(LocalDateTime actionAt) {
        this.actionAt = actionAt;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public Long getPointId() {
        return pointId;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
    }

    public PointDetailEntity toEntity(PointEntity pointEntity) {
        return new PointDetailEntity(
                null,
                this.getStatus(),
                this.getAmount(),
                this.getActionAt(),
                this.getExpireAt(),
                pointEntity
        );
    }
}

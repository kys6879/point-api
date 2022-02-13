package com.musinsa.pointapi.point_detail.dto;

import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.PointStatusEnum;
import com.musinsa.pointapi.point_detail.PointDetailEntity;

import java.time.LocalDateTime;

public class SavePointDetailDto {
    private PointStatusEnum status;

    private Integer amount;

    private LocalDateTime actionAt;

    private LocalDateTime expireAt;

    private PointEntity pointEntity;

    private PointDetailEntity pointDetailEntity;

    public SavePointDetailDto(PointStatusEnum status, Integer amount, LocalDateTime actionAt, LocalDateTime expireAt, PointEntity pointEntity, PointDetailEntity pointDetailEntity) {
        this.status = status;
        this.amount = amount;
        this.actionAt = actionAt;
        this.expireAt = expireAt;
        this.pointEntity = pointEntity;
        this.pointDetailEntity = pointDetailEntity;
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

    public PointEntity getPointEntity() {
        return pointEntity;
    }

    public void setPointEntity(PointEntity pointEntity) {
        this.pointEntity = pointEntity;
    }

    public PointDetailEntity getPointDetailEntity() {
        return pointDetailEntity;
    }

    public void setPointDetailEntity(PointDetailEntity pointDetailEntity) {
        this.pointDetailEntity = pointDetailEntity;
    }

    public PointDetailEntity toEntity() {

        PointDetailEntity target = new PointDetailEntity(
                null,
                this.getStatus(),
                this.getAmount(),
                this.getActionAt(),
                this.getExpireAt(),
                this.getPointEntity()
        );

        target.setPointDetail(pointDetailEntity);

        return target;
    }
}

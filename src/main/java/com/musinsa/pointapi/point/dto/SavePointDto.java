package com.musinsa.pointapi.point.dto;

import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.PointStatusEnum;
import com.musinsa.pointapi.point_detail.PointDetailEntity;

import java.time.LocalDateTime;

public class SavePointDto {
    private PointStatusEnum status;

    private Integer amount;

    private LocalDateTime actionAt;

    private LocalDateTime expireAt;

    private Long memberId;

    public SavePointDto(PointStatusEnum status, Integer amount, LocalDateTime actionAt, LocalDateTime expireAt, Long memberId) {
        this.status = status;
        this.amount = amount;
        this.actionAt = actionAt;
        this.expireAt = expireAt;
        this.memberId = memberId;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public PointEntity toEntity(MemberEntity memberEntity) {
        return new PointEntity(
                null,
                this.getStatus(),
                this.getAmount(),
                this.getActionAt(),
                this.getExpireAt(),
                memberEntity
        );
    }
}

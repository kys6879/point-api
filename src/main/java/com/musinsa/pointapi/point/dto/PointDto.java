package com.musinsa.pointapi.point.dto;

import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.dto.MemberDto;
import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.PointStatusEnum;

import java.time.LocalDateTime;

public class PointDto {
    private Long id;

    private PointStatusEnum status;

    private Integer amount;

    private LocalDateTime actionAt;

    private LocalDateTime expireAt;

    private MemberDto member;

    public PointDto(Long id, PointStatusEnum status, Integer amount, LocalDateTime actionAt, LocalDateTime expireAt, MemberDto member) {
        this.id = id;
        this.status = status;
        this.amount = amount;
        this.actionAt = actionAt;
        this.expireAt = expireAt;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MemberDto getMember() {
        return member;
    }

    public void setMember(MemberDto member) {
        this.member = member;
    }

    public static PointDto from(PointEntity pointEntity) {

        MemberDto memberDto = MemberDto.from(pointEntity.getMember());

        return new PointDto(
                pointEntity.getId(),
                pointEntity.getStatus(),
                pointEntity.getAmount(),
                pointEntity.getActionAt(),
                pointEntity.getExpireAt(),
                memberDto
                );
    }
}

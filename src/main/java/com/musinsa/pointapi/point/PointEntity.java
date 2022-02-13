package com.musinsa.pointapi.point;

import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.point_detail.PointDetailEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "point")
public class PointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = PointStatusConverter.class)
    private PointStatusEnum status;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private LocalDateTime actionAt;

    @Column
    private LocalDateTime expireAt;

    @ManyToOne(targetEntity = MemberEntity.class, fetch = FetchType.LAZY)
    private MemberEntity member;

    @OneToMany(mappedBy = "point", fetch = FetchType.LAZY)
    private List<PointDetailEntity> pointDetailEntities = new ArrayList<>();

    public PointEntity(Long id, PointStatusEnum status, Integer amount, LocalDateTime actionAt, LocalDateTime expireAt, MemberEntity member) {
        this.id = id;
        this.status = status;
        this.amount = amount;
        this.actionAt = actionAt;
        this.expireAt = expireAt;
        this.member = member;
    }

    public PointEntity() {

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

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public List<PointDetailEntity> getPointDetailEntities() {
        return pointDetailEntities;
    }

    public void setPointDetailEntities(List<PointDetailEntity> pointDetailEntities) {
        this.pointDetailEntities = pointDetailEntities;
    }
}

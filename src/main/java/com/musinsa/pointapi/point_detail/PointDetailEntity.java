package com.musinsa.pointapi.point_detail;

import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.PointStatusConverter;
import com.musinsa.pointapi.point.PointStatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "point_detail")
public class PointDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = PointStatusConverter.class)
    private PointStatusEnum status;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private LocalDateTime actionAt;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    @ManyToOne(targetEntity = PointEntity.class, fetch = FetchType.LAZY)
    private PointEntity point;

    @ManyToOne(targetEntity = PointDetailEntity.class, fetch = FetchType.LAZY)
    private PointDetailEntity pointDetail;

    @OneToMany(mappedBy = "pointDetail", fetch = FetchType.LAZY)
    private List<PointDetailEntity> pointDetails;

    public PointDetailEntity(Long id, PointStatusEnum status, Integer amount, PointEntity point) {
        this.id = id;
        this.status = status;
        this.amount = amount;
        this.actionAt = point.getActionAt();
        this.expireAt = point.getExpireAt();
        this.point = point;
        this.pointDetail = this;
    }

    public PointDetailEntity(Long id, PointStatusEnum status, Integer amount, LocalDateTime actionAt, LocalDateTime expireAt, PointEntity point) {
        this.id = id;
        this.status = status;
        this.amount = amount;
        this.actionAt = actionAt;
        this.expireAt = expireAt;
        this.point = point;
        this.pointDetail = this;
    }

    public PointDetailEntity() {

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

    public PointEntity getPoint() {
        return point;
    }

    public void setPoint(PointEntity point) {
        this.point = point;
    }

    public PointDetailEntity getPointDetail() {
        return pointDetail;
    }

    public void setPointDetail(PointDetailEntity pointDetail) {
        this.pointDetail = pointDetail;
    }

    public List<PointDetailEntity> getPointDetails() {
        return pointDetails;
    }

    public void setPointDetails(List<PointDetailEntity> pointDetails) {
        this.pointDetails = pointDetails;
    }
}

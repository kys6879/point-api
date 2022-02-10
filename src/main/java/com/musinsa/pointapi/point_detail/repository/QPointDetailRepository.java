package com.musinsa.pointapi.point_detail.repository;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.point_detail.PointDetailEntity;
import com.musinsa.pointapi.point_detail.QPointDetailEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class QPointDetailRepository extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public QPointDetailRepository(JPAQueryFactory jpaQueryFactory) {
        super(PointDetailEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Integer findTotalPoint(Long memberId) {

        LocalDateTime now = CommonDateService.getToday();
        QPointDetailEntity pointDetailModel = QPointDetailEntity.pointDetailEntity;

        return this.jpaQueryFactory
                .select(
                        pointDetailModel.amount.sum()
                )
                .from(pointDetailModel)
                .where(
                        pointDetailModel.point.member.id.eq(memberId),
                        pointDetailModel.expireAt.after(now))
                .fetchOne();
    }
}
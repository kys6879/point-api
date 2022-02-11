package com.musinsa.pointapi.point_detail.repository;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.point_detail.PointDetailEntity;
import com.musinsa.pointapi.point_detail.QPointDetailEntity;
import com.musinsa.pointapi.point_detail.repository.projection.AvailablePointDto;
import com.querydsl.core.types.Projections;
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
                /* 상세 포인트별로 묶고 */
                .groupBy(pointDetailModel.pointDetail)
                /* 합계가 0이상인걸 구하기. (사용되지않은 포인트) */
                .having(pointDetailModel.amount.sum().gt(0))
                .where(
                        pointDetailModel.point.member.id.eq(memberId),
                        pointDetailModel.expireAt.after(now))
                .fetch()
                .stream()
                .mapToInt(value -> value)
                .sum();
    }

    public List<AvailablePointDto> findAvailablePoints(Long memberId) {

        LocalDateTime now = CommonDateService.getToday();
        QPointDetailEntity pointDetailModel = QPointDetailEntity.pointDetailEntity;

        return this.jpaQueryFactory
                .select(
                        Projections.bean(
                                AvailablePointDto.class,
                                pointDetailModel.amount.sum().as("sum"),
                                pointDetailModel.pointDetail.as("pointDetail"))
                        )
                .from(pointDetailModel)
                /* 상세 포인트별로 묶고 */
                .groupBy(pointDetailModel.pointDetail)
                /* 합계가 0이상인걸 구하기. (사용되지않은 포인트) */
                .having(pointDetailModel.amount.sum().gt(0))
                .where(
                        pointDetailModel.point.member.id.eq(memberId),
                        pointDetailModel.expireAt.after(now))
                .fetch();
    }

}
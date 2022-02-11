package com.musinsa.pointapi.point_detail.repository;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.point_detail.PointDetailEntity;
import com.musinsa.pointapi.point_detail.QPointDetailEntity;
import com.musinsa.pointapi.point_detail.repository.projection.AvailablePointDto;
import com.querydsl.core.Tuple;
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

    /* TODO self key join으로 합계 최적화 할 수 있음. */
    public Integer findTotalPoint(Long memberId) {

        LocalDateTime now = CommonDateService.getToday();
        QPointDetailEntity pointDetailModel = QPointDetailEntity.pointDetailEntity;

        return this.jpaQueryFactory
                /* 포인트의 합계를 */
                .select(
                        pointDetailModel.amount.sum()
                )
                /* point_detail 테이블에서 */
                .from(pointDetailModel)
                .where(
                        /* 특정 회원의 */
                        pointDetailModel.point.member.id.eq(memberId),
                        /* 유효한 것들을 */
                        pointDetailModel.expireAt.after(now))
                /* 가져온다. */
                .fetchOne();
    }

    public List<AvailablePointDto> findAvailablePoints(Long memberId) {

        LocalDateTime now = CommonDateService.getToday();
        QPointDetailEntity pointDetailModel = QPointDetailEntity.pointDetailEntity;

        return this.jpaQueryFactory
                .select(
                        Projections.bean(
                                AvailablePointDto.class,
                                pointDetailModel.amount.sum().as("sum"),
                                pointDetailModel.pointDetail.as("pointDetail")
                        )
                        )
                /* point_detail 테이블에서 */
                .from(pointDetailModel)
                /* 한 포인트에 대해 적립 또는 사용 여부를 묶어 */
                .groupBy(pointDetailModel.pointDetail)
                /* 합계가 0이 이상인것들중
                (묶인 적립 / 사용의 합이 0 이상이라는것은, 사용되지 않았거나 잔금이 남았다는 뜻. ) */
                .having(pointDetailModel.amount.sum().gt(0))
                .where(
                        /* 특정 회원의 */
                        pointDetailModel.point.member.id.eq(memberId),
                        /* 유효한 것들을 */
                        pointDetailModel.expireAt.after(now))
                /* 가져온다. */
                .fetch();
    }
}
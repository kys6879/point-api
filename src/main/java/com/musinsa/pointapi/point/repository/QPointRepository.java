package com.musinsa.pointapi.point.repository;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.QPointEntity;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class QPointRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public QPointRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    /* 가장 먼저 적립된 포인트 가져오기 */
    public PointEntity findOneFirstEarnedPoint() {

        LocalDateTime today = CommonDateService.getToday();

        QPointEntity model = QPointEntity.pointEntity;

        return this.jpaQueryFactory
                /* Point 테이블에서 */
                .selectFrom(model)
                /* 만료되지 않은 포인트들을*/
                .where(model.expireAt.after(today))
                /* 오름차순으로 정렬 후 */
                .orderBy(model.expireAt.asc())
                /* 최근 1개를 */
                .limit(1)
                /* 가져온다. */
                .fetchOne();
    }

}

package com.musinsa.pointapi.point.repository;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.member.QMemberEntity;
import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.QPointEntity;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class QPointRepository extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public QPointRepository(JPAQueryFactory jpaQueryFactory) {
        super(PointEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<PointEntity> findPointPage(Long memberId, Pageable pageable) {

        QPointEntity model = QPointEntity.pointEntity;
        QMemberEntity memberModel =QMemberEntity.memberEntity;

        JPAQuery<PointEntity> selectQuery = this.jpaQueryFactory
                /* point 테이블에서 */
                .selectFrom(model)
                /* member를 조인하고 */
                .leftJoin(model.member, memberModel)
                /* 특정 유저의 */
                .where(model.member.id.eq(memberId));

        JPQLQuery<PointEntity> pagingQuery = getQuerydsl()
                /* 페이징처리된 포인트들을 */
                .applyPagination(pageable,selectQuery);

        List<PointEntity> points =  pagingQuery
                /* 기져온다. */
                .fetch();
        Long totalCount = pagingQuery.fetchCount();

        return new PageImpl<>(points,pageable,totalCount);
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

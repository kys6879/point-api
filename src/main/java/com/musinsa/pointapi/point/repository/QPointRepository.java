package com.musinsa.pointapi.point.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QPointRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public QPointRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

//    public List<PointEntity> findPointsByMemberId(Long memberId) {
//
//    }

//    public PointEntity savePoint(PointEntity pointEntity) {
//        return this.jpaQueryFactory.
//    }

}

package com.musinsa.pointapi.member.repository;

import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.QMemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class QMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public QMemberRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public MemberEntity findMemberById(Long memberId) {

        QMemberEntity model = QMemberEntity.memberEntity;

        return this.jpaQueryFactory
                .selectFrom(model)
                .where(model.id.eq(memberId))
                .fetchOne();
    }
}

package com.musinsa.pointapi.point;

import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointServiceRealTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private MemberRepository memberRepository;

    private final Long member01Id = 1L;

    @BeforeEach
    void setUp() {

        this.memberRepository.save(new MemberEntity(
                member01Id,
                "test01",
                "1234",
                0
        ));

    }

    @Test
    void earnPointTest() {

        this.pointService.earnPoint(
                1000,
                member01Id);
    }

    @Test
    void usePointTest() {
        this.pointService.usePoint(500,member01Id);
    }
}

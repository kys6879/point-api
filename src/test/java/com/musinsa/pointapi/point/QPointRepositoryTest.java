package com.musinsa.pointapi.point;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class QPointRepositoryTest {

    private final TestEntityManager testEntityManager;


    @Autowired
    public QPointRepositoryTest(TestEntityManager testEntityManager) {
        this.testEntityManager = testEntityManager;
    }

    @DisplayName("회원별 포인트 합계 조회")
    @Test
    void findSumOfPointByMember() {

    }

}

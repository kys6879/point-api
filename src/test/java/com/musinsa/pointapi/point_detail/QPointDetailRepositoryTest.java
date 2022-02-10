package com.musinsa.pointapi.point_detail;

import com.musinsa.pointapi.persistence.QueryDslConfig;
import com.musinsa.pointapi.point_detail.repository.QPointDetailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(QueryDslConfig.class)
@ExtendWith(SpringExtension.class)
public class QPointDetailRepositoryTest {

    @Autowired
    private QPointDetailRepository qPointDetailRepository;

    @DisplayName("포인트의 합계를 구한다.")
    @Test
    void findOneFirstEarnedPointTest() {

        Integer totalPoint = this.qPointDetailRepository.findTotalPoint(1L);

        assertEquals(2700,totalPoint);
    }
}

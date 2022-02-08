package com.musinsa.pointapi.point;


import com.musinsa.pointapi.persistence.QueryDslConfig;
import com.musinsa.pointapi.point.repository.QPointRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Import(QueryDslConfig.class)
@ExtendWith(SpringExtension.class)
public class QPointRepositoryTest {

    @Autowired
    private QPointRepository qPointRepository;

    @DisplayName("현재로부터 만료일이 가장 가까운 적립된 포인트를 찾는다.")
    @Test
    void findOneFirstEarnedPointTest() {
        PointEntity FirstEarnedPoint = this.qPointRepository.findOneFirstEarnedPoint();

        System.out.println("asdf");
    }

}

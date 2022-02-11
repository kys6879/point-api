package com.musinsa.pointapi.point_detail;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.repository.MemberRepository;
import com.musinsa.pointapi.persistence.QueryDslConfig;
import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.PointStatusEnum;
import com.musinsa.pointapi.point.repository.PointRepository;
import com.musinsa.pointapi.point_detail.repository.PointDetailRepository;
import com.musinsa.pointapi.point_detail.repository.QPointDetailRepository;
import com.musinsa.pointapi.point_detail.repository.projection.AvailablePointDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(QueryDslConfig.class)
@ExtendWith(SpringExtension.class)
public class QPointDetailRepositoryTest {

    @Autowired
    private QPointDetailRepository qPointDetailRepository;

    @Autowired
    private PointDetailRepository pointDetailRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    private MemberEntity mockMember01;

    @BeforeEach
    void setUp() {
        this.mockMember01 = this.buildMockMember01();
        this.memberRepository.save(this.mockMember01);
        this.pointDetailRepository.deleteAll();
        this.pointRepository.deleteAll();
    }

    @DisplayName("포인트의 합계를 구한다.")
    @Test
    void testFindTotalPoint() {

        /* Given */

        /* 1000 적립 */
        PointEntity pointEntity01 = this.pointRepository.save(this.buildMockEarnPoint(1000,this.mockMember01));
        this.pointDetailRepository.save(this.buildMockEarnPointDetail(pointEntity01));

        /* 2000 적립 */
        PointEntity pointEntity02 = this.pointRepository.save(this.buildMockEarnPoint(2000,this.mockMember01));
        this.pointDetailRepository.save(this.buildMockEarnPointDetail(pointEntity02));

        /* 3000 적립 */
        PointEntity pointEntity03 = this.pointRepository.save(this.buildMockEarnPoint(3000,this.mockMember01));
        this.pointDetailRepository.save(this.buildMockEarnPointDetail(pointEntity03));

        /* When */
        Integer totalPoint = this.qPointDetailRepository.findTotalPoint(this.mockMember01.getId());

        assertEquals(6000,totalPoint);
    }

    @DisplayName("사용 가능한 포인트들을 가져온다.")
    @Test
    void findAvailablePointsTest() {
        List<AvailablePointDto> results = this.qPointDetailRepository.findAvailablePoints(1L);
    }

    private PointEntity buildMockEarnPoint(Integer amount, MemberEntity memberEntity) {
        LocalDateTime now = CommonDateService.getToday();
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

        return new PointEntity(null, PointStatusEnum.EARN,amount,now,afterOneYear,memberEntity);
    }

    private PointDetailEntity buildMockEarnPointDetail(PointEntity pointEntity) {
        return new PointDetailEntity(null,pointEntity.getStatus(),pointEntity.getAmount(),pointEntity.getActionAt(),pointEntity.getExpireAt(),pointEntity);
    }

    public MemberEntity buildMockMember01() {
        return new MemberEntity(null,"mock01@example.com","1234");
    }
}

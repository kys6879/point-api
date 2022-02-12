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
        this.pointDetailRepository.deleteAll();
        this.pointRepository.deleteAll();
        this.memberRepository.deleteAll();

        this.mockMember01 = this.buildMockMember01();
        this.memberRepository.save(this.mockMember01);
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

        /* Then */
        assertEquals(6000,totalPoint);
    }

    @DisplayName("사용 가능한 포인트들을 가져온다.")
    @Test
    void findAvailablePointsTest() {

        /* Given */

        /* 1000 적립 */
        PointEntity pointEntity01 = this.pointRepository.save(this.buildMockEarnPoint(1000,this.mockMember01));
        PointDetailEntity pointDetailEntity01 = this.buildMockEarnPointDetail(pointEntity01);
        this.pointDetailRepository.save(pointDetailEntity01);

        /* 2000 적립 */
        PointEntity pointEntity02 = this.pointRepository.save(this.buildMockEarnPoint(2000,this.mockMember01));
        PointDetailEntity pointDetailEntity02 = this.buildMockEarnPointDetail(pointEntity02);
        this.pointDetailRepository.save(pointDetailEntity02);

        /* 3000 적립 */
        PointEntity pointEntity03 = this.pointRepository.save(this.buildMockEarnPoint(3000,this.mockMember01));
        PointDetailEntity pointDetailEntity03 = this.buildMockEarnPointDetail(pointEntity03);
        this.pointDetailRepository.save(pointDetailEntity03);

        /* 3300 사용 */
        PointEntity pointEntity04 = this.pointRepository.save(this.buildMockUsePoint(-3300,this.mockMember01));

        /* 먼저 적립했던 1000 사용 */
        PointDetailEntity pointDetailEntity04 = this.buildMockUsePointDetail(pointEntity04,-1000);
        pointDetailEntity04.setPointDetail(pointDetailEntity01);
        this.pointDetailRepository.save(pointDetailEntity04);

        /* 먼저 적립했던 2000 사용 */
        PointDetailEntity pointDetailEntity05 = this.buildMockUsePointDetail(pointEntity04,-2000);
        pointDetailEntity05.setPointDetail(pointDetailEntity02);
        this.pointDetailRepository.save(pointDetailEntity05);

        /* 먼저 적립했던 3000에서 300 사용 */
        PointDetailEntity pointDetailEntity06 = this.buildMockUsePointDetail(pointEntity04,-300);
        pointDetailEntity06.setPointDetail(pointDetailEntity03);
        this.pointDetailRepository.save(pointDetailEntity06);

        /* When */
        List<AvailablePointDto> results = this.qPointDetailRepository.findAvailablePoints(this.mockMember01.getId());

        /* Then */

        /* 1000 + 2000 + 3000 - 3300 = 2700 */
        assertEquals(2700,results.get(0).getSum());

        /* 2700은 적립했던 3000 포인트의 잔액이다. */
        assertEquals(pointEntity03.getId(),results.get(0).getPointDetail().getPoint().getId());
    }

    @DisplayName("만료 대상 포인트들을 가져온다.")
    @Test
    void findAvailableExpiredPoints() {
        /* Given */

        /* 과거에 1000 적립 */
        LocalDateTime now01  = LocalDateTime.now().minusYears(2);
        PointEntity pointEntity01 = this.pointRepository.save(this.buildMockEarnPoint(1000,now01,this.mockMember01));
        PointDetailEntity pointDetailEntity01 = this.buildMockEarnPointDetail(pointEntity01);
        this.pointDetailRepository.save(pointDetailEntity01);

        /* 2년전에 2000 적립 */
        LocalDateTime now02  = LocalDateTime.now().minusYears(2);
        PointEntity pointEntity02 = this.pointRepository.save(this.buildMockEarnPoint(2000,now02,this.mockMember01));
        PointDetailEntity pointDetailEntity02 = this.buildMockEarnPointDetail(pointEntity02);
        this.pointDetailRepository.save(pointDetailEntity02);

        /* 2년전에 3000 적립 */
        LocalDateTime now03  = LocalDateTime.now().minusYears(2);
        PointEntity pointEntity03 = this.pointRepository.save(this.buildMockEarnPoint(3000,now03,this.mockMember01));
        PointDetailEntity pointDetailEntity03 = this.buildMockEarnPointDetail(pointEntity03);
        this.pointDetailRepository.save(pointDetailEntity03);

        /* 2년전에 3300 사용 */
        LocalDateTime now04  = LocalDateTime.now().minusYears(2);
        PointEntity pointEntity04 = this.pointRepository.save(this.buildMockUsePoint(-3300,now04,this.mockMember01));

        /* 2년전에 먼저 적립했던 1000 사용 */
        /* 1000 적립했던걸 사용했다는 연관관계 설정 */
        PointDetailEntity pointDetailEntity04 = this.buildMockUsePointDetail(pointEntity04,-1000);
        pointDetailEntity04.setPointDetail(pointDetailEntity01);
        this.pointDetailRepository.save(pointDetailEntity04);

        /* 2년전에 먼저 적립했던 2000 사용 */
        /* 2000 적립했던걸 사용했다는 연관관계 설정 */
        PointDetailEntity pointDetailEntity05 = this.buildMockUsePointDetail(pointEntity04,-2000);
        pointDetailEntity05.setPointDetail(pointDetailEntity02);
        this.pointDetailRepository.save(pointDetailEntity05);

        /* 2년전에 먼저 적립했던 3000에서 300 사용 */
        /* 3000 적립했던걸 사용했다는 연관관계 설정 */
        PointDetailEntity pointDetailEntity06 = this.buildMockUsePointDetail(pointEntity04,-300);
        pointDetailEntity06.setPointDetail(pointDetailEntity03);
        this.pointDetailRepository.save(pointDetailEntity06);

        /* 현재 1000 적립 */
        PointEntity pointEntity05 = this.pointRepository.save(this.buildMockEarnPoint(1000,this.mockMember01));
        PointDetailEntity pointDetailEntity07 = this.buildMockEarnPointDetail(pointEntity05);
        this.pointDetailRepository.save(pointDetailEntity07);

        /* 현재 2000 적립 */
        PointEntity pointEntity06 = this.pointRepository.save(this.buildMockEarnPoint(2000,this.mockMember01));
        PointDetailEntity pointDetailEntity08 = this.buildMockEarnPointDetail(pointEntity06);
        this.pointDetailRepository.save(pointDetailEntity08);

        /* 현재 1300 사용 */
        PointEntity pointEntity07 = this.pointRepository.save(this.buildMockUsePoint(-1300,this.mockMember01));

        /* 먼저 적립했던 1000 사용 */
        PointDetailEntity pointDetailEntity09 = this.buildMockUsePointDetail(pointEntity07,-1000);
        pointDetailEntity09.setPointDetail(pointDetailEntity07);
        this.pointDetailRepository.save(pointDetailEntity09);

        /* 먼저 적립했던 2000에서 300 사용 */
        /* 2000 적립했던걸 사용했다는 연관관계 설정 */
        PointDetailEntity pointDetailEntity10 = this.buildMockUsePointDetail(pointEntity07,-300);
        pointDetailEntity10.setPointDetail(pointDetailEntity08);
        this.pointDetailRepository.save(pointDetailEntity10);

        /* When */
        List<AvailablePointDto> availableExpiredPoints = this.qPointDetailRepository.findAvailableExpiredPoints(this.mockMember01.getId());
        List<AvailablePointDto> availablePoints = this.qPointDetailRepository.findAvailablePoints(this.mockMember01.getId());

        /* 현재 적립한 사용가능한 포인트는 나오지 말아야함. */
        assertEquals(1,availableExpiredPoints.size());

        /* 1000 + 2000 + 3000 - 3300 = 2700 */
        assertEquals(2700,availableExpiredPoints.get(0).getSum());

        /* 2700은 적립했던 3000 포인트의 잔액이다. */
        assertEquals(pointEntity03.getId(),availableExpiredPoints.get(0).getPointDetail().getPoint().getId());

        assertEquals(1,availablePoints.size());

        /* 1000 + 2000 - 1300 = 1700 */
        assertEquals(1700,availablePoints.get(0).getSum());

        /* 1700은 적립했던 2000 포인트의 잔액이다. */
        assertEquals(pointEntity06.getId(),availablePoints.get(0).getPointDetail().getPoint().getId());

    }

    private PointEntity buildMockEarnPoint(Integer amount, MemberEntity memberEntity) {
        return this.buildMockPoint(PointStatusEnum.EARN,amount,memberEntity);
    }

    private PointEntity buildMockEarnPoint(Integer amount, LocalDateTime now, MemberEntity memberEntity) {
        return this.buildMockPoint(PointStatusEnum.EARN,now,amount,memberEntity);
    }

    private PointEntity buildMockUsePoint(Integer amount, MemberEntity memberEntity) {
        return this.buildMockPoint(PointStatusEnum.USED,amount,memberEntity);
    }

    private PointEntity buildMockUsePoint(Integer amount, LocalDateTime now, MemberEntity memberEntity) {
        return this.buildMockPoint(PointStatusEnum.USED,now,amount,memberEntity);
    }

    private PointEntity buildMockPoint(PointStatusEnum pointStatusEnum, Integer amount, MemberEntity memberEntity) {
        LocalDateTime now = CommonDateService.getToday();
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

        return new PointEntity(null, pointStatusEnum,amount,now,afterOneYear,memberEntity);
    }

    private PointEntity buildMockPoint(PointStatusEnum pointStatusEnum, LocalDateTime now, Integer amount, MemberEntity memberEntity) {
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

        return new PointEntity(null, pointStatusEnum,amount,now,afterOneYear,memberEntity);
    }

    private PointDetailEntity buildMockEarnPointDetail(PointEntity pointEntity) {
        return this.buildMockPointDetail(PointStatusEnum.EARN,pointEntity.getAmount(),pointEntity);
    }

    private PointDetailEntity buildMockUsePointDetail(PointEntity pointEntity) {
        return this.buildMockPointDetail(PointStatusEnum.USED,pointEntity.getAmount(),pointEntity);
    }

    private PointDetailEntity buildMockUsePointDetail(PointEntity pointEntity,Integer amount) {
        return this.buildMockPointDetail(PointStatusEnum.USED,amount,pointEntity);
    }

    private PointDetailEntity buildMockPointDetail(PointStatusEnum pointStatusEnum, Integer amount, PointEntity pointEntity) {
        return new PointDetailEntity(null,pointStatusEnum,amount,pointEntity.getActionAt(),pointEntity.getExpireAt(),pointEntity);
    }

    public MemberEntity buildMockMember01() {
        return new MemberEntity(null,"mock01@example.com","1234");
    }
}

// 1000
// 2000
// 3000
// -3300

// -1000

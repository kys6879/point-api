package com.musinsa.pointapi.point;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.point.repository.PointRepository;
import com.musinsa.pointapi.point.repository.QPointRepository;
import com.musinsa.pointapi.point_detail.PointDetailEntity;
import com.musinsa.pointapi.point_detail.PointDetailService;
import com.musinsa.pointapi.point_detail.repository.projection.AvailablePointDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private MemberService memberService;
    @Mock
    private PointDetailService pointDetailService;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private QPointRepository qPointRepository;

    private final Long mockMember01Id = 1L;

    private final Long mockPoint01Id = 1L;

    @DisplayName("회원별 포인트 적립/사용 내역 조회")
    @Test
    public void findPointsByMemberIdTest() {

        /* Given */
        int size = 10;
        int page = 0;
        MemberEntity mockMemberEntity = this.buildMockMember01();

        given(this.memberService.findMemberById(mockMember01Id))
                .willReturn(mockMemberEntity);

        PageRequest pageRequest = PageRequest.of(page,size);

        List<PointEntity> mockPointEntities = this.buildMockPoints(size,mockMemberEntity);

        Page<PointEntity> mockPointPage = this.buildMockPointPage(mockPointEntities,pageRequest,1L);

        given(this.qPointRepository.findPointPage(mockMemberEntity.getId(),pageRequest))
                .willReturn(mockPointPage);

        /* When */
        Page<PointEntity> pointPage = this.pointService.findPoints(mockMember01Id,pageRequest);

        /* Then */
        assertEquals(size,pointPage.getContent().size());
        assertEquals(page,pointPage.getPageable().getPageNumber());
    }

    @DisplayName("포인트 적립")
    @Test
    public void earnPointTest() {

        /* Given */
        Integer amount = 1000; // 1000 Point 적립
        MemberEntity mockMemberEntity = this.buildMockMember01();
        PointEntity mockPointEntity = this.buildMockEarnPoint01(amount,mockMemberEntity);

        given(this.memberService.findMemberById(mockMember01Id))
                .willReturn(mockMemberEntity);

        given(this.pointRepository.save(any()))
                .willReturn(mockPointEntity);

        given(this.pointDetailService.savePointDetailSelf(any())).willReturn(any());

        /* When */
        PointEntity earnedPoint = this.pointService.earnPoint(amount,mockMemberEntity.getId());

        /* Then */
        assertEquals(mockPointEntity.getId(),earnedPoint.getId());
        assertEquals(mockPointEntity.getStatus(),earnedPoint.getStatus());
        assertEquals(mockPointEntity.getAmount(),earnedPoint.getAmount());
        assertEquals(mockPointEntity.getActionAt(),earnedPoint.getActionAt());
        assertEquals(mockPointEntity.getExpireAt(),earnedPoint.getExpireAt());
        assertEquals(mockPointEntity.getMember().getId(),earnedPoint.getMember().getId());

        assertEquals(amount,earnedPoint.getAmount());
    }

    @DisplayName("포인트 사용")
    @Test
    public void usePointTest() {
        /* Given */
        Integer amount = 1500;
        Integer totalPoint = 10000;

        MemberEntity mockMemberEntity = this.buildMockMember01();
        PointEntity pointEntity = this.buildMockUsePoint(amount,mockMemberEntity);

        /* 현재 잔액 설정 */
        given(this.pointDetailService.findTotalPoint(mockMemberEntity.getId()))
                .willReturn(totalPoint);

        /* 1500원 사용 포인트 이력 설정 */
        given(this.pointRepository.save(any()))
                .willReturn(pointEntity);

        /* 사용가능한 10000포인트 1개 설정 */
        List<AvailablePointDto> dtos = new ArrayList<>();
        dtos.add(this.buildMockAvailablePointDto(totalPoint,this.buildMockEarnPointDetail(pointEntity)));

        given(this.pointDetailService.findAvailablePoints(mockMemberEntity.getId()))
                .willReturn(dtos);

        /* 1500원 사용 포인트 디테일 이력 설정 */

        List<PointDetailEntity> detailDtos = new ArrayList<>();

        PointDetailEntity detail01 = this.buildMockUsePointDetail(pointEntity,amount);

        detail01.setPointDetail(this.buildMockEarnPointDetail(
                this.buildMockEarnPoint01(amount,mockMemberEntity)
        ));
        detailDtos.add(detail01);

        given(this.pointDetailService.saveAllpointDetail(any()))
                .willReturn(detailDtos);

        /* When */
        PointEntity usedPoint = this.pointService.usePoint(amount,mockMemberEntity.getId());

        assertEquals(pointEntity.getAmount(),usedPoint.getAmount());
        assertEquals(pointEntity.getStatus(),usedPoint.getStatus());
        assertEquals(pointEntity.getActionAt(),usedPoint.getActionAt());
        assertEquals(pointEntity.getExpireAt(),usedPoint.getExpireAt());

        assertEquals(amount,usedPoint.getAmount());

        /* 사용된 포인트의 상세정보는 적립했던 금액(원본) 이다.*/
        assertEquals(PointStatusEnum.EARN,usedPoint.getPointDetailEntities().get(0).getStatus());
    }

    public MemberEntity buildMockMember01() {
        return new MemberEntity(mockMember01Id,"mock01@example.com","1234");
    }

    public PointEntity buildMockEarnPoint01(Integer amount, MemberEntity memberEntity) {
        LocalDateTime now = CommonDateService.getToday();
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

        return new PointEntity(mockPoint01Id,PointStatusEnum.EARN,amount,now,afterOneYear,memberEntity);
    }

    private PointEntity buildMockUsePoint(Integer amount, MemberEntity memberEntity) {
        return this.buildMockPoint(PointStatusEnum.USED,amount,memberEntity);
    }

    private PointEntity buildMockPoint(PointStatusEnum pointStatusEnum, Integer amount, MemberEntity memberEntity) {
        LocalDateTime now = CommonDateService.getToday();
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

        return new PointEntity(null, pointStatusEnum,amount,now,afterOneYear,memberEntity);
    }

    private AvailablePointDto buildMockAvailablePointDto(Integer sum,PointDetailEntity pointDetailEntity) {
        return new AvailablePointDto(sum,pointDetailEntity);
    }

    private PointDetailEntity buildMockEarnPointDetail(PointEntity pointEntity) {
        return this.buildMockPointDetail(PointStatusEnum.EARN,pointEntity.getAmount(),pointEntity);
    }

    private PointDetailEntity buildMockUsePointDetail(PointEntity pointEntity,Integer amount) {
        return this.buildMockPointDetail(PointStatusEnum.USED,amount,pointEntity);
    }

    private PointDetailEntity buildMockPointDetail(PointStatusEnum pointStatusEnum, Integer amount, PointEntity pointEntity) {
        return new PointDetailEntity(null,pointStatusEnum,amount,pointEntity.getActionAt(),pointEntity.getExpireAt(),pointEntity);
    }



    public List<PointEntity> buildMockPoints(Integer size, MemberEntity memberEntity) {

        List<PointEntity> pointEntities = new ArrayList<>();
        for (int i=0; i<size; i++) {
            LocalDateTime now = CommonDateService.getToday();
            LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

            pointEntities.add(new PointEntity(mockPoint01Id,PointStatusEnum.EARN,1000,now,afterOneYear,memberEntity));
        }

        return pointEntities;
    }



    public Page<PointEntity> buildMockPointPage(List<PointEntity> points, Pageable pageable, Long totalCount) {
        return new PageImpl<>(points,pageable,totalCount);
    }
}

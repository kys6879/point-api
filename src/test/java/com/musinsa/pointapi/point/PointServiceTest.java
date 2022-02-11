package com.musinsa.pointapi.point;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.point.repository.PointRepository;
import com.musinsa.pointapi.point.repository.QPointRepository;
import com.musinsa.pointapi.point_detail.PointDetailEntity;
import com.musinsa.pointapi.point_detail.PointDetailService;
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

    private final Long mockPointDetail01Id = 1L;

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
        PointDetailEntity mockPointDetailEntity = this.buildMockPointDetail01(mockPointEntity);

        given(this.memberService.findMemberById(mockMember01Id))
                .willReturn(mockMemberEntity);

        given(this.pointRepository.save(any()))
                .willReturn(mockPointEntity);

        given(this.pointDetailService.savePointDetail(any()))
                .willReturn(mockPointDetailEntity);

        /* When */
        PointEntity pointEntity = this.pointService.earnPoint(amount,mockMemberEntity.getId());

        /* Then */
        assertEquals(mockPointEntity.getId(),pointEntity.getId());
        assertEquals(mockPointEntity.getStatus(),pointEntity.getStatus());
        assertEquals(mockPointEntity.getAmount(),pointEntity.getAmount());
        assertEquals(mockPointEntity.getActionAt(),pointEntity.getActionAt());
        assertEquals(mockPointEntity.getExpireAt(),pointEntity.getExpireAt());
        assertEquals(mockPointEntity.getMember().getId(),pointEntity.getMember().getId());

        assertEquals(amount,pointEntity.getAmount());
    }

    public MemberEntity buildMockMember01() {
        return new MemberEntity(mockMember01Id,"mock01@example.com","1234");
    }

    public PointEntity buildMockEarnPoint01(Integer amount, MemberEntity memberEntity) {
        LocalDateTime now = CommonDateService.getToday();
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

        return new PointEntity(mockPoint01Id,PointStatusEnum.EARN,amount,now,afterOneYear,memberEntity);
    }

    public PointDetailEntity buildMockPointDetail01(PointEntity pointEntity) {
        return new PointDetailEntity(mockPointDetail01Id,pointEntity.getStatus(),pointEntity.getAmount(),pointEntity.getActionAt(),pointEntity.getExpireAt(),pointEntity);
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

package com.musinsa.pointapi.point;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.point.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private MemberService memberService;
    @Mock
    private PointRepository pointRepository;

    private final Long mockMember01Id = 1L;

    private final Long mockPoint01Id = 1L;

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
        return new MemberEntity(mockMember01Id,"mock01@example.com","1234",0);
    }

    public PointEntity buildMockEarnPoint01(Integer amount, MemberEntity memberEntity) {
        LocalDateTime now = CommonDateService.getToday();
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

        return new PointEntity(mockPoint01Id,PointStatusEnum.EARN,amount,now,afterOneYear,memberEntity);
    }
}

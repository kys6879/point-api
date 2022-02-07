package com.musinsa.pointapi.point;

import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.point.repository.PointRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class PointService {

    private final MemberService memberService;
    private final PointRepository pointRepository;

    public PointService(MemberService memberService, PointRepository pointRepository) {
        this.memberService = memberService;
        this.pointRepository = pointRepository;
    }

    // 포인트 적립
    // TODO => AOP로 포인트의 정합성 체크 필요
    @Transactional
    public PointEntity earnPoint(Integer amount, Long memberId) {

        LocalDateTime now = CommonDateService.getToday();
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(now);

        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        /* 포인트 테이블에 INSERT */
        PointEntity pointEntity = new PointEntity(
                0L,
                PointStatusEnum.EARN,
                amount,
                now,
                afterOneYear,
                memberEntity
        );

        PointEntity savedPointEntity = pointRepository.save(pointEntity);

        /* 유저 테이블에 UPDATE */
        this.memberService.increaseMemberPoint(amount,memberId);

        return savedPointEntity;
    }

}

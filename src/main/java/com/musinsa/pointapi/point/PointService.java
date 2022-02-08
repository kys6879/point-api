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
    // TODO AOP로 포인트의 정합성 체크 필요
    @Transactional
    public PointEntity earnPoint(Integer amount, Long memberId) {

        LocalDateTime actionAt = CommonDateService.getToday();
        LocalDateTime expireAt = CommonDateService.getAfterOneYear(actionAt);

        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        /* 포인트 테이블에 INSERT */
        PointEntity pointEntity = new PointEntity(
                0L,
                PointStatusEnum.EARN,
                amount,
                actionAt,
                expireAt,
                memberEntity
        );

        return pointRepository.save(pointEntity);
    }

    // 포인트 사용
//    public PointEntity usePoint(Integer amount, Long memberId) {
//
//        /* 현재로부터 만료일이 가장 가까운 적립된 포인트를 찾는다. */
//
//        /* 그 포인트의 만료일을 가져온다. */
//
//        /* 가까운 포인트의 만료일을 가지고 포인트를 사용한다. */
//
//    }

}

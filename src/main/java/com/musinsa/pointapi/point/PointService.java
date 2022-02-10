package com.musinsa.pointapi.point;

import com.musinsa.pointapi.advice.exception.NotEnoughPointException;
import com.musinsa.pointapi.common.CommonDateService;
import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.point.repository.PointRepository;
import com.musinsa.pointapi.point.repository.QPointRepository;
import com.musinsa.pointapi.point_detail.PointDetailEntity;
import com.musinsa.pointapi.point_detail.PointDetailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class PointService {

    private final MemberService memberService;
    private final PointDetailService pointDetailService;
    private final PointRepository pointRepository;
    private final QPointRepository qPointRepository;

    public PointService(MemberService memberService, PointDetailService pointDetailService, PointRepository pointRepository, QPointRepository qPointRepository) {
        this.memberService = memberService;
        this.pointDetailService = pointDetailService;
        this.pointRepository = pointRepository;
        this.qPointRepository = qPointRepository;
    }

    public Page<PointEntity> findPoints(Long memberId, Pageable pageable) {
        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        return this.qPointRepository.findPointPage(memberEntity.getId(),pageable);
    }

    // 포인트 적립
    // TODO AOP로 포인트의 정합성 체크 필요
    @Transactional
    public PointEntity earnPoint(Integer amount, Long memberId) {

        /* 포인트 적립은 1원 이상부터 가능합니다. 음수일시 양수로 전환하고, 0원일시 IllegalArgumentException 예외를 던집니다. */
        amount = this.toPositiveNumber(amount);

        LocalDateTime actionAt = CommonDateService.getToday();
        LocalDateTime expireAt = CommonDateService.getAfterOneYear(actionAt);

        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        /* INSERT into point  */
        PointEntity pointEntity = new PointEntity(
                null,
                PointStatusEnum.EARN,
                amount,
                actionAt,
                expireAt,
                memberEntity
        );

        PointEntity savedPointEntity = pointRepository.save(pointEntity);

        /* INSERT into point_detail */
        PointDetailEntity pointDetailEntity = new PointDetailEntity(
                null,
                PointStatusEnum.EARN,
                amount,
                actionAt,
                expireAt,
                pointEntity
        );

        pointDetailEntity.setPointDetail();

        pointDetailService.savePointDetail(pointDetailEntity);

        return savedPointEntity;
    }

    // 포인트 사용
    @Transactional
    public PointEntity usePoint(Integer amount, Long memberId) {

        /* 포인트 사용은 -1원 이하부터 가능합니다. 양수일시 음수로 전환하고, 0원일시 IllegalArgumentException 예외를 던집니다. */
        amount = this.toNegativeNumber(amount);

        /* 잔액이 충분한지 */
        if(!this.isEnoughPoint(amount,memberId)) {
            throw new NotEnoughPointException("잔액이 부족합니다.");
        }

        return null;


//        LocalDateTime actionAt = CommonDateService.getToday();
//
//        MemberEntity memberEntity = this.memberService.findMemberById(memberId);
//
//        /* INSERT into point  */
//        PointEntity pointEntity = new PointEntity(
//                null,
//                PointStatusEnum.USED,
//                amount,
//                actionAt,
//                null,
//                memberEntity
//        );
//
//        PointEntity savedPointEntity = pointRepository.save(pointEntity);
//
//        // 유효기간이 가장 가까운 것들을 가져온다.
//
//        //
//
//        return savedPointEntity;
    }

    private Integer toNegativeNumber(int amount) {
        if(amount > 0) {
            return amount * -1;
        }
        if(amount < 0) {
            return amount;
        }
        throw new IllegalArgumentException("입력값이 잘못되었습니다. 0원은 사용할 수 없습니다.");
    }

    private Integer toPositiveNumber(int amount) {
        if(amount > 0) {
            return amount;
        }
        if(amount < 0) {
            return amount * -1;
        }
        throw new IllegalArgumentException("입력값이 잘못되었습니다. 0원은 사용할 수 없습니다.");
    }

    private Boolean isEnoughPoint(Integer amount, Long memberId) {
        Integer totalPoint = this.pointDetailService.findTotalPoint(memberId);
        return totalPoint - Math.abs(amount) >= 0;
    }

    // 2700
    // -3300

}

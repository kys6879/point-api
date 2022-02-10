package com.musinsa.pointapi.point;

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
    // 잔액 부족 예외처리 필요
    @Transactional
    public PointEntity usePoint(Integer amount, Long memberId) {

        amount = this.toNegativeNumber(amount);

        LocalDateTime actionAt = CommonDateService.getToday();

        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        /* INSERT into point  */
        PointEntity pointEntity = new PointEntity(
                null,
                PointStatusEnum.USED,
                amount,
                actionAt,
                null,
                memberEntity
        );

        PointEntity savedPointEntity = pointRepository.save(pointEntity);

        // 유효기간이 가장 가까운 것들을 가져온다.

        //

        return savedPointEntity;
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

}

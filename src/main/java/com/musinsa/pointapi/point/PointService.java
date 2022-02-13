package com.musinsa.pointapi.point;

import com.musinsa.pointapi.advice.exception.NotEnoughPointException;
import com.musinsa.pointapi.advice.exception.NotFoundException;

import com.musinsa.pointapi.common.CommonDateService; // @TODO static method 서비스로 분리
import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.point.dto.SavePointDto;
import com.musinsa.pointapi.point.repository.PointRepository;
import com.musinsa.pointapi.point.repository.QPointRepository;
import com.musinsa.pointapi.point_detail.PointDetailEntity;
import com.musinsa.pointapi.point_detail.PointDetailService;
import com.musinsa.pointapi.point_detail.dto.SavePointDetailDto;
import com.musinsa.pointapi.point_detail.dto.SavePointDetailSelfDto;
import com.musinsa.pointapi.point_detail.repository.projection.AvailablePointDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public PointEntity savePoint(SavePointDto pointDto) {
        MemberEntity memberEntity = this.memberService.findMemberById(pointDto.getMemberId());

        /* INSERT into point  */
        PointEntity pointEntity = pointDto.toEntity(memberEntity);
        return pointRepository.save(pointEntity);
    }

    // 포인트 적립
    @Transactional
    public PointEntity earnPoint(Integer amount, Long memberId) {

        /* 포인트 적립은 1원 이상부터 가능합니다. 음수일시 양수로 전환하고, 0원일시 IllegalArgumentException 예외를 던집니다. */
        amount = this.toPositiveNumber(amount);

        LocalDateTime actionAt = CommonDateService.getToday();
        LocalDateTime expireAt = CommonDateService.getAfterOneYear(actionAt);

        PointEntity savedPointEntity = this.savePoint(new SavePointDto(PointStatusEnum.EARN,amount,actionAt,expireAt,memberId));

        this.pointDetailService.savePointDetailSelf(new SavePointDetailSelfDto(PointStatusEnum.EARN,amount,actionAt,expireAt,savedPointEntity.getId()));

        return savedPointEntity;
    }

    // 포인트 사용
    // TODO 단일책임원칙 리팩토링
    @Transactional
    public PointEntity usePoint(Integer amount, Long memberId) {

        /* 포인트 사용은 -1원 이하부터 가능합니다. 양수일시 음수로 전환하고, 0원일시 IllegalArgumentException 예외를 던집니다. */
        amount = this.toNegativeNumber(amount);

        /* 잔액이 충분한지 */
        if(!this.isEnoughPoint(amount,memberId)) {
            throw new NotEnoughPointException("잔액이 부족합니다.");
        }

        LocalDateTime actionAt = CommonDateService.getToday();

        PointEntity savedPointEntity = this.savePoint(new SavePointDto(PointStatusEnum.USED,amount,actionAt,null,memberId));

        /* 차감이 가능한 포인트를 가져온다. */
        List<AvailablePointDto> availablePoints = this.pointDetailService.findAvailablePoints(memberId);

        List<PointDetailEntity> pointDetailEntities = new ArrayList<>();

        /* 먼저 적립된 포인트부터 소모 */
        for (AvailablePointDto point: availablePoints) {

            int sum = point.getSum();

            int remain = sum - this.toPositiveNumber(amount);

            /* 포인트 소모를 했는데 음수이면 그 다음 적립되었던 포인트도 소모 (선입선출)를 해야한다. */
            if(remain < 0) {
                PointDetailEntity pointDetail = new PointDetailEntity(
                        null,
                        PointStatusEnum.USED,
                        this.toNegativeNumber(sum),
                        savedPointEntity.getActionAt(),
                        point.getPointDetail().getExpireAt(),
                        savedPointEntity
                );
                pointDetail.setPointDetail(point.getPointDetail());

                pointDetailEntities.add(pointDetail);

                amount += sum;

            } else {
                PointDetailEntity pointDetail = new PointDetailEntity(
                        null,
                        PointStatusEnum.USED,
                        amount,
                        savedPointEntity.getActionAt(),
                        point.getPointDetail().getExpireAt(),
                        savedPointEntity
                );
                pointDetail.setPointDetail(point.getPointDetail());
                pointDetailEntities.add(pointDetail);
                break;
            }
        }

        this.pointDetailService.saveAllpointDetail(pointDetailEntities);

        return savedPointEntity;
    }

    @Transactional
    public List<PointEntity> expirePoint(Long memberId) {
        MemberEntity memberEntity = this.memberService.findMemberById(memberId);
        LocalDateTime actionAt = CommonDateService.getToday();

        List<AvailablePointDto> expiredPoints = this.pointDetailService.findAvailableExpiredPoints(memberId);
        if(expiredPoints.size() < 1) {
            throw new NotFoundException("유효기간이 지난 포인트가 없어 만료처리에 실패하였습니다.");
        }

        return expiredPoints.stream().map(dto -> {
            PointDetailEntity expiredPoint = dto.getPointDetail();

            Integer amount = this.toNegativeNumber(dto.getSum());

            /* INSERT into point  */
            PointEntity pointEntity = new PointEntity(
                    null,
                    PointStatusEnum.EXPIRED,
                    amount,
                    actionAt,
                    expiredPoint.getExpireAt(),
                    memberEntity
            );
            PointEntity savedPointEntity = pointRepository.save(pointEntity);

            this.pointDetailService.savePointDetail(
                    new SavePointDetailDto(PointStatusEnum.EXPIRED,amount,actionAt,expiredPoint.getExpireAt(),savedPointEntity.getId(),expiredPoint)
            );

            return savedPointEntity;
        }).collect(Collectors.toList());
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

    private Boolean isUsedStatus(PointStatusEnum status) {
        return status.equals(PointStatusEnum.USED);
    }
}

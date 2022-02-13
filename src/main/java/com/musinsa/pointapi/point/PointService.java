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

        List<SavePointDetailDto> pointDetailDtos = new ArrayList<>();

        /* 먼저 적립된 포인트부터 소모 */
        for (AvailablePointDto point: availablePoints) {

            int sum = point.getSum();

            /* 포인트 소모를 했는데 음수이면 그 다음 적립되었던 포인트도 소모 (선입선출)를 해야한다. */
            Integer usedAmount = this.getUsedAmount(sum,amount);

            pointDetailDtos.add(new SavePointDetailDto(PointStatusEnum.USED,usedAmount,
                    savedPointEntity.getActionAt(),point.getPointDetail().getExpireAt(),
                    savedPointEntity,point.getPointDetail()));

            /* 지불하려는 amount가 사용가능한 현재 Point를 다 안썼으면 break */
            if(sum - this.toPositiveNumber(amount) >= 0) {
                break;
            }

            amount += sum;
        }

        this.pointDetailService.saveAllpointDetail(pointDetailDtos);

        return savedPointEntity;
    }

    private Integer getUsedAmount(Integer availableSum, Integer amount) {
        Integer remain = availableSum - this.toPositiveNumber(amount);

        // 잔액을 다 사용했다면 사용가능했던 포인트 금액 반환
        // 다 사용하지 않았다면 사용한 포인트 금액 반환
        return remain < 0 ? this.toNegativeNumber(availableSum) : amount;
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
            PointEntity savedPointEntity = this.savePoint(
                    new SavePointDto(PointStatusEnum.EXPIRED,amount,actionAt,expiredPoint.getExpireAt(),memberEntity.getId())
            );

            this.pointDetailService.savePointDetail(
                    new SavePointDetailDto(PointStatusEnum.EXPIRED,amount,actionAt,expiredPoint.getExpireAt(),savedPointEntity,expiredPoint)
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

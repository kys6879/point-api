package com.musinsa.pointapi.point_detail;

import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.point_detail.repository.PointDetailRepository;
import com.musinsa.pointapi.point_detail.repository.QPointDetailRepository;
import com.musinsa.pointapi.point_detail.repository.projection.AvailablePointDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointDetailService {

    private final PointDetailRepository pointDetailRepository;
    private final QPointDetailRepository qPointDetailRepository;
    private final MemberService memberService;

    public PointDetailService(PointDetailRepository pointDetailRepository, QPointDetailRepository qPointDetailRepository, MemberService memberService) {
        this.pointDetailRepository = pointDetailRepository;
        this.qPointDetailRepository = qPointDetailRepository;
        this.memberService = memberService;
    }

    public PointDetailEntity savePointDetail(PointDetailEntity pointDetailEntity) {
        return this.pointDetailRepository.save(pointDetailEntity);
    }

    public List<PointDetailEntity> saveAllpointDetail(List<PointDetailEntity> entities) {
        return this.pointDetailRepository.saveAll(entities);
    }

    public Integer findTotalPoint(Long memberId) {

        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        return this.qPointDetailRepository.findTotalPoint(memberEntity.getId());
    }

    public List<AvailablePointDto> findAvailablePoints(Long memberId) {

        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        return this.qPointDetailRepository.findAvailablePoints(memberEntity.getId());
    }

    public List<AvailablePointDto> findAvailableExpiredPoints(Long memberId) {

        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        return this.qPointDetailRepository.findAvailableExpiredPoints(memberEntity.getId());
    }
}

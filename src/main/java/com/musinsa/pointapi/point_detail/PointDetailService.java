package com.musinsa.pointapi.point_detail;

import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.MemberService;
import com.musinsa.pointapi.point_detail.repository.PointDetailRepository;
import com.musinsa.pointapi.point_detail.repository.QPointDetailRepository;
import org.springframework.stereotype.Service;

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

    public Integer findTotalPoint(Long memberId) {

        MemberEntity memberEntity = this.memberService.findMemberById(memberId);

        return this.qPointDetailRepository.findTotalPoint(memberEntity.getId());
    }

}

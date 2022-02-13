package com.musinsa.pointapi.member.service;

import com.musinsa.pointapi.advice.exception.NotFoundException;
import com.musinsa.pointapi.member.MemberEntity;
import com.musinsa.pointapi.member.repository.MemberRepository;
import com.musinsa.pointapi.member.repository.QMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final QMemberRepository qMemberRepository;
    private final MemberRepository memberRepository;

    public MemberService(QMemberRepository qMemberRepository, MemberRepository memberRepository) {
        this.qMemberRepository = qMemberRepository;
        this.memberRepository = memberRepository;
    }

    public MemberEntity findMemberById(Long memberId) {

        Boolean isExist = this.memberRepository.existsById(memberId);
        if(!isExist) {
            throw new NotFoundException("사용자를 찾을 수 없습니다.");
        }

        return this.qMemberRepository.findMemberById(memberId);
    }

}

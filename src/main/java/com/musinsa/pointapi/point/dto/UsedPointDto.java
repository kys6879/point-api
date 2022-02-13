package com.musinsa.pointapi.point.dto;

import com.musinsa.pointapi.member.dto.MemberDto;
import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.PointStatusEnum;
import com.musinsa.pointapi.point_detail.dto.PointDetailDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UsedPointDto extends PointDto {

    private List<PointDetailDto> details;

    public UsedPointDto(Long id, PointStatusEnum status, Integer amount, LocalDateTime actionAt, LocalDateTime expireAt, MemberDto member, List<PointDetailDto> details) {
        super(id, status, amount, actionAt, expireAt, member);
        this.details = details;
    }

    public List<PointDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<PointDetailDto> details) {
        this.details = details;
    }

    public static UsedPointDto from(PointEntity pointEntity) {

        MemberDto memberDto = MemberDto.from(pointEntity.getMember());

        List<PointDetailDto> dtos = pointEntity.getPointDetailEntities()
                .stream()
                .map(entity -> new PointDetailDto(entity.getId(),entity.getAmount(),entity.getStatus(),entity.getActionAt()))
                .collect(Collectors.toList());

        return new UsedPointDto(
                pointEntity.getId(),
                pointEntity.getStatus(),
                pointEntity.getAmount(),
                pointEntity.getActionAt(),
                pointEntity.getExpireAt(),
                memberDto,
                dtos
        );
    }
}

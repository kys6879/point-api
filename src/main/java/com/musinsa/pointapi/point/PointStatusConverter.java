package com.musinsa.pointapi.point;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

// TODO 상태 이외의 값 예외처리 필요
@Convert
public class PointStatusConverter implements AttributeConverter<PointStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PointStatusEnum attribute) {

        String name = attribute.getName();

        if (PointStatusEnum.EARN.getName().equals(name)) return PointStatusEnum.EARN.getCode();
        if (PointStatusEnum.USED.getName().equals(name)) return PointStatusEnum.USED.getCode();

        return 0;
    }

    @Override
    public PointStatusEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == 1) return PointStatusEnum.EARN;
        if (dbData == 2) return PointStatusEnum.USED;

        throw new IllegalArgumentException("잘못된 Point Status 입니다.");
    }
}

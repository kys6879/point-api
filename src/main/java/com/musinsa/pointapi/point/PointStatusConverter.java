package com.musinsa.pointapi.point;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class PointStatusConverter implements AttributeConverter<PointStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PointStatusEnum attribute) {

        String name = attribute.getName();

        if (PointStatusEnum.EARN.getName().equals(name)) return PointStatusEnum.EARN.getCode();
        if (PointStatusEnum.USED.getName().equals(name)) return PointStatusEnum.USED.getCode();
        if (PointStatusEnum.CANCEL.getName().equals(name)) return PointStatusEnum.CANCEL.getCode();

        return 0;
    }

    @Override
    public PointStatusEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == 1) return PointStatusEnum.EARN;
        if (dbData == 2) return PointStatusEnum.USED;
        if (dbData == 3) return PointStatusEnum.CANCEL;

        throw new IllegalArgumentException("잘못된 Point Status 입니다.");
    }
}

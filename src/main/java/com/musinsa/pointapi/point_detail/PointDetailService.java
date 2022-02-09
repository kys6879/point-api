package com.musinsa.pointapi.point_detail;

import com.musinsa.pointapi.point_detail.repository.PointDetailRepository;
import org.springframework.stereotype.Service;

@Service
public class PointDetailService {

    private final PointDetailRepository pointDetailRepository;

    public PointDetailService(PointDetailRepository pointDetailRepository) {
        this.pointDetailRepository = pointDetailRepository;
    }

    public PointDetailEntity savePointDetail(PointDetailEntity pointDetailEntity) {
        return this.pointDetailRepository.save(pointDetailEntity);
    }

}

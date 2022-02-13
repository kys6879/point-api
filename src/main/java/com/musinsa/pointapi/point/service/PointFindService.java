package com.musinsa.pointapi.point.service;

import com.musinsa.pointapi.advice.exception.NotFoundException;
import com.musinsa.pointapi.point.PointEntity;
import com.musinsa.pointapi.point.repository.PointRepository;
import org.springframework.stereotype.Service;

@Service
public class PointFindService {
    private final PointRepository pointRepository;

    public PointFindService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointEntity findPointById(Long pointId) {
        return this.pointRepository.findById(pointId).orElseThrow(() -> new NotFoundException("포인트를 찾을 수 없습니다."));
    }
}

package com.musinsa.pointapi.point.repository;

import com.musinsa.pointapi.point.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {

}

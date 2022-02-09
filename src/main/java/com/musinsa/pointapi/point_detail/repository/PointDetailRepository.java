package com.musinsa.pointapi.point_detail.repository;

import com.musinsa.pointapi.point_detail.PointDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointDetailRepository extends JpaRepository<PointDetailEntity, Long> {

}
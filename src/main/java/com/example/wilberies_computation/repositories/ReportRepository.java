package com.example.wilberies_computation.repositories;

import com.example.wilberies_computation.entity.ReportEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<ReportEntity, Long> {

}

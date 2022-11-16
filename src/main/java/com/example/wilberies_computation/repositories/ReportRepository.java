package com.example.wilberies_computation.repositories;

import com.example.wilberies_computation.entity.wildberies.ReportWbEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<ReportWbEntity, Long> {

}

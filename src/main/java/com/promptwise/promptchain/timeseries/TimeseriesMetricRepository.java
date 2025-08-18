package com.promptwise.promptchain.timeseries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeseriesMetricRepository extends JpaRepository<TimeseriesMetric, Long> {

}
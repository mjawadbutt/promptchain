package com.promptwise.promptchain.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;


import static com.promptwise.promptchain.generated.jooq.Tables.*;

@Repository
public class TimeseriesMetricsRepository {

  private final DSLContext dsl;
  private final ObjectMapper objectMapper;

  public TimeseriesMetricsRepository(DSLContext dsl) {
    this.dsl = dsl;
    this.objectMapper = new ObjectMapper();
  }

  public void insertMetric(Instant timestamp,
                           String serviceName,
                           String metricName,
                           Double value,
                           String unit,
                           Map<String, ?> labels) {

    try {
      // Convert Instant to OffsetDateTime
      OffsetDateTime offsetDateTime = timestamp.atOffset(ZoneOffset.UTC);

      // Convert labels map to JSONB
      String labelsJson = objectMapper.writeValueAsString(labels);
      JSONB labelsJsonB = JSONB.valueOf(labelsJson);

      // Insert into TimescaleDB
      dsl.insertInto(TIMESERIES_METRICS)
              .columns(
                      TIMESERIES_METRICS.TIME,
                      TIMESERIES_METRICS.SERVICE_NAME,
                      TIMESERIES_METRICS.METRIC_NAME,
                      TIMESERIES_METRICS.VALUE,
                      TIMESERIES_METRICS.LABELS
              )
              .values(
                      offsetDateTime,
                      serviceName,
                      metricName,
                      value,
                      labelsJsonB // use the JSONB object here
              )
              .execute();

    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert labels to JSON", e);
    }
  }

  /**
   * Get metrics from the last N minutes.
   */
  public List<Map<String, Object>> getMetricsLastNMinutes(int minutes) {
    return dsl.selectFrom(TIMESERIES_METRICS)
             .where(TIMESERIES_METRICS.TIME.ge(OffsetDateTime.now().minusMinutes(minutes)))
            .orderBy(TIMESERIES_METRICS.TIME.desc())
            .fetchMaps();

  }

  /**
   * Get metrics filtered by service name and metric name in the last N minutes.
   */
  public List<Map<String, Object>> getMetricsByServiceAndName(String serviceName, String metricName, int minutes) {
    return dsl.selectFrom(TIMESERIES_METRICS)
            .where(TIMESERIES_METRICS.SERVICE_NAME.eq(serviceName))
            .and(TIMESERIES_METRICS.METRIC_NAME.eq(metricName))
            .and(TIMESERIES_METRICS.TIME.ge(OffsetDateTime.now().minusMinutes(minutes)))
            .orderBy(TIMESERIES_METRICS.TIME.desc())
            .fetchMaps();

  }
}


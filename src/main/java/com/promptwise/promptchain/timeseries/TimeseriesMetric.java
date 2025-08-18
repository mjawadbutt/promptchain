package com.promptwise.promptchain.timeseries;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "timeseries_metrics")
public class TimeseriesMetric {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Instant time;

  @Column(name = "service_name", nullable = false)
  private String serviceName;

  @Column(name = "metric_name", nullable = false)
  private String metricName;

  private Double value;

  @Column(columnDefinition = "jsonb")
  @Convert(converter = JsonbConverter.class)
  private Map<String, Object> labels;

  // Getters and setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Instant getTime() { return time; }
  public void setTime(Instant time) { this.time = time; }

  public String getServiceName() { return serviceName; }
  public void setServiceName(String serviceName) { this.serviceName = serviceName; }

  public String getMetricName() { return metricName; }
  public void setMetricName(String metricName) { this.metricName = metricName; }

  public Double getValue() { return value; }
  public void setValue(Double value) { this.value = value; }

  public Map<String, Object> getLabels() { return labels; }
  public void setLabels(Map<String, Object> labels) { this.labels = labels; }
}
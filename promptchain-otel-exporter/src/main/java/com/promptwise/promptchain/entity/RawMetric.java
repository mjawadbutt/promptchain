package com.promptwise.promptchain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "raw_metric")
@IdClass(RawMetricId.class)
public class RawMetric {

  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "org_id", nullable = false)
  private Long orgId;

  @Id
  @Column(name = "client_app_id", nullable = false)
  private Long clientAppId;

  @Column(name = "payload", columnDefinition = "jsonb")
  @JdbcTypeCode(SqlTypes.JSON)
  private JsonNode payload;

  @Id
  @Column(name = "received_at", nullable = false)
  private OffsetDateTime receivedAt;

  //Requirement of JPA
  public RawMetric() {
  }

  public RawMetric(Long id, Long orgId, Long clientAppId, JsonNode payload, OffsetDateTime receivedAt) {
    this.id = id;
    this.orgId = orgId;
    this.clientAppId = clientAppId;
    this.payload = payload;
    this.receivedAt = receivedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
  }

  public Long getClientAppId() {
    return clientAppId;
  }

  public void setClientAppId(Long clientAppId) {
    this.clientAppId = clientAppId;
  }

  public JsonNode getPayload() {
    return payload;
  }

  public void setPayload(JsonNode payload) {
    this.payload = payload;
  }

  public OffsetDateTime getReceivedAt() {
    return receivedAt;
  }

  public void setReceivedAt(OffsetDateTime receivedAt) {
    this.receivedAt = receivedAt;
  }
}


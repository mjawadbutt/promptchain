package com.promptwise.promptchain.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Composite primary key class for the RawMetrics entity.
 * <p>
 * Why we need this:
 * - The table `raw_metrics` has a composite primary key made up of
 * (id, client_app_id, received_at).
 * - JPA requires a separate Serializable class to represent that key
 * when using @IdClass in the entity.
 * <p>
 * Key points:
 * - Must implement Serializable.
 * - Must override equals() and hashCode() based on all PK fields.
 * - Must have a no-args constructor (required by JPA).
 */
public class RawMetricId implements Serializable {
  private Long id;
  private Long clientAppId;
  private OffsetDateTime receivedAt;

  public RawMetricId() {
  }

  public RawMetricId(Long id, Long clientAppId, OffsetDateTime receivedAt) {
    this.id = id;
    this.clientAppId = clientAppId;
    this.receivedAt = receivedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RawMetricId)) return false;
    RawMetricId that = (RawMetricId) o;
    return Objects.equals(id, that.id) &&
            Objects.equals(clientAppId, that.clientAppId) &&
            Objects.equals(receivedAt, that.receivedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clientAppId, receivedAt);
  }
}

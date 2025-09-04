package com.promptwise.promptchain.repository;

import com.promptwise.promptchain.domain.RawMetricsId;
import com.promptwise.promptchain.entity.RawMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMetricsRepository extends JpaRepository<RawMetrics, RawMetricsId> {

  /**
   * Fetches the next value from the Postgres sequence `raw_metrics_id_seq`.
   *
   * Reason:
   *  - Our table `raw_metrics` uses a composite primary key (id, client_app_id, received_at).
   *  - Because of the composite key, we cannot use @GeneratedValue on the `id` field directly.
   *  - Instead, we manually fetch the next sequence value from Postgres to populate the `id`
   *    before persisting the entity.
   */
  @Query(value = "SELECT nextval('raw_metrics_id_seq')", nativeQuery = true)
  Long getNextId();

}

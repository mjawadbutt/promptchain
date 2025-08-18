
        -- DROP TABLE timeseries_metrics;
CREATE TABLE "timeseries_metrics" (
    "id" BIGSERIAL PRIMARY KEY NOT NULL,
    "time" TIMESTAMPTZ NOT NULL,
    "service_name" TEXT NOT NULL,
    "metric_name" TEXT NOT NULL,
    "value" DOUBLE PRECISION,
    "labels" JSONB
);

DO $$
BEGIN
IF EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'timescaledb') THEN
PERFORM create_hypertable('timeseries_metrics', 'time', if_not_exists => TRUE);
END IF;
END
$$;
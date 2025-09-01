CREATE EXTENSION IF NOT EXISTS timescaledb;
-- DROP TABLE timeseries_metrics;
        CREATE TABLE IF NOT EXISTS  timeseries_metrics (
            id BIGSERIAL NOT NULL,
            time TIMESTAMPTZ NOT NULL,
            service_name TEXT NOT NULL,
            metric_name TEXT NOT NULL,
            value DOUBLE PRECISION,
            labels JSONB,
            PRIMARY KEY (time, id)
        );

-- Convert it into a hypertable
SELECT create_hypertable('timeseries_metrics', 'time', if_not_exists => TRUE);
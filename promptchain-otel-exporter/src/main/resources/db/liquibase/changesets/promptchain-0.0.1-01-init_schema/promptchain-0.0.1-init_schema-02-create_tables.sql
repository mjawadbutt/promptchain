----------------------
-- For our APP itself:
----------------------

CREATE TABLE IF NOT EXISTS app_role (
    app_role_id BIGSERIAL PRIMARY KEY NOT NULL,
    role_name VARCHAR(31) UNIQUE NOT NULL,
    role_description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS app_group (
    app_group_id BIGSERIAL PRIMARY KEY NOT NULL,
    group_name VARCHAR(31) UNIQUE NOT NULL,
    group_description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS app_super_user (
    super_user_id BIGSERIAL PRIMARY KEY NOT NULL,
    user_name VARCHAR(31) NOT NULL,
    password VARCHAR(31) NOT NULL,
    user_email VARCHAR(127) UNIQUE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
    app_user_id BIGSERIAL PRIMARY KEY NOT NULL,
    user_name VARCHAR(31) NOT NULL,
    password VARCHAR(31) NOT NULL,
    user_email VARCHAR(127) UNIQUE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE TABLE IF NOT EXISTS mtm_app_role_to_app_group (
    app_role_id BIGINT NOT NULL,
    app_group_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS mtm_app_user_to_app_group (
    app_user_id BIGINT NOT NULL,
    app_group_id BIGINT NOT NULL
);

-------------------------------------------------
-- For our clients/organizations (multitenancy):
-------------------------------------------------

CREATE TABLE IF NOT EXISTS org (
    org_id BIGSERIAL PRIMARY KEY,
    org_name TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE TABLE IF NOT EXISTS org_super_user (
    org_id BIGINT NOT NULL,
    super_user_id BIGSERIAL PRIMARY KEY NOT NULL,
    user_name VARCHAR(31) NOT NULL,
    password VARCHAR(31) NOT NULL,
    user_email VARCHAR(127) UNIQUE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE TABLE IF NOT EXISTS org_role (
    org_id BIGINT NOT NULL,
    org_role_id BIGSERIAL PRIMARY KEY NOT NULL,
    role_name VARCHAR(31) UNIQUE NOT NULL,
    role_description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS org_group (
    org_id BIGINT NOT NULL,
    org_group_id BIGSERIAL PRIMARY KEY NOT NULL,
    group_name VARCHAR(31) UNIQUE NOT NULL,
    group_description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS org_user (
    org_id BIGINT NOT NULL,
    org_user_id BIGSERIAL PRIMARY KEY NOT NULL,
    user_name VARCHAR(31) NOT NULL,
    password VARCHAR(31) NOT NULL,
    user_email VARCHAR(127) UNIQUE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE TABLE IF NOT EXISTS mtm_org_role_to_org_group (
    org_id BIGINT NOT NULL,
    org_role_id BIGINT NOT NULL,
    org_group_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS mtm_org_user_to_org_group (
    org_id BIGINT NOT NULL,
    org_user_id BIGINT NOT NULL,
    org_group_id BIGINT NOT NULL
);

------------------------------------------
-- For apps of our clients/organizations:
------------------------------------------

CREATE TABLE IF NOT EXISTS org_app (
    org_id BIGINT NOT NULL,
    org_app_id BIGSERIAL PRIMARY KEY,
    app_name TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE TABLE IF NOT EXISTS org_app_role (
    org_app_id BIGINT NOT NULL,
    org_app_role_id BIGSERIAL PRIMARY KEY NOT NULL,
    role_name VARCHAR(31) UNIQUE NOT NULL,
    role_description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS org_app_group (
    org_app_id BIGINT NOT NULL,
    org_app_group_id BIGSERIAL PRIMARY KEY NOT NULL,
    group_name VARCHAR(31) UNIQUE NOT NULL,
    group_description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS org_app_user (
    org_app_id BIGINT NOT NULL,
    org_app_user_id BIGSERIAL PRIMARY KEY NOT NULL,
    user_name VARCHAR(31) NOT NULL,
    password VARCHAR(31) NOT NULL,
    user_email VARCHAR(127) UNIQUE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE TABLE IF NOT EXISTS mtm_org_app_role_to_org_app_group (
    org_app_id BIGINT NOT NULL,
    org_app_role_id BIGINT NOT NULL,
    org_app_group_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS mtm_org_app_user_to_org_app_group (
    org_app_id BIGINT NOT NULL,
    org_app_user_id BIGINT NOT NULL,
    org_app_group_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS raw_trace (
    org_app_id BIGINT NOT NULL,
    raw_trace_id BIGSERIAL NOT NULL,
    payload JSONB,
    received_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS raw_metric (
    org_app_id BIGINT NOT NULL,
    raw_metric_id BIGSERIAL NOT NULL,
    payload JSONB,
    received_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS raw_log (
    org_app_id BIGINT NOT NULL,
    raw_log_id BIGSERIAL NOT NULL,
    payload JSONB,
    received_at TIMESTAMPTZ NOT NULL
);


-- Reserve values between 1-999 for manual inserts / dev-test-data

-- APP schema
ALTER SEQUENCE app_role_app_role_id_seq RESTART WITH 1000;
ALTER SEQUENCE app_group_app_group_id_seq RESTART WITH 1000;
ALTER SEQUENCE app_super_user_super_user_id_seq RESTART WITH 1000;
ALTER SEQUENCE app_user_app_user_id_seq RESTART WITH 1000;

-- ORG schema
ALTER SEQUENCE org_org_id_seq RESTART WITH 1000;
ALTER SEQUENCE org_super_user_super_user_id_seq RESTART WITH 1000;
ALTER SEQUENCE org_role_org_role_id_seq RESTART WITH 1000;
ALTER SEQUENCE org_group_org_group_id_seq RESTART WITH 1000;
ALTER SEQUENCE org_user_org_user_id_seq RESTART WITH 1000;

-- ORG_APP schema
ALTER SEQUENCE org_app_org_app_id_seq RESTART WITH 1000;
ALTER SEQUENCE org_app_role_org_app_role_id_seq RESTART WITH 1000;
ALTER SEQUENCE org_app_group_org_app_group_id_seq RESTART WITH 1000;
ALTER SEQUENCE org_app_user_org_app_user_id_seq RESTART WITH 1000;

-- RAW data tables
ALTER SEQUENCE raw_trace_raw_trace_id_seq RESTART WITH 1000;
ALTER SEQUENCE raw_metric_raw_metric_id_seq RESTART WITH 1000;
ALTER SEQUENCE raw_log_raw_log_id_seq RESTART WITH 1000;

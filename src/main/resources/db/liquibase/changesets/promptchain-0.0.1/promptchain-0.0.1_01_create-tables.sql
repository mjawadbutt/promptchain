CREATE TABLE app_role (
    role_id BIGSERIAL PRIMARY KEY NOT NULL,
    role_name VARCHAR(31) UNIQUE NOT NULL,
    role_description VARCHAR(255)
);

CREATE TABLE user_group (
    user_group_id BIGSERIAL PRIMARY KEY NOT NULL,
    group_name VARCHAR(31) UNIQUE NOT NULL,
    group_description VARCHAR(255)
);

CREATE TABLE app_user (
    app_user_id BIGSERIAL PRIMARY KEY NOT NULL,
    user_email VARCHAR(127) UNIQUE NOT NULL,
    password VARCHAR(31) NOT NULL,
    user_name VARCHAR(31) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE TABLE role_group_mapping (
    role_id BIGINT NOT NULL,
    user_group_id BIGINT NOT NULL
);

CREATE TABLE user_group_mapping (
    app_user_id BIGINT NOT NULL,
    user_group_id BIGINT NOT NULL
);

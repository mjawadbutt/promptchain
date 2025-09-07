-- APP mapping tables
ALTER TABLE mtm_app_role_to_app_group
ADD CONSTRAINT pk_mtm_app_role_to_app_group PRIMARY KEY (app_role_id, app_group_id);

ALTER TABLE mtm_app_role_to_app_group
ADD CONSTRAINT fk_mtm_role FOREIGN KEY (app_role_id) REFERENCES app_role (app_role_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_app_role_to_app_group
ADD CONSTRAINT fk_mtm_group FOREIGN KEY (app_group_id) REFERENCES app_group (app_group_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_app_user_to_app_group
ADD CONSTRAINT pk_mtm_app_user_to_app_group PRIMARY KEY (app_user_id, app_group_id);

ALTER TABLE mtm_app_user_to_app_group
ADD CONSTRAINT fk_mtm_user FOREIGN KEY (app_user_id) REFERENCES app_user (app_user_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_app_user_to_app_group
ADD CONSTRAINT fk_mtm_group_user FOREIGN KEY (app_group_id) REFERENCES app_group (app_group_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;


-- ORG mapping tables
ALTER TABLE mtm_org_role_to_org_group
ADD CONSTRAINT pk_mtm_org_role_to_org_group PRIMARY KEY (org_id, org_role_id, org_group_id);

ALTER TABLE mtm_org_role_to_org_group
ADD CONSTRAINT fk_mtm_org_role FOREIGN KEY (org_role_id) REFERENCES org_role (org_role_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_role_to_org_group
ADD CONSTRAINT fk_mtm_org_group FOREIGN KEY (org_group_id) REFERENCES org_group (org_group_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_role_to_org_group
ADD CONSTRAINT fk_mtm_org FOREIGN KEY (org_id) REFERENCES org (org_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_user_to_org_group
ADD CONSTRAINT pk_mtm_org_user_to_org_group PRIMARY KEY (org_id, org_user_id, org_group_id);

ALTER TABLE mtm_org_user_to_org_group
ADD CONSTRAINT fk_mtm_org_user FOREIGN KEY (org_user_id) REFERENCES org_user (org_user_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_user_to_org_group
ADD CONSTRAINT fk_mtm_org_group_user FOREIGN KEY (org_group_id) REFERENCES org_group (org_group_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_user_to_org_group
ADD CONSTRAINT fk_mtm_org_ref FOREIGN KEY (org_id) REFERENCES org (org_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE org_super_user
ADD CONSTRAINT fk_org_super_user_org FOREIGN KEY (org_id) REFERENCES org (org_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE org_role
ADD CONSTRAINT fk_org_role_org FOREIGN KEY (org_id) REFERENCES org (org_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE org_group
ADD CONSTRAINT fk_org_group_org FOREIGN KEY (org_id) REFERENCES org (org_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE org_user
ADD CONSTRAINT fk_org_user_org FOREIGN KEY (org_id) REFERENCES org (org_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;


-- ORG APP mapping tables
ALTER TABLE mtm_org_app_role_to_org_app_group
ADD CONSTRAINT pk_mtm_org_app_role_to_org_app_group PRIMARY KEY (org_app_role_id, org_app_group_id);

ALTER TABLE mtm_org_app_role_to_org_app_group
ADD CONSTRAINT fk_mtm_org_app_role FOREIGN KEY (org_app_role_id)
REFERENCES org_app_role (org_app_role_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_app_role_to_org_app_group
ADD CONSTRAINT fk_mtm_org_app_group FOREIGN KEY (org_app_group_id)
REFERENCES org_app_group (org_app_group_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_app_role_to_org_app_group
ADD CONSTRAINT fk_mtm_org_app FOREIGN KEY (org_app_id)
REFERENCES org_app (org_app_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_app_user_to_org_app_group
ADD CONSTRAINT pk_mtm_org_app_user_to_org_app_group PRIMARY KEY (org_app_user_id, org_app_group_id);

ALTER TABLE mtm_org_app_user_to_org_app_group
ADD CONSTRAINT fk_mtm_org_app_user FOREIGN KEY (org_app_user_id)
REFERENCES org_app_user (org_app_user_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_app_user_to_org_app_group
ADD CONSTRAINT fk_mtm_org_app_group_user FOREIGN KEY (org_app_group_id)
REFERENCES org_app_group (org_app_group_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE mtm_org_app_user_to_org_app_group
ADD CONSTRAINT fk_mtm_org_app_ref FOREIGN KEY (org_app_id)
REFERENCES org_app (org_app_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE org_app_role
ADD CONSTRAINT fk_org_app_role FOREIGN KEY (org_app_id)
REFERENCES org_app (org_app_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE org_app_group
ADD CONSTRAINT fk_org_app_group FOREIGN KEY (org_app_id)
REFERENCES org_app (org_app_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE org_app_user
ADD CONSTRAINT fk_org_app_user FOREIGN KEY (org_app_id)
REFERENCES org_app (org_app_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;


-- RAW tables
ALTER TABLE raw_trace
ADD CONSTRAINT pk_raw_trace PRIMARY KEY (raw_trace_id, org_app_id, received_at);

SELECT create_hypertable('raw_trace', 'received_at', 'org_app_id', 4, if_not_exists => TRUE);

ALTER TABLE raw_metric
ADD CONSTRAINT pk_raw_metric PRIMARY KEY (raw_metric_id, org_app_id, received_at);

SELECT create_hypertable('raw_metric', 'received_at', 'org_app_id', 4, if_not_exists => TRUE);

ALTER TABLE raw_log
ADD CONSTRAINT pk_raw_log PRIMARY KEY (raw_log_id, org_app_id, received_at);

SELECT create_hypertable('raw_log', 'received_at', 'org_app_id', 4, if_not_exists => TRUE);

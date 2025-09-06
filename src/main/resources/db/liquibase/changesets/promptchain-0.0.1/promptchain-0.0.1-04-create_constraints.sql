ALTER TABLE "ROLE_GROUP_MAPPING"
ADD CONSTRAINT "PK_ROLE_GROUP_MAPPING" PRIMARY KEY ("ROLE_ID", "USER_GROUP_ID");

-- Add foreign key constraints to role_group_mapping table
ALTER TABLE "ROLE_GROUP_MAPPING"
ADD CONSTRAINT "FK_RGM_APP_ROLE" FOREIGN KEY ("ROLE_ID") REFERENCES "APP_ROLE" ("ROLE_ID")
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE "ROLE_GROUP_MAPPING"
ADD CONSTRAINT "FK_RGM_USER_GROUP" FOREIGN KEY ("USER_GROUP_ID") REFERENCES "USER_GROUP" ("USER_GROUP_ID")
ON UPDATE RESTRICT
ON DELETE CASCADE;

-- Add primary key to user_group_mapping table
ALTER TABLE "USER_GROUP_MAPPING"
ADD CONSTRAINT "PK_USER_GROUP_MAPPING" PRIMARY KEY ("APP_USER_ID", "USER_GROUP_ID");

-- Add foreign key constraints to user_group_mapping table
ALTER TABLE "USER_GROUP_MAPPING"
ADD CONSTRAINT "FK_UGM_APP_USER" FOREIGN KEY ("APP_USER_ID") REFERENCES "APP_USER" ("APP_USER_ID")
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE "USER_GROUP_MAPPING"
ADD CONSTRAINT "FK_UGM_USER_GROUP" FOREIGN KEY ("USER_GROUP_ID") REFERENCES "USER_GROUP" ("USER_GROUP_ID")
ON UPDATE RESTRICT
ON DELETE CASCADE;


ALTER TABLE raw_traces
ADD CONSTRAINT pk_raw_traces PRIMARY KEY (id, client_app_id, received_at);

SELECT create_hypertable('raw_traces', 'received_at', 'client_app_id', 4, if_not_exists => TRUE);


ALTER TABLE raw_metrics
ADD CONSTRAINT pk_raw_metrics PRIMARY KEY (id, client_app_id, received_at);

SELECT create_hypertable('raw_metrics', 'received_at', 'client_app_id', 4, if_not_exists => TRUE);

ALTER TABLE raw_logs
ADD CONSTRAINT pk_raw_logs PRIMARY KEY (id, client_app_id, received_at);

SELECT create_hypertable('raw_logs', 'received_at', 'client_app_id', 4, if_not_exists => TRUE);
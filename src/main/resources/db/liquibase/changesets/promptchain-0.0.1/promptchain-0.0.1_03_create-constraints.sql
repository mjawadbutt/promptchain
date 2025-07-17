ALTER TABLE role_group_mapping
ADD CONSTRAINT pk_role_group_mapping PRIMARY KEY (role_id, user_group_id);

-- Add foreign key constraints to role_group_mapping table
ALTER TABLE role_group_mapping
ADD CONSTRAINT fk_rgm_app_role FOREIGN KEY (role_id) REFERENCES app_role (role_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE role_group_mapping
ADD CONSTRAINT fk_rgm_user_group FOREIGN KEY (user_group_id) REFERENCES user_group (user_group_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

-- Add primary key to user_group_mapping table (from Liquibase changeSet id: v0.0.1-create-user-group-mapping-table)
ALTER TABLE user_group_mapping
ADD CONSTRAINT pk_user_group_mapping PRIMARY KEY (app_user_id, user_group_id);

-- Add foreign key constraints to user_group_mapping table
ALTER TABLE user_group_mapping
ADD CONSTRAINT fk_ugm_app_user FOREIGN KEY (app_user_id) REFERENCES app_user (app_user_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE user_group_mapping
ADD CONSTRAINT fk_ugm_user_group FOREIGN KEY (user_group_id) REFERENCES user_group (user_group_id)
ON UPDATE RESTRICT
ON DELETE CASCADE;

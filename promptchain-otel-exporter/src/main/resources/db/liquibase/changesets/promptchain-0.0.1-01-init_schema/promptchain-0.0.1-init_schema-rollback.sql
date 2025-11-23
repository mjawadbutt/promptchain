------------------------------------------------------
-- Drop script for APP / ORG / ORG_APP schema objects
------------------------------------------------------

-- 1. Drop MTM (junction) tables first
DROP TABLE IF EXISTS mtm_org_app_user_to_org_app_group CASCADE;
DROP TABLE IF EXISTS mtm_org_app_role_to_org_app_group CASCADE;
DROP TABLE IF EXISTS mtm_org_user_to_org_group CASCADE;
DROP TABLE IF EXISTS mtm_org_role_to_org_group CASCADE;
DROP TABLE IF EXISTS mtm_app_user_to_app_group CASCADE;
DROP TABLE IF EXISTS mtm_app_role_to_app_group CASCADE;

-- 2. Drop RAW tables (depend on ORG_APP)
DROP TABLE IF EXISTS raw_log CASCADE;
DROP TABLE IF EXISTS raw_metric CASCADE;
DROP TABLE IF EXISTS raw_trace CASCADE;

-- 3. Drop ORG_APP child tables
DROP TABLE IF EXISTS org_app_user CASCADE;
DROP TABLE IF EXISTS org_app_group CASCADE;
DROP TABLE IF EXISTS org_app_role CASCADE;

-- 4. Drop ORG_APP itself
DROP TABLE IF EXISTS org_app CASCADE;

-- 5. Drop ORG child tables
DROP TABLE IF EXISTS org_user CASCADE;
DROP TABLE IF EXISTS org_group CASCADE;
DROP TABLE IF EXISTS org_role CASCADE;
DROP TABLE IF EXISTS org_super_user CASCADE;

-- 6. Drop ORG itself
DROP TABLE IF EXISTS org CASCADE;

-- 7. Drop APP child tables
DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS app_super_user CASCADE;
DROP TABLE IF EXISTS app_group CASCADE;
DROP TABLE IF EXISTS app_role CASCADE;

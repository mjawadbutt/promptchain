-- Delete MTM mapping rows first (foreign key dependencies)
DELETE FROM mtm_app_user_to_app_group
WHERE (app_user_id, app_group_id) IN ((1,1),(2,2),(3,3));

DELETE FROM mtm_app_role_to_app_group
WHERE (app_role_id, app_group_id) IN ((1,1),(2,2),(2,3));

-- Delete from child tables
DELETE FROM app_user
WHERE (user_name, user_email, password) IN (
  ('Admin User', 'admin@example.com', 'adminpass'),
  ('Dev One', 'dev1@example.com', 'devpass1'),
  ('Support One', 'support1@example.com', 'supppass1')
);

DELETE FROM app_group
WHERE (group_name, group_description) IN (
  ('Admins', 'Group for all administrators'),
  ('Developers', 'Group for development team'),
  ('Support', 'Group for support team')
);

DELETE FROM app_role
WHERE (role_name, role_description) IN (
  ('ADMIN', 'Administrator role with full access'),
  ('USER', 'Standard user role'),
  ('GUEST', 'Guest role with limited access')
);

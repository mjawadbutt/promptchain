INSERT INTO app_role (app_role_id, role_name, role_description) VALUES
(1, 'ADMIN', 'Administrator role with full access'),
(2, 'USER', 'Standard user role'),
(3, 'GUEST', 'Guest role with limited access');

INSERT INTO app_group (app_group_id, group_name, group_description) VALUES
(1, 'Admins', 'Group for all administrators'),
(2, 'Developers', 'Group for development team'),
(3, 'Support', 'Group for support team');

INSERT INTO app_user (app_user_id, user_name, user_email, password) VALUES
(1, 'Admin User', 'admin@example.com', 'adminpass'),
(2, 'Dev One', 'dev1@example.com', 'devpass1'),
(3, 'Support One', 'support1@example.com', 'supppass1');

INSERT INTO mtm_app_role_to_app_group (app_role_id, app_group_id) VALUES
(1, 1),
(2, 2),
(2, 3);

INSERT INTO mtm_app_user_to_app_group (app_user_id, app_group_id) VALUES
(1, 1),
(2, 2),
(3, 3);

INSERT INTO app_role (role_name, role_description) VALUES
('ADMIN', 'Administrator role with full access'),
('USER', 'Standard user role'),
('GUEST', 'Guest role with limited access');

INSERT INTO user_group (group_name, group_description) VALUES
('Admins', 'Group for all administrators'),
('Developers', 'Group for development team'),
('Support', 'Group for support team');

INSERT INTO app_user (user_name, user_email, password) VALUES
('adminpass', 'Admin User','admin@example.com'),
('devpass1', 'Dev One', 'dev1@example.com'),
('supppass1', 'Support One', 'support1@example.com');

INSERT INTO role_group_mapping (role_id, user_group_id) VALUES
(1, 1),
(2, 2),
(2, 3);

INSERT INTO user_group_mapping (app_user_id, user_group_id) VALUES
(1, 1),
(2, 2),
(3, 3);

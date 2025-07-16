INSERT INTO app_role (role_name, role_description) VALUES
('ADMIN', 'Administrator role with full access'),
('USER', 'Standard user role'),
('GUEST', 'Guest role with limited access');

INSERT INTO user_group (group_name, group_description) VALUES
('Admins', 'Group for all administrators'),
('Developers', 'Group for development team'),
('Support', 'Group for support team');

INSERT INTO app_user (user_email, password, user_name) VALUES
('admin@example.com', 'adminpass', 'Admin User'),
('dev1@example.com', 'devpass1', 'Dev One'),
('support1@example.com', 'supppass1', 'Support One');

INSERT INTO role_group_mapping (role_id, group_id) VALUES
(1, 1), -- ADMIN in Admins group
(2, 2), -- USER in Developers group
(2, 3); -- USER in Support group

INSERT INTO user_group_mapping (user_id, group_id) VALUES
(1, 1), -- admin@example.com in Admins group
(2, 2), -- dev1@example.com in Developers group
(3, 3); -- support1@example.com in Support group

INSERT INTO "APP_ROLE" ("ROLE_NAME", "ROLE_DESCRIPTION") VALUES
('ADMIN', 'Administrator role with full access'),
('USER', 'Standard user role'),
('GUEST', 'Guest role with limited access');

INSERT INTO "USER_GROUP" ("GROUP_NAME", "GROUP_DESCRIPTION") VALUES
('Admins', 'Group for all administrators'),
('Developers', 'Group for development team'),
('Support', 'Group for support team');

INSERT INTO "APP_USER" ("USER_NAME", "USER_EMAIL", "PASSWORD") VALUES
('Admin User', 'admin@example.com', 'adminpass'),
('Dev One', 'dev1@example.com', 'devpass1'),
('Support One', 'support1@example.com', 'supppass1');

INSERT INTO "ROLE_GROUP_MAPPING" ("ROLE_ID", "USER_GROUP_ID") VALUES
(1, 1),
(2, 2),
(2, 3);

INSERT INTO "USER_GROUP_MAPPING" ("APP_USER_ID", "USER_GROUP_ID") VALUES
(1, 1),
(2, 2),
(3, 3);
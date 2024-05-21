SELECT users.username, users.password, roles.role_name, permissions.name
FROM users INNER JOIN user_roles ON users.id = user_roles.user_id
INNER JOIN roles ON user_roles.role_id = roles.id
INNER JOIN role_permissions ON roles.id = role_permissions.role_id
INNER JOIN permissions ON role_permissions.permission_id = permissions.id;
INSERT INTO ROLES (role_name)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM ROLES WHERE role_name = 'ADMIN');

INSERT INTO ROLES (role_name)
SELECT 'STUDENT'
WHERE NOT EXISTS (SELECT 1 FROM ROLES WHERE role_name = 'STUDENT');

INSERT INTO users (username, password)
SELECT 'admin', '$2a$10$/cDtT9Ij.PHs6e8QrgeqWO4YxoxRp/NaPW6q/1TISqH7zzSC7uAe6'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password)
SELECT 'student', '$2a$10$/cDtT9Ij.PHs6e8QrgeqWO4YxoxRp/NaPW6q/1TISqH7zzSC7uAe6'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'student');

INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u, roles r
WHERE u.username = 'admin' AND r.role_name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
  );

INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u, roles r
WHERE u.username = 'student' AND r.role_name = 'STUDENT'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
  );

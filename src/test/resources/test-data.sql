
DELETE FROM users;
DELETE FROM role;

INSERT INTO role (id, name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

INSERT INTO users (user_name, age, is_man, email, password, role_id, is_hidden, created_at, updated_at)
VALUES
    ('username', 30, TRUE, 'email@email.com', '$2a$12$lpwBT6UMxH8qkdanUpHxXuenpbmb3b3ic/BcILqWcJUGRbku2OWay', 1, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin', 40, TRUE, 'admin@email.com', '$2a$12$lpwBT6UMxH8qkdanUpHxXuenpbmb3b3ic/BcILqWcJUGRbku2OWay', 2, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

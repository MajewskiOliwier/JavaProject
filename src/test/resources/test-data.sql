delete from users;
delete from role;

insert into role (id, name) values (1, 'ROLE_USER');

INSERT INTO users (user_name, age, is_man, email, password, role_id, is_hidden, created_at, updated_at)
VALUES ('username', 30, TRUE, 'email@email.com', '$2a$12$lpwBT6UMxH8qkdanUpHxXuenpbmb3b3ic/BcILqWcJUGRbku2OWay', 1, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

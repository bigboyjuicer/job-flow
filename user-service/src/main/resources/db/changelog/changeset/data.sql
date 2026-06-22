--liquibase formatted sql

--changeset m.zinin:create-user
INSERT INTO users(email, password_hash, role, first_name, last_name, created_at) VALUES('123', '123', 'CANDIDATE', '123', '123', '2026-06-22');
--rollback DELETE FROM users WHERE email = '123' and password = '123';
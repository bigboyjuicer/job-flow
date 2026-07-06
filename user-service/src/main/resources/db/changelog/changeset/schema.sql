--liquibase formatted sql

--changeset m.zinin:role-type-creation
CREATE TYPE ROLE AS ENUM ('CANDIDATE', 'EMPLOYER', 'ADMIN');
--rollback DROP TYPE ROLE;

--changeset m.zinin:users-table-creation
CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ROLE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE NOT NULL
);
--rollback DROP TABLE users;

--changeset m.zinin:email-index-creation
CREATE INDEX idx_users_email ON users(email);
--rollback DROP INDEX idx_users_email;
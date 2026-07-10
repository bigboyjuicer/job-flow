--liquibase formatted sql

--changeset m.zinin:create-status-type
CREATE TYPE STATUS AS ENUM ('DRAFT','ACTIVE','CLOSED');
--rollback DROP TYPE STATUS CASCADE;

--changeset m.zinin:create-vacancies-table
CREATE TABLE IF NOT EXISTS vacancies (
    id BIGSERIAL PRIMARY KEY,
    employer_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    salary_from INT,
    salary_to INT,
    city VARCHAR(255) NOT NULL,
    experience_years INT,
    status STATUS NOT NULL,
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL
);
--rollback DROP TABLE vacancies CASCADE;

--changeset m.zinin:create-categories-table
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(10) NOT NULL
);
--rollback DROP TABLE categories CASCADE;

--changeset m.zinin:create-vacancy-categories-table
CREATE TABLE IF NOT EXISTS vacancy_categories (
    vacancy_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    CONSTRAINT u_vacancy_categories UNIQUE(vacancy_id, category_id),
    CONSTRAINT fk_vacancycategories_vacancies FOREIGN KEY (vacancy_id) REFERENCES vacancies(id),
    CONSTRAINT fk_vacancycategories_categories FOREIGN KEY (category_id) REFERENCES categories(id)
);
--rollback DROP TABLE vacancy-categories;
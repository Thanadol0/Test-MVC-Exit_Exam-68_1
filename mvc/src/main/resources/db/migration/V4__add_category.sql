CREATE TABLE IF NOT EXISTS category (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);


ALTER TABLE project
    ADD COLUMN IF NOT EXISTS category_id BIGINT;

ALTER TABLE project
    ADD CONSTRAINT fk_project_category
    FOREIGN KEY (category_id) REFERENCES category(id)
    ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_project_category ON project(category_id);

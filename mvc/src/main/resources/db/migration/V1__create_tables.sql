
CREATE TABLE IF NOT EXISTS project (
    id               BIGSERIAL PRIMARY KEY,
    project_code     VARCHAR(8) NOT NULL UNIQUE
        CHECK (project_code ~ '^[1-9][0-9]{7}$'),
    name             VARCHAR(255) NOT NULL,
    goal_amount      NUMERIC(14,2) NOT NULL CHECK (goal_amount > 0),
    deadline         TIMESTAMPTZ NOT NULL,
    current_balance  NUMERIC(14,2) NOT NULL DEFAULT 0 CHECK (current_balance >= 0)
);

CREATE TABLE IF NOT EXISTS reward_tier (
    id           BIGSERIAL PRIMARY KEY,
    project_id   BIGINT NOT NULL REFERENCES project(id) ON DELETE CASCADE,
    title        VARCHAR(255) NOT NULL,
    min_amount   NUMERIC(14,2) NOT NULL CHECK (min_amount >= 0),
    quota        INTEGER
);


CREATE TABLE IF NOT EXISTS pledge (
    id             BIGSERIAL PRIMARY KEY,
    project_id     BIGINT NOT NULL REFERENCES project(id) ON DELETE CASCADE,
    reward_tier_id BIGINT REFERENCES reward_tier(id) ON DELETE SET NULL,
    supporter_name VARCHAR(255),
    pledged_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    amount         NUMERIC(14,2) NOT NULL CHECK (amount > 0)
);

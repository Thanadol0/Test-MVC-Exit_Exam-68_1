CREATE TABLE IF NOT EXISTS pledge_history (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT REFERENCES app_user(id) ON DELETE SET NULL,
    project_id      BIGINT NOT NULL REFERENCES project(id) ON DELETE CASCADE,
    reward_tier_id  BIGINT REFERENCES reward_tier(id) ON DELETE SET NULL,
    amount          NUMERIC(14,2) NOT NULL CHECK (amount > 0),
    status          VARCHAR(16) NOT NULL,
    reason          TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    pledge_id       BIGINT REFERENCES pledge(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_ph_user      ON pledge_history(user_id);
CREATE INDEX IF NOT EXISTS idx_ph_project   ON pledge_history(project_id);
CREATE INDEX IF NOT EXISTS idx_ph_status    ON pledge_history(status);

ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS rejected_count INTEGER NOT NULL DEFAULT 0;

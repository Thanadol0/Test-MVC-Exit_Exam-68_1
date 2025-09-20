ALTER TABLE pledge
    ADD COLUMN IF NOT EXISTS user_id BIGINT;

ALTER TABLE pledge
    ADD CONSTRAINT fk_pledge_user
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_pledge_user ON pledge(user_id);

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name='app_user' AND column_name='reject_count'
  ) THEN
    IF EXISTS (
      SELECT 1 FROM information_schema.columns
      WHERE table_name='app_user' AND column_name='rejected_count'
    ) THEN
      EXECUTE 'ALTER TABLE app_user RENAME COLUMN rejected_count TO reject_count';
    ELSE
      EXECUTE 'ALTER TABLE app_user ADD COLUMN reject_count INTEGER NOT NULL DEFAULT 0';
    END IF;
  END IF;
END $$;

ALTER TABLE app_user
  ADD COLUMN IF NOT EXISTS approve_count INTEGER NOT NULL DEFAULT 0;

UPDATE app_user u
SET approve_count = COALESCE(s.approved, 0),
    reject_count  = COALESCE(s.rejected, u.reject_count)
FROM (
  SELECT user_id,
         SUM((status='APPROVED')::int) AS approved,
         SUM((status='REJECTED')::int) AS rejected
  FROM pledge_history
  GROUP BY user_id
) s
WHERE u.id = s.user_id;

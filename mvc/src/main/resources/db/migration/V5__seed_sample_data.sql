-- V5__seed_sample_data.sql
-- Seed อย่างน้อย 3 หมวด, 8 โครงการ, 2–3 reward/โครงการ, ผู้ใช้ >= 10, และ pledge หลายรายการ

-- 1) Categories
INSERT INTO category (name) VALUES
  ('Technology'),
  ('Art & Design'),
  ('Education'),
  ('Health & Wellness')
ON CONFLICT (name) DO NOTHING;

-- 2) Users (รหัสผ่านง่ายๆ เพื่อทดสอบ)
INSERT INTO app_user (username, password) VALUES
  ('user01', '1234'), ('user02', '1234'), ('user03', '1234'), ('user04', '1234'), ('user05', '1234'),
  ('user06', '1234'), ('user07', '1234'), ('user08', '1234'), ('user09', '1234'), ('user10', '1234')
ON CONFLICT (username) DO NOTHING;

-- ดึงรหัสหมวดเพื่อใช้ต่อ
WITH c AS (
  SELECT
    (SELECT id FROM category WHERE name='Technology')      AS tech,
    (SELECT id FROM category WHERE name='Art & Design')    AS art,
    (SELECT id FROM category WHERE name='Education')       AS edu,
    (SELECT id FROM category WHERE name='Health & Wellness') AS health
)
-- 3) Projects (>=8)
INSERT INTO project (project_code, name, goal_amount, deadline, current_balance, category_id)
SELECT * FROM (
  SELECT '10000001', 'Smart Sensor Kit',         50000.00, NOW() + INTERVAL '30 days', 0.00, (SELECT tech FROM c)   UNION ALL
  SELECT '10000002', 'Open-source Dev Board',    80000.00, NOW() + INTERVAL '45 days', 0.00, (SELECT tech FROM c)   UNION ALL
  SELECT '10000003', 'Indie Art Book',           30000.00, NOW() + INTERVAL '25 days', 0.00, (SELECT art  FROM c)   UNION ALL
  SELECT '10000004', '3D Printing Workshop',     40000.00, NOW() + INTERVAL '20 days', 0.00, (SELECT art  FROM c)   UNION ALL
  SELECT '10000005', 'STEM Learning Kit',        45000.00, NOW() + INTERVAL '35 days', 0.00, (SELECT edu  FROM c)   UNION ALL
  SELECT '10000006', 'Online Math Course',       60000.00, NOW() + INTERVAL '28 days', 0.00, (SELECT edu  FROM c)   UNION ALL
  SELECT '10000007', 'Fitness Tracker App',      70000.00, NOW() + INTERVAL '40 days', 0.00, (SELECT health FROM c) UNION ALL
  SELECT '10000008', 'Healthy Meal Planner',     55000.00, NOW() + INTERVAL '18 days', 0.00, (SELECT health FROM c)
) p
ON CONFLICT (project_code) DO NOTHING;

-- 4) Reward tiers (2–3 ต่อโครงการ)
-- สร้างฟังก์ชันสั้นๆช่วยหา id ตาม code
DO $$
DECLARE pid BIGINT;
BEGIN
  FOR pid IN SELECT id FROM project LOOP
    -- ลบของเก่าซ้ำซ้อน (ถ้ามี) แล้วเติมใหม่เบื้องต้นให้แต่ละโปรเจกต์ 3 ระดับ
    DELETE FROM reward_tier WHERE project_id = pid;

    INSERT INTO reward_tier(project_id, title, min_amount, quota) VALUES
      (pid, 'Supporter',  200.00,  NULL),
      (pid, 'Backer',     500.00,  100),
      (pid, 'Sponsor',   1000.00,   50);
  END LOOP;
END $$;

-- 5) Pledges ตัวอย่าง (กระจายหลาย user หลาย project)
-- สุ่มง่าย ๆ โดยวน user01..user10 ใส่ 2–4 โปรเจกต์คนละนิด
DO $$
DECLARE
  u   RECORD;
  p   RECORD;
  t   BIGINT;
  cnt INT;
BEGIN
  FOR u IN SELECT id, username FROM app_user ORDER BY id LOOP
    cnt := 0;
    FOR p IN SELECT id FROM project ORDER BY random() LIMIT 4 LOOP
      -- เลือก tier ขั้นต่ำสุ่ม 1 ระดับ
      SELECT id INTO t FROM reward_tier WHERE project_id = p.id ORDER BY random() LIMIT 1;

      INSERT INTO pledge(project_id, reward_tier_id, supporter_name, pledged_at, amount, user_id)
      VALUES (p.id, t, u.username, NOW() - (random() * INTERVAL '10 days'),
              CASE floor(random()*3)::int
                WHEN 0 THEN 200.00
                WHEN 1 THEN 500.00
                ELSE 1000.00
              END,
              u.id);
      cnt := cnt + 1;
      EXIT WHEN cnt >= 3; -- ให้แต่ละ user มี ~3 pledges
    END LOOP;
  END LOOP;
END $$;

-- 6) Update current_balance ของ project ให้ตรงกับผลรวม pledge
UPDATE project p
SET current_balance = COALESCE(s.sum_amt, 0)
FROM (
  SELECT project_id, SUM(amount) AS sum_amt
  FROM pledge
  GROUP BY project_id
) s
WHERE p.id = s.project_id;

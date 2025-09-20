วิธีการ run
1. docker compose up -d ก่อนเพื่่อให้ database พร้อมทำงาน
2. จากนั้นก็กด run MvcApplication.java

มี SQL Script 7 อันใน resources
- V1__create_tables.sql
- V2__create_user_table.sql 
- V3__add_user_fk_to_pledge.sql
- V4__add_category.sql
- V5__seed_sample_data.sql
- V6__add_pledge_history.sql
- V7__add_user_counters.sql

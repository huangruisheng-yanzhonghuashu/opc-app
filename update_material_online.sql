-- =============================================
-- 素材全部上架并记录上架时间 SQL 脚本
-- =============================================

-- 查看更新前的状态
SELECT 
    '更新前统计' AS info,
    COUNT(*) AS total,
    SUM(CASE WHEN status = '0' THEN 1 ELSE 0 END) AS online_count,
    SUM(CASE WHEN status = '1' THEN 1 ELSE 0 END) AS offline_count
FROM core_material;

-- 更新所有素材为上架状态，并记录上架时间
UPDATE core_material
SET 
    status = '0',
    online_time = NOW()
WHERE status = '1' OR online_time IS NULL;

-- 查看更新后的状态
SELECT 
    '更新后统计' AS info,
    COUNT(*) AS total,
    SUM(CASE WHEN status = '0' THEN 1 ELSE 0 END) AS online_count,
    SUM(CASE WHEN status = '1' THEN 1 ELSE 0 END) AS offline_count
FROM core_material;

-- 查看最近上架的素材
SELECT 
    id,
    title,
    status,
    online_time
FROM core_material
ORDER BY online_time DESC
LIMIT 10;

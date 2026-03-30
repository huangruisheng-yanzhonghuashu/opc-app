-- =============================================
-- 随机给素材打标签 SQL 脚本
-- =============================================

-- 第1步：从 core_tag 查询所有标签ID
SELECT id FROM core_tag;

-- 第2步：从 core_material 查询所有素材ID  
SELECT id FROM core_material;

-- 第3步：为每个素材随机选择1-3个标签，插入到 core_material_tag
-- 方法：使用子查询和 RAND() 随机分配

INSERT IGNORE INTO core_material_tag (material_id, tag_id, create_by, create_time)
SELECT 
    m.id AS material_id,
    (SELECT id FROM core_tag ORDER BY RAND() LIMIT 1) AS tag_id,
    'script',
    NOW()
FROM core_material m
WHERE RAND() <= 1  -- 所有素材都打1个标签

UNION ALL

SELECT 
    m.id AS material_id,
    (SELECT id FROM core_tag ORDER BY RAND() LIMIT 1) AS tag_id,
    'script',
    NOW()
FROM core_material m
WHERE RAND() <= 0.5  -- 50%的素材再打第2个标签

UNION ALL

SELECT 
    m.id AS material_id,
    (SELECT id FROM core_tag ORDER BY RAND() LIMIT 1) AS tag_id,
    'script',
    NOW()
FROM core_material m
WHERE RAND() <= 0.3;  -- 30%的素材再打第3个标签

-- 查看结果
SELECT '打标签完成' AS message;
SELECT COUNT(*) AS total_relations FROM core_material_tag;
SELECT COUNT(DISTINCT material_id) AS material_with_tags FROM core_material_tag;

-- ----------------------------
-- 素材二级分类表 core_material_category
-- ----------------------------
DROP TABLE IF EXISTS core_material_category;
CREATE TABLE core_material_category (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '分类ID',
    category_name       VARCHAR(100)    NOT NULL                   COMMENT '分类名称',
    package_type        INT             NOT NULL                   COMMENT '套餐分类（0晨报 1普通素材 2VIP素材 3超级VIP）',
    sort_order          INT             DEFAULT 0                  COMMENT '排序',
    status              CHAR(1)         DEFAULT '0'                COMMENT '状态（0启用 1禁用）',
    create_by           VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
    create_time         DATETIME                                   COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
    update_time         DATETIME                                   COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_package_type (package_type),
    KEY idx_status (status)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='素材二级分类表';

-- ----------------------------
-- 初始化数据
-- ----------------------------
INSERT INTO core_material_category (category_name, package_type, sort_order, status, create_time) VALUES
('热点新闻', 2, 1, '0', NOW()),
('市场分析', 2, 2, '0', NOW()),
('行业动态', 2, 3, '0', NOW()),
('投资指南', 2, 4, '0', NOW()),
('深度研报', 3, 1, '0', NOW()),
('独家分析', 3, 2, '0', NOW()),
('专业策略', 3, 3, '0', NOW()),
('高端资讯', 3, 4, '0', NOW());



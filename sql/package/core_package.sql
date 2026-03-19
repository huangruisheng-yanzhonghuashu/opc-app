-- ----------------------------
-- 套餐配置表 core_package
-- ----------------------------
DROP TABLE IF EXISTS core_package;
CREATE TABLE core_package (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '套餐ID',
    package_name        VARCHAR(100)    NOT NULL                   COMMENT '套餐名称',
    package_price       DECIMAL(10,2)   NOT NULL                   COMMENT '套餐价格',
    package_type        VARCHAR(20)     NOT NULL                   COMMENT '套餐分类（normal/vip/svip）',
    description         TEXT            DEFAULT NULL               COMMENT '套餐描述',
    status              CHAR(1)         DEFAULT '0'                 COMMENT '状态（0上架 1下架）',
    create_by           VARCHAR(64)     DEFAULT ''                  COMMENT '创建者',
    create_time         DATETIME                                    COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                  COMMENT '更新者',
    update_time         DATETIME                                    COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL                COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_package_type (package_type),
    KEY idx_status (status)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='套餐配置表';

-- 套餐分类说明：
-- package_type = 'normal' 时为普通会员（一级套餐）
-- package_type = 'vip' 时为VIP会员（二级套餐）
-- package_type = 'svip' 时为超级VIP会员（三级套餐）

-- ----------------------------
-- 初始化默认套餐数据
-- ----------------------------
INSERT INTO core_package (package_name, package_price, package_type, description, status) VALUES
('普通会员', 99.00, 'normal', '普通会员套餐', '0'),
('VIP会员', 199.00, 'vip', 'VIP会员套餐', '0'),
('超级VIP会员', 299.00, 'svip', '超级VIP会员套餐', '0');

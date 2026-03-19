-- ----------------------------
-- 会员页面配置表 core_member_config
-- ----------------------------
DROP TABLE IF EXISTS core_member_config;
CREATE TABLE core_member_config (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '配置ID',
    config_type         VARCHAR(50)     NOT NULL                   COMMENT '配置类型（banner/vip_guide）',
    image_url           VARCHAR(500)    DEFAULT NULL               COMMENT '图片URL',
    article_link        VARCHAR(500)    DEFAULT NULL               COMMENT '文章链接/id',
    rich_content        LONGTEXT        DEFAULT NULL               COMMENT '富文本内容（用于vip引导图片）',
    status              CHAR(1)         DEFAULT '0'                 COMMENT '状态（0启用 1禁用）',
    create_by           VARCHAR(64)     DEFAULT ''                  COMMENT '创建者',
    create_time         DATETIME                                    COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                  COMMENT '更新者',
    update_time         DATETIME                                    COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL                COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_type (config_type)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='会员页面配置表';

-- 配置类型说明：
-- config_type = 'banner' 时为会员页banner图配置
-- config_type = 'vip_guide' 时为vip文章下方引导图片配置

-- ----------------------------
-- 初始化默认配置数据
-- ----------------------------
INSERT INTO core_member_config (config_type, status) VALUES ('banner', '0');
INSERT INTO core_member_config (config_type, status) VALUES ('vip_guide', '0');

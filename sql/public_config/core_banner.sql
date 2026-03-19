-- ----------------------------
-- 资讯页banner配置表 core_banner
-- ----------------------------
DROP TABLE IF EXISTS core_banner;
CREATE TABLE core_banner (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT 'BannerID',
    title               VARCHAR(255)    DEFAULT NULL               COMMENT '标题',
    image_url           VARCHAR(500)    NOT NULL                   COMMENT '图片URL',
    link_type           CHAR(1)         DEFAULT '1'                COMMENT '链接类型（1文章ID 2外部链接）',
    link_value          VARCHAR(500)    DEFAULT NULL               COMMENT '链接值（文章ID或外部链接）',
    sort_order          INT             DEFAULT 0                  COMMENT '排序（数字越小越靠前）',
    status              CHAR(1)         DEFAULT '0'                COMMENT '状态（0启用 1禁用）',
    create_by           VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
    create_time         DATETIME                                    COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
    update_time         DATETIME                                    COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_status (status),
    KEY idx_sort_order (sort_order)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='资讯页banner配置表';

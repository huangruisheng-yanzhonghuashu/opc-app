-- ----------------------------
-- 搜索热词配置表 core_search_hotword
-- ----------------------------
DROP TABLE IF EXISTS core_search_hotword;
CREATE TABLE core_search_hotword (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '热词ID',
    keyword             VARCHAR(100)    NOT NULL                   COMMENT '热词内容',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='搜索热词配置表';

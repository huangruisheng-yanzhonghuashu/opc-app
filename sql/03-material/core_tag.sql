-- ----------------------------
-- 标签库表 core_tag
-- ----------------------------
DROP TABLE IF EXISTS core_tag;
CREATE TABLE core_tag (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '标签ID',
    tag_name            VARCHAR(100)    NOT NULL                   COMMENT '标签名称',
    tag_color           VARCHAR(50)     DEFAULT NULL               COMMENT '标签颜色',
    sort_order          INT             DEFAULT 0                  COMMENT '排序',
    status              CHAR(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
    create_by           VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
    create_time         DATETIME                                   COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
    update_time         DATETIME                                   COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tag_name (tag_name),
    KEY idx_status (status),
    KEY idx_sort_order (sort_order)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='标签库表';


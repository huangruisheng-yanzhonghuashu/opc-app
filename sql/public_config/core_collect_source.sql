-- ----------------------------
-- 采集信息源配置表 core_collect_source
-- ----------------------------
DROP TABLE IF EXISTS core_collect_source;
CREATE TABLE core_collect_source (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '配置ID',
    keyword             VARCHAR(255)    NOT NULL                   COMMENT '关键词',
    source_url          VARCHAR(500)    NOT NULL                   COMMENT '信息源链接',
    source_type         VARCHAR(50)     DEFAULT 'twitter'          COMMENT '来源类型（twitter/telegram/youtube等）',
    status              CHAR(1)         DEFAULT '0'                COMMENT '状态（0启用 1禁用）',
    create_by           VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
    create_time         DATETIME                                    COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
    update_time         DATETIME                                    COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_status (status),
    KEY idx_keyword (keyword)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='采集信息源配置表';

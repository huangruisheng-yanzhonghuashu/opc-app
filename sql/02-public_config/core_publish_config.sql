-- ----------------------------
-- 发布配置表
-- ----------------------------
drop table if exists core_publish_config;
CREATE TABLE core_publish_config (
    id bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    platform_type varchar(20) NOT NULL COMMENT '平台类型 (ios/android)',
    version varchar(50) NOT NULL COMMENT '版本号',
    publish_status char(1) DEFAULT '0' COMMENT '发布状态 (0=发布中, 1=发布完成)',
    publish_time datetime DEFAULT NULL COMMENT '发布时间',
    remark varchar(500) DEFAULT NULL COMMENT '备注',
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_platform_version (platform_type, version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发布配置表';

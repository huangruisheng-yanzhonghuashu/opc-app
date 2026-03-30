-- ----------------------------
-- 素材标签关联表 core_material_tag
-- ----------------------------
DROP TABLE IF EXISTS core_material_tag;
CREATE TABLE core_material_tag (
           id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '关联ID',
           material_id         BIGINT(20)      NOT NULL                   COMMENT '素材ID',
           tag_id              BIGINT(20)      NOT NULL                   COMMENT '标签ID',
           create_by           VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
           create_time         DATETIME                                   COMMENT '创建时间',
           PRIMARY KEY (id),
           UNIQUE KEY uk_material_tag (material_id, tag_id),
           KEY idx_material_id (material_id),
           KEY idx_tag_id (tag_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='素材标签关联表';

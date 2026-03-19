-- ----------------------------
-- 素材表 core_material
-- ----------------------------
DROP TABLE IF EXISTS core_material;
CREATE TABLE core_material (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '素材ID',
    title               VARCHAR(255)    NOT NULL                   COMMENT '标题',
    author              VARCHAR(100)    DEFAULT NULL               COMMENT '作者（注明出处）',
    summary             TEXT            DEFAULT NULL               COMMENT '总结',
    content             LONGTEXT        DEFAULT NULL               COMMENT '正文',
    original_url        VARCHAR(500)    DEFAULT NULL               COMMENT '原链接',
    publish_time        DATETIME        DEFAULT NULL               COMMENT '发布时间',
    view_permission     INT             DEFAULT 1                  COMMENT '查看权限（1一级套餐 2二级套餐 3三级套餐）',
    content_type        VARCHAR(20)     DEFAULT 'text'             COMMENT '内容类型（text/image/video）',
    cover_image         VARCHAR(500)    DEFAULT NULL               COMMENT '会员展示图片',
    category            VARCHAR(50)     DEFAULT NULL               COMMENT '分类（normal/vip_column/vip/svip）',
    status              CHAR(1)         DEFAULT '0'                COMMENT '状态（0上线 1下线）',
    is_top              CHAR(1)         DEFAULT '0'                COMMENT '是否置顶（0否 1是）',
    source              VARCHAR(50)     DEFAULT NULL               COMMENT '来源（crawler爬取/manual手动）',
    create_by           VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
    create_time         DATETIME                                    COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
    update_time         DATETIME                                    COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_status (status),
    KEY idx_is_top (is_top),
    KEY idx_view_permission (view_permission),
    KEY idx_category (category),
    KEY idx_publish_time (publish_time)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='素材表';

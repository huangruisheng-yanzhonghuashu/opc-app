-- 创建素材用户行为记录表
CREATE TABLE core_material_user_action (
    id              BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '主键ID',
    material_id     BIGINT(20)      NOT NULL                   COMMENT '素材ID',
    user_id         BIGINT(20)      NOT NULL                   COMMENT '用户ID（会员ID）',
    action_type     VARCHAR(20)     NOT NULL                   COMMENT '行为类型（like:喜欢, dislike:不喜欢）',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_material_user_action (material_id, user_id, action_type),
    KEY idx_material_id (material_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='素材用户行为记录表';

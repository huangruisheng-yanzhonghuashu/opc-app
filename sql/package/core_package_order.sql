-- ----------------------------
-- 套餐购买订单表 core_package_order
-- ----------------------------
DROP TABLE IF EXISTS core_package_order;
CREATE TABLE core_package_order (
    id                  BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '订单ID',
    order_no            VARCHAR(64)     NOT NULL                   COMMENT '订单号',
    member_id           BIGINT(20)     DEFAULT NULL                COMMENT '会员ID',
    email               VARCHAR(100)    DEFAULT NULL                COMMENT '邮箱',
    third_party_account VARCHAR(100)    DEFAULT NULL                COMMENT '第三方账号',
    nickname            VARCHAR(50)     DEFAULT NULL                COMMENT '昵称',
    package_id          BIGINT(20)     NOT NULL                   COMMENT '套餐ID',
    package_name        VARCHAR(100)    NOT NULL                   COMMENT '套餐名称',
    package_type        VARCHAR(20)     NOT NULL                   COMMENT '套餐分类',
    price               DECIMAL(10,2)   NOT NULL                   COMMENT '价格',
    pay_time            DATETIME        DEFAULT NULL                COMMENT '支付时间',
    pay_status          CHAR(1)         DEFAULT '0'                 COMMENT '支付状态（0待支付 1已支付 2已取消）',
    create_by           VARCHAR(64)     DEFAULT ''                  COMMENT '创建者',
    create_time         DATETIME                                    COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                  COMMENT '更新者',
    update_time         DATETIME                                    COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL                COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_member_id (member_id),
    KEY idx_email (email),
    KEY idx_pay_status (pay_status),
    KEY idx_pay_time (pay_time)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='套餐购买订单表';

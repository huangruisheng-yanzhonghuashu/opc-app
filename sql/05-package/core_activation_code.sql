-- 创建激活码表
CREATE TABLE IF NOT EXISTS `core_activation_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(32) NOT NULL COMMENT '激活码',
  `valid_days` int(11) DEFAULT NULL COMMENT '有效天数',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `channel_tag` varchar(50) DEFAULT NULL COMMENT '渠道标签',
  `batch_no` varchar(32) DEFAULT NULL COMMENT '批次号',
  `status` char(1) DEFAULT '0' COMMENT '状态（0未使用 1已发送-未使用 2已发送-已使用 3已注销 4已过期）',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `use_user_id` bigint(20) DEFAULT NULL COMMENT '使用用户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_status` (`status`),
  KEY `idx_channel_tag` (`channel_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='激活码表';

-- 创建激活码下发记录表
CREATE TABLE IF NOT EXISTS `core_activation_code_issue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `activation_code_id` bigint(20) NOT NULL COMMENT '激活码ID',
  `code` varchar(32) NOT NULL COMMENT '激活码',
  `user_id` bigint(20) NOT NULL COMMENT '下发用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '用户手机号',
  `issue_time` datetime DEFAULT NULL COMMENT '下发时间',
  `issue_by` varchar(64) DEFAULT '' COMMENT '下发人',
  `issue_type` char(1) DEFAULT '1' COMMENT '下发类型（1手动下发 2批量下发）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0未使用 1已使用）',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_activation_code_id` (`activation_code_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='激活码下发记录表';



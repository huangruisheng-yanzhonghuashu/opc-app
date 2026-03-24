-- 意见反馈表
CREATE TABLE `core_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `member_id` bigint(20) DEFAULT NULL COMMENT '会员ID',
  `member_name` varchar(50) DEFAULT NULL COMMENT '会员名称',
  `type` varchar(20) NOT NULL COMMENT '反馈类型：bug-功能异常, feature-功能建议, other-其他',
  `title` varchar(200) NOT NULL COMMENT '反馈标题',
  `content` text NOT NULL COMMENT '反馈内容',
  `contact` varchar(100) DEFAULT NULL COMMENT '联系方式',
  `status` char(1) DEFAULT '0' COMMENT '处理状态：0-待处理, 1-处理中, 2-已处理',
  `reply` text COMMENT '回复内容',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `reply_by` varchar(64) DEFAULT NULL COMMENT '回复人',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='意见反馈表';

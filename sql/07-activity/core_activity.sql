-- 活动表
CREATE TABLE IF NOT EXISTS `core_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `activity_name` varchar(200) NOT NULL COMMENT '活动名称',
  `poster_url` varchar(500) DEFAULT NULL COMMENT '活动海报URL',
  `organizer_name` varchar(100) DEFAULT NULL COMMENT '活动组织者名称',
  `organizer_avatar` varchar(500) DEFAULT NULL COMMENT '活动组织者头像URL',
  `activity_time` datetime DEFAULT NULL COMMENT '活动时间',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `address` varchar(500) DEFAULT NULL COMMENT '详细地址',
  `total_capacity` int(11) DEFAULT '0' COMMENT '活动总人数',
  `registered_count` int(11) DEFAULT '0' COMMENT '已报名人数',
  `registration_fee` decimal(10,2) DEFAULT '0.00' COMMENT '活动报名费用',
  `activity_detail` longtext COMMENT '活动详情（富文本）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_activity_time` (`activity_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 活动区Banner配置表
CREATE TABLE IF NOT EXISTS `core_activity_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'BannerID',
  `banner_name` varchar(200) DEFAULT NULL COMMENT 'Banner名称',
  `image_url` varchar(500) NOT NULL COMMENT 'Banner图片URL',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID（点击跳转）',
  `sort_order` int(11) DEFAULT '0' COMMENT '显示顺序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动区Banner配置表';

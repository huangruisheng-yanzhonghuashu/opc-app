-- 素材媒体文件表
DROP TABLE IF EXISTS `core_material_media`;

CREATE TABLE `core_material_media` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '媒体ID',
  `material_id` bigint(20) NOT NULL COMMENT '素材ID',
  `media_type` varchar(20) NOT NULL COMMENT '媒体类型（image/video）',
  `file_url` varchar(500) DEFAULT NULL COMMENT '文件URL',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序号',
  `status` char(1) DEFAULT '0' COMMENT '状态（0=正常，1=删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='素材媒体文件表';

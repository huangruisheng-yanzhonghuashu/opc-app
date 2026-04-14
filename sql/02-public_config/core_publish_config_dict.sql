-- ----------------------------
-- 发布配置字典数据
-- ----------------------------

-- 字典类型：platform_type（平台类型）
-- 先检查是否已存在
DELETE FROM sys_dict_data WHERE dict_type = 'platform_type';
DELETE FROM sys_dict_type WHERE dict_type = 'platform_type';

INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) 
VALUES ('平台类型', 'platform_type', '0', 'admin', sysdate(), '', null, 'iOS/Android平台类型');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, 'iOS', 'ios', 'platform_type', '', '', 'N', '0', 'admin', sysdate(), '苹果平台'),
(2, 'Android', 'android', 'platform_type', '', '', 'N', '0', 'admin', sysdate(), '安卓平台');

-- 字典类型：publish_status（发布状态）
DELETE FROM sys_dict_data WHERE dict_type = 'publish_status';
DELETE FROM sys_dict_type WHERE dict_type = 'publish_status';

INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) 
VALUES ('发布状态', 'publish_status', '0', 'admin', sysdate(), '', null, '发布配置状态：0=发布中，1=发布完成');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '发布中', '0', 'publish_status', '', 'danger', 'Y', '0', 'admin', sysdate(), '正在发布'),
(2, '发布完成', '1', 'publish_status', '', 'success', 'N', '0', 'admin', sysdate(), '发布已完成');

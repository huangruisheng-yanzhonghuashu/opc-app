-- ----------------------------
-- 公共配置菜单（与会员管理同级，都在运营管理下，parent_id=5）
-- ----------------------------

-- 公共配置目录
INSERT INTO sys_menu VALUES('400', '公共配置', '5', '3', 'publicConfig', '', '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', SYSDATE(), '', NULL, '公共配置目录');

-- ----------------------------
-- 资讯页banner配置菜单（parent_id=400）
-- ----------------------------
INSERT INTO sys_menu VALUES('401', '资讯页banner配置', '400', '1', 'banner', 'core/banner/index', '', '', 1, 0, 'C', '0', '0', 'core:banner:list', 'chart', 'admin', SYSDATE(), '', NULL, '资讯页banner配置菜单');

-- 按钮权限
INSERT INTO sys_menu VALUES('4010', 'Banner查询', '401', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4011', 'Banner新增', '401', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:add', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4012', 'Banner修改', '401', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:edit', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4013', 'Banner删除', '401', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:remove', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4014', 'Banner状态', '401', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:changeStatus', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4015', 'Banner导出', '401', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- ----------------------------
-- 采集信息源配置菜单（parent_id=400）
-- ----------------------------
INSERT INTO sys_menu VALUES('402', '采集信息源配置', '400', '2', 'collectSource', 'core/collectSource/index', '', '', 1, 0, 'C', '0', '0', 'core:collect:list', 'link', 'admin', SYSDATE(), '', NULL, '采集信息源配置菜单');

-- 按钮权限
INSERT INTO sys_menu VALUES('4020', '配置查询', '402', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4021', '配置新增', '402', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:add', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4022', '配置修改', '402', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:edit', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4023', '配置删除', '402', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:remove', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4024', '配置状态', '402', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:changeStatus', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4025', '配置导出', '402', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- 为超级管理员角色赋予公共配置所有权限
INSERT INTO sys_role_menu VALUES ('1', '400');
INSERT INTO sys_role_menu VALUES ('1', '401');
INSERT INTO sys_role_menu VALUES ('1', '4010');
INSERT INTO sys_role_menu VALUES ('1', '4011');
INSERT INTO sys_role_menu VALUES ('1', '4012');
INSERT INTO sys_role_menu VALUES ('1', '4013');
INSERT INTO sys_role_menu VALUES ('1', '4014');
INSERT INTO sys_role_menu VALUES ('1', '4015');
INSERT INTO sys_role_menu VALUES ('1', '402');
INSERT INTO sys_role_menu VALUES ('1', '4020');
INSERT INTO sys_role_menu VALUES ('1', '4021');
INSERT INTO sys_role_menu VALUES ('1', '4022');
INSERT INTO sys_role_menu VALUES ('1', '4023');
INSERT INTO sys_role_menu VALUES ('1', '4024');
INSERT INTO sys_role_menu VALUES ('1', '4025');

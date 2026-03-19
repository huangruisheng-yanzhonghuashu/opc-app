-- ----------------------------
-- 公共配置菜单（与会员管理同级，都在运营管理下，parent_id=5）
-- 注意：菜单ID已改为600系列，避免与套餐管理(400系列)冲突
-- ----------------------------

-- 公共配置目录
INSERT INTO sys_menu VALUES('600', '公共配置', '5', '3', 'publicConfig', '', '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', SYSDATE(), '', NULL, '公共配置目录');

-- ----------------------------
-- 资讯页banner配置菜单（parent_id=600）
-- ----------------------------
INSERT INTO sys_menu VALUES('601', '资讯页banner配置', '600', '1', 'banner', 'core/banner/index', '', '', 1, 0, 'C', '0', '0', 'core:banner:list', 'chart', 'admin', SYSDATE(), '', NULL, '资讯页banner配置菜单');

-- 按钮权限
INSERT INTO sys_menu VALUES('6010', 'Banner查询', '601', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6011', 'Banner新增', '601', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:add', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6012', 'Banner修改', '601', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:edit', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6013', 'Banner删除', '601', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:remove', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6014', 'Banner状态', '601', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:changeStatus', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6015', 'Banner导出', '601', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- ----------------------------
-- 采集信息源配置菜单（parent_id=600）
-- ----------------------------
INSERT INTO sys_menu VALUES('602', '采集信息源配置', '600', '2', 'collectSource', 'core/collectSource/index', '', '', 1, 0, 'C', '0', '0', 'core:collect:list', 'link', 'admin', SYSDATE(), '', NULL, '采集信息源配置菜单');

-- 按钮权限
INSERT INTO sys_menu VALUES('6020', '配置查询', '602', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6021', '配置新增', '602', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:add', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6022', '配置修改', '602', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:edit', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6023', '配置删除', '602', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:remove', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6024', '配置状态', '602', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:changeStatus', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('6025', '配置导出', '602', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- 为超级管理员角色赋予公共配置所有权限
INSERT INTO sys_role_menu VALUES ('1', '600');
INSERT INTO sys_role_menu VALUES ('1', '601');
INSERT INTO sys_role_menu VALUES ('1', '6010');
INSERT INTO sys_role_menu VALUES ('1', '6011');
INSERT INTO sys_role_menu VALUES ('1', '6012');
INSERT INTO sys_role_menu VALUES ('1', '6013');
INSERT INTO sys_role_menu VALUES ('1', '6014');
INSERT INTO sys_role_menu VALUES ('1', '6015');
INSERT INTO sys_role_menu VALUES ('1', '602');
INSERT INTO sys_role_menu VALUES ('1', '6020');
INSERT INTO sys_role_menu VALUES ('1', '6021');
INSERT INTO sys_role_menu VALUES ('1', '6022');
INSERT INTO sys_role_menu VALUES ('1', '6023');
INSERT INTO sys_role_menu VALUES ('1', '6024');
INSERT INTO sys_role_menu VALUES ('1', '6025');

-- ----------------------------
-- 套餐管理菜单（与会员管理同级，都在运营管理下）
-- ----------------------------

-- ----------------------------
-- 一级菜单：套餐管理（与会员管理同级）
-- ----------------------------
INSERT INTO sys_menu VALUES('400', '套餐管理', '5', '3', 'package', '', '', '', 1, 0, 'M', '0', '0', '', 'money', 'admin', SYSDATE(), '', NULL, '套餐管理目录');

-- ----------------------------
-- 二级菜单：会员页面配置（包含banner图和VIP引导图两个配置区块）
-- ----------------------------
INSERT INTO sys_menu VALUES('410', '会员页面配置', '400', '1', 'memberConfig', 'core/memberConfig/index', '', '', 1, 0, 'C', '0', '0', 'core:memberConfig:list', 'setting', 'admin', SYSDATE(), '', NULL, '会员页面配置菜单');

-- ----------------------------
-- 二级菜单：付费套餐配置
-- ----------------------------
INSERT INTO sys_menu VALUES('420', '付费套餐配置', '400', '2', 'package', 'core/package/index', '', '', 1, 0, 'C', '0', '0', 'core:package:list', 'shopping', 'admin', SYSDATE(), '', NULL, '付费套餐配置菜单');

-- ----------------------------
-- 二级菜单：套餐购买明细
-- ----------------------------
INSERT INTO sys_menu VALUES('430', '套餐购买明细', '400', '3', 'packageOrder', 'core/packageOrder/index', '', '', 1, 0, 'C', '0', '0', 'core:packageOrder:list', 'list', 'admin', SYSDATE(), '', NULL, '套餐购买明细菜单');

-- ----------------------------
-- 按钮权限 - 会员页面配置
-- ----------------------------
INSERT INTO sys_menu VALUES('4100', '配置查询', '410', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:memberConfig:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4101', '配置保存', '410', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:memberConfig:save', '#', 'admin', SYSDATE(), '', NULL, '');

-- ----------------------------
-- 按钮权限 - 付费套餐配置
-- ----------------------------
INSERT INTO sys_menu VALUES('4200', '套餐查询', '420', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:package:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4201', '套餐新增', '420', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:package:add', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4202', '套餐修改', '420', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:package:edit', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4203', '套餐删除', '420', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:package:remove', '#', 'admin', SYSDATE(), '', NULL, '');

-- ----------------------------
-- 按钮权限 - 套餐购买明细
-- ----------------------------
INSERT INTO sys_menu VALUES('4300', '订单查询', '430', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:packageOrder:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4301', '订单新增', '430', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:packageOrder:add', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4302', '订单修改', '430', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:packageOrder:edit', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('4303', '订单删除', '430', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:packageOrder:remove', '#', 'admin', SYSDATE(), '', NULL, '');

-- ----------------------------
-- 为超级管理员角色赋予套餐管理所有权限
-- ----------------------------
INSERT INTO sys_role_menu VALUES ('1', '400');
INSERT INTO sys_role_menu VALUES ('1', '410');
INSERT INTO sys_role_menu VALUES ('1', '4100');
INSERT INTO sys_role_menu VALUES ('1', '4101');
INSERT INTO sys_role_menu VALUES ('1', '420');
INSERT INTO sys_role_menu VALUES ('1', '4200');
INSERT INTO sys_role_menu VALUES ('1', '4201');
INSERT INTO sys_role_menu VALUES ('1', '4202');
INSERT INTO sys_role_menu VALUES ('1', '4203');
INSERT INTO sys_role_menu VALUES ('1', '430');
INSERT INTO sys_role_menu VALUES ('1', '4300');
INSERT INTO sys_role_menu VALUES ('1', '4301');
INSERT INTO sys_role_menu VALUES ('1', '4302');
INSERT INTO sys_role_menu VALUES ('1', '4303');

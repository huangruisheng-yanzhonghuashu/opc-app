-- ----------------------------
-- 套餐管理 菜单及权限
-- ----------------------------

-- 一级菜单：套餐管理（目录类型，上级：运营管理 menu_id=5，排序5，menu_id=60000）
insert into sys_menu values('60000', '套餐管理', '5', '5', 'packageManage', '', '', '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', null, '套餐管理目录');

-- 菜单：会员页面（上级：套餐管理）
insert into sys_menu values('60001', '会员页面', '60000', '1', 'memberConfig', 'core/memberConfig/index', '', '', 1, 0, 'C', '0', '0', 'core:memberConfig:list', 'config', 'admin', sysdate(), '', null, '会员页面配置菜单');

-- 菜单：套餐配置（上级：套餐管理）
insert into sys_menu values('60002', '套餐配置', '60000', '2', 'package', 'core/package/index', '', '', 1, 0, 'C', '0', '0', 'core:package:list', 'gift', 'admin', sysdate(), '', null, '套餐配置菜单');

-- 菜单：套餐订单（上级：套餐管理）
insert into sys_menu values('60003', '套餐订单', '60000', '3', 'packageOrder', 'core/packageOrder/index', '', '', 1, 0, 'C', '0', '0', 'core:packageOrder:list', 'order', 'admin', sysdate(), '', null, '套餐订单菜单');

-- ----------------------------
-- 会员页面配置权限按钮
-- ----------------------------
insert into sys_menu values('60010', '会员页面配置查询', '60001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:memberConfig:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('60011', '会员页面配置保存', '60001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:memberConfig:save', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 套餐配置权限按钮
-- ----------------------------
insert into sys_menu values('60020', '套餐查询', '60002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:package:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('60021', '套餐新增', '60002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:package:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('60022', '套餐修改', '60002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:package:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('60023', '套餐删除', '60002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:package:remove', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 套餐订单权限按钮
-- ----------------------------
insert into sys_menu values('60030', '订单查询', '60003', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:packageOrder:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('60031', '订单新增', '60003', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:packageOrder:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('60032', '订单修改', '60003', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:packageOrder:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('60033', '订单删除', '60003', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:packageOrder:remove', '#', 'admin', sysdate(), '', null, '');

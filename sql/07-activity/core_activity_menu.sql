-- ----------------------------
-- 活动管理 菜单及权限
-- ----------------------------

-- 一级菜单：活动管理（目录类型，上级：运营管理 menu_id=5，排序7，menu_id=80000）
insert into sys_menu values('80000', '活动管理', '5', '7', 'activityManage', '', '', '', 1, 0, 'M', '0', '0', '', 'star', 'admin', sysdate(), '', null, '活动管理目录');

-- 菜单：活动列表（上级：活动管理）
insert into sys_menu values('80001', '活动列表', '80000', '1', 'activity', 'core/activity/index', '', '', 1, 0, 'C', '0', '0', 'core:activity:list', 'list', 'admin', sysdate(), '', null, '活动列表菜单');

-- 菜单：活动Banner配置（上级：活动管理）
insert into sys_menu values('80002', '活动Banner配置', '80000', '2', 'activityBanner', 'core/activityBanner/index', '', '', 1, 0, 'C', '0', '0', 'core:activityBanner:list', 'image', 'admin', sysdate(), '', null, '活动Banner配置菜单');

-- ----------------------------
-- 活动列表权限按钮
-- ----------------------------
insert into sys_menu values('80010', '活动查询', '80001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activity:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80011', '活动新增', '80001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activity:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80012', '活动修改', '80001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activity:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80013', '活动删除', '80001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activity:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80014', '活动导出', '80001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activity:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80015', '活动状态修改', '80001', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activity:changeStatus', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 活动Banner配置权限按钮
-- ----------------------------
insert into sys_menu values('80020', '活动Banner查询', '80002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activityBanner:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80021', '活动Banner新增', '80002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activityBanner:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80022', '活动Banner修改', '80002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activityBanner:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80023', '活动Banner删除', '80002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activityBanner:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80024', '活动Banner导出', '80002', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activityBanner:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('80025', '活动Banner状态修改', '80002', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:activityBanner:changeStatus', '#', 'admin', sysdate(), '', null, '');

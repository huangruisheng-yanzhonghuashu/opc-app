-- ----------------------------
-- 会员管理 菜单及权限
-- ----------------------------

-- 菜单：会员管理（上级：运营管理 menu_id=5，与数据概览、公共配置、素材管理同级）
insert into sys_menu values('3200', '会员管理', '5', '4', 'member', 'core/member/index', '', '', 1, 0, 'C', '0', '0', 'core:member:list', 'user', 'admin', sysdate(), '', null, '会员管理菜单');

-- ----------------------------
-- 会员管理权限按钮
-- ----------------------------
insert into sys_menu values('3210', '会员查询', '3200', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3211', '会员新增', '3200', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3212', '会员修改', '3200', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3213', '会员导出', '3200', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:export', '#', 'admin', sysdate(), '', null, '');

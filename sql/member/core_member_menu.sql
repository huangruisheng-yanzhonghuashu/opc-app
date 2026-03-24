-- ----------------------------
-- 会员管理 菜单及权限
-- ----------------------------

-- 目录：会员管理（上级：运营管理 menu_id=5）
insert into sys_menu values('3200', '会员管理', '5', '4', 'member', '', '', '', 1, 0, 'M', '0', '0', '', 'user', 'admin', sysdate(), '', null, '会员管理目录');

-- 菜单：会员列表（上级：会员管理）
insert into sys_menu values('3201', '会员列表', '3200', '1', 'memberList', 'core/member/index', '', '', 1, 0, 'C', '0', '0', 'core:member:list', 'user', 'admin', sysdate(), '', null, '会员列表菜单');

-- ----------------------------
-- 会员列表权限按钮
-- ----------------------------
insert into sys_menu values('3210', '会员查询', '3201', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3211', '会员新增', '3201', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3212', '会员修改', '3201', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3213', '会员导出', '3201', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:export', '#', 'admin', sysdate(), '', null, '');

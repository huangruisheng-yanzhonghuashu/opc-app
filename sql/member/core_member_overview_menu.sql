-- ----------------------------
-- 运营管理 - 会员概览 菜单及权限
-- ----------------------------

-- 菜单：会员概览（上级：运营管理，menu_id = 5）
insert into sys_menu values('2010', '数据概览', '5', '1', 'overview', 'core/member/overview', '', '', 1, 0, 'C', '0', '0', 'core:member:overview:query', 'chart', 'admin', sysdate(), '', null, '会员概览菜单');

-- 权限：会员概览查询
insert into sys_menu values('2011', '数据概览查询', '2010', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:overview:query', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 运营管理 - 数据概览 菜单及权限
-- ----------------------------

-- 菜单：数据概览（上级：运营管理 menu_id=5，排序1，menu_id=20000）
insert into sys_menu values('20000', '数据概览', '5', '1', 'overview', 'core/overview/index', '', '', 1, 0, 'C', '0', '0', 'core:member:overview:query', 'chart', 'admin', sysdate(), '', null, '会员概览菜单');

-- 权限：数据概览查询
insert into sys_menu values('20001', '数据概览查询', '20000', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:overview:query', '#', 'admin', sysdate(), '', null, '');

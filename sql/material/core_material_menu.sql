-- ----------------------------
-- 素材管理 菜单及权限
-- ----------------------------

-- 菜单：素材管理（上级：运营管理 menu_id=5，与数据概览、公共配置同级）
insert into sys_menu values('3100', '素材管理', '5', '3', 'material', 'core/material/index', '', '', 1, 0, 'C', '0', '0', 'core:material:list', 'file', 'admin', sysdate(), '', null, '素材管理菜单');

-- ----------------------------
-- 素材管理权限按钮
-- ----------------------------
insert into sys_menu values('3110', '素材查询', '3100', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3111', '素材新增', '3100', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3112', '素材修改', '3100', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3113', '素材删除', '3100', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3114', '素材导出', '3100', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3115', '素材状态修改', '3100', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:changeStatus', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3116', '素材置顶修改', '3100', '7', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:changeTop', '#', 'admin', sysdate(), '', null, '');

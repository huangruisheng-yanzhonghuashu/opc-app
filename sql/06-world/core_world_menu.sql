-- ----------------------------
-- 看世界模块菜单
-- ----------------------------

-- 一级菜单：看世界（目录类型，上级：运营管理 menu_id=5，排序6，在package后，menu_id=70000）
insert into sys_menu values('70000', '看世界', '5', '6', 'world', '', '', '', 1, 0, 'M', '0', '0', '', 'star', 'admin', sysdate(), '', null, '看世界目录');

-- 二级菜单：社区管理
insert into sys_menu values('70001', '社区管理', '70000', '1', 'community', 'core/world/community/index', '', '', 1, 0, 'C', '0', '0', 'core:community:list', 'list', 'admin', sysdate(), '', null, '社区管理菜单');

-- 社区管理按钮权限
insert into sys_menu values('70010', '社区查询', '70001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:query', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70011', '社区新增', '70001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:add', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70012', '社区修改', '70001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:edit', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70013', '社区删除', '70001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:remove', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70014', '社区导出', '70001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:export', '', 'admin', sysdate(), '', null, '');

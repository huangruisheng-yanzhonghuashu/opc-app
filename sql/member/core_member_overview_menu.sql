-- ----------------------------
-- 会员数据概览菜单SQL
-- ----------------------------

-- ----------------------------
-- 在会员管理菜单下添加子菜单：数据概览
-- ----------------------------
insert into sys_menu values('202', '数据概览', '200', '7', '', 'core/member/overview', '', '', 1, 0, 'C', '0', '0', 'core:member:overview:list', 'data-analysis', 'admin', sysdate(), '', null, '会员数据概览菜单');

-- ----------------------------
-- 按钮权限
-- ----------------------------
insert into sys_menu values('2020', '数据查询', '202', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:overview:query', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 为超级管理员角色赋予数据概览权限
-- ----------------------------
insert into sys_role_menu values ('1', '202');
insert into sys_role_menu values ('1', '2020');

-- ----------------------------
-- 为普通角色赋予数据概览查看权限
-- ----------------------------
insert into sys_role_menu values ('2', '202');
insert into sys_role_menu values ('2', '2020');

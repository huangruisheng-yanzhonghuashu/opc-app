-- ----------------------------
-- 会员管理菜单SQL
-- ----------------------------

-- ----------------------------
-- 一级菜单：会员管理（与系统管理同级）
-- ----------------------------
insert into sys_menu values('5', '会员管理', '0', '4', '', null, '', '', 1, 0, 'M', '0', '0', '', 'user', 'admin', sysdate(), '', null, '会员管理目录');

-- ----------------------------
-- 二级菜单：会员列表
-- ----------------------------
insert into sys_menu values('200', '会员列表', '5', '1', 'member', 'core/member/index', '', '', 1, 0, 'C', '0', '0', 'core:member:list', 'peoples', 'admin', sysdate(), '', null, '会员列表菜单');

-- ----------------------------
-- 按钮权限
-- ----------------------------
insert into sys_menu values('2000', '会员查询', '200', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2001', '会员新增', '200', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2002', '会员修改', '200', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2003', '会员删除', '200', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2004', '会员导出', '200', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 为超级管理员角色赋予会员管理所有权限
-- ----------------------------
insert into sys_role_menu values ('1', '5');
insert into sys_role_menu values ('1', '200');
insert into sys_role_menu values ('1', '2000');
insert into sys_role_menu values ('1', '2001');
insert into sys_role_menu values ('1', '2002');
insert into sys_role_menu values ('1', '2003');
insert into sys_role_menu values ('1', '2004');

-- ----------------------------
-- 为普通角色赋予会员管理查看权限（可根据需要调整）
-- ----------------------------
insert into sys_role_menu values ('2', '5');
insert into sys_role_menu values ('2', '200');
insert into sys_role_menu values ('2', '2000');

-- ----------------------------
-- 会员管理 菜单及权限
-- ----------------------------

-- 目录：会员管理（上级：运营管理 menu_id=5，排序4，menu_id=50000）
insert into sys_menu values('50000', '会员管理', '5', '4', 'member', '', '', '', 1, 0, 'M', '0', '0', '', 'user', 'admin', sysdate(), '', null, '会员管理目录');

-- 菜单：会员列表（上级：会员管理）
insert into sys_menu values('50001', '会员列表', '50000', '1', 'memberList', 'core/member/index', '', '', 1, 0, 'C', '0', '0', 'core:member:list', 'user', 'admin', sysdate(), '', null, '会员列表菜单');

-- ----------------------------
-- 会员列表权限按钮
-- ----------------------------
insert into sys_menu values('50110', '会员查询', '50001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('50111', '会员新增', '50001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('50112', '会员修改', '50001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('50113', '会员导出', '50001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:member:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 会员管理 - 意见反馈管理 菜单及权限
-- ----------------------------

-- 菜单：意见反馈管理（上级：会员管理 menu_id=50000，排序2，menu_id=50002）
insert into sys_menu values('50002', '意见反馈管理', '50000', '2', 'feedback', 'core/feedback/index', '', '', 1, 0, 'C', '0', '0', 'core:feedback:list', 'message', 'admin', sysdate(), '', null, '意见反馈管理菜单');

-- ----------------------------
-- 意见反馈管理权限按钮
-- ----------------------------
insert into sys_menu values('50210', '意见反馈查询', '50002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:feedback:query', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu values('50211', '意见反馈导出', '50002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:feedback:export', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu values('50212', '意见反馈回复', '50002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:feedback:reply', '#', 'admin', sysdate(), '', null, '');

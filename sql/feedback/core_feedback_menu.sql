-- 菜单SQL（意见反馈放在会员管理目录 menu_id=3200 下面）
insert into sys_menu values('3300', '意见反馈管理', '3200', '2', 'feedback', 'core/feedback/index', '', '', 1, 0, 'C', '0', '0', 'core:feedback:list', 'message', 'admin', sysdate(), '', null, '意见反馈管理菜单');

-- 按钮 SQL
insert into sys_menu values('3310', '意见反馈查询', '3300', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:feedback:query', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu values('3311', '意见反馈导出', '3300', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:feedback:export', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu values('3312', '意见反馈回复', '3300', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:feedback:reply', '#', 'admin', sysdate(), '', null, '');

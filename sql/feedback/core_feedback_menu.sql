-- 菜单SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('意见反馈管理', 2000, 6, 'feedback', 'core/feedback/index', 1, 0, 'C', '0', '0', 'core:feedback:list', 'message', 'admin', sysdate(), '', null, '意见反馈管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('意见反馈查询', @parentId, 1, '#', '', 1, 0, 'F', '0', '0', 'core:feedback:query', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('意见反馈导出', @parentId, 2, '#', '', 1, 0, 'F', '0', '0', 'core:feedback:export', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('意见反馈回复', @parentId, 3, '#', '', 1, 0, 'F', '0', '0', 'core:feedback:reply', '#', 'admin', sysdate(), '', null, '');

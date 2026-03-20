-- ----------------------------
-- 更新运营管理菜单图标
-- ----------------------------
UPDATE sys_menu SET icon = 'operation' WHERE menu_name = '运营管理' AND parent_id = 0;

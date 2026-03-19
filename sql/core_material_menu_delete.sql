-- ----------------------------
-- 素材管理菜单删除脚本
-- ----------------------------

-- 删除素材管理相关菜单权限
DELETE FROM sys_role_menu WHERE menu_id IN (300, 3000, 3001, 3002, 3003, 3004, 3005, 3006);

-- 删除素材管理按钮权限
DELETE FROM sys_menu WHERE menu_id IN (3000, 3001, 3002, 3003, 3004, 3005, 3006);

-- 删除素材管理菜单
DELETE FROM sys_menu WHERE menu_id = 300;

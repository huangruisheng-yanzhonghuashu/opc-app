-- ----------------------------
-- 公共配置菜单删除脚本
-- ----------------------------

-- 删除公共配置相关菜单权限
DELETE FROM sys_role_menu WHERE menu_id IN (400, 401, 4010, 4011, 4012, 4013, 4014, 4015, 402, 4020, 4021, 4022, 4023, 4024, 4025);

-- 删除采集信息源按钮权限
DELETE FROM sys_menu WHERE menu_id IN (4020, 4021, 4022, 4023, 4024, 4025);

-- 删除采集信息源菜单
DELETE FROM sys_menu WHERE menu_id = 402;

-- 删除Banner按钮权限
DELETE FROM sys_menu WHERE menu_id IN (4010, 4011, 4012, 4013, 4014, 4015);

-- 删除Banner菜单
DELETE FROM sys_menu WHERE menu_id = 401;

-- 删除公共配置目录
DELETE FROM sys_menu WHERE menu_id = 400;

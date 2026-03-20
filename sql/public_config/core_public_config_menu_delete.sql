-- ----------------------------
-- 删除公共配置菜单及权限
-- ----------------------------

-- 删除采集信息源权限
delete from sys_menu where menu_id in ('3025', '3024', '3023', '3022', '3021', '3020');

-- 删除Banner权限
delete from sys_menu where menu_id in ('3015', '3014', '3013', '3012', '3011', '3010');

-- 删除采集信息源菜单
delete from sys_menu where menu_id = '3002';

-- 删除Banner菜单
delete from sys_menu where menu_id = '3001';

-- 删除公共配置目录
delete from sys_menu where menu_id = '3000';

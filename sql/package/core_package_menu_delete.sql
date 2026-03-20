-- ----------------------------
-- 删除套餐管理菜单及权限
-- ----------------------------

-- 删除订单权限
delete from sys_menu where menu_id in ('4033', '4032', '4031', '4030');

-- 删除套餐权限
delete from sys_menu where menu_id in ('4023', '4022', '4021', '4020');

-- 删除会员页面配置权限
delete from sys_menu where menu_id in ('4011', '4010');

-- 删除订单菜单
delete from sys_menu where menu_id = '4003';

-- 删除套餐菜单
delete from sys_menu where menu_id = '4002';

-- 删除会员页面配置菜单
delete from sys_menu where menu_id = '4001';

-- 删除套餐管理目录
delete from sys_menu where menu_id = '4000';

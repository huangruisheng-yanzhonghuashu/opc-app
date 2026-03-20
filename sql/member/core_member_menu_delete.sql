-- ----------------------------
-- 删除会员管理菜单及权限
-- ----------------------------

-- 删除会员权限
delete from sys_menu where menu_id in ('3213', '3212', '3211', '3210');

-- 删除会员菜单
delete from sys_menu where menu_id = '3200';

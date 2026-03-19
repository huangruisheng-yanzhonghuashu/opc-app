-- ----------------------------
-- 素材管理菜单（与会员管理同级，都在运营管理下）
-- ----------------------------

-- 二级菜单：素材管理（parent_id=5 表示在运营管理下，icon: 'edit' 表示编辑图标）
INSERT INTO sys_menu VALUES('300', '素材管理', '5', '2', 'material', 'core/material/index', '', '', 1, 0, 'C', '0', '0', 'core:material:list', 'edit', 'admin', SYSDATE(), '', NULL, '素材管理菜单');

-- 按钮权限
INSERT INTO sys_menu VALUES('3000', '素材查询', '300', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('3001', '素材新增', '300', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:add', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('3002', '素材修改', '300', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:edit', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('3003', '素材删除', '300', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:remove', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('3004', '素材上下线', '300', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:changeStatus', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('3005', '素材置顶', '300', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:changeTop', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('3006', '素材导出', '300', '7', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- 为超级管理员角色赋予素材管理所有权限
INSERT INTO sys_role_menu VALUES ('1', '300');
INSERT INTO sys_role_menu VALUES ('1', '3000');
INSERT INTO sys_role_menu VALUES ('1', '3001');
INSERT INTO sys_role_menu VALUES ('1', '3002');
INSERT INTO sys_role_menu VALUES ('1', '3003');
INSERT INTO sys_role_menu VALUES ('1', '3004');
INSERT INTO sys_role_menu VALUES ('1', '3005');
INSERT INTO sys_role_menu VALUES ('1', '3006');

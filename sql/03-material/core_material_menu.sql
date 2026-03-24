-- ----------------------------
-- 素材管理 菜单及权限
-- ----------------------------

-- 素材管理（上级：运营管理 menu_id=5，排序3，menu_id=40000）
INSERT INTO sys_menu VALUES('40000', '素材管理', '5', '3', 'material', NULL, '', '', 1, 0, 'M', '0', '0', '', 'file', 'admin', sysdate(), '', null, '素材管理目录');

-- ----------------------------
-- 素材管理子菜单
-- ----------------------------
INSERT INTO sys_menu VALUES('40001', '素材列表', '40000', '1', 'material', 'core/material/index', '', '', 1, 0, 'C', '0', '0', 'core:material:list', 'file', 'admin', sysdate(), '', null, '素材管理菜单');

-- ----------------------------
-- 素材列表权限按钮
-- ----------------------------
INSERT INTO sys_menu VALUES('40110', '素材查询', '40001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40111', '素材新增', '40001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40112', '素材修改', '40001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40113', '素材删除', '40001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40114', '素材导出', '40001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:export', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40115', '素材状态修改', '40001', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:changeStatus', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40116', '素材置顶修改', '40001', '7', '', '', '', '', 1, 0, 'F', '0', '0', 'core:material:changeTop', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 标签库管理子菜单
-- ----------------------------
INSERT INTO sys_menu VALUES('40002', '标签库管理', '40000', '2', 'tag', 'core/tag/index', '', '', 1, 0, 'C', '0', '0', 'core:tag:list', 'tag', 'admin', sysdate(), '', null, '标签库管理菜单');

-- ----------------------------
-- 标签库管理权限按钮
-- ----------------------------
INSERT INTO sys_menu VALUES('40120', '标签查询', '40002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:tag:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40121', '标签新增', '40002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:tag:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40122', '标签修改', '40002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:tag:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40123', '标签删除', '40002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:tag:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40124', '标签导出', '40002', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:tag:export', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40125', '标签状态修改', '40002', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:tag:changeStatus', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('40126', '标签置顶修改', '40002', '7', '', '', '', '', 1, 0, 'F', '0', '0', 'core:tag:changeTop', '#', 'admin', sysdate(), '', null, '');

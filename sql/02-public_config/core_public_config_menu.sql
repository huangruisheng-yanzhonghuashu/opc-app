-- ----------------------------
-- 公共配置 菜单及权限
-- ----------------------------

-- 一级菜单：公共配置（目录类型，上级：运营管理 menu_id=5，排序2，menu_id=30000）
insert into sys_menu values('30000', '公共配置', '5', '2', 'publicConfig', '', '', '', 1, 0, 'M', '0', '0', '', 'cog', 'admin', sysdate(), '', null, '公共配置目录');

-- 菜单：banner配置（上级：公共配置）
insert into sys_menu values('30001', 'Banner配置', '30000', '1', 'banner', 'core/banner/index', '', '', 1, 0, 'C', '0', '0', 'core:banner:list', 'image', 'admin', sysdate(), '', null, '资讯页banner配置菜单');

-- 菜单：采集信息源（上级：公共配置）
insert into sys_menu values('30002', '采集信息源', '30000', '2', 'collectSource', 'core/collectSource/index', '', '', 1, 0, 'C', '0', '0', 'core:collect:list', 'source', 'admin', sysdate(), '', null, '采集信息源配置菜单');

-- 菜单：搜索热词（上级：公共配置）
insert into sys_menu values('30003', '搜索热词', '30000', '3', 'searchHotword', 'core/searchHotword/index', '', '', 1, 0, 'C', '0', '0', 'core:searchHotword:list', 'search', 'admin', sysdate(), '', null, '搜索热词配置菜单');

-- ----------------------------
-- Banner配置权限按钮
-- ----------------------------
insert into sys_menu values('30010', 'Banner查询', '30001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30011', 'Banner新增', '30001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30012', 'Banner修改', '30001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30013', 'Banner删除', '30001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30014', 'Banner导出', '30001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30015', 'Banner状态修改', '30001', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:changeStatus', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 采集信息源配置权限按钮
-- ----------------------------
insert into sys_menu values('30020', '采集信息源查询', '30002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30021', '采集信息源新增', '30002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30022', '采集信息源修改', '30002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30023', '采集信息源删除', '30002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30024', '采集信息源导出', '30002', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30025', '采集信息源状态修改', '30002', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:changeStatus', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 搜索热词配置权限按钮
-- ----------------------------
insert into sys_menu values('30030', '搜索热词查询', '30003', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:searchHotword:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30031', '搜索热词新增', '30003', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:searchHotword:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30032', '搜索热词修改', '30003', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:searchHotword:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30033', '搜索热词删除', '30003', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:searchHotword:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30034', '搜索热词导出', '30003', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:searchHotword:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30035', '搜索热词状态修改', '30003', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:searchHotword:changeStatus', '#', 'admin', sysdate(), '', null, '');

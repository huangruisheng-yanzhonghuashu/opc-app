-- ----------------------------
-- 公共配置 菜单及权限
-- ----------------------------

-- 一级菜单：公共配置（目录类型，上级：运营管理 menu_id=5）
insert into sys_menu values('3000', '公共配置', '5', '2', 'publicConfig', '', '', '', 1, 0, 'M', '0', '0', '', 'cog', 'admin', sysdate(), '', null, '公共配置目录');

-- 菜单：资讯页banner配置（上级：公共配置）
insert into sys_menu values('3001', 'banner配置', '3000', '1', 'banner', 'core/banner/index', '', '', 1, 0, 'C', '0', '0', 'core:banner:list', 'image', 'admin', sysdate(), '', null, '资讯页banner配置菜单');

-- 菜单：采集信息源配置（上级：公共配置）
insert into sys_menu values('3002', '采集信息源', '3000', '2', 'collectSource', 'core/collectSource/index', '', '', 1, 0, 'C', '0', '0', 'core:collect:list', 'source', 'admin', sysdate(), '', null, '采集信息源配置菜单');

-- ----------------------------
-- Banner配置权限按钮
-- ----------------------------
insert into sys_menu values('3010', 'Banner查询', '3001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3011', 'Banner新增', '3001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3012', 'Banner修改', '3001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3013', 'Banner删除', '3001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3014', 'Banner导出', '3001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3015', 'Banner状态修改', '3001', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:banner:changeStatus', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 采集信息源配置权限按钮
-- ----------------------------
insert into sys_menu values('3020', '采集信息源查询', '3002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3021', '采集信息源新增', '3002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3022', '采集信息源修改', '3002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3023', '采集信息源删除', '3002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3024', '采集信息源导出', '3002', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('3025', '采集信息源状态修改', '3002', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:collect:changeStatus', '#', 'admin', sysdate(), '', null, '');

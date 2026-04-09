-- ----------------------------
-- 客服配置 数据表
-- ----------------------------

-- 创建客服配置表
drop table if exists core_customer_service;
create table core_customer_service (
  id                    bigint(20)      not null auto_increment    comment '客服ID',
  service_name          varchar(100)    not null                   comment '客服名称',
  qr_code_url           varchar(500)    default null               comment '客服二维码URL',
  wechat_id             varchar(100)    default null               comment '客服微信号',
  phone                 varchar(20)     default null               comment '客服电话',
  is_default            char(1)         default '1'                comment '是否默认（0是 1否）',
  status                char(1)         default '0'                comment '状态（0启用 1禁用）',
  sort_order            int(11)         default 0                  comment '排序号',
  create_by             varchar(64)     default ''                 comment '创建者',
  create_time           datetime                                   comment '创建时间',
  update_by             varchar(64)     default ''                 comment '更新者',
  update_time           datetime                                   comment '更新时间',
  remark                varchar(500)    default null               comment '备注',
  primary key (id)
) engine=innodb auto_increment=1 comment='客服配置表';

-- ----------------------------
-- 客服配置 菜单及权限
-- ----------------------------

-- 菜单：客服配置（上级：公共配置 menu_id=30000，排序4）
insert into sys_menu values('30004', '客服配置', '30000', '4', 'customerService', 'core/customerService/index', '', '', 1, 0, 'C', '0', '0', 'core:customerService:list', 'customer-service', 'admin', sysdate(), '', null, '客服配置菜单');

-- ----------------------------
-- 客服配置权限按钮
-- ----------------------------
insert into sys_menu values('30040', '客服查询', '30004', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:customerService:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30041', '客服新增', '30004', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:customerService:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30042', '客服修改', '30004', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:customerService:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('30043', '客服删除', '30004', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:customerService:remove', '#', 'admin', sysdate(), '', null, '');

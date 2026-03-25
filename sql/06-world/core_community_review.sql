-- ----------------------------
-- 社区评价表 core_community_review
-- ----------------------------
drop table if exists core_community_review;
create table core_community_review
(
    id                    bigint(20)      not null auto_increment    comment '评价ID',
    community_id          bigint(20)      not null                   comment '社区ID',
    member_id             bigint(20)      not null                   comment '会员ID',
    rating                decimal(2, 1)   not null default 5.0        comment '评价星级(0-5)',
    content               text            default null               comment '评价内容',
    images                varchar(2000)   default null               comment '评价图片(JSON数组)',
    status                char(1)         default '0'                comment '状态（0正常 1隐藏 2删除）',
    like_count            int(11)         default 0                  comment '点赞数',
    create_by             varchar(64)     default ''                 comment '创建者',
    create_time           datetime                                   comment '创建时间',
    update_by             varchar(64)     default ''                 comment '更新者',
    update_time           datetime                                   comment '更新时间',
    remark                varchar(500)    default null               comment '备注',
    primary key (id),
    key idx_community_id (community_id),
    key idx_member_id (member_id),
    key idx_status (status),
    key idx_create_time (create_time)
) engine=innodb auto_increment=1 comment = '社区评价表';

-- ----------------------------
-- 社区评价菜单
-- ----------------------------
-- 三级菜单：社区评价管理
insert into sys_menu values('70002', '社区评价', '70000', '2', 'review', 'core/world/review/index', '', '', 1, 0, 'C', '0', '0', 'core:community:review:list', 'rate', 'admin', sysdate(), '', null, '社区评价管理菜单');

-- 社区评价按钮权限
insert into sys_menu values('70020', '评价查询', '70002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:review:query', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70021', '评价新增', '70002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:review:add', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70022', '评价修改', '70002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:review:edit', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70023', '评价删除', '70002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:review:remove', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70024', '评价导出', '70002', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:review:export', '', 'admin', sysdate(), '', null, '');
insert into sys_menu values('70025', '评价审核', '70002', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'core:community:review:audit', '', 'admin', sysdate(), '', null, '');

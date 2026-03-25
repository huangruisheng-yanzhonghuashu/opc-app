-- ----------------------------
-- 社区想去记录表 core_community_want_to_go
-- ----------------------------
drop table if exists core_community_want_to_go;
create table core_community_want_to_go
(
    id                    bigint(20)      not null auto_increment    comment '记录ID',
    community_id          bigint(20)      not null                   comment '社区ID',
    member_id             bigint(20)      not null                   comment '会员ID',
    status                char(1)         default '0'                comment '状态（0正常 1取消）',
    create_by             varchar(64)     default ''                 comment '创建者',
    create_time           datetime                                   comment '创建时间',
    update_by             varchar(64)     default ''                 comment '更新者',
    update_time           datetime                                   comment '更新时间',
    remark                varchar(500)    default null               comment '备注',
    primary key (id),
    unique key uk_community_member (community_id, member_id)
) engine=innodb auto_increment=1 comment = '社区想去记录表';

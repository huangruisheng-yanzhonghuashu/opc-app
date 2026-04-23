-- ----------------------------
-- 会员注销表 core_member_cancel
-- ----------------------------
drop table if exists core_member_cancel;
create table core_member_cancel
(
    id                    bigint(20)      not null auto_increment    comment '会员ID',
    username              varchar(50) not null comment '会员名',
    nickname              varchar(50)  default null comment '会员昵称',
    password              VARCHAR(100) DEFAULT NULL COMMENT '密码',
    phone_number          varchar(11)  default null comment '手机号',
    email                 varchar(100) default null comment '邮箱',
    avatar                varchar(500) default null comment '头像',
    last_active_time      timestamp    default null comment '最近活跃时间',
    current_package       varchar(100) default null comment '当前购买套餐',
    package_type          int(11)       default null               comment '套餐分类（1普通会员 2VIP会员 3超级VIP会员）',
    source                varchar(50)  default null comment '来源（email邮箱 x X facebook Facebook apple Apple google Google）',
    source_id             varchar(64)  default null comment '来源ID',
    token                 varchar(500) default null comment 'Token',
    status                char(1)      default '2' comment '会员状态（2已注销）',
    register_time         timestamp    default null comment '注册时间',
    invite_code           varchar(50)  default null comment '邀请码',
    cancel_time           datetime     default null comment '注销时间',
    create_by             varchar(64)  default '' comment '创建者',
    create_time           datetime comment '创建时间',
    update_by             varchar(64)  default '' comment '更新者',
    update_time           datetime comment '更新时间',
    remark                varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb auto_increment=1 comment = '会员注销信息表';

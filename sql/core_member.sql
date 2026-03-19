-- ----------------------------
-- 会员表 core_member
-- ----------------------------
drop table if exists core_member;
create table core_member (
                             id                  bigint(20)      not null auto_increment    comment '会员ID',
                             username            varchar(50)     not null                   comment '会员名',
                             nickname            varchar(50)     default null               comment '会员昵称',
                             phone_number        varchar(11)     default null               comment '手机号',
                             email               varchar(100)    default null               comment '邮箱',
                             avatar              varchar(500)    default null               comment '头像',
                             last_active_time    timestamp       default null               comment '最近活跃时间',
                             current_package     varchar(100)    default null               comment '当前购买套餐',
                             current_package_level int(11)       default 0                  comment '当前套餐等级（1一级 2二级 3三级）',
                             source              varchar(50)     default null               comment '来源（email邮箱 x X facebook Facebook apple Apple google Google）',
                             source_id           varchar(64)     default null               comment '来源ID',
                             token               varchar(500)    default null               comment 'Token',
                             status              char(1)         default '0'                comment '会员状态（0正常 1禁用/拉黑）',
                             register_time       timestamp       default null               comment '注册时间',
                             create_by           varchar(64)     default ''                 comment '创建者',
                             create_time         datetime                                   comment '创建时间',
                             update_by           varchar(64)     default ''                 comment '更新者',
                             update_time         datetime                                   comment '更新时间',
                             remark              varchar(500)    default null               comment '备注',
                             primary key (id),
                             unique key uk_username (username),
                             unique key uk_phone_number (phone_number),
                             unique key uk_email (email)
) engine=innodb auto_increment=1 comment = '会员信息表';

-- ----------------------------
-- 初始化会员数据
-- ----------------------------
-- insert into core_member values(1, 'testuser', '测试用户', '13800138000', 'test@example.com', 'https://example.com/avatar.jpg', '2024-01-01 10:00:00', 'VIP', 3, 'email', 'SRC001', 'token123', '0', '2024-01-01 00:00:00', 'admin', sysdate(), '', null, '测试会员');

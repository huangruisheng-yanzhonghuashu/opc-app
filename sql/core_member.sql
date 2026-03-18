-- ----------------------------
-- 会员表 core_member
-- ----------------------------
drop table if exists core_member;
create table core_member (
                             member_id           bigint(20)      not null auto_increment    comment '会员ID',
                             phone_number        varchar(11)     default null               comment '手机号',
                             email               varchar(100)    default null               comment '邮箱',
                             username            varchar(50)     not null                   comment '会员名',
                             nickname            varchar(50)     default null               comment '会员昵称',
                             last_active_time    varchar(50)    default null               comment '最近活跃时间',
                             current_package     varchar(100)    default null               comment '当前购买套餐',
                             level1_users        int(11)         default 0                  comment '一级用户数',
                             level2_users        int(11)         default 0                  comment '二级用户数',
                             level3_users        int(11)         default 0                  comment '三级用户数',
                             source              varchar(50)     default null               comment '来源',
                             source_id           varchar(64)     default null               comment '来源ID',
                             status              char(1)         default '0'                comment '会员状态（0正常 1禁用）',
                             register_time       varchar(50)    default null               comment '注册时间',
                             create_by           varchar(64)     default ''                 comment '创建者',
                             create_time         datetime                                   comment '创建时间',
                             update_by           varchar(64)     default ''                 comment '更新者',
                             update_time         datetime                                   comment '更新时间',
                             remark              varchar(500)    default null               comment '备注',
                             primary key (member_id),
                             unique key uk_username (username),
                             key idx_phone_number (phone_number),
                             key idx_email (email),
                             key idx_status (status)
) engine=innodb auto_increment=1 comment = '会员信息表';

-- ----------------------------
-- 初始化会员数据
-- ----------------------------
-- insert into core_member values(1, '13800138000', 'test@example.com', 'testuser', '测试用户', '2024-01-01 10:00:00', 'VIP', 10, 20, 30, 'Website', 'SRC001', '0', '2024-01-01 00:00:00', 'admin', sysdate(), '', null, '测试会员');

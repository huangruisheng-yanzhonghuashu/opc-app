-- ----------------------------
-- 社区表 core_community
-- ----------------------------
drop table if exists core_community;
create table core_community
(
    id                    bigint(20)      not null auto_increment    comment '社区ID',
    name                  varchar(100) not null comment '社区名',
    image                 varchar(500) default null comment '社区图片',
    address               varchar(255) default null comment '社区地址',
    longitude             decimal(10, 7) default null comment '经度',
    latitude              decimal(10, 7) default null comment '纬度',
    details               text         default null comment '相关详情',
    want_to_go_count      int(11)      default 0 comment '想去数',
    visited_count         int(11)      default 0 comment '已去过数',
    review_count          int(11)      default 0 comment '评价数',
    rating                decimal(2, 1) default 5.0 comment '评价星级(0-5)',
    status                char(1)      default '0' comment '状态（0正常 1停用）',
    sort_order            int(11)      default 0 comment '排序',
    create_by             varchar(64)  default '' comment '创建者',
    create_time           datetime comment '创建时间',
    update_by             varchar(64)  default '' comment '更新者',
    update_time           datetime comment '更新时间',
    remark                varchar(500) default null comment '备注',
    primary key (id),
    key idx_status (status),
    key idx_sort_order (sort_order)
) engine=innodb auto_increment=1 comment = '社区信息表';

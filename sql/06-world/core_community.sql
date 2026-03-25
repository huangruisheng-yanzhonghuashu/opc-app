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



INSERT INTO `core_community`
(`name`, `address`, `longitude`, `latitude`, `details`, `want_to_go_count`, `visited_count`, `review_count`, `rating`, `status`, `sort_order`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    ('青岛新 100 创意文化产业园', '山东省青岛市市南区南京路 100-1 号', 120.3821000, 36.0722000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('青岛 AIGC・OPC 产业基地', '山东省青岛市市南区银川西路 67、69 号', 120.4015000, 36.0789000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('青岛 OPC 商贸金融智联产业园', '山东省青岛市市北区馆陶路 34 号', 120.3178000, 36.0912000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('东软 OPC 工业数智产业园', '山东省青岛市市北区湖溪路 1 号', 120.3206000, 36.1025000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('金水创新产业园', '山东省青岛市李沧区九水东路 130 号', 120.4357000, 36.1628000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('繁星空间・青岛国际创新园 OPC 创业孵化平台', '山东省青岛市崂山区新利路 11 号国际创新园 G 座', 120.4783000, 36.1167000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('青岛蓝谷崂山实验室成果转化基地', '山东省青岛市即墨区蓝谷国实大厦', 120.6954000, 36.3782000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('磊鑫都市智造园 OPC 载体', '山东省青岛市城阳区春阳路 88 号', 120.4126000, 36.3012000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('鲁港科技合作创新中心 OPC 基地', '山东省青岛市胶州市上合示范区湘江路', 120.0789000, 36.1358000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('江北新区中央商务区 OPC 社区', '江苏省南京市浦口区浦滨路 91 号', 118.6924000, 32.0587000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('江北新区 beeplus 研创 OPC 社区', '江苏省南京市江北新区华创路 65 号智信大厦 B 座 2-8 层', 118.7103000, 32.0476000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('江北新区骐骥驰骋 AIGC 漫剧 OPC 千人基地', '江苏省南京市江北新区腾飞大厦 C 座 16 楼（过渡）/ 数智溪谷 7 号楼', 118.7025000, 32.0398000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('江北新区药智汇 OPC 社区', '江苏省南京市江北新区龙山南路 141 号', 118.7215000, 32.1896000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('江北新区 AI + 新材料 OPC 社区', '江苏省南京市江北新区宁六路 606 号新材料国际创新社区 D 栋', 118.7302000, 32.2015000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('m + 金狮校园里 OPC 社区', '江苏省苏州市姑苏区金狮河沿 45 号', 120.6123000, 31.3087000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('信息技术应用创新 OPC 社区', '江苏省苏州市姑苏区朱家湾街 8 号 5 栋 301', 120.5896000, 31.3215000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('华贸云享 OPC 社区', '江苏省苏州市姑苏区广济南路 369 号 1 幢 6 层', 120.5987000, 31.3152000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('姑苏云谷・长三角数字经济双创中心 OPC 社区', '江苏省苏州市姑苏区朱家湾街 8 号 5 幢 1-3 层', 120.5894000, 31.3217000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('m + 驻这里创意设计产业园 OPC 社区', '江苏省苏州市姑苏区长洲路 20 号', 120.6208000, 31.3256000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('城市智谷 OPC 社区', '江苏省苏州市姑苏区苏站路 1599 号 1 号楼 9、10 层', 120.6135000, 31.3302000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('零界魔方 OPC 社区', '上海市静安区 / 浦东新区核心载体', 121.4737000, 31.2304000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('上海临港超级个体 288 基地', '上海市浦东新区临港新片区', 121.8631000, 30.9052000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('张江 AI 小镇 OPC 孵化中心', '上海市浦东新区张江高科技园区', 121.5812000, 31.2135000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('洞泾人工智能特色小镇 OPC 基地', '上海市松江区洞泾镇', 121.3018000, 31.1026000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('中关村 AI 北纬 OPC 社区', '北京市海淀区中关村核心区', 116.3054000, 39.9872000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('北京经开区模数 OPC 社区', '北京市北京经济技术开发区', 116.5079000, 39.7983000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('中关村软件园 OPC 专项载体', '北京市海淀区西北旺中关村软件园', 116.2987000, 40.0492000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('751d・park 时尚设计 OPC 社区', '北京市朝阳区 798 艺术区 751 园区', 116.4958000, 39.9845000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('深圳天使荟 OPC 社区', '广东省深圳市福田区深圳新一代产业园、北方大厦', 114.0623000, 22.5412000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('深圳创空间 OPC 社区', '广东省深圳市罗湖区笋清片区', 114.1025000, 22.5708000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('深圳模力营 AI 生态 OPC 社区', '广东省深圳市南山区南山智城片区', 113.9294000, 22.5367000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('济南城投大厦 OPC 社区', '山东省济南市历下区城投大厦', 117.1325000, 36.6652000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('济南科金大厦 OPC 社区', '山东省济南市历下区科金大厦', 117.1298000, 36.6687000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('齐鲁软件园 OPC 社区', '山东省济南市高新区齐鲁软件园', 117.1356000, 36.6703000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('济南人工智能岛 OPC 社区', '山东省济南市高新区人工智能岛', 117.1389000, 36.6721000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('明途启航营 OPC 基地', '四川省成都市天府新区核心载体', 104.0658000, 30.5512000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('天府新谷 OPC 创业社区', '四川省成都市高新区天府新谷', 104.0587000, 30.5426000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('海艺互娱 OPC 基地', '四川省成都市高新区数字文创园', 104.0612000, 30.5489000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL),
    ('蓉数 OPC 社区', '四川省成都市天府新区天府长岛数字文创园', 104.0596000, 30.5603000, NULL, 0, 0, 0, 5.0, '0', 0, '', NULL, '', NULL, NULL);
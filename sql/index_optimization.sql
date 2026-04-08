-- ============================================================
-- 数据库索引优化脚本
-- 根据表结构和查询语句分析生成的索引优化建议
-- ============================================================

-- ============================================================
-- 1. core_material 表索引优化
-- ============================================================

-- 删除现有索引（如果需要重建）
-- DROP INDEX idx_status ON core_material;
-- DROP INDEX idx_is_top ON core_material;
-- DROP INDEX idx_package_type ON core_material;
-- DROP INDEX idx_category_id ON core_material;
-- DROP INDEX idx_publish_time ON core_material;

-- 新增索引：复合索引用于列表查询 (status + is_top + create_time)
-- 查询: selectMaterialList 中常用条件组合
ALTER TABLE core_material ADD INDEX idx_status_top_time (status, is_top, create_time);

-- 新增索引：复合索引用于套餐类型查询 (status + package_type + is_top + publish_time)
-- 查询: selectMaterialListByTagId 等接口常用
ALTER TABLE core_material ADD INDEX idx_status_pkg_top_pub (status, package_type, is_top, publish_time);

-- 新增索引：分类查询优化 (category_id + status + issue_no)
-- 查询: selectLatestMaterialByCategoryId, selectMaterialListByCategoryIdExcludeLatest
ALTER TABLE core_material ADD INDEX idx_cat_status_issue (category_id, status, issue_no);

-- 新增索引：来源查询 (source + status)
-- 查询: selectMaterialList 中 source 条件筛选
ALTER TABLE core_material ADD INDEX idx_source_status (source, status);

-- 新增索引：素材类型查询 (material_type + status)
ALTER TABLE core_material ADD INDEX idx_type_status (material_type, status);

-- 新增索引：内容类型查询 (content_type + status)
ALTER TABLE core_material ADD INDEX idx_content_type_status (content_type, status);

-- 新增索引：原标题模糊查询优化（前缀索引）
-- 注意: 需要MySQL 5.6+ 支持，或者使用全文索引
ALTER TABLE core_material ADD INDEX idx_title (title(100));

-- 新增索引：作者查询
ALTER TABLE core_material ADD INDEX idx_author (author);

-- 新增索引：original_id 唯一查询
-- 查询: selectMaterialByOriginalId
ALTER TABLE core_material ADD INDEX idx_original_id (original_id);

-- 新增索引：上线时间范围查询
ALTER TABLE core_material ADD INDEX idx_online_time (online_time);

-- 新增索引：创建时间范围查询（用于后台管理列表）
ALTER TABLE core_material ADD INDEX idx_create_time (create_time);


-- ============================================================
-- 2. core_member 表索引优化
-- ============================================================

-- 新增索引：用户名查询（用于模糊搜索）
ALTER TABLE core_member ADD INDEX idx_username (username);

-- 新增索引：昵称查询（用于模糊搜索）
ALTER TABLE core_member ADD INDEX idx_nickname (nickname);

-- 新增索引：套餐类型 + 状态（用于统计查询）
-- 查询: selectMemberOverview 中的分组统计
ALTER TABLE core_member ADD INDEX idx_pkg_type_status (package_type, status);

-- 新增索引：来源查询
ALTER TABLE core_member ADD INDEX idx_source (source);

-- 新增索引：source_id 查询（第三方登录）
ALTER TABLE core_member ADD INDEX idx_source_id (source_id);

-- 新增索引：注册时间（用于统计）
ALTER TABLE core_member ADD INDEX idx_register_time (register_time);

-- 新增索引：最近活跃时间（用于统计）
ALTER TABLE core_member ADD INDEX idx_last_active_time (last_active_time);

-- 新增索引：状态查询
ALTER TABLE core_member ADD INDEX idx_status (status);

-- 新增索引：邀请码查询
ALTER TABLE core_member ADD INDEX idx_invite_code (invite_code);


-- ============================================================
-- 3. core_community 表索引优化
-- ============================================================

-- 新增索引：省份查询
-- 查询: selectCommunityList 中 province 条件
ALTER TABLE core_community ADD INDEX idx_province (province);

-- 新增索引：复合索引（状态 + 排序）
ALTER TABLE core_community ADD INDEX idx_status_sort (status, sort_order);

-- 新增索引：地理位置查询（用于附近社区查询）
ALTER TABLE core_community ADD INDEX idx_location (longitude, latitude);

-- 新增索引：社区名称模糊搜索
ALTER TABLE core_community ADD INDEX idx_name (name);


-- ============================================================
-- 4. core_package_order 表索引优化
-- ============================================================

-- 新增索引：复合索引（支付状态 + 创建时间）
-- 查询: selectOrderList 常用条件
ALTER TABLE core_package_order ADD INDEX idx_pay_status_create (pay_status, create_time);

-- 新增索引：套餐类型查询
ALTER TABLE core_package_order ADD INDEX idx_package_type (package_type);

-- 新增索引：套餐ID查询
ALTER TABLE core_package_order ADD INDEX idx_package_id (package_id);

-- 新增索引：第三方账号查询
ALTER TABLE core_package_order ADD INDEX idx_third_party (third_party_account);

-- 新增索引：昵称查询
ALTER TABLE core_package_order ADD INDEX idx_nickname (nickname);


-- ============================================================
-- 5. core_community_review 表索引优化
-- ============================================================

-- 新增索引：复合索引（社区ID + 状态 + 创建时间）
-- 查询: selectReviewsByCommunityId, getReviewStatsByCommunityId
ALTER TABLE core_community_review ADD INDEX idx_comm_status_time (community_id, status, create_time);

-- 新增索引：会员ID查询
ALTER TABLE core_community_review ADD INDEX idx_member_id (member_id);

-- 新增索引：状态查询
ALTER TABLE core_community_review ADD INDEX idx_status (status);


-- ============================================================
-- 6. core_material_tag 表索引优化
-- ============================================================

-- 已有: uk_material_tag (material_id, tag_id) - 唯一索引
-- 已有: idx_material_id (material_id)
-- 已有: idx_tag_id (tag_id)

-- 新增索引：标签ID + 素材ID（用于标签查询素材列表）
-- 查询: selectMaterialListByTagId
ALTER TABLE core_material_tag ADD INDEX idx_tag_material (tag_id, material_id);


-- ============================================================
-- 7. core_material_user_action 表索引优化
-- ============================================================

-- 已有: uk_material_user_action (material_id, user_id, action_type) - 唯一索引
-- 已有: idx_material_id (material_id)
-- 已有: idx_user_id (user_id)

-- 新增索引：用户ID + 行为类型（查询用户的所有行为）
ALTER TABLE core_material_user_action ADD INDEX idx_user_action (user_id, action_type);


-- ============================================================
-- 8. core_community_want_to_go 表索引优化
-- ============================================================

-- 已有: uk_community_member (community_id, member_id) - 唯一索引

-- 新增索引：会员ID查询（查询用户想去的社区列表）
ALTER TABLE core_community_want_to_go ADD INDEX idx_member_id (member_id);

-- 新增索引：状态查询
ALTER TABLE core_community_want_to_go ADD INDEX idx_status (status);


-- ============================================================
-- 9. core_community_visited 表索引优化
-- ============================================================

-- 已有: uk_community_member (community_id, member_id) - 唯一索引

-- 新增索引：会员ID查询（查询用户去过的社区列表）
ALTER TABLE core_community_visited ADD INDEX idx_member_id (member_id);

-- 新增索引：去过时间
ALTER TABLE core_community_visited ADD INDEX idx_visit_time (visit_time);

-- 新增索引：状态查询
ALTER TABLE core_community_visited ADD INDEX idx_status (status);


-- ============================================================
-- 10. core_activity 表索引优化
-- ============================================================

-- 已有: idx_status (status)
-- 已有: idx_activity_time (activity_time)

-- 新增索引：复合索引（状态 + 活动时间）
ALTER TABLE core_activity ADD INDEX idx_status_time (status, activity_time);

-- 新增索引：省份查询
ALTER TABLE core_activity ADD INDEX idx_province (province);

-- 新增索引：城市查询
ALTER TABLE core_activity ADD INDEX idx_city (city);


-- ============================================================
-- 11. core_tag 表索引优化
-- ============================================================

-- 已有: uk_tag_name (tag_name) - 唯一索引
-- 已有: idx_status (status)
-- 已有: idx_sort_order (sort_order)

-- 新增索引：复合索引（状态 + 排序）
ALTER TABLE core_tag ADD INDEX idx_status_sort (status, sort_order);


-- ============================================================
-- 12. core_material_category 表索引优化
-- ============================================================

-- 已有: idx_package_type (package_type)
-- 已有: idx_status (status)

-- 新增索引：复合索引（套餐类型 + 状态 + 排序）
ALTER TABLE core_material_category ADD INDEX idx_pkg_status_sort (package_type, status, sort_order);


-- ============================================================
-- 索引优化说明
-- ============================================================
/*
优化原则:
1. 最左前缀原则: 复合索引的列顺序要根据查询条件的使用频率排列
2. 选择性原则: 选择性高的列放在前面
3. 覆盖索引: 尽量让查询只使用索引就能返回结果
4. 避免冗余: 已有联合索引包含的单列索引可以删除

主要优化点:
1. core_material: 增加了多个复合索引，优化了列表查询、分类查询、标签查询
2. core_member: 增加了统计查询和第三方登录相关的索引
3. core_community: 增加了地理位置和省份查询索引
4. core_package_order: 增加了支付状态相关的复合索引
5. core_community_review: 优化了评价统计查询
6. 关联表: 增加了反向查询索引

注意事项:
1. 执行前请在测试环境验证
2. 对于大数据量表，建议使用 pt-online-schema-change 或 gh-ost 进行在线DDL
3. 索引会增加写操作的开销，需要根据实际查询频率权衡
4. 建议定期使用 EXPLAIN 分析查询执行计划
*/

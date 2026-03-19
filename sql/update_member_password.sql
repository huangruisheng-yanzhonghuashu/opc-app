-- ----------------------------
-- 为会员表添加密码字段
-- ----------------------------
ALTER TABLE core_member ADD COLUMN password VARCHAR(100) DEFAULT NULL COMMENT '密码' AFTER username;

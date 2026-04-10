# SQL注入及安全分析报告

## 项目概述
- **项目名称**: OPC管理系统（基于若依框架）
- **技术栈**: Spring Boot + MyBatis + MySQL
- **分析时间**: 2025年

---

## 一、SQL注入风险分析

### 1.1 高风险 - 动态SQL执行（需要立即修复）

#### 1.1.1 GenTableMapper.xml - createTable方法
**位置**: `opc-generator/src/main/resources/mapper/generator/GenTableMapper.xml:175`

```xml
<update id="createTable">
    ${sql}
</update>
```

**风险描述**: 
- 使用 `${sql}` 直接拼接SQL语句，存在严重的SQL注入风险
- 虽然代码中使用了Druid SQL解析进行过滤，但仍可能被绕过

**调用链**:
1. `GenController.createTableSave()` 接收用户传入的sql参数
2. 调用 `SqlUtil.filterKeyword(sql)` 进行关键字过滤
3. 使用Druid解析SQL语句
4. 调用 `genTableService.createTable(createTableStatement.toString())`
5. 最终执行 `${sql}`

**修复建议**:
```java
// 当前过滤规则不够严格，建议：
// 1. 限制只能执行CREATE TABLE语句
// 2. 使用白名单机制限制表名、字段名
// 3. 禁止执行其他类型的SQL语句
```

---

### 1.2 中风险 - 数据权限SQL拼接

#### 1.2.1 SysUserMapper.xml
**位置**: 
- Line 86: `${params.dataScope}`
- Line 103: `${params.dataScope}`
- Line 121: `${params.dataScope}`

#### 1.2.2 SysRoleMapper.xml
**位置**: 
- Line 55: `${params.dataScope}`

#### 1.2.3 SysDeptMapper.xml
**位置**: 
- Line 46: `${params.dataScope}`

**风险描述**:
- 这些 `${params.dataScope}` 用于数据权限过滤
- 由 `DataScopeAspect.java` 自动生成SQL片段
- 虽然使用了预定义的权限范围，但仍需确保生成的SQL片段安全

**DataScopeAspect分析**:
```java
// DataScopeAspect.java 第125-130行
sqlString.append(StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id in ({}) ) ", deptAlias, String.join(",", scopeCustomIds)));
```

**安全措施**:
- ✅ 使用了 `StringUtils.format` 进行格式化
- ✅ roleId 是从数据库查询获取的，非用户直接输入
- ✅ 第175-183行的 `clearDataScope` 方法会清空参数防止注入

**结论**: 数据权限部分相对安全，因为SQL片段是由系统根据用户角色自动生成的，不是直接来自用户输入。

---

### 1.3 低风险 - 其他Mapper分析

#### 1.3.1 所有其他Mapper XML文件
**分析结果**: 
- ✅ 所有查询条件都使用 `#{}` 参数绑定
- ✅ 没有直接使用 `${}` 拼接用户输入
- ✅ 使用了MyBatis的动态SQL标签（`<if>`, `<foreach>` 等）

**示例**:
```xml
<!-- CoreMaterialMapper.xml -->
<if test="materialName != null and materialName != ''">
    AND material_name like concat('%', #{materialName}, '%')
</if>
```

---

## 二、其他安全问题

### 2.1 配置文件敏感信息泄露 ⚠️

#### 2.1.1 application.yml
**位置**: `opc-admin/src/main/resources/application.yml`

**问题**:
```yaml
# Line 99 - 邮箱密码明文存储
password: nnrjhormkzhnciee

# Line 146 - JWT密钥过于简单
secret: abcdefghijklmnopqrstuvwxyz

# Line 156 - 会员JWT密钥
secret: memberSecretKeyForJwtTokenGeneration2024

# Line 162 - 跳过邮箱验证码（开发环境配置）
skipEmailCode: true
```

#### 2.1.2 application-twitter.yml
**位置**: `opc-admin/src/main/resources/application-twitter.yml`

**问题**:
```yaml
# Line 4 - Twitter API Token明文存储
bearer-token: AAAAAAAAAAAAAAAAAAAAAPqP8gEAAAAA9kijiLWK2xqUVSFZibEFfna2gGs%3DWilvmCsBgPNAnVtDGB1Oi1scjnvIu7W3AcNYqzdn6NJVVrajC2
```

#### 2.1.3 application-ai.yml
**位置**: `opc-admin/src/main/resources/application-ai.yml`

**问题**:
```yaml
# Line 7 - xAI API Key明文存储
api-key: xai-Mu1EVXMbfeiEPToexIUcnaoaIlvpm4xmwc8apW2FuZbBiYAB14UFUA0Gya30jG6bzqCQVF5xmJorsPJR

# Line 17 - Minimax API Key明文存储
api-key: sk-api-TWPBVEDtyvfFqjCk2Vn5K74CJM6KBWxCdPpVOup6GwI89fgS6snAKg5XkCtRxlpB9DRB-j4SXpxWK3Amh7MvHi3aWfMAHUBCQ-TCaJE4rmXtqKnmFW1Oskc
```

#### 2.1.4 application-druid.yml
**位置**: `opc-admin/src/main/resources/application-druid.yml`

**问题**:
```yaml
# Line 13 - 数据库密码
password: qazwsxedc

# Line 53 - Druid监控登录密码
login-password: 123456
```

**修复建议**:
1. 使用环境变量或外部配置中心（如Nacos、Apollo）
2. 使用加密工具（如Jasypt）加密敏感配置
3. 禁止将生产环境密钥提交到代码仓库

---

### 2.2 SQL关键字过滤分析

#### 2.2.1 SqlUtil.java
**位置**: `opc-common/src/main/java/com/opc/common/utils/sql/SqlUtil.java`

**当前过滤规则**:
```java
public static String SQL_REGEX = "\u000B|and |extractvalue|updatexml|sleep|information_schema|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |union |like |+|/*|user()";
```

**问题**:
1. 过滤规则使用字符串匹配，容易被绕过（如使用注释 `/**/and/**/`）
2. 缺少对 `;` 分号的过滤
3. 缺少对 `--` 注释的过滤
4. 缺少对十六进制编码的过滤

**改进建议**:
```java
// 建议使用更严格的正则表达式或白名单机制
// 对于createTable功能，应该：
// 1. 只允许CREATE TABLE语句
// 2. 使用白名单验证表名、字段名
// 3. 禁止所有其他SQL操作
```

---

### 2.3 文件上传安全

#### 2.3.1 FileUpload/index.vue
**位置**: `opc-admin-ui/src/components/FileUpload/index.vue`

**安全措施**:
- ✅ 文件类型限制（白名单）
- ✅ 文件大小限制
- ✅ 文件名特殊字符检查（禁止逗号）

**潜在风险**:
1. 文件类型检查在前端，可以被绕过
2. 需要配合后端进行文件类型校验

---

### 2.4 XSS防护

#### 2.4.1 application.yml配置
```yaml
# Line 207-213
xss:
  enabled: true
  excludes: /system/notice
  urlPatterns: /system/*,/monitor/*,/tool/*
```

**分析**:
- ✅ 启用了XSS过滤
- ⚠️ `/system/notice` 被排除在过滤之外，需要注意该接口的输入处理

---

### 2.5 认证与授权

#### 2.5.1 登录安全
**位置**: `SysLoginService.java`

**安全措施**:
- ✅ 验证码校验
- ✅ 密码长度校验
- ✅ IP黑名单校验
- ✅ 密码加密存储（使用Spring Security）

#### 2.5.2 Token配置
```yaml
# 系统Token - 7天有效期
token:
  secret: abcdefghij
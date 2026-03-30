# Twitter API v2 搜索功能实现规格

## Why
当前项目使用 opencli 命令行工具抓取 Twitter 数据，但这种方式依赖外部工具且不够稳定。用户提供了 Twitter API v2 的 Bearer Token (`AAAAAAAAAAAAAAAAAAAAAPqP8gEAAAAA9kijiLWK2xqUVSFZibEFfna2gGs%3DWilvmCsBgPNAnVtDGB1Oi1scjnvIu7W3AcNYqzdn6NJVVrajC2`)，需要直接调用 `https://api.x.com/2/tweets/search/recent` 接口实现推文搜索功能，提高数据获取的稳定性和可靠性。

## What Changes
- **新增** `TwitterApiV2Service` 服务类：封装 Twitter API v2 调用逻辑
- **新增** `TwitterApiV2Controller` 控制器：提供 REST API 端点
- **新增** `TwitterApiV2Config` 配置类：管理 API 密钥和配置
- **新增** DTO 类：处理 API 请求和响应数据
- **新增** `application-twitter.yml` 配置文件：存储 Bearer Token 等配置
- **修改** 现有的 `TwitterImportController`：添加使用 API v2 的选项

## Impact
- 新增能力：直接通过 Twitter API v2 搜索最近 7 天的推文
- 受影响代码：opc-admin 模块
- 依赖变化：需要添加 HTTP 客户端依赖（如 OkHttp 或 WebClient）
- 配置变化：需要配置 Twitter Bearer Token

## ADDED Requirements

### Requirement: Twitter API v2 搜索功能
系统 SHALL 提供通过 Twitter API v2 搜索最近推文的功能。

#### Scenario: 成功搜索
- **GIVEN** 用户提供了有效的搜索关键词
- **WHEN** 调用 `/core/twitter/api-v2/search` 接口
- **THEN** 系统 SHALL 返回匹配的推文列表，并将数据导入到 CoreMaterial 表

#### Scenario: 带分页的搜索
- **GIVEN** 用户提供了搜索关键词和分页参数（max_results, next_token）
- **WHEN** 调用搜索接口
- **THEN** 系统 SHALL 返回指定数量的推文，并提供下一页令牌

#### Scenario: 搜索参数配置
- **GIVEN** 用户需要指定推文字段和扩展
- **WHEN** 调用搜索接口时传入 tweet.fields 和 expansions 参数
- **THEN** 系统 SHALL 返回包含指定字段的推文数据

#### Scenario: 带时段的搜索
- **GIVEN** 用户提供了搜索关键词和时段参数（start_time, end_time）
- **WHEN** 调用搜索接口
- **THEN** 系统 SHALL 返回在指定时段内发布的推文

## API 规格

### 端点
```
GET /core/twitter/api-v2/search
```

### 请求参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| query | string | 是 | 搜索查询语句 |
| max_results | int | 否 | 返回结果数量（10-100，默认 10）|
| next_token | string | 否 | 分页令牌 |
| tweet.fields | string | 否 | 返回的推文字段，逗号分隔 |
| expansions | string | 否 | 扩展数据，如 author_id |
| start_time | string | 否 | 开始时间（ISO 8601格式，如 2024-01-01T00:00:00Z）|
| end_time | string | 否 | 结束时间（ISO 8601格式，如 2024-01-31T23:59:59Z）|

### 响应格式
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "tweets": [...],
    "meta": {
      "result_count": 10,
      "next_token": "..."
    }
  }
}
```

## 配置要求
```yaml
twitter:
  api:
    bearer-token: AAAAAAAAAAAAAAAAAAAAAPqP8gEAAAAA9kijiLWK2xqUVSFZibEFfna2gGs%3DWilvmCsBgPNAnVtDGB1Oi1scjnvIu7W3AcNYqzdn6NJVVrajC2
    base-url: https://api.x.com/2
    timeout: 30000
```

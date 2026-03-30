# 验收检查清单

- [x] `TwitterApiV2Properties` 配置类正确读取 Bearer Token
- [x] `application-twitter.yml` 配置文件存在且配置正确
- [x] DTO 类（TwitterSearchRequestDTO, TwitterSearchResponseDTO, TweetDTO）定义完整
- [x] `TwitterApiV2Service` 接口和实现类能够成功调用 Twitter API
- [x] `TwitterApiV2Controller` 提供 `/core/twitter/api-v2/search` 端点
- [x] 搜索功能支持 query, max_results, next_token, start_time, end_time 等参数
- [x] 搜索结果能够正确解析并导入到 CoreMaterial 表
- [x] HTTP 客户端依赖已添加到 pom.xml（使用 Spring Boot 内置 RestTemplate）
- [x] API 返回格式符合项目统一的 AjaxResult 规范
- [x] 错误处理机制完善，包含超时、认证失败等场景

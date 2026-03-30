# 实现任务列表

- [ ] Task 1: 创建配置类和配置文件
  - [ ] SubTask 1.1: 创建 `TwitterApiV2Properties` 配置属性类
  - [ ] SubTask 1.2: 创建 `application-twitter.yml` 配置文件，配置 Bearer Token
  - [ ] SubTask 1.3: 在主配置中引入 twitter 配置

- [ ] Task 2: 创建 DTO 类
  - [ ] SubTask 2.1: 创建 `TwitterSearchRequestDTO` 搜索请求 DTO（包含 startTime, endTime 时段参数）
  - [ ] SubTask 2.2: 创建 `TwitterSearchResponseDTO` 搜索响应 DTO
  - [ ] SubTask 2.3: 创建 `TweetDTO` 推文数据 DTO

- [ ] Task 3: 创建服务类
  - [ ] SubTask 3.1: 创建 `TwitterApiV2Service` 接口
  - [ ] SubTask 3.2: 实现 `TwitterApiV2ServiceImpl`，包含 HTTP 调用逻辑
  - [ ] SubTask 3.3: 添加错误处理和重试机制

- [ ] Task 4: 创建控制器
  - [ ] SubTask 4.1: 创建 `TwitterApiV2Controller` 控制器类
  - [ ] SubTask 4.2: 实现搜索端点 `/core/twitter/api-v2/search`
  - [ ] SubTask 4.3: 实现导入端点，将搜索结果保存到 CoreMaterial

- [ ] Task 5: 添加 HTTP 客户端依赖
  - [ ] SubTask 5.1: 在 pom.xml 中添加 OkHttp 或 WebClient 依赖

- [ ] Task 6: 测试验证
  - [ ] SubTask 6.1: 测试 API 连接和认证
  - [ ] SubTask 6.2: 测试搜索功能
  - [ ] SubTask 6.3: 测试数据导入功能

# 任务依赖
- Task 2 依赖 Task 1
- Task 3 依赖 Task 2
- Task 4 依赖 Task 3
- Task 6 依赖 Task 4 和 Task 5

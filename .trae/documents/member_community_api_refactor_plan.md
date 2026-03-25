# 会员端社区接口重构计划

## 目标
合并会员端社区相关接口，统一使用POST请求

## 当前接口状态

### 想去相关接口（需要合并）
- `POST /want/{communityId}/{memberId}` - 标记想去
- `DELETE /want/{communityId}/{memberId}` - 取消想去
- `GET /want/check/{communityId}/{memberId}` - 检查是否已标记（需要删除）

### 去过相关接口（需要合并）
- `POST /visited/{communityId}/{memberId}` - 标记去过
- `DELETE /visited/{communityId}/{memberId}` - 取消去过
- `GET /visited/check/{communityId}/{memberId}` - 检查是否已标记（需要删除）

### 评价相关接口（需要合并）
- `POST /review` - 提交评价
- `PUT /review` - 修改评价
- `DELETE /review/{id}` - 删除评价
- `GET /review/my/{communityId}/{memberId}` - 获取我的评价
- `GET /review/list/{communityId}` - 获取社区评价列表

## 重构后的接口

### 1. 想去/取消想去（合并为一个POST接口）
```
POST /member/community/want/{communityId}/{memberId}
```
- 逻辑：检查是否已标记，已标记则取消，未标记则标记
- 返回：标记成功/取消成功的消息

### 2. 去过/取消去过（合并为一个POST接口）
```
POST /member/community/visited/{communityId}/{memberId}
```
- 逻辑：检查是否已标记，已标记则取消，未标记则标记
- 返回：标记成功/取消成功的消息

### 3. 提交/修改评价（合并为一个POST接口）
```
POST /member/community/review
```
- 逻辑：请求体中有id则修改，无id则新增
- 评分限制：1-5分
- 返回：提交成功/修改成功的消息

### 4. 删除评价（改为POST）
```
POST /member/community/review/delete/{id}
```

### 5. 保留的GET接口（查询类）
- `GET /member/community/list` - 社区列表查询
- `GET /member/community/{id}` - 社区详情

## 实施步骤

1. 修改 MemberCommunityController.java
   - 合并想去接口为单个POST方法
   - 合并去过接口为单个POST方法
   - 合并评价提交/修改为单个POST方法
   - 删除检查接口
   - 将删除评价改为POST请求

2. 编译验证
   - 确保代码编译通过

## 文件变更
- `d:\opc\opc-admin\src\main\java\com\opc\web\controller\core\MemberCommunityController.java`

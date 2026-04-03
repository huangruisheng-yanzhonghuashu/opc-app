<template>
   <div class="app-container">
      <div class="material-tab">
         <el-radio-group v-model="activeTab" @change="handleTabChange" class="tab-group">
            <el-radio-button value="0">
               <el-icon><Sunrise /></el-icon>
               晨报
            </el-radio-button>
            <el-radio-button value="1">
               <el-icon><User /></el-icon>
               普通素材
            </el-radio-button>
            <el-radio-button value="2">
               <el-icon><Star /></el-icon>
               VIP素材
            </el-radio-button>
            <el-radio-button value="3">
               <el-icon><Medal /></el-icon>
               超级VIP
            </el-radio-button>
         </el-radio-group>
      </div>

      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="标题" prop="title">
            <el-input
               v-model="queryParams.title"
               placeholder="请输入标题"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="作者" prop="author">
            <el-input
               v-model="queryParams.author"
               placeholder="请输入作者"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 200px">
               <el-option label="上线" value="0" />
               <el-option label="下线" value="1" />
            </el-select>
         </el-form-item>
         <el-form-item label="首页Tab" prop="tagIds">
            <el-select
               v-model="queryParams.tagIds"
               multiple
               collapse-tags
               collapse-tags-tooltip
               placeholder="请选择Tab"
               clearable
               style="width: 200px"
            >
               <el-option
                  v-for="tag in tagOptions"
                  :key="tag.id"
                  :label="tag.tagName"
                  :value="tag.id"
               >
                  <span class="tag-option">
                     <span class="tag-color-dot" :style="{ backgroundColor: tag.tagColor || '#909399' }"></span>
                     <span>{{ tag.tagName }}</span>
                  </span>
               </el-option>
            </el-select>
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button
               type="primary"
               plain
               icon="Plus"
               @click="handleAdd"
               v-hasPermi="['core:material:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:material:remove']"
            >删除</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange" style="width: 100%">
         <el-table-column type="selection" width="55" align="center" fixed="left" />
         <el-table-column label="素材ID" align="center" prop="id" width="80" />
         <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) || '-' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" min-width="200">
            <template #default="scope">
               <el-button link type="primary" @click="handleView(scope.row)" style="max-width: 100%; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{ scope.row.title }}</el-button>
            </template>
         </el-table-column>
         <el-table-column label="作者" align="center" prop="author" width="120" />
         <el-table-column label="首页Tab" align="center" width="150" :show-overflow-tooltip="false">
            <template #default="scope">
               <div v-if="scope.row.tags && scope.row.tags.length > 0" class="tag-list" style="display: flex; flex-wrap: nowrap; overflow: hidden; justify-content: center;">
                  <span
                     v-for="tag in scope.row.tags.slice(0, 2)"
                     :key="tag.id"
                     :style="{ backgroundColor: tag.tagColor || '#909399', color: getContrastTextColor(tag.tagColor), padding: '2px 6px', borderRadius: '4px', fontSize: '11px', marginRight: '2px', display: 'inline-block', lineHeight: '1.4', whiteSpace: 'nowrap', flexShrink: 0 }"
                  >
                     {{ tag.tagName }}
                  </span>
                  <span v-if="scope.row.tags.length > 2" style="font-size: 11px; color: #666; white-space: nowrap;">+{{ scope.row.tags.length - 2 }}</span>
               </div>
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="内容标签" align="center" width="150" :show-overflow-tooltip="false">
            <template #default="scope">
               <div v-if="scope.row.tags2 && scope.row.tags2.length > 0" class="tag-list" style="display: flex; flex-wrap: nowrap; overflow: hidden; justify-content: center;">
                  <span
                     v-for="tag in scope.row.tags2.slice(0, 2)"
                     :key="tag.id"
                     :style="{ backgroundColor: tag.tagColor || '#409EFF', color: getContrastTextColor(tag.tagColor || '#409EFF'), padding: '2px 6px', borderRadius: '4px', fontSize: '11px', marginRight: '2px', display: 'inline-block', lineHeight: '1.4', whiteSpace: 'nowrap', flexShrink: 0 }"
                  >
                     {{ tag.tagName }}
                  </span>
                  <span v-if="scope.row.tags2.length > 2" style="font-size: 11px; color: #666; white-space: nowrap;">+{{ scope.row.tags2.length - 2 }}</span>
               </div>
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="套餐分类" align="center" prop="packageType" width="100">
            <template #default="scope">
               <span>{{ getPackageTypeLabel(scope.row.packageType) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="素材类型" align="center" prop="materialType" width="100">
            <template #default="scope">
               <el-tag v-if="scope.row.materialType" :type="scope.row.materialType === 'post' ? 'primary' : 'success'">
                  {{ scope.row.materialType === 'post' ? '帖子' : '文章' }}
               </el-tag>
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="期数" align="center" prop="issueNo" width="80">
            <template #default="scope">
               <span>{{ scope.row.issueNo || 0 }}</span>
            </template>
         </el-table-column>
         <el-table-column label="来源" align="center" prop="source" width="80">
            <template #default="scope">
               <span>{{ getSourceLabel(scope.row.source) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
                  {{ scope.row.status === '0' ? '上线' : '下线' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="置顶" align="center" prop="isTop" width="80">
            <template #default="scope">
               <el-tag :type="scope.row.isTop === '1' ? 'warning' : 'info'">
                  {{ scope.row.isTop === '1' ? '是' : '否' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="查看数" align="center" prop="viewCount" width="80">
            <template #default="scope">
               <span>{{ scope.row.viewCount || 0 }}</span>
            </template>
         </el-table-column>
         <el-table-column label="点赞数" align="center" prop="likeCount" width="80">
            <template #default="scope">
               <span>{{ scope.row.likeCount || 0 }}</span>
            </template>
         </el-table-column>
         <el-table-column label="不喜欢数" align="center" prop="dislikeCount" width="90">
            <template #default="scope">
               <span>{{ scope.row.dislikeCount || 0 }}</span>
            </template>
         </el-table-column>
         <el-table-column label="上线时间" align="center" prop="onlineTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.onlineTime) || '-' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="下线时间" align="center" prop="offlineTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.offlineTime) || '-' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="置顶时间" align="center" prop="topTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.topTime) || '-' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="取消置顶时间" align="center" prop="untopTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.untopTime) || '-' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width" fixed="right">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:material:query']">详情</el-button>
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:material:edit']">编辑</el-button>
               <el-button
                  link
                  :type="scope.row.status === '0' ? 'danger' : 'success'"
                  :icon="scope.row.status === '0' ? 'CircleClose' : 'CircleCheck'"
                  @click="handleChangeStatus(scope.row)"
                  v-hasPermi="['core:material:changeStatus']"
               >{{ scope.row.status === '0' ? '下线' : '上线' }}</el-button>
               <el-button
                  link
                  :type="scope.row.isTop === '1' ? 'warning' : 'primary'"
                  :icon="scope.row.isTop === '1' ? 'Top' : 'Rank'"
                  @click="handleChangeTop(scope.row)"
                  v-hasPermi="['core:material:changeTop']"
               >{{ scope.row.isTop === '1' ? '取消置顶' : '置顶' }}</el-button>
            </template>
         </el-table-column>
      </el-table>

      <pagination
         v-show="total > 0"
         :total="total"
         v-model:page="queryParams.pageNum"
         v-model:limit="queryParams.pageSize"
         @pagination="getList"
      />

      <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
         <el-form ref="materialRef" :model="form" :rules="rules" label-width="100px">
            <!-- 标题：整行显示 -->
            <el-form-item label="标题" prop="title">
               <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="作者" prop="author">
                     <el-input v-model="form.author" placeholder="请输入作者（注明出处）" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="来源" prop="source">
                     <el-select v-model="form.source" placeholder="请选择来源" style="width: 100%">
                        <el-option label="手动" value="manual" />
                        <el-option label="推特" value="twitter" />
                     </el-select>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="套餐分类" prop="packageType">
                     <el-select v-model="form.packageType" placeholder="请选择套餐分类" style="width: 100%" @change="handlePackageTypeChange">
                        <el-option label="晨报" :value="0" />
                        <el-option label="普通素材" :value="1" />
                        <el-option label="VIP素材" :value="2" />
                        <el-option label="超级VIP" :value="3" />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="内容类型" prop="contentType">
                     <el-select v-model="form.contentType" placeholder="请选择内容类型" style="width: 100%">
                        <el-option label="纯文本" value="text" />
                        <el-option label="图文" value="image" />
                        <el-option label="视频" value="video" />
                     </el-select>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="素材类型" prop="materialType">
                     <el-select v-model="form.materialType" placeholder="请选择素材类型" style="width: 100%">
                        <el-option label="帖子" value="post" />
                        <el-option label="文章" value="article" />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="期数" prop="issueNo">
                     <el-input-number v-model="form.issueNo" :min="0" :max="99999" placeholder="请输入期数" style="width: 100%" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row v-if="form.packageType === 2 || form.packageType === 3">
               <el-col :span="24">
                  <el-form-item label="二级分类" prop="categoryId">
                     <div style="display: flex; gap: 8px; width: calc(100% - 100px);">
                        <el-select v-model="form.categoryId" placeholder="请选择二级分类" style="flex: 1;" clearable>
                           <el-option
                              v-for="category in categoryOptions"
                              :key="category.id"
                              :label="category.categoryName"
                              :value="category.id"
                           />
                        </el-select>
                        <el-button type="primary" plain @click="handleManageCategory">管理</el-button>
                     </div>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="状态" prop="status">
                     <el-radio-group v-model="form.status">
                        <el-radio label="0">上线</el-radio>
                        <el-radio label="1">下线</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-form-item label="首页Tab" prop="tagIds">
               <el-select
                  v-model="form.tagIds"
                  multiple
                  collapse-tags
                  collapse-tags-tooltip
                  placeholder="请选择标签"
                  style="width: 100%"
               >
                  <el-option
                     v-for="tag in tagOptions"
                     :key="tag.id"
                     :label="tag.tagName"
                     :value="tag.id"
                  >
                     <span class="tag-option">
                        <span class="tag-color-dot" :style="{ backgroundColor: tag.tagColor || '#909399' }"></span>
                        <span>{{ tag.tagName }}</span>
                     </span>
                  </el-option>
               </el-select>
            </el-form-item>
            <!-- 正文编辑器 -->
            <!-- 根据素材类型选择不同的编辑器 -->
            <el-form-item label="正文" prop="content" v-if="form.materialType === 'post'">
               <el-input v-model="form.content" type="textarea" placeholder="请输入正文" :rows="6" />
            </el-form-item>
            <el-form-item label="正文" prop="content" v-else-if="form.materialType === 'article'">
               <Editor v-model="form.content" :min-height="300" :key="'editor-article-' + form.id" />
            </el-form-item>
            <el-form-item label="正文" prop="content" v-else>
               <Editor v-model="form.content" :min-height="300" :key="'editor-other-' + form.id" />
            </el-form-item>
            <!-- 帖子类型：图文显示图片上传（在正文后面） -->
            <el-form-item label="图片" prop="mediaList" v-if="form.materialType === 'post' && form.contentType === 'image'">
               <div class="media-upload-container">
                  <div v-for="(media, index) in form.mediaList.filter(m => m.mediaType === 'image')" :key="index" class="media-upload-item">
                     <div class="media-preview">
                        <el-image :src="media.fileUrl" fit="cover" style="width: 100%; height: 100%;" />
                     </div>
                     <div class="media-actions">
                        <el-button link type="danger" icon="Delete" size="small" @click="removeMedia(form.mediaList.indexOf(media))">删除</el-button>
                        <el-button link type="primary" icon="Top" size="small" @click="moveMedia(form.mediaList.indexOf(media), -1)" :disabled="index === 0">上移</el-button>
                        <el-button link type="primary" icon="Bottom" size="small" @click="moveMedia(form.mediaList.indexOf(media), 1)" :disabled="index === form.mediaList.filter(m => m.mediaType === 'image').length - 1">下移</el-button>
                     </div>
                  </div>
                  <div class="media-upload-add">
                     <el-upload
                        class="media-uploader"
                        :action="uploadUrl"
                        :headers="uploadHeaders"
                        :show-file-list="false"
                        :on-success="(res, file) => handleMediaUploadSuccess(res, file, 'image')"
                        :before-upload="(file) => beforeMediaUpload(file, 'image')"
                        accept="image/*"
                     >
                        <el-button type="primary" plain icon="Plus">添加图片</el-button>
                     </el-upload>
                  </div>
               </div>
            </el-form-item>
            <!-- 帖子类型：视频显示视频上传（在正文后面） -->
            <el-form-item label="视频" prop="mediaList" v-if="form.materialType === 'post' && form.contentType === 'video'">
               <div class="media-upload-container">
                  <div v-for="(media, index) in form.mediaList.filter(m => m.mediaType === 'video')" :key="index" class="media-upload-item">
                     <div class="media-preview">
                        <video :src="media.fileUrl" controls style="width: 100%; height: 100%;" />
                     </div>
                     <div class="media-actions">
                        <el-button link type="danger" icon="Delete" size="small" @click="removeMedia(form.mediaList.indexOf(media))">删除</el-button>
                     </div>
                  </div>
                  <div class="media-upload-add" v-if="form.mediaList.filter(m => m.mediaType === 'video').length === 0">
                     <el-upload
                        class="media-uploader"
                        :action="uploadUrl"
                        :headers="uploadHeaders"
                        :show-file-list="false"
                        :on-success="(res, file) => handleMediaUploadSuccess(res, file, 'video')"
                        :before-upload="(file) => beforeMediaUpload(file, 'video')"
                        accept="video/*"
                     >
                        <el-button type="primary" plain icon="Plus">添加视频</el-button>
                     </el-upload>
                  </div>
               </div>
            </el-form-item>
            <el-form-item label="封面图" prop="coverImage">
               <image-upload v-model="form.coverImage" :limit="1" />
            </el-form-item>
            <!-- 文章类型：视频显示视频上传 -->
            <el-form-item label="视频上传" prop="videoUrl" v-if="form.materialType === 'article' && form.contentType === 'video'">
               <file-upload v-model="form.videoUrl" :limit="1" :file-size="500" :file-type="['mp4', 'avi', 'mov', 'wmv', 'flv', 'mkv']" :is-full-url="true" />
            </el-form-item>
            <el-form-item label="总结" prop="summary">
               <el-input v-model="form.summary" type="textarea" placeholder="请输入总结" :rows="2" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="2" />
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>

      <el-dialog title="素材详情" v-model="detailOpen" width="800px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData" style="table-layout: fixed; width: 100%;">
            <el-descriptions-item label="素材ID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="作者" :span="1">{{ detailData.author || '-' }}</el-descriptions-item>
            <el-descriptions-item label="来源" :span="1">
               <span>{{ getSourceLabel(detailData.source) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="素材类型" :span="1">
               <el-tag :type="detailData.materialType === 'post' ? 'primary' : 'success'">
                  {{ detailData.materialType === 'post' ? '帖子' : '文章' }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="原ID" :span="1">{{ detailData.originalId || '-' }}</el-descriptions-item>
            <el-descriptions-item label="套餐分类" :span="1">{{ getPackageTypeLabel(detailData.packageType) }}</el-descriptions-item>
            <el-descriptions-item label="期数" :span="1">{{ detailData.issueNo || 0 }}</el-descriptions-item>
            <el-descriptions-item label="内容类型" :span="1">
               <span>{{ getContentTypeLabel(detailData.contentType) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="状态" :span="1">
               <el-tag :type="detailData.status === '0' ? 'success' : 'danger'">
                  {{ detailData.status === '0' ? '上线' : '下线' }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="置顶" :span="1">
               <el-tag :type="detailData.isTop === '1' ? 'warning' : 'info'">
                  {{ detailData.isTop === '1' ? '是' : '否' }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="首页Tab" :span="2">
               <div v-if="detailData.tags && detailData.tags.length > 0" class="detail-tag-list">
                  <el-tag 
                     v-for="tag in detailData.tags" 
                     :key="tag.id"
                     :style="{ backgroundColor: tag.tagColor, color: '#fff', borderColor: tag.tagColor }"
                     class="detail-tag"
                  >
                     {{ tag.tagName }}
                  </el-tag>
               </div>
               <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="内容标签" :span="2">
               <div v-if="detailData.tags2 && detailData.tags2.length > 0" class="detail-tag-list">
                  <el-tag 
                     v-for="tag in detailData.tags2" 
                     :key="tag.id"
                     :style="{ backgroundColor: tag.tagColor || '#409EFF', color: '#fff', borderColor: tag.tagColor || '#409EFF' }"
                     class="detail-tag"
                     type="success"
                  >
                     {{ tag.tagName }}
                  </el-tag>
               </div>
               <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="原链接" :span="2">
               <a v-if="detailData.originalUrl" :href="detailData.originalUrl" target="_blank">{{ detailData.originalUrl }}</a>
               <span v-else>-</span>
            </el-descriptions-item>
            <!-- 标题：整行显示，放在正文上面 -->
            <el-descriptions-item label="标题" :span="2">
               <div style="font-weight: bold; font-size: 16px; max-width: 600px; word-break: break-all;">{{ detailData.title }}</div>
            </el-descriptions-item>
            <!-- 正文 -->
            <el-descriptions-item label="正文" :span="2">
               <div v-if="detailData.content" style="max-height: 400px; max-width: 100%; width: 700px; overflow-y: auto; overflow-x: hidden; word-break: break-all; border: 1px solid #e4e7ed; padding: 10px; border-radius: 4px; box-sizing: border-box;" v-html="detailData.content"></div>
               <span v-else>-</span>
            </el-descriptions-item>
            <!-- 媒体文件：放在正文后面 -->
            <!-- 帖子类型：根据contentType过滤显示，图文只显示图片，视频只显示视频 -->
            <el-descriptions-item label="媒体文件" :span="2" v-if="detailData.materialType === 'post' && detailData.contentType === 'image' && detailMediaList && detailMediaList.filter(m => m.mediaType === 'image').length > 0">
               <div class="media-list">
                  <div v-for="(media, index) in detailMediaList.filter(m => m.mediaType === 'image')" :key="media.id" class="media-item">
                     <div class="media-index">{{ index + 1 }}</div>
                     <div class="media-content">
                        <el-image 
                           :src="media.fileUrl" 
                           style="max-width: 150px; max-height: 150px;" 
                           fit="cover"
                           :preview-src-list="detailMediaList.filter(m => m.mediaType === 'image').map(m => m.fileUrl)"
                           :initial-index="detailMediaList.filter(m => m.mediaType === 'image').indexOf(media)"
                        />
                     </div>
                     <div class="media-info">
                        <el-tag size="small" type="success">图片</el-tag>
                        <span class="media-sort">排序: {{ media.sortOrder }}</span>
                     </div>
                  </div>
               </div>
            </el-descriptions-item>
            <el-descriptions-item label="媒体文件" :span="2" v-if="detailData.materialType === 'post' && detailData.contentType === 'video' && detailMediaList && detailMediaList.filter(m => m.mediaType === 'video').length > 0">
               <div class="media-list">
                  <div v-for="(media, index) in detailMediaList.filter(m => m.mediaType === 'video')" :key="media.id" class="media-item">
                     <div class="media-index">{{ index + 1 }}</div>
                     <div class="media-content">
                        <video 
                           controls 
                           style="max-width: 300px; max-height: 200px;"
                        >
                           <source :src="media.fileUrl" type="video/mp4">
                           您的浏览器不支持视频播放
                        </video>
                     </div>
                     <div class="media-info">
                        <el-tag size="small" type="warning">视频</el-tag>
                        <span class="media-sort">排序: {{ media.sortOrder }}</span>
                     </div>
                  </div>
               </div>
            </el-descriptions-item>
            <!-- 封面图：放在媒体后面 -->
            <el-descriptions-item label="封面图" :span="2">
               <el-image v-if="detailData.coverImage" :src="detailData.coverImage" style="max-width: 200px; max-height: 200px;" fit="cover" />
               <span v-else>-</span>
            </el-descriptions-item>
            <!-- 文章类型的视频 -->
            <el-descriptions-item label="视频" :span="2" v-if="detailData.materialType === 'article' && detailData.contentType === 'video'">
               <video v-if="detailData.videoUrl" controls style="max-width: 100%; max-height: 400px;">
                  <source :src="detailData.videoUrl" type="video/mp4">
                  您的浏览器不支持视频播放
               </video>
               <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="总结" :span="2">{{ detailData.summary || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间" :span="1">{{ parseTime(detailData.createTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="更新时间" :span="1">{{ parseTime(detailData.updateTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
            <el-descriptions-item label="上线时间" :span="1">{{ parseTime(detailData.onlineTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="下线时间" :span="1">{{ parseTime(detailData.offlineTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="置顶时间" :span="1">{{ parseTime(detailData.topTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="取消置顶时间" :span="1">{{ parseTime(detailData.untopTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="回复数" :span="1">{{ detailData.replyCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="点赞数" :span="1">{{ detailData.likeCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="查看数" :span="1">{{ detailData.viewCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="不喜欢数" :span="1">{{ detailData.dislikeCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="转发数" :span="1">{{ detailData.shareCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="评论数" :span="1">{{ detailData.commentCount || 0 }}</el-descriptions-item>
         </el-descriptions>
         <template #footer>
            <div class="dialog-footer">
               <el-button @click="detailOpen = false">关 闭</el-button>
            </div>
         </template>
      </el-dialog>

      <!-- 二级分类管理弹窗 -->
      <el-dialog :title="categoryTitle" v-model="categoryOpen" width="900px" append-to-body :close-on-click-modal="false">
         <el-form :model="categoryQueryParams" ref="categoryQueryRef" :inline="true" v-show="true">
            <el-form-item label="分类名称" prop="categoryName">
               <el-input
                  v-model="categoryQueryParams.categoryName"
                  placeholder="请输入分类名称"
                  clearable
                  style="width: 180px"
                  @keyup.enter="handleCategoryQuery"
               />
            </el-form-item>
            <el-form-item label="套餐分类" prop="packageType">
               <el-select v-model="categoryQueryParams.packageType" placeholder="全部" clearable style="width: 120px">
                  <el-option label="VIP素材" :value="2" />
                  <el-option label="超级VIP" :value="3" />
               </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
               <el-select v-model="categoryQueryParams.status" placeholder="全部" clearable style="width: 100px">
                  <el-option label="正常" value="0" />
                  <el-option label="停用" value="1" />
               </el-select>
            </el-form-item>
            <el-form-item>
               <el-button type="primary" icon="Search" @click="handleCategoryQuery">搜索</el-button>
               <el-button icon="Refresh" @click="resetCategoryQuery">重置</el-button>
            </el-form-item>
         </el-form>

         <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
               <el-button
                  type="primary"
                  plain
                  icon="Plus"
                  @click="handleCategoryAdd"
               >新增</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="danger"
                  plain
                  icon="Delete"
                  :disabled="categoryMultiple"
                  @click="handleCategoryDelete"
               >删除</el-button>
            </el-col>
         </el-row>

         <el-table v-loading="categoryLoading" :data="categoryList" @selection-change="handleCategorySelectionChange" height="300">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="分类ID" align="center" prop="id" width="70" />
            <el-table-column label="分类名称" align="center" prop="categoryName" />
            <el-table-column label="套餐分类" align="center" prop="packageType" width="90">
               <template #default="scope">
                  <el-tag v-if="scope.row.packageType === 2" type="success" size="small">VIP素材</el-tag>
                  <el-tag v-else-if="scope.row.packageType === 3" type="warning" size="small">超级VIP</el-tag>
                  <span v-else>-</span>
               </template>
            </el-table-column>
            <el-table-column label="排序" align="center" prop="sortOrder" width="60" />
            <el-table-column label="状态" align="center" prop="status" width="70">
               <template #default="scope">
                  <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" size="small">
                     {{ scope.row.status === '0' ? '正常' : '停用' }}
                  </el-tag>
               </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center" class-name="small-padding fixed-width">
               <template #default="scope">
                  <el-button link type="primary" icon="Edit" size="small" @click="handleCategoryUpdate(scope.row)">编辑</el-button>
                  <el-button link type="danger" icon="Delete" size="small" @click="handleCategoryDelete(scope.row)">删除</el-button>
               </template>
            </el-table-column>
         </el-table>

         <pagination
            v-show="categoryTotal > 0"
            :total="categoryTotal"
            v-model:page="categoryQueryParams.pageNum"
            v-model:limit="categoryQueryParams.pageSize"
            @pagination="getCategoryList"
         />

         <!-- 二级分类表单 -->
         <el-divider content-position="left">{{ categoryForm.id ? '修改' : '新增' }}分类</el-divider>
         <el-form ref="categoryFormRef" :model="categoryForm" :rules="categoryRules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="分类名称" prop="categoryName">
                     <el-input v-model="categoryForm.categoryName" placeholder="请输入分类名称" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="套餐分类" prop="packageType">
                     <el-select v-model="categoryForm.packageType" placeholder="请选择套餐分类" style="width: 100%">
                        <el-option label="VIP素材" :value="2" />
                        <el-option label="超级VIP" :value="3" />
                     </el-select>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="排序" prop="sortOrder">
                     <el-input-number v-model="categoryForm.sortOrder" :min="0" :max="999" placeholder="请输入排序" style="width: 100%" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="状态" prop="status">
                     <el-radio-group v-model="categoryForm.status">
                        <el-radio label="0">正常</el-radio>
                        <el-radio label="1">停用</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="24">
                  <el-form-item label="备注" prop="remark">
                     <el-input v-model="categoryForm.remark" type="textarea" placeholder="请输入备注" :rows="2" />
                  </el-form-item>
               </el-col>
            </el-row>
         </el-form>

         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitCategoryForm">保 存</el-button>
               <el-button @click="cancelCategory">关 闭</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Material">
import { listMaterial, addMaterial, getMaterial, updateMaterial, delMaterial, changeMaterialStatus, changeMaterialTop, getMaterialMedia, saveMaterialMedia, deleteMaterialMedia } from "@/api/core/material"
import { getAllActiveTags } from "@/api/core/tag"
import { listCategoryByPackageType, listMaterialCategory, addMaterialCategory, getMaterialCategory, updateMaterialCategory, delMaterialCategory } from "@/api/core/materialCategory"
import { User, Star, Medal, Sunrise } from '@element-plus/icons-vue'
import { nextTick } from 'vue'
import Editor from "@/components/Editor/index.vue"
import FileUpload from "@/components/FileUpload/index.vue"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()

const materialList = ref([])
const open = ref(false)
const detailOpen = ref(false)
const categoryOpen = ref(false)
const categoryLoading = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const categoryTitle = ref("")
const activeTab = ref("0")
const detailData = ref({})
const detailMediaList = ref([])
const tagOptions = ref([])
const categoryOptions = ref([])
const categoryList = ref([])
const categoryTotal = ref(0)
const categoryIds = ref([])
const categorySingle = ref(true)
const categoryMultiple = ref(true)

// 上传相关
const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + '/common/uploadToServer')
const uploadHeaders = ref({
  Authorization: 'Bearer ' + getToken()
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    author: undefined,
    status: undefined,
    tagIds: []
  },
  rules: {
    title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
  }
})

const categoryData = reactive({
  categoryForm: {},
  categoryQueryParams: {
    pageNum: 1,
    pageSize: 10,
    categoryName: undefined,
    packageType: undefined,
    status: undefined
  },
  categoryRules: {
    categoryName: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
    packageType: [{ required: true, message: "套餐分类不能为空", trigger: "change" }],
    sortOrder: [{ required: true, message: "排序不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)
const { categoryQueryParams, categoryForm, categoryRules } = toRefs(categoryData)

function getPackageTypeLabel(packageType) {
  if (packageType === 0) return '晨报'
  if (packageType === 1) return '普通素材'
  if (packageType === 2) return 'VIP素材'
  if (packageType === 3) return '超级VIP'
  return '-'
}

function getContentTypeLabel(contentType) {
  const map = {
    'text': '纯文本',
    'image': '图文',
    'video': '视频'
  }
  return map[contentType] || contentType || '-'
}

function getSourceLabel(source) {
  const map = {
    'crawler': '爬取',
    'manual': '手动',
    'twitter': '推特'
  }
  return map[source] || source || '-'
}

// 根据背景色计算文字颜色（深色背景用白字，浅色背景用黑字）
function getContrastTextColor(bgColor) {
  if (!bgColor) return '#fff'
  // 将颜色转换为 RGB
  let r, g, b
  if (bgColor.startsWith('#')) {
    const hex = bgColor.replace('#', '')
    r = parseInt(hex.substr(0, 2), 16)
    g = parseInt(hex.substr(2, 2), 16)
    b = parseInt(hex.substr(4, 2), 16)
  } else if (bgColor.startsWith('rgb')) {
    const matches = bgColor.match(/\d+/g)
    if (matches) {
      r = parseInt(matches[0])
      g = parseInt(matches[1])
      b = parseInt(matches[2])
    }
  }
  if (r === undefined) return '#fff'
  // 计算亮度 (YIQ公式)
  const brightness = (r * 299 + g * 587 + b * 114) / 1000
  return brightness > 128 ? '#333' : '#fff'
}

// 根据套餐分类获取二级分类
function getCategoryOptions(packageType) {
  if (packageType === 2 || packageType === 3) {
    listCategoryByPackageType(packageType).then(response => {
      categoryOptions.value = response.data || []
    })
  } else {
    categoryOptions.value = []
  }
}

// 套餐分类改变时
function handlePackageTypeChange(packageType) {
  // 清空已选的二级分类
  form.value.categoryId = undefined
  getCategoryOptions(packageType)
}

// 打开二级分类管理弹窗
function handleManageCategory() {
  categoryOpen.value = true
  categoryTitle.value = "二级分类管理"
  getCategoryList()
}

// 查询二级分类列表
function getCategoryList() {
  categoryLoading.value = true
  listMaterialCategory(categoryQueryParams.value).then(response => {
    categoryList.value = response.data
    categoryTotal.value = response.total
    categoryLoading.value = false
  })
}

// 二级分类搜索
function handleCategoryQuery() {
  categoryQueryParams.value.pageNum = 1
  getCategoryList()
}

// 重置二级分类搜索
function resetCategoryQuery() {
  categoryQueryParams.value.categoryName = undefined
  categoryQueryParams.value.packageType = undefined
  categoryQueryParams.value.status = undefined
  handleCategoryQuery()
}

// 二级分类选择变化
function handleCategorySelectionChange(selection) {
  categoryIds.value = selection.map(item => item.id)
  categorySingle.value = selection.length != 1
  categoryMultiple.value = !selection.length
}

// 重置二级分类表单
function resetCategoryForm() {
  categoryForm.value = {
    id: undefined,
    categoryName: undefined,
    packageType: undefined,
    sortOrder: 0,
    status: '0',
    remark: undefined
  }
  proxy.resetForm("categoryFormRef")
}

// 取消二级分类弹窗
function cancelCategory() {
  categoryOpen.value = false
  resetCategoryForm()
}

// 新增二级分类
function handleCategoryAdd() {
  resetCategoryForm()
  categoryTitle.value = "添加二级分类"
  categoryForm.value.status = '0'
}

// 修改二级分类
function handleCategoryUpdate(row) {
  resetCategoryForm()
  const id = row.id || categoryIds.value[0]
  getMaterialCategory(id).then(response => {
    categoryForm.value = response.data
    categoryTitle.value = "修改二级分类"
  })
}

// 删除二级分类
function handleCategoryDelete(row) {
  const ids = row.id ? [row.id] : categoryIds.value
  proxy.$modal.confirm('是否确认删除二级分类编号为"' + ids + '"的数据项？').then(function() {
    return delMaterialCategory(ids)
  }).then(() => {
    getCategoryList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

// 提交二级分类表单
function submitCategoryForm() {
  proxy.$refs["categoryFormRef"].validate(valid => {
    if (valid) {
      if (categoryForm.value.id != undefined) {
        updateMaterialCategory(categoryForm.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          resetCategoryForm()
          getCategoryList()
          // 刷新当前素材表单的二级分类选项
          getCategoryOptions(form.value.packageType)
        })
      } else {
        addMaterialCategory(categoryForm.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          resetCategoryForm()
          getCategoryList()
          // 刷新当前素材表单的二级分类选项
          getCategoryOptions(form.value.packageType)
        })
      }
    }
  })
}

function getList() {
  loading.value = true
  listMaterial(queryParams.value).then(response => {
    materialList.value = response.data
    total.value = response.total
    loading.value = false
  })
}

function getTagOptions() {
  getAllActiveTags().then(response => {
    tagOptions.value = response.data || []
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    id: undefined,
    title: undefined,
    author: undefined,
    summary: undefined,
    content: undefined,
    originalUrl: undefined,
    originalId: undefined,
    replyCount: 0,
    likeCount: 0,
    dislikeCount: 0,
    viewCount: 0,
    shareCount: 0,
    commentCount: 0,
    contentType: 'text',
    packageType: 1,
    status: '0',
    isTop: '0',
    source: 'manual',
    materialType: 'post',
    issueNo: 0,
    coverImage: undefined,
    videoUrl: undefined,
    tagIds: [],
    mediaList: [],
    remark: undefined
  }
  proxy.resetForm("materialRef")
}

// 媒体文件上传相关方法
function handleMediaUploadSuccess(response, file, mediaType) {
  if (response.code !== 200) {
    proxy.$modal.msgError(response.msg || '上传失败')
    return
  }
  if (!form.value.mediaList) {
    form.value.mediaList = []
  }
  // 如果是视频类型，先清空列表（只能有一个视频）
  if (mediaType === 'video') {
    form.value.mediaList = form.value.mediaList.filter(m => m.mediaType !== 'video')
  }
  form.value.mediaList.push({
    mediaType: mediaType,
    fileUrl: response.url,
    status: '0'
  })
  proxy.$modal.msgSuccess('上传成功')
}

function beforeMediaUpload(file, mediaType) {
  const isImage = file.type.startsWith('image/')
  const isVideo = file.type.startsWith('video/')
  const isLt50M = file.size / 1024 / 1024 < 50

  if (mediaType === 'image' && !isImage) {
    proxy.$modal.msgError('只能上传图片文件!')
    return false
  }
  if (mediaType === 'video' && !isVideo) {
    proxy.$modal.msgError('只能上传视频文件!')
    return false
  }
  if (!isLt50M) {
    proxy.$modal.msgError('文件大小不能超过 50MB!')
    return false
  }
  return true
}

function removeMedia(index) {
  const media = form.value.mediaList[index]
  // 如果媒体有ID，则从服务器删除
  if (media.id) {
    proxy.$modal.confirm('确认要删除该媒体文件吗？').then(function() {
      return deleteMaterialMedia(media.id)
    }).then(() => {
      form.value.mediaList.splice(index, 1)
      proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {})
  } else {
    // 新上传的媒体，直接从列表中删除
    form.value.mediaList.splice(index, 1)
  }
}

function moveMedia(index, direction) {
  const list = form.value.mediaList
  const newIndex = index + direction
  if (newIndex < 0 || newIndex >= list.length) return
  const temp = list[index]
  list[index] = list[newIndex]
  list[newIndex] = temp
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.status = undefined
  queryParams.value.tagIds = []
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleTabChange(tabName) {
  queryParams.value.pageNum = 1
  queryParams.value.packageType = parseInt(tabName)
  getList()
}

function handleAdd() {
  reset()
  if (activeTab.value === '0') {
    form.value.packageType = 0
  } else if (activeTab.value === '1') {
    form.value.packageType = 1
  } else if (activeTab.value === '2') {
    form.value.packageType = 2
  } else {
    form.value.packageType = 3
  }
  getTagOptions()
  getCategoryOptions(form.value.packageType)
  open.value = true
  title.value = "添加素材"
}

function handleView(row) {
  getMaterial(row.id).then(response => {
    detailData.value = response.data
    // 加载素材媒体列表
    getMaterialMedia(row.id).then(mediaResponse => {
      detailMediaList.value = mediaResponse.data || []
    })
    detailOpen.value = true
  })
}

function handleUpdate(row) {
  const id = row.id || ids.value
  getTagOptions()
  getMaterial(id).then(response => {
    // 先获取数据
    const data = response.data
    // 确保materialType有默认值（兼容旧数据）
    if (data.materialType === undefined || data.materialType === null || data.materialType === '') {
      data.materialType = 'post'
    }
    // 确保issueNo有默认值
    if (data.issueNo === undefined || data.issueNo === null) {
      data.issueNo = 0
    }
    // 确保mediaList有默认值
    if (!data.mediaList) {
      data.mediaList = []
    }
    // 将标签转换为id数组
    if (data.tags && data.tags.length > 0) {
      data.tagIds = data.tags.map(tag => tag.id)
    } else {
      data.tagIds = []
    }
    // 先设置表单数据（确保materialType正确）
    form.value = data
    // 加载媒体列表
    if (form.value.materialType === 'post' && (form.value.packageType === 2 || form.value.packageType === 3)) {
      getMaterialMedia(id).then(mediaResponse => {
        form.value.mediaList = mediaResponse.data || []
      })
    }
    // 获取二级分类选项
    getCategoryOptions(form.value.packageType)
    // 最后打开弹窗
    title.value = "修改素材"
    open.value = true
  })
}

function handleDelete() {
  const materialIds = ids.value
  proxy.$modal.confirm('是否确认删除素材编号为"' + materialIds + '"的数据项？').then(function() {
    return delMaterial(materialIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleChangeStatus(row) {
  const text = row.status === '0' ? "下线" : "上线"
  const newStatus = row.status === '0' ? '1' : '0'
  proxy.$modal.confirm('确认要"' + text + '"素材"' + row.title + '"吗？').then(function() {
    return changeMaterialStatus(row.id, newStatus)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {})
}

function handleChangeTop(row) {
  const text = row.isTop === '0' ? "置顶" : "取消置顶"
  const newIsTop = row.isTop === '0' ? '1' : '0'
  proxy.$modal.confirm('确认要"' + text + '"素材"' + row.title + '"吗？').then(function() {
    return changeMaterialTop(row.id, newIsTop)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {})
}

function submitForm() {
  proxy.$refs["materialRef"].validate(valid => {
    if (valid) {
      const isPost = form.value.materialType === 'post' && (form.value.packageType === 2 || form.value.packageType === 3)
      if (form.value.id != undefined) {
        updateMaterial(form.value).then(() => {
          // 如果是帖子类型，保存媒体列表
          if (isPost && form.value.mediaList) {
            saveMaterialMedia(form.value.id, form.value.mediaList)
          }
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addMaterial(form.value).then(response => {
          // 如果是帖子类型，保存媒体列表
          if (isPost && form.value.mediaList && form.value.mediaList.length > 0) {
            const materialId = response.data?.id || response.id
            if (materialId) {
              saveMaterialMedia(materialId, form.value.mediaList)
            }
          }
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

getTagOptions()
queryParams.value.packageType = 0
getList()
</script>

<style scoped>
.material-tab {
  margin-bottom: 20px;
}
.tab-group :deep(.el-radio-button__inner) {
  padding: 10px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}
.tab-group :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: #667eea;
}
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  justify-content: center;
}
.material-tag {
  margin: 0;
}
.detail-tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.detail-tag {
  margin: 0;
}
.tag-option {
  display: flex;
  align-items: center;
  gap: 8px;
}
.tag-color-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}
.media-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}
.media-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background-color: #f5f7fa;
}
.media-index {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 50%;
  font-size: 12px;
  font-weight: bold;
}
.media-content {
  display: flex;
  align-items: center;
  justify-content: center;
}
.media-info {
  display: flex;
  align-items: center;
  gap: 8px;
}
.media-sort {
  font-size: 12px;
  color: #909399;
}
.media-upload-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}
.media-upload-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background-color: #f5f7fa;
  width: 150px;
}
.media-preview {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border-radius: 4px;
  background-color: #fff;
}
.media-actions {
  display: flex;
  flex-direction: column;
  gap: 4px;
  width: 100%;
}
.media-upload-add {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 150px;
  height: 150px;
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  cursor: pointer;
  transition: border-color 0.3s;
}
.media-upload-add:hover {
  border-color: #409eff;
}
.media-uploader {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 详情页样式修复 */
:deep(.el-descriptions__body .el-descriptions__table) {
  table-layout: fixed;
  width: 100%;
}
:deep(.el-descriptions__body .el-descriptions__cell) {
  word-break: break-all;
  max-width: 700px;
}
</style>

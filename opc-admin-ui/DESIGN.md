# Claude Design System

## 概述

Claude 是一款以温暖赤陶色调、编辑式布局为特色的设计系统。强调舒适的阅读体验、优雅的排版和温暖的视觉氛围。

---

## 颜色系统

### 主色调
```
--color-primary: #C45C3E          /* 赤陶橙 - 主品牌色 */
--color-primary-hover: #A84D33    /* 悬停状态 */
--color-primary-light: #FDF1EE    /* 浅赤陶背景 */
```

### 背景色
```
--color-bg-primary: #FAF8F5       /* 主背景 - 温暖纸白 */
--color-bg-secondary: #FFFFFF     /* 次级背景 - 纯白 */
--color-bg-tertiary: #F5F0EB      /* 三级背景 - 卡片 */
--color-bg-elevated: #EDE8E2      /* 悬浮背景 */
```

### 文字色
```
--color-text-primary: #2D2A26     /* 主要文字 - 温暖黑 */
--color-text-secondary: #6B6560   /* 次要文字 - 暖灰 */
--color-text-tertiary: #9A9590    /* 辅助文字 */
--color-text-disabled: #C4C0BB    /* 禁用文字 */
```

### 功能色
```
--color-success: #4A7C59          /* 成功 - 森林绿 */
--color-warning: #B8833F          /* 警告 - 琥珀 */
--color-danger: #B54A4A           /* 危险 - 暗红 */
--color-info: #5A7A96             /* 信息 - 灰蓝 */
```

### 边框色
```
--color-border: #E8E2DC           /* 边框 - 温暖灰 */
--color-border-hover: #D4CFC8     /* 悬停边框 */
--color-divider: #EDE8E2          /* 分隔线 */
```

---

## 字体系统

### 字体族
```
--font-family-serif: "Source Serif 4", "Merriweather", Georgia, serif    /* 标题 */
--font-family-sans: "Inter", -apple-system, BlinkMacSystemFont, sans-serif  /* 正文 */
--font-family-mono: "SF Mono", "JetBrains Mono", monospace  /* 代码 */
```

### 字号规范
```
--text-xs: 12px    /* 标签、徽章 */
--text-sm: 14px    /* 次要文字 */
--text-base: 16px  /* 正文 */
--text-md: 18px    /* 菜单项 */
--text-lg: 22px    /* 小标题 */
--text-xl: 32px    /* 页面标题 - 衬线 */
--text-2xl: 42px   /* 大标题 - 衬线 */
```

### 字重
```
--font-normal: 400
--font-medium: 500
--font-semibold: 600
```

### 行高
```
--leading-tight: 1.4
--leading-normal: 1.7
--leading-relaxed: 1.9
```

---

## 间距系统

```
--space-1: 4px
--space-2: 8px
--space-3: 12px
--space-4: 16px
--space-5: 24px
--space-6: 32px
--space-8: 48px
--space-10: 64px
```

---

## 圆角系统

```
--radius-sm: 6px    /* 小元素 */
--radius-md: 10px   /* 按钮、输入框 */
--radius-lg: 14px   /* 卡片 */
--radius-xl: 20px   /* 大卡片、弹窗 */
```

---

## 阴影系统

```
--shadow-sm: 0 1px 3px rgba(45, 42, 38, 0.05)
--shadow-md: 0 4px 12px rgba(45, 42, 38, 0.08)
--shadow-lg: 0 12px 28px rgba(45, 42, 38, 0.12)
```

---

## 组件样式

### 按钮
- **主按钮**: 赤陶橙背景(#C45C3E)，白色文字，圆角 10px
- **次按钮**: 白色背景，温暖灰边框
- **幽灵按钮**: 透明背景，赤陶橙文字
- 高度: 40px (小) / 48px (默认) / 56px (大)
- 内边距: 12px 24px

### 输入框
- 背景: #FFFFFF
- 边框: 1px solid #E8E2DC
- 圆角: 10px
- 高度: 48px
- 聚焦: 边框变为 #C45C3E，添加温暖光晕

### 卡片
- 背景: #FFFFFF
- 圆角: 14px
- 边框: 1px solid #E8E2DC
- 内边距: 32px
- 柔和阴影

### 表格
- 表头: #F5F0EB 背景，#6B6560 文字
- 行悬停: #FAF8F5 背景
- 边框: 1px solid #EDE8E2
- 行高: 56px

### 侧边栏
- 宽度: 260px
- 背景: #FAF8F5 (温暖纸白)
- 边框: 1px solid #E8E2DC (右侧)
- 菜单项悬停: #F5F0EB 背景
- 激活项: #C45C3E 背景，白色文字

### 标签/徽章
- 背景: #F5F0EB
- 文字: #6B6560
- 圆角: 8px
- 内边距: 6px 12px

---

## 排版规范

### 标题（衬线字体）
```
H1: 42px, font-weight: 600, line-height: 1.2, letter-spacing: -0.02em
H2: 32px, font-weight: 600, line-height: 1.3
H3: 24px, font-weight: 600, line-height: 1.4
```

### 正文（无衬线字体）
```
Body: 16px, font-weight: 400, line-height: 1.7
Small: 14px, font-weight: 400, line-height: 1.6
```

---

## 布局规范

### 页面结构
- 顶部导航高度: 72px
- 内容区最大宽度: 1100px
- 内容区内边距: 40px
- 卡片间距: 32px

### 响应式断点
```
sm: 640px
md: 768px
lg: 1024px
xl: 1280px
```

---

## 动效规范

### 过渡时间
```
--duration-fast: 200ms
--duration-normal: 300ms
--duration-slow: 500ms
```

### 缓动函数
```
--ease-default: cubic-bezier(0.4, 0, 0.2, 1)
--ease-out: cubic-bezier(0, 0, 0.2, 1)
```

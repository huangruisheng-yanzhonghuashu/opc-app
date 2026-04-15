# STAREFILD Design System (Claude Style)

> AI工具导航平台 - 温暖人性化设计

---

## 1. Visual Theme & Atmosphere

### Design Philosophy
- **温暖人文**: 陶土色调传递亲和力与信任感
- **编辑美学**: 杂志般的排版层次，阅读舒适
- **简洁优雅**: 克制装饰，内容为王
- **智能友好**: AI产品但不过度科技感，更人性化

### Visual Identity
- **密度**: 中低密度，舒适留白
- **情绪**: 温暖、可信、专业、友好
- **视觉层次**: 衬线标题 + 无衬线正文，对比优雅

---

## 2. Color Palette

### Primary Colors
| Name | Hex | Usage |
|------|-----|-------|
| Terracotta | `#D97757` | 主强调色、按钮、链接 |
| Terracotta Light | `#E8956E` | 悬停状态 |
| Terracotta Dark | `#B85C3D` | 按下状态 |

### Background Colors
| Name | Hex | Usage |
|------|-----|-------|
| Cream | `#FAF6F1` | 主背景 |
| Cream Dark | `#F5EFE6` | 次级背景、卡片 |
| White | `#FFFFFF` | 纯白表面 |
| Warm Gray | `#F0EBE3` | 分隔区域 |

### Text Colors
| Name | Hex | Usage |
|------|-----|-------|
| Text Primary | `#1A1A1A` | 主标题、正文 |
| Text Secondary | `#4A4A4A` | 次要文本 |
| Text Tertiary | `#6B6B6B` | 辅助文本 |
| Text Muted | `#9A9A9A` | 禁用、占位符 |
| Text Inverse | `#FFFFFF` | 深色背景上的文本 |

### Border Colors
| Name | Hex | Usage |
|------|-----|-------|
| Border Light | `#E8E2D9` | 默认边框 |
| Border Default | `#D4CDBF` | 分隔线 |
| Border Hover | `#C4BCAC` | 悬停边框 |
| Border Accent | `#D97757` | 强调边框 |

### Accent Colors
| Name | Hex | Usage |
|------|-----|-------|
| Sage Green | `#8B9D77` | 成功状态 |
| Warm Gold | `#D4A574` | 次要强调 |
| Soft Coral | `#E07A5F` | 警告、特色 |

### Gradient Patterns
```
Hero Gradient: linear-gradient(180deg, #FAF6F1 0%, #F5EFE6 100%)
Warm Glow: radial-gradient(ellipse at top, rgba(217, 119, 87, 0.1), transparent 50%)
Divider: linear-gradient(90deg, transparent, #D4CDBF, transparent)
```

---

## 3. Typography

### Font Family
- **Serif (Headings)**: `'Merriweather', Georgia, 'Noto Serif SC', serif`
- **Sans-serif (Body)**: `'Inter', -apple-system, BlinkMacSystemFont, 'Noto Sans SC', sans-serif`
- **Monospace**: `'JetBrains Mono', monospace`

### Type Scale
| Style | Font | Size | Weight | Line Height | Usage |
|-------|------|------|--------|-------------|-------|
| Display | Merriweather | 3.5rem (56px) | 700 | 1.1 | Hero 主标题 |
| Title 1 | Merriweather | 2.5rem (40px) | 700 | 1.2 | 页面大标题 |
| Title 2 | Merriweather | 2rem (32px) | 600 | 1.3 | 区块标题 |
| Title 3 | Inter | 1.5rem (24px) | 600 | 1.4 | 卡片标题 |
| Headline | Inter | 1.25rem (20px) | 600 | 1.4 | 子标题 |
| Body Large | Inter | 1.125rem (18px) | 400 | 1.7 | 重要正文 |
| Body | Inter | 1rem (16px) | 400 | 1.7 | 标准正文 |
| Body Small | Inter | 0.875rem (14px) | 400 | 1.6 | 次要正文 |
| Caption | Inter | 0.75rem (12px) | 500 | 1.4 | 标签、徽章 |

### Typography Rules
- **标题**: 使用衬线字体 (Merriweather)，优雅经典
- **正文**: 使用无衬线字体 (Inter)，清晰易读
- **行高**: 正文 1.7，保证阅读舒适
- **字重**: 正文 400，标题 600-700

---

## 4. Component Stylings

### Buttons

#### Primary Button
```
Background: #D97757
Color: #FFFFFF
Padding: 12px 24px
Border Radius: 8px
Font Weight: 500
Font Size: 15px
Transition: all 0.2s ease
Hover: background #E8956E, transform translateY(-1px)
Active: background #B85C3D
```

#### Secondary Button
```
Background: transparent
Color: #1A1A1A
Border: 1.5px solid #D4CDBF
Padding: 12px 24px
Border Radius: 8px
Hover: border-color #D97757, color #D97757
```

#### Ghost Button
```
Background: transparent
Color: #6B6B6B
Hover: color #D97757, background rgba(217, 119, 87, 0.05)
```

### Cards

#### Feature Card
```
Background: #FFFFFF
Border: 1px solid #E8E2D9
Border Radius: 16px
Padding: 32px
Box Shadow: 0 2px 8px rgba(0, 0, 0, 0.04)
Transition: all 0.3s ease
Hover: 
  - box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08)
  - transform: translateY(-2px)
  - border-color: #D4CDBF
```

#### Tool Card
```
Background: #FFFFFF
Border: 1px solid #E8E2D9
Border Radius: 12px
Padding: 24px
Hover: box-shadow增强, border-color变暖
```

### Tags/Badges

#### Category Tag
```
Background: rgba(217, 119, 87, 0.1)
Color: #D97757
Border: 1px solid rgba(217, 119, 87, 0.2)
Padding: 4px 12px
Border Radius: 20px
Font Size: 12px
Font Weight: 500
```

#### Status Badge
```
Background: rgba(139, 157, 119, 0.1)
Color: #8B9D77
Border: 1px solid rgba(139, 157, 119, 0.2)
Padding: 2px 8px
Border Radius: 4px
Font Size: 11px
Font Weight: 600
```

---

## 5. Layout Principles

### Spacing Scale
| Token | Value | Usage |
|-------|-------|-------|
| space-1 | 4px | 微小间距 |
| space-2 | 8px | 紧凑间距 |
| space-3 | 12px | 小间距 |
| space-4 | 16px | 标准间距 |
| space-5 | 20px | 中等间距 |
| space-6 | 24px | 组件间距 |
| space-8 | 32px | 区块间距 |
| space-10 | 40px | 大间距 |
| space-12 | 48px | Section padding |
| space-16 | 64px | 大Section padding |

### Container
- **Max Width**: 1140px
- **Padding**: 24px (移动端 16px)
- **Grid Gap**: 24px

---

## 6. Animation & Motion

### Timing
- **Fast**: 200ms (hover)
- **Normal**: 300ms (transitions)
- **Slow**: 400ms (page elements)

### Effects
- **Easing**: `cubic-bezier(0.4, 0, 0.2, 1)`
- **Card Hover**: translateY(-2px) + shadow增强
- **Button Hover**: translateY(-1px) + color变化
- **Scroll Reveal**: fadeInUp, staggered

---

## 7. Do's and Don'ts

### Do's ✓
- 使用奶油色背景 (#FAF6F1)
- 陶土色 (#D97757) 作为主强调色
- 衬线字体用于标题，无衬线用于正文
- 保持大量留白，呼吸感
- 使用圆角 8-16px

### Don'ts ✗
- 不要使用冷色调（蓝、紫）作为主色
- 不要使用尖锐的直角
- 不要过度拥挤
- 不要使用科技感过强的元素

---

## 8. Agent Prompt Guide

### Quick Reference
```
Theme: Claude-style warm editorial
Background: #FAF6F1 (warm cream)
Primary: #D97757 (terracotta)
Text: #1A1A1A (primary), #4A4A4A (secondary)
Border: #E8E2D9
Radius: 8px (buttons), 12-16px (cards)
Heading Font: Merriweather (serif)
Body Font: Inter (sans-serif)
```

### Prompt Template
> "使用 Claude 风格设计系统创建页面：
> - 背景: 温暖奶油色 (#FAF6F1)
> - 强调色: 陶土色 (#D97757) 用于按钮和交互
> - 卡片: 白色背景, 12px圆角, 柔和阴影
> - 字体: Merriweather衬线标题 + Inter无衬线正文
> - 动效: 温和的悬停过渡，微妙的阴影变化
> - 风格: 编辑式美学，优雅留白，人性化温暖"

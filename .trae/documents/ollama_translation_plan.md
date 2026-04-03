# Ollama 大模型翻译功能改造计划（使用 Ollama4j 客户端）

## 一、现状分析

### 1.1 现有翻译架构

项目目前使用 **DeepLX** 作为翻译服务，架构如下：

```
┌─────────────────────────────────────────────────────────────┐
│                        前端 (Vue)                            │
│              opc-admin-ui/src/views/tool/translate           │
└───────────────────────────┬─────────────────────────────────┘
                            │ HTTP 请求
┌───────────────────────────▼─────────────────────────────────┐
│                      后端 Controller                         │
│           TranslateController.java (/translate/*)            │
└───────────────────────────┬─────────────────────────────────┘
                            │ 调用
┌───────────────────────────▼─────────────────────────────────┐
│                     翻译服务接口                             │
│              TranslationService (接口定义)                   │
└───────────────────────────┬─────────────────────────────────┘
                            │ 实现
┌───────────────────────────▼─────────────────────────────────┐
│                   DeepLX 翻译实现                            │
│           DeepLxTranslationService.java                      │
│              调用外部 DeepLX HTTP API                        │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 现有代码文件

| 文件路径 | 说明 |
|---------|------|
| `opc-common/src/main/java/com/opc/common/utils/translate/TranslationService.java` | 翻译服务接口 |
| `opc-common/src/main/java/com/opc/common/utils/translate/DeepLxTranslationService.java` | DeepLX 实现类 |
| `opc-common/src/main/java/com/opc/common/utils/translate/DeepLxProperties.java` | DeepLX 配置属性 |
| `opc-common/src/main/java/com/opc/common/utils/translate/TranslateUtils.java` | 翻译工具类 |
| `opc-common/src/main/java/com/opc/common/utils/translate/LanguageCode.java` | 语言代码常量 |
| `opc-admin/src/main/java/com/opc/web/controller/common/TranslateController.java` | 翻译接口 Controller |
| `opc-admin/src/main/resources/application.yml` | 配置文件（deeplx 配置） |

### 1.3 现有接口定义

```java
public interface TranslationService {
    String translate(String text, String sourceLang, String targetLang);
    String translate(String text, String targetLang);
    String[] translate(String[] texts, String sourceLang, String targetLang);
}
```

---

## 二、改造方案（使用 Ollama4j 客户端）

### 2.1 目标架构

将 DeepLX 替换为 **Ollama4j 客户端**调用本地 Ollama 大模型翻译，使用 `translategemma:4b` 模型：

```
┌─────────────────────────────────────────────────────────────┐
│                        前端 (Vue)                            │
│              opc-admin-ui/src/views/tool/translate           │
└───────────────────────────┬─────────────────────────────────┘
                            │ HTTP 请求 (保持不变)
┌───────────────────────────▼─────────────────────────────────┐
│                      后端 Controller                         │
│           TranslateController.java (/translate/*)            │
│                      (保持不变)                              │
└───────────────────────────┬─────────────────────────────────┘
                            │ 调用 (保持不变)
┌───────────────────────────▼─────────────────────────────────┐
│                     翻译服务接口                             │
│              TranslationService (接口不变)                   │
└───────────────────────────┬─────────────────────────────────┘
                            │ 新实现（使用 Ollama4j）
┌───────────────────────────▼─────────────────────────────────┐
│                   Ollama 翻译实现                            │
│           OllamaTranslationService.java                      │
│         使用 Ollama4j 客户端调用本地 Ollama                  │
└───────────────────────────┬─────────────────────────────────┘
                            │ Ollama4j API
┌───────────────────────────▼─────────────────────────────────┐
│              Ollama 本地服务 (http://localhost:11434)         │
│                   translategemma:4b 模型                     │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Ollama4j 简介

**Ollama4j** 是一个用于与 Ollama 服务器交互的 Java 库，提供了简洁的 API：

- **Maven 依赖**：`io.github.ollama4j:ollama4j:1.1.6`
- **GitHub**：https://github.com/amithkoujalgi/ollama4j
- **文档**：https://ollama4j.github.io/ollama4j/

**核心类**：
- `Ollama` - 主 API 类，用于与 Ollama 服务器通信
- `OllamaResult` - 模型生成结果
- `PromptBuilder` - 提示词构建器
- `OptionsBuilder` - 模型参数选项构建器

### 2.3 改造步骤

#### 步骤 1：添加 Ollama4j Maven 依赖

在 `opc-common/pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>io.github.ollama4j</groupId>
    <artifactId>ollama4j</artifactId>
    <version>1.1.6</version>
</dependency>
```

#### 步骤 2：创建 Ollama 配置属性类

**文件**：`opc-common/src/main/java/com/opc/common/utils/translate/OllamaProperties.java`

```java
@Component
@ConfigurationProperties(prefix = "ollama.translation")
public class OllamaProperties {
    // Ollama 服务地址，默认本地
    private String url = "http://localhost:11434";
    // 使用的模型名称
    private String model = "translategemma:4b";
    // 请求超时时间（秒）
    private int requestTimeoutSeconds = 60;
    // 温度参数（创造性程度，0-1）
    private float temperature = 0.3f;
}
```

#### 步骤 3：创建 Ollama 翻译服务实现（使用 Ollama4j）

**文件**：`opc-common/src/main/java/com/opc/common/utils/translate/OllamaTranslationService.java`

核心逻辑：
1. 使用 `Ollama` 类创建客户端
2. 使用 `PromptBuilder` 构建翻译提示词
3. 使用 `OptionsBuilder` 设置模型参数
4. 调用 `ollama.generate()` 获取翻译结果

#### 步骤 4：修改配置文件

**文件**：`opc-admin/src/main/resources/application.yml`

添加 Ollama 配置：
```yaml
# Ollama 翻译服务配置
ollama:
  translation:
    # Ollama 服务地址
    url: http://localhost:11434
    # 使用的模型
    model: translategemma:4b
    # 请求超时时间（秒）- 大模型推理较慢，设置较长超时
    request-timeout-seconds: 60
    # 温度参数（0-1，越低越保守）
    temperature: 0.3
```

#### 步骤 5：切换服务实现

在 `OllamaTranslationService` 上添加 `@Primary` 注解，使其成为默认实现。

---

## 三、详细实现代码

### 3.1 添加 Maven 依赖

在 `opc-common/pom.xml` 的 `<dependencies>` 节中添加：

```xml
<!-- Ollama4j 客户端 -->
<dependency>
    <groupId>io.github.ollama4j</groupId>
    <artifactId>ollama4j</artifactId>
    <version>1.1.6</version>
</dependency>
```

### 3.2 OllamaProperties.java

```java
package com.opc.common.utils.translate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ollama.translation")
public class OllamaProperties {

    private String url = "http://localhost:11434";
    private String model = "translategemma:4b";
    private int requestTimeoutSeconds = 60;
    private float temperature = 0.3f;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getRequestTimeoutSeconds() {
        return requestTimeoutSeconds;
    }

    public void setRequestTimeoutSeconds(int requestTimeoutSeconds) {
        this.requestTimeoutSeconds = requestTimeoutSeconds;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
```

### 3.3 OllamaTranslationService.java

```java
package com.opc.common.utils.translate;

import io.github.ollama4j.Ollama;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import io.github.ollama4j.utils.PromptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Primary  // 标记为默认实现
public class OllamaTranslationService implements TranslationService {

    private static final Logger log = LoggerFactory.getLogger(OllamaTranslationService.class);

    @Autowired
    private OllamaProperties properties;

    @Override
    public String translate(String text, String sourceLang, String targetLang) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        try {
            // 创建 Ollama 客户端
            Ollama ollama = new Ollama(properties.getUrl());
            ollama.setRequestTimeoutSeconds(properties.getRequestTimeoutSeconds());

            // 构建翻译提示词
            String prompt = buildPrompt(text, sourceLang, targetLang);

            // 构建模型选项参数
            OptionsBuilder optionsBuilder = new OptionsBuilder();
            optionsBuilder.setTemperature(properties.getTemperature());

            // 调用模型生成翻译
            OllamaResult result = ollama.generate(
                    properties.getModel(),
                    prompt,
                    false,  // raw = false，使用模板
                    optionsBuilder.build()
            );

            String translatedText = result.getResponse();
            
            // 清理结果（去除可能的引号和多余空格）
            if (translatedText != null) {
                translatedText = translatedText.trim();
                // 如果结果被引号包裹，去除引号
                if ((translatedText.startsWith("\"") && translatedText.endsWith("\"")) ||
                    (translatedText.startsWith("'") && translatedText.endsWith("'"))) {
                    translatedText = translatedText.substring(1, translatedText.length() - 1);
                }
            }

            log.debug("Ollama 翻译成功: {} -> {}", text, translatedText);
            return translatedText != null ? translatedText : text;

        } catch (Exception e) {
            log.error("Ollama 翻译异常，原文: {}", text, e);
            return text;
        }
    }

    @Override
    public String translate(String text, String targetLang) {
        return translate(text, "auto", targetLang);
    }

    @Override
    public String[] translate(String[] texts, String sourceLang, String targetLang) {
        if (texts == null || texts.length == 0) {
            return texts;
        }

        String[] results = new String[texts.length];
        for (int i = 0; i < texts.length; i++) {
            results[i] = translate(texts[i], sourceLang, targetLang);
        }
        return results;
    }

    /**
     * 构建翻译提示词
     */
    private String buildPrompt(String text, String sourceLang, String targetLang) {
        String sourceLangName = getLanguageName(sourceLang);
        String targetLangName = getLanguageName(targetLang);
        
        // 使用 PromptBuilder 构建提示词
        PromptBuilder promptBuilder = new PromptBuilder()
                .addLine("你是一个专业的翻译助手。")
                .addLine("请将以下文本从" + sourceLangName + "翻译成" + targetLangName + "。")
                .addLine("只返回翻译结果，不要添加任何解释、引号或额外内容。")
                .addSeparator()
                .addLine("原文：")
                .addLine(text)
                .addSeparator()
                .addLine("译文：");
        
        return promptBuilder.build();
    }

    /**
     * 获取语言名称
     */
    private String getLanguageName(String langCode) {
        if (langCode == null || "auto".equalsIgnoreCase(langCode)) {
            return "原文语言";
        }
        
        Map<String, String> langMap = new HashMap<>();
        langMap.put("ZH", "中文");
        langMap.put("ZH-HANT", "繁体中文");
        langMap.put("EN", "英语");
        langMap.put("JA", "日语");
        langMap.put("KO", "韩语");
        langMap.put("FR", "法语");
        langMap.put("DE", "德语");
        langMap.put("ES", "西班牙语");
        langMap.put("IT", "意大利语");
        langMap.put("PT", "葡萄牙语");
        langMap.put("RU", "俄语");
        langMap.put("NL", "荷兰语");
        langMap.put("PL", "波兰语");
        langMap.put("TR", "土耳其语");
        langMap.put("AR", "阿拉伯语");
        langMap.put("SV", "瑞典语");
        langMap.put("ID", "印尼语");
        langMap.put("HI", "印地语");
        langMap.put("VI", "越南语");
        langMap.put("TH", "泰语");
        
        return langMap.getOrDefault(langCode.toUpperCase(), langCode);
    }
}
```

### 3.4 application.yml 配置更新

在 `application.yml` 中添加：

```yaml
# Ollama 翻译服务配置
ollama:
  translation:
    # Ollama 服务地址
    url: http://localhost:11434
    # 使用的模型
    model: translategemma:4b
    # 请求超时时间（秒）- 大模型推理较慢，设置较长超时
    request-timeout-seconds: 60
    # 温度参数（0-1，越低越保守）
    temperature: 0.3

# 保留 DeepLX 配置作为备用（可选）
deeplx:
  url: https://vps-sg-aws-opc.43046721.xyz/translate
  token:
  connect-timeout: 5000
  read-timeout: 10000
```

---

## 四、Ollama 环境准备

### 4.1 安装 Ollama

```bash
# 访问 https://ollama.com 下载安装
# 或在 Linux 上运行：
curl -fsSL https://ollama.com/install.sh | sh
```

### 4.2 拉取 translategemma:4b 模型

```bash
ollama pull translategemma:4b
```

### 4.3 验证服务运行

```bash
# 检查 Ollama 服务状态
curl http://localhost:11434/api/tags

# 测试翻译
curl -X POST http://localhost:11434/api/generate -d '{
  "model": "translategemma:4b",
  "prompt": "请将以下文本从英语翻译成中文：Hello World",
  "stream": false
}'
```

---

## 五、文件变更清单

### 5.1 新增文件

| 文件路径 | 说明 |
|---------|------|
| `opc-common/src/main/java/com/opc/common/utils/translate/OllamaProperties.java` | Ollama 配置属性类 |
| `opc-common/src/main/java/com/opc/common/utils/translate/OllamaTranslationService.java` | Ollama 翻译服务实现（使用 Ollama4j） |

### 5.2 修改文件

| 文件路径 | 修改内容 |
|---------|---------|
| `opc-common/pom.xml` | 添加 Ollama4j Maven 依赖 |
| `opc-admin/src/main/resources/application.yml` | 添加 ollama.translation 配置 |

### 5.3 保留文件（无需修改）

| 文件路径 | 说明 |
|---------|------|
| `TranslationService.java` | 接口保持不变 |
| `TranslateUtils.java` | 工具类保持不变 |
| `LanguageCode.java` | 语言代码常量保持不变 |
| `TranslateController.java` | Controller 保持不变 |
| `DeepLxTranslationService.java` | 保留作为备用 |
| `DeepLxProperties.java` | 保留作为备用 |

---

## 六、Ollama4j API 说明

### 6.1 核心方法

```java
// 创建 Ollama 客户端
Ollama ollama = new Ollama("http://localhost:11434");
ollama.setRequestTimeoutSeconds(60);

// 生成文本
OllamaResult result = ollama.generate(
    model,      // 模型名称，如 "translategemma:4b"
    prompt,     // 提示词
    raw,        // 是否使用原始提示词（不使用模板）
    options     // 模型选项参数
);

// 获取结果
String response = result.getResponse();
```

### 6.2 OptionsBuilder 常用参数

```java
OptionsBuilder optionsBuilder = new OptionsBuilder();
optionsBuilder.setTemperature(0.3f);  // 温度（0-1）
optionsBuilder.setNumCtx(4096);       // 上下文窗口大小
optionsBuilder.setTopK(40);           // Top-K 采样
optionsBuilder.setTopP(0.9f);         // Top-P 采样
```

### 6.3 PromptBuilder 使用

```java
PromptBuilder promptBuilder = new PromptBuilder()
    .addLine("第一行提示词")
    .addLine("第二行提示词")
    .addSeparator()  // 添加分隔线
    .add("单行文本");

String prompt = promptBuilder.build();
```

---

## 七、风险与注意事项

### 7.1 性能考虑

- 大模型翻译比 DeepLX 慢，需要设置较长的超时时间（建议 60 秒）
- Ollama4j 的 `setRequestTimeoutSeconds()` 方法设置请求超时
- 考虑添加异步翻译或缓存机制
- 批量翻译时建议减少并发量

### 7.2 错误处理

- Ollama 服务未启动时，翻译会失败并返回原文
- 建议添加健康检查接口
- 考虑实现降级策略（Ollama 失败时切换到 DeepLX）

### 7.3 资源占用

- translategemma:4b 模型需要一定的内存和显存
- 确保服务器资源充足

---

## 八、验证步骤

1. 启动 Ollama 服务并确认模型已加载
2. 在 `opc-common/pom.xml` 中添加 Ollama4j 依赖
3. 创建 `OllamaProperties.java` 和 `OllamaTranslationService.java`
4. 更新 `application.yml` 配置
5. 启动后端应用
6. 调用翻译接口测试：
   ```bash
   curl -X POST http://localhost:8080/translate/text \
     -d "text=Hello World" \
     -d "sourceLang=EN" \
     -d "targetLang=ZH"
   ```
7. 检查日志确认调用的是 Ollama 服务
8. 测试批量翻译和自动语言检测功能

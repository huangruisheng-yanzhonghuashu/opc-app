package com.opc.common.utils.translate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * DeepLxTranslationService 单元测试
 */
public class DeepLxTranslationServiceTest {

    private DeepLxTranslationService translationService;

    @Mock
    private DeepLxProperties properties;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        translationService = new DeepLxTranslationService();

        // 使用反射注入 mock 的 properties
        Field propertiesField = DeepLxTranslationService.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        propertiesField.set(translationService, properties);

        // 使用反射替换 restTemplate
        Field restTemplateField = DeepLxTranslationService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(translationService, restTemplate);

        // 配置默认属性
        when(properties.getUrl()).thenReturn("https://vps-sg-aws-opc.43046721.xyz/deeplx");
        when(properties.getToken()).thenReturn(null);
    }

    @Test
    @DisplayName("测试空文本返回原值")
    public void testTranslateWithEmptyText() {
        assertNull(translationService.translate((String) null, "EN", "ZH"));
        assertEquals("", translationService.translate("", "EN", "ZH"));
        assertEquals("   ", translationService.translate("   ", "EN", "ZH"));
    }

    @Test
    @DisplayName("测试正常翻译 - 返回 data 字段")
    public void testTranslateSuccessWithData() {
        // 准备 mock 响应
        String jsonResponse = "{\"data\":\"你好世界\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://vps-sg-aws-opc.43046721.xyz/deeplx/translate"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        // 执行测试
        String result = translationService.translate("Hello World", "EN", "ZH");

        // 验证结果
        assertEquals("你好世界", result);
    }

    @Test
    @DisplayName("测试正常翻译 - 返回 alternatives 字段")
    public void testTranslateSuccessWithAlternatives() {
        // 准备 mock 响应
        String jsonResponse = "{\"alternatives\":[\"你好世界\",\"你好，世界\"]}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        // 执行测试
        String result = translationService.translate("Hello World", "EN", "ZH");

        // 验证结果
        assertEquals("你好世界", result);
    }

    @Test
    @DisplayName("测试翻译失败返回原文")
    public void testTranslateFailureReturnsOriginal() {
        // 准备 mock 响应 - 非 200 状态码
        ResponseEntity<String> response = new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        // 执行测试
        String result = translationService.translate("Hello World", "EN", "ZH");

        // 验证结果 - 失败时返回原文
        assertEquals("Hello World", result);
    }

    @Test
    @DisplayName("测试翻译异常返回原文")
    public void testTranslateExceptionReturnsOriginal() {
        // 模拟异常
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Connection refused"));

        // 执行测试
        String result = translationService.translate("Hello World", "EN", "ZH");

        // 验证结果 - 异常时返回原文
        assertEquals("Hello World", result);
    }

    @Test
    @DisplayName("测试自动检测源语言")
    public void testTranslateWithAutoDetect() {
        // 准备 mock 响应
        String jsonResponse = "{\"data\":\"你好世界\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        // 执行测试 - 只传目标语言，自动检测源语言
        String result = translationService.translate("Hello World", "ZH");

        // 验证结果
        assertEquals("你好世界", result);
    }

    @Test
    @DisplayName("测试批量翻译")
    public void testTranslateBatch() {
        // 准备 mock 响应
        String jsonResponse = "{\"data\":\"翻译结果\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        // 执行测试
        String[] texts = {"Hello", "World"};
        String[] results = translationService.translate(texts, "EN", "ZH");

        // 验证结果
        assertNotNull(results);
        assertEquals(2, results.length);
        assertEquals("翻译结果", results[0]);
        assertEquals("翻译结果", results[1]);
    }

    @Test
    @DisplayName("测试批量翻译空数组")
    public void testTranslateBatchWithEmptyArray() {
        String[] emptyArray = new String[0];
        String[] results = translationService.translate(emptyArray, "EN", "ZH");

        assertNotNull(results);
        assertEquals(0, results.length);
    }

    @Test
    @DisplayName("测试批量翻译 null 数组")
    public void testTranslateBatchWithNullArray() {
        String[] results = translationService.translate((String[]) null, "EN", "ZH");
        assertNull(results);
    }

    @Test
    @DisplayName("测试带 Token 的请求头")
    public void testTranslateWithToken() {
        // 配置 token
        when(properties.getToken()).thenReturn("test-token");

        // 准备 mock 响应
        String jsonResponse = "{\"data\":\"你好\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        // 执行测试
        String result = translationService.translate("Hi", "EN", "ZH");

        // 验证结果
        assertEquals("你好", result);
        // 验证请求头中包含 Authorization
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                argThat((HttpEntity<?> entity) -> {
                    String auth = entity.getHeaders().getFirst("Authorization");
                    return auth != null && auth.equals("Bearer test-token");
                }),
                eq(String.class)
        );
    }

    @Test
    @DisplayName("测试语言代码标准化 - ZH-CN")
    public void testNormalizeLangCodeZhCn() {
        String jsonResponse = "{\"data\":\"result\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(response);

        translationService.translate("test", "zh-cn", "en");

        // 验证请求体中的语言代码已被标准化
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                argThat((HttpEntity<?> entity) -> {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> body = (java.util.Map<String, Object>) entity.getBody();
                    return "ZH".equals(body.get("source_lang"));
                }),
                eq(String.class)
        );
    }

    @Test
    @DisplayName("测试语言代码标准化 - null 转为 auto")
    public void testNormalizeLangCodeNullToAuto() {
        String jsonResponse = "{\"data\":\"result\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(response);

        translationService.translate("test", null, "en");

        // 验证请求体中的语言代码为 auto（小写）
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                argThat((HttpEntity<?> entity) -> {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> body = (java.util.Map<String, Object>) entity.getBody();
                    return "auto".equals(body.get("source_lang"));
                }),
                eq(String.class)
        );
    }

    @Test
    @DisplayName("测试语言代码标准化 - 空字符串转为 auto")
    public void testNormalizeLangCodeEmptyToAuto() {
        String jsonResponse = "{\"data\":\"result\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(response);

        translationService.translate("test", "", "en");

        // 验证请求体中的语言代码为 auto（小写）
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                argThat((HttpEntity<?> entity) -> {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> body = (java.util.Map<String, Object>) entity.getBody();
                    return "auto".equals(body.get("source_lang"));
                }),
                eq(String.class)
        );
    }

    @Test
    @DisplayName("测试响应 body 为 null 的情况")
    public void testTranslateWithNullBody() {
        ResponseEntity<String> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        String result = translationService.translate("Hello", "EN", "ZH");

        // body 为 null 时返回原文
        assertEquals("Hello", result);
    }

    @Test
    @DisplayName("测试响应不含 data 和 alternatives 字段")
    public void testTranslateWithNoDataField() {
        String jsonResponse = "{\"other\":\"value\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        String result = translationService.translate("Hello", "EN", "ZH");

        // 返回 data 字段的值（为 null）
        assertNull(result);
    }

    @Test
    @DisplayName("测试 alternatives 为空数组")
    public void testTranslateWithEmptyAlternatives() {
        String jsonResponse = "{\"alternatives\":[]}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        String result = translationService.translate("Hello", "EN", "ZH");

        // alternatives 为空时返回 null
        assertNull(result);
    }
}

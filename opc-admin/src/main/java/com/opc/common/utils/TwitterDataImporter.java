package com.opc.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.core.domain.CoreMaterial;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Twitter 数据导入工具类
 * 用于将 opencli twitter search 返回的 JSON 数据转换为 CoreMaterial 对象
 */
public class TwitterDataImporter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 解析 Twitter JSON 文件并转换为 CoreMaterial 列表
     *
     * @param jsonFile JSON 文件路径
     * @return CoreMaterial 列表
     */
    public static List<CoreMaterial> parseTwitterJsonFile(String jsonFile) throws Exception {
        String content = new String(java.nio.file.Files.readAllBytes(new File(jsonFile).toPath()));
        return parseTwitterJson(content);
    }

    /**
     * 解析 Twitter JSON 字符串并转换为 CoreMaterial 列表
     *
     * @param twitterJson Twitter JSON 字符串
     * @return CoreMaterial 列表
     */
    public static List<CoreMaterial> parseTwitterJson(String twitterJson) throws Exception {
        List<CoreMaterial> materials = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(twitterJson);

        // 处理 JSON 数组
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                CoreMaterial material = convertToMaterial(node);
                if (material != null) {
                    materials.add(material);
                }
            }
        } else {
            // 处理单个对象
            CoreMaterial material = convertToMaterial(rootNode);
            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    /**
     * 将单个 Twitter JSON 节点转换为 CoreMaterial
     *
     * @param node JSON 节点
     * @return CoreMaterial 对象
     */
    public static CoreMaterial convertToMaterial(JsonNode node) {
        if (node == null || !node.has("id")) {
            return null;
        }

        CoreMaterial material = new CoreMaterial();

        // 设置原ID
        material.setOriginalId(getTextValue(node, "id"));

        // 设置内容（text字段）
        String text = getTextValue(node, "text");
        material.setContent(text);

        // 设置标题（取text前50字符，如果没有text则使用默认值）
        String title = text != null && !text.isEmpty()
                ? (text.length() > 50 ? text.substring(0, 50) + "..." : text)
                : "Twitter 内容";
        material.setTitle(title);

        // 设置作者
        material.setAuthor(getTextValue(node, "author"));

        // 设置原链接
        material.setOriginalUrl(getTextValue(node, "url"));

        // 设置点赞数
        if (node.has("likes") && !node.get("likes").isNull()) {
            material.setLikeCount(node.get("likes").asLong());
        }

        // 设置查看数（views字段可能是字符串数字）
        if (node.has("views") && !node.get("views").isNull()) {
            String viewsStr = node.get("views").asText().replaceAll(",", "");
            try {
                material.setViewCount(Long.parseLong(viewsStr));
            } catch (NumberFormatException e) {
                material.setViewCount(0L);
            }
        }

        // 设置套餐类型为普通会员 (1)
        material.setPackageType(1);

        // 设置状态为下线 (1)
        material.setStatus("1");

        // 设置内容类型为文本
        material.setContentType("text");

        // 设置来源为爬虫
        material.setSource("crawler");

        // 设置发布时间（当前时间）
        material.setPublishTime(Instant.now());

        return material;
    }

    /**
     * 安全获取文本值
     */
    private static String getTextValue(JsonNode node, String fieldName) {
        if (node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return null;
    }

    /**
     * 测试方法：直接解析示例 JSON
     */
    public static void main(String[] args) {
        String json = "[\n" +
                "  {\n" +
                "    \"id\": \"2036759968276509097\",\n" +
                "    \"author\": \"manifestpower4X\",\n" +
                "    \"text\": \"CONGRATULATIONS!!! The Money is coming!\",\n" +
                "    \"likes\": 2567,\n" +
                "    \"views\": \"68289\",\n" +
                "    \"url\": \"https://x.com/i/status/2036759968276509097\"\n" +
                "  }\n" +
                "]";

        try {
            List<CoreMaterial> materials = parseTwitterJson(json);
            System.out.println("解析成功，共 " + materials.size() + " 条数据");
            for (CoreMaterial material : materials) {
                System.out.println("=================================");
                System.out.println("Original ID: " + material.getOriginalId());
                System.out.println("Title: " + material.getTitle());
                System.out.println("Author: " + material.getAuthor());
                System.out.println("Content: " + material.getContent());
                System.out.println("Original URL: " + material.getOriginalUrl());
                System.out.println("Package Type: " + material.getPackageType());
                System.out.println("Status: " + material.getStatus());
                System.out.println("Like Count: " + material.getLikeCount());
                System.out.println("View Count: " + material.getViewCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

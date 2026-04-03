package com.opc.web.enums;

/**
 * 内容类型枚举
 * <p>
 * 定义素材的内容类型
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
public enum ContentType {

    TEXT("text", "文本"),
    IMAGE("image", "图片"),
    VIDEO("video", "视频");

    private final String value;
    private final String label;

    ContentType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return ContentType 枚举，找不到返回 null
     */
    public static ContentType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (ContentType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}

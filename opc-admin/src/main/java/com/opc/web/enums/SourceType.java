package com.opc.web.enums;

/**
 * 来源类型枚举
 * <p>
 * 定义素材的来源平台类型
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
public enum SourceType {

    TWITTER("twitter", "Twitter"),
    REDDIT("reddit", "Reddit"),
    YOUTUBE("youtube", "YouTube");

    private final String value;
    private final String label;

    SourceType(String value, String label) {
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
     * @return SourceType 枚举，找不到返回 null
     */
    public static SourceType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (SourceType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}

package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclEnumType;
import com.ysh.jcms.utils.scl.model.template.SclEnumVal;

import java.util.Optional;

/**
 * bType → Java 原生类型 值映射器。
 * <p>
 * 负责两件事：
 * <ol>
 *   <li>bType + 字符串值 → Java 原生类型（Integer、Float 等）</li>
 *   <li>枚举值 ord ↔ label 双向查找</li>
 * </ol>
 */
public class ValueMapper {

    private final SclDataTypeTemplates templates;

    public ValueMapper(SclDataTypeTemplates templates) {
        this.templates = templates;
    }

    // ==================== 值转换 ====================

    /**
     * 将字符串值按 bType 转换为对应的 Java 类型。
     */
    public Optional<Object> mapValue(String bType, String value) {
        if (value == null) return Optional.empty();
        try {
            switch (bType) {
                case "BOOLEAN": return Optional.of(mapBoolean(value));
                case "INT8":    return Optional.of(Byte.parseByte(value));
                case "INT16":   return Optional.of(Short.parseShort(value));
                case "INT32":   return Optional.of(Integer.parseInt(value));
                case "INT64":   return Optional.of(Long.parseLong(value));
                case "INT128":  return Optional.of(value);
                case "INT8U":   return Optional.of(Short.parseShort(value));
                case "INT16U":  return Optional.of(Integer.parseInt(value));
                case "INT24U":  return Optional.of(Integer.parseInt(value));
                case "INT32U":  return Optional.of(Long.parseLong(value));
                case "FLOAT32": return Optional.of(Float.parseFloat(value));
                case "FLOAT64": return Optional.of(Double.parseDouble(value));
                default:        return Optional.of(value);
            }
        } catch (NumberFormatException e) {
            return Optional.of(value);
        }
    }

    // ==================== 枚举映射 ====================

    /** 根据枚举类型 id 和 ord 查找对应的枚举值 label */
    public Optional<String> mapEnumValue(String enumTypeId, int ord) {
        if (templates == null) return Optional.empty();
        SclEnumVal ev = findEnumValByOrd(enumTypeId, ord);
        return Optional.ofNullable(ev != null ? ev.value() : null);
    }

    /** 根据枚举类型 id 和 label 查找对应的 ord */
    public Optional<Integer> mapEnumOrd(String enumTypeId, String value) {
        if (templates == null || value == null) return Optional.empty();
        SclEnumType enumType = templates.findEnumTypeById(enumTypeId);
        if (enumType == null) return Optional.empty();
        for (SclEnumVal ev : enumType.enumVals()) {
            if (value.equals(ev.value())) {
                return Optional.of(ev.ord());
            }
        }
        return Optional.empty();
    }

    private SclEnumVal findEnumValByOrd(String enumTypeId, int ord) {
        SclEnumType enumType = templates.findEnumTypeById(enumTypeId);
        if (enumType == null) return null;
        for (SclEnumVal ev : enumType.enumVals()) {
            if (ev.ord() == ord) return ev;
        }
        return null;
    }

    private boolean mapBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }
}

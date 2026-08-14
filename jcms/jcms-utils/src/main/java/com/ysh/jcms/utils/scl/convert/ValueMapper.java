package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclEnumType;
import com.ysh.jcms.utils.scl.model.template.SclEnumVal;

import java.util.Optional;

/**
 * bType → Java native type value mapper.
 * <p>
 * Responsible for two things:
 * <ol>
 * <li>bType + string value → Java native type (Integer, Float, etc.)</li>
 * <li>bidirectional lookup between enumeration value ord ↔ label</li>
 * </ol>
 */
public class ValueMapper {

    private final SclDataTypeTemplates templates;

    public ValueMapper(SclDataTypeTemplates templates) {
        this.templates = templates;
    }

    // ==================== Value conversion ====================

    /**
     * Converts a string value to the corresponding Java type according to the
     * bType.
     */
    public Optional<Object> mapValue(String bType, String value) {
        if (value == null)
            return Optional.empty();
        try {
            switch (bType) {
                case "BOOLEAN" :
                    return Optional.of(mapBoolean(value));
                case "INT8" :
                    return Optional.of(Byte.parseByte(value));
                case "INT16" :
                    return Optional.of(Short.parseShort(value));
                case "INT32" :
                    return Optional.of(Integer.parseInt(value));
                case "INT64" :
                    return Optional.of(Long.parseLong(value));
                case "INT128" :
                    return Optional.of(value);
                case "INT8U" :
                    return Optional.of(Short.parseShort(value));
                case "INT16U" :
                    return Optional.of(Integer.parseInt(value));
                case "INT24U" :
                    return Optional.of(Integer.parseInt(value));
                case "INT32U" :
                    return Optional.of(Long.parseLong(value));
                case "FLOAT32" :
                    return Optional.of(Float.parseFloat(value));
                case "FLOAT64" :
                    return Optional.of(Double.parseDouble(value));
                default :
                    return Optional.of(value);
            }
        } catch (NumberFormatException e) {
            return Optional.of(value);
        }
    }

    // ==================== Enumeration mapping ====================

    /** Finds the enumeration value label by enumeration type id and ord */
    public Optional<String> mapEnumValue(String enumTypeId, int ord) {
        if (templates == null)
            return Optional.empty();
        SclEnumVal ev = findEnumValByOrd(enumTypeId, ord);
        return Optional.ofNullable(ev != null ? ev.value() : null);
    }

    /** Finds the ord by enumeration type id and label */
    public Optional<Integer> mapEnumOrd(String enumTypeId, String value) {
        if (templates == null || value == null)
            return Optional.empty();
        SclEnumType enumType = templates.findEnumTypeById(enumTypeId);
        if (enumType == null)
            return Optional.empty();
        for (SclEnumVal ev : enumType.enumVals()) {
            if (value.equals(ev.value())) {
                return Optional.of(ev.ord());
            }
        }
        return Optional.empty();
    }

    private SclEnumVal findEnumValByOrd(String enumTypeId, int ord) {
        SclEnumType enumType = templates.findEnumTypeById(enumTypeId);
        if (enumType == null)
            return null;
        for (SclEnumVal ev : enumType.enumVals()) {
            if (ev.ord() == ord)
                return ev;
        }
        return null;
    }

    private boolean mapBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }
}

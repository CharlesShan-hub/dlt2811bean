package com.ysh.jcms.utils.scl.util;

import com.ysh.jcms.utils.scl.model.template.SclEnumType;
import com.ysh.jcms.utils.scl.model.template.SclEnumVal;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;

import java.util.Optional;

public class SclValueMapper {

    private final SclDataTypeTemplates templates;

    public SclValueMapper(SclDataTypeTemplates templates) {
        this.templates = templates;
    }

    public Optional<Object> mapValue(String bType, String value) {
        if (value == null) return Optional.empty();
        switch (bType) {
            case "BOOLEAN": return Optional.of(mapBoolean(value));
            case "INT8": return Optional.of(Byte.parseByte(value));
            case "INT16": return Optional.of(Short.parseShort(value));
            case "INT32": return Optional.of(Integer.parseInt(value));
            case "INT64": return Optional.of(Long.parseLong(value));
            case "INT128": return Optional.of(value);
            case "INT8U": return Optional.of(Short.parseShort(value));
            case "INT16U": return Optional.of(Integer.parseInt(value));
            case "INT24U": return Optional.of(Integer.parseInt(value));
            case "INT32U": return Optional.of(Long.parseLong(value));
            case "FLOAT32": return Optional.of(Float.parseFloat(value));
            case "FLOAT64": return Optional.of(Double.parseDouble(value));
            case "Enum":
            case "Dbpos":
            case "Check":
            case "CURRENCY":
            case "PHASE":
            case "QUALITY":
            case "TIMESTAMP":
            case "VisString32":
            case "VisString64":
            case "VisString129":
            case "VisString255":
            case "Unicode255":
            case "Octet64":
            default: return Optional.of(value);
        }
    }

    public Optional<String> mapEnumValue(String enumTypeId, int ord) {
        if (templates == null) return Optional.empty();
        SclEnumType enumType = templates.findEnumTypeById(enumTypeId);
        if (enumType == null) return Optional.empty();
        return Optional.ofNullable(enumType.findEnumValByOrdAsString(ord));
    }

    public Optional<Integer> mapEnumOrd(String enumTypeId, String value) {
        if (templates == null) return Optional.empty();
        SclEnumType enumType = templates.findEnumTypeById(enumTypeId);
        if (enumType == null) return Optional.empty();
        return enumType.getEnumVals().stream()
            .filter(ev -> ev.getValue().equals(value))
            .map(SclEnumVal::getOrd)
            .findFirst();
    }

    private boolean mapBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }
}

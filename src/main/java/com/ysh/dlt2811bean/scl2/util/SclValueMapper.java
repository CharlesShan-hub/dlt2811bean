package com.ysh.dlt2811bean.scl2.util;

import com.ysh.dlt2811bean.scl2.model.SclEnumType;
import com.ysh.dlt2811bean.scl2.model.SclEnumVal;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;

import java.util.Optional;

public class SclValueMapper {

    private final SclDataTypeTemplates templates;

    public SclValueMapper(SclDataTypeTemplates templates) {
        this.templates = templates;
    }

    public Optional<Object> mapValue(String bType, String value) {
        if (value == null) return Optional.empty();
        return switch (bType) {
            case "BOOLEAN" -> Optional.of(mapBoolean(value));
            case "INT8" -> Optional.of(Byte.parseByte(value));
            case "INT16" -> Optional.of(Short.parseShort(value));
            case "INT32" -> Optional.of(Integer.parseInt(value));
            case "INT64" -> Optional.of(Long.parseLong(value));
            case "INT128" -> Optional.of(value);
            case "INT8U" -> Optional.of(Short.parseShort(value));
            case "INT16U" -> Optional.of(Integer.parseInt(value));
            case "INT24U" -> Optional.of(Integer.parseInt(value));
            case "INT32U" -> Optional.of(Long.parseLong(value));
            case "FLOAT32" -> Optional.of(Float.parseFloat(value));
            case "FLOAT64" -> Optional.of(Double.parseDouble(value));
            case "Enum" -> Optional.of(value);
            case "Dbpos" -> Optional.of(value);
            case "Check" -> Optional.of(value);
            case "CURRENCY" -> Optional.of(value);
            case "PHASE" -> Optional.of(value);
            case "QUALITY" -> Optional.of(value);
            case "TIMESTAMP" -> Optional.of(value);
            case "VisString32" -> Optional.of(value);
            case "VisString64" -> Optional.of(value);
            case "VisString129" -> Optional.of(value);
            case "VisString255" -> Optional.of(value);
            case "Unicode255" -> Optional.of(value);
            case "Octet64" -> Optional.of(value);
            default -> Optional.of(value);
        };
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

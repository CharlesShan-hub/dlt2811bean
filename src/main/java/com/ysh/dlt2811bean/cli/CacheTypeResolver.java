package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.SclTypeResolver;

import java.util.Map;

/**
 * 从缓存中解析 DA 的类型信息，用于值解析。
 * <p>
 * 优先使用缓存中已保存的 DataDefinition 类型信息（如 BOOLEAN、INT32、UNICODE STRING 等），
 * 避免每次都去读取 SCL 文件。如果缓存中没有类型信息，返回 null 由调用方 fallback。
 */
public class CacheTypeResolver {

    /**
     * 从缓存中查找 DA 的类型，并解析用户输入的值。
     *
     * @param ctx   CLI 上下文（含缓存）
     * @param ref   DA 引用，格式为 LD/LN.DO.DA
     * @param value 用户输入的字符串值
     * @return 解析后的 CmsType，如果缓存中无类型信息则返回 null
     */
    public static CmsType<?> resolveFromCache(CliContext ctx, String ref, String value) {
        if (!ref.contains("/") || !ref.contains(".")) return null;
        String[] parts = ref.split("\\.");
        if (parts.length < 3) return null;
        String[] ldLn = parts[0].split("/", 2);
        if (ldLn.length < 2) return null;
        String ld = ldLn[0], ln = ldLn[1];
        String doName = parts[1], daName = parts[2];
        Map<String, Object> das = ctx.lnEntry(ld, ln).get("DATA_OBJECT");
        if (das == null) return null;
        Object doMap = das.get(doName);
        if (!(doMap instanceof Map)) return null;
        Object daEntry = ((Map<?, ?>) doMap).get(daName);
        if (!(daEntry instanceof Map)) return null;
        Object typeObj = ((Map<?, ?>) daEntry).get("type");
        if (!(typeObj instanceof String)) return null;
        String bType = cacheTypeToBType((String) typeObj);
        if (bType == null) return null;
        return SclTypeResolver.createTypedValue(bType, value);
    }

    /**
     * 将缓存中的类型名称映射为 SclTypeResolver.createTypedValue 可识别的 bType。
     */
    public static String cacheTypeToBType(String cacheType) {
        return switch (cacheType) {
            case "BOOLEAN" -> "BOOLEAN";
            case "INT8" -> "INT8";
            case "INT16" -> "INT16";
            case "INT32" -> "INT32";
            case "INT64" -> "INT64";
            case "INT8U" -> "INT8U";
            case "INT16U" -> "INT16U";
            case "INT32U" -> "INT32U";
            case "INT64U" -> "INT64U";
            case "FLOAT32" -> "FLOAT32";
            case "FLOAT64" -> "FLOAT64";
            case "BIT STRING" -> "INT32";
            case "OCTET STRING" -> "VisString255";
            case "VISIBLE STRING" -> "VISIBLE STRING";
            case "UNICODE STRING" -> "UNICODE STRING";
            case "Timestamp" -> "INT64U";
            case "Quality" -> "INT32";
            case "Dbpos" -> "Dbpos";
            case "Tcmd" -> "Tcmd";
            case "Check" -> "Check";
            case "Enum" -> "Enum";
            default -> null;
        };
    }
}

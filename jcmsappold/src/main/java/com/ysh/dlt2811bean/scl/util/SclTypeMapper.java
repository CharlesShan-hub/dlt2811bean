package com.ysh.dlt2811bean.scl.util;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.datatypes.numeric.*;
import com.ysh.dlt2811bean.datatypes.string.CmsUtf8String;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.query.SclQuery;
import com.ysh.dlt2811bean.scl.reader.SclReader;

public class SclTypeMapper {

    private SclTypeMapper() {
    }

    public static CmsType<?> createTypedValue(String bType, String value) {
        if (bType == null || value == null) {
            return new CmsVisibleString(value != null ? value : "").max(255);
        }
        try {
            return switch (bType) {
                case "BOOLEAN", "BOOL" -> new CmsBoolean(Boolean.parseBoolean(value.trim()));
                case "INT8" -> new CmsInt8(Integer.parseInt(value.trim()));
                case "INT16" -> new CmsInt16(Integer.parseInt(value.trim()));
                case "INT32" -> new CmsInt32(Integer.parseInt(value.trim()));
                case "INT64" -> new CmsInt64(Long.parseLong(value.trim()));
                case "INT8U" -> new CmsInt8U(Integer.parseInt(value.trim()));
                case "INT16U" -> new CmsInt16U(Integer.parseInt(value.trim()));
                case "INT32U" -> new CmsInt32U(Long.parseLong(value.trim()));
                case "INT64U" -> new CmsInt64U(new java.math.BigInteger(value.trim()));
                case "FLOAT32" -> new CmsFloat32(Float.parseFloat(value.trim()));
                case "FLOAT64" -> new CmsFloat64(Double.parseDouble(value.trim()));
                case "Enum", "Dbpos", "Tcmd" -> new CmsInt32(Integer.parseInt(value.trim()));
                case "VisString255", "VISIBLE STRING" -> new CmsVisibleString(value).max(255);
                case "Unicode255", "UNICODE STRING" -> new CmsUtf8String(value).max(255);
                case "Check" -> new CmsInt32(Integer.parseInt(value.trim()));
                default -> new CmsVisibleString(value).max(255);
            };
        } catch (Exception e) {
            return new CmsVisibleString(value).max(255);
        }
    }

    public static CmsType<?> parseControlValue(CmsConfig config, String ref, String value) {
        try {
            String sclPath = config.getServer().getResolvedSclFile();
            if (sclPath != null) {
                SclReader reader = new SclReader();
                SclQuery query = new SclQuery(reader.read(sclPath));
                CmsType<?> result = query.resolveBType(ref)
                    .map(bType -> createTypedValue(bType, value))
                    .orElse(null);
                if (result != null) return result;
            }
        } catch (Exception e) {
            // fall through
        }
        return new CmsBoolean(value.equalsIgnoreCase("true"));
    }

    public static CmsType<?> resolveTypedValue(CmsConfig config, String ref, String value) {
        if (ref != null) {
            String[] dotParts = ref.split("\\.");
            if (dotParts.length == 2) {
                return new CmsVisibleString(value).max(255);
            }
        }
        try {
            String sclPath = config.getServer().getResolvedSclFile();
            if (sclPath != null) {
                SclReader reader = new SclReader();
                SclQuery query = new SclQuery(reader.read(sclPath));
                CmsType<?> result = query.resolveBType(ref)
                    .map(bType -> createTypedValue(bType, value))
                    .orElse(null);
                if (result != null) return result;
            }
        } catch (Exception e) {
            // fall through
        }
        return new CmsVisibleString(value).max(255);
    }
}

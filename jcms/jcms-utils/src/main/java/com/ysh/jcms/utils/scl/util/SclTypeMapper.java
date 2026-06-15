package com.ysh.jcms.utils.scl.util;

import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsUtf8String;
import com.ysh.jcms.data.string.CmsVisibleString;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.utils.scl.query.SclQuery;
import com.ysh.jcms.utils.scl.reader.SclReader;

public class SclTypeMapper {

    private SclTypeMapper() {
    }

    public static CmsType<?> createTypedValue(String bType, String value) {
        if (bType == null || value == null) {
            return new CmsVisibleString(value != null ? value : "").max(255);
        }
        try {
            switch (bType) {
                case "BOOLEAN":
                case "BOOL":
                    return new CmsBoolean(Boolean.parseBoolean(value.trim()));
                case "INT8":
                    return new CmsInt8(Integer.parseInt(value.trim()));
                case "INT16":
                    return new CmsInt16(Integer.parseInt(value.trim()));
                case "INT32":
                    return new CmsInt32(Integer.parseInt(value.trim()));
                case "INT64":
                    return new CmsInt64(Long.parseLong(value.trim()));
                case "INT8U":
                    return new CmsInt8U(Integer.parseInt(value.trim()));
                case "INT16U":
                    return new CmsInt16U(Integer.parseInt(value.trim()));
                case "INT32U":
                    return new CmsInt32U(Long.parseLong(value.trim()));
                case "INT64U":
                    return new CmsInt64U(new java.math.BigInteger(value.trim()));
                case "FLOAT32":
                    return new CmsFloat32(Float.parseFloat(value.trim()));
                case "FLOAT64":
                    return new CmsFloat64(Double.parseDouble(value.trim()));
                case "Enum":
                case "Dbpos":
                case "Tcmd":
                    return new CmsInt32(Integer.parseInt(value.trim()));
                case "VisString255":
                case "VISIBLE STRING":
                    return new CmsVisibleString(value).max(255);
                case "Unicode255":
                case "UNICODE STRING":
                    return new CmsUtf8String(value).max(255);
                case "Check":
                    return new CmsInt32(Integer.parseInt(value.trim()));
                default:
                    return new CmsVisibleString(value).max(255);
            }
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

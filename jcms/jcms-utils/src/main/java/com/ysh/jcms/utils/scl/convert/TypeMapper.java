package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.choice.CmsData;

import java.nio.charset.StandardCharsets;

/**
 * Maps an SCL bType + string value to a {@link CmsData} CHOICE instance.
 *
 * <p>
 * Pure lookup; falls back to OCTET STRING on parse failure or unknown type.
 */
public final class TypeMapper {

    private TypeMapper() {
    }

    /**
     * Convert a bType + string value to a CmsData CHOICE.
     *
     * @param bType
     *            SCL bType, e.g. "INT32", "FLOAT32", "BOOLEAN"
     * @param value
     *            string value
     * @return CmsData instance; OCTET STRING fallback on failure
     */
    public static CmsData createTypedValue(String bType, String value) {
        if (bType == null || value == null) {
            return new CmsData().alt_visible_string(value != null ? value : "");
        }
        try {
            String v = value.trim();
            switch (bType) {
                case "BOOLEAN" :
                case "BOOL" :
                    return new CmsData().alt_boolean(Boolean.parseBoolean(v));
                case "INT8" :
                    return new CmsData().alt_int8(Integer.parseInt(v));
                case "INT16" :
                    return new CmsData().alt_int16(Integer.parseInt(v));
                case "INT32" :
                    return new CmsData().alt_int32(Integer.parseInt(v));
                case "INT64" :
                    return new CmsData().alt_int64(Long.parseLong(v));
                case "INT8U" :
                    return new CmsData().alt_int8u(Integer.parseInt(v));
                case "INT16U" :
                    return new CmsData().alt_int16u(Integer.parseInt(v));
                case "INT32U" :
                    return new CmsData().alt_int32u(Long.parseLong(v));
                case "INT64U" :
                    return new CmsData().alt_int64u(new java.math.BigInteger(v));
                case "FLOAT32" :
                    return new CmsData().alt_float32(Float.parseFloat(v));
                case "FLOAT64" :
                    return new CmsData().alt_float64(Double.parseDouble(v));
                case "Enum" :
                    return new CmsData().alt_int32(Integer.parseInt(v));
                case "Dbpos" :
                    return new CmsData().alt_dbpos(Integer.parseInt(v));
                case "Tcmd" :
                    return new CmsData().alt_tcmd(Integer.parseInt(v));
                case "VisString255" :
                case "VISIBLE STRING" :
                    return new CmsData().alt_visible_string(value);
                case "Unicode255" :
                case "UNICODE STRING" :
                    return new CmsData().alt_unicode_string(value);
                case "BIT_STRING" :
                case "BITSTRING" :
                    return new CmsData().alt_bit_string(value.getBytes(StandardCharsets.UTF_8));
                case "Check" : {
                    CmsCheck check = new CmsCheck();
                    check.value(Integer.parseInt(v));
                    return new CmsData().alt_check(check);
                }
                default :
                    return new CmsData().alt_octet_string(value.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            return new CmsData().alt_octet_string(value.getBytes(StandardCharsets.UTF_8));
        }
    }
}

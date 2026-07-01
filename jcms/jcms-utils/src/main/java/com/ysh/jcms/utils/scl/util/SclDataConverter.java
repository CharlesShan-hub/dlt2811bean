package com.ysh.jcms.utils.scl.util;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;

import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

/**
 * Converts {@link SclDataValue} (bType + value string) to {@link CmsData} CHOICE.
 *
 * <p>This is used by GetDataValues, GetDataSetValues, ReportEngine, and any
 * other service that needs to marshal SCL-stored values into PER-encodable
 * protocol types.
 */
public final class SclDataConverter {

    private SclDataConverter() {}

    /**
     * Convert an SclDataValue (bType + val string) to a {@link CmsData} CHOICE.
     *
     * @param sv the SCL data value, with bType and val fields
     * @return a populated CmsData instance, or a VISIBLE_STRING fallback on error
     */
    public static CmsData toCmsData(SclDataValue sv) {
        CmsData data = new CmsData();
        String bType = sv.bType != null ? sv.bType.toUpperCase() : "";
        String val = sv.val;
        try {
            switch (bType) {
                case "BOOLEAN":
                    data.choice(CmsData.CHOICE_BOOLEAN);
                    data.alt_boolean.value("true".equalsIgnoreCase(val) || "1".equals(val));
                    break;
                case "INT8":
                    data.choice(CmsData.CHOICE_INT8);
                    data.alt_int8.value(Byte.parseByte(val));
                    break;
                case "INT16":
                    data.choice(CmsData.CHOICE_INT16);
                    data.alt_int16.value(Short.parseShort(val));
                    break;
                case "INT32":
                case "ENUM":
                case "ENUMERATED":
                case "CODED_ENUM":
                    data.choice(CmsData.CHOICE_INT32);
                    data.alt_int32.value(Integer.parseInt(val));
                    break;
                case "INT64":
                    data.choice(CmsData.CHOICE_INT64);
                    data.alt_int64.value(Long.parseLong(val));
                    break;
                case "INT8U":
                    data.choice(CmsData.CHOICE_INT8U);
                    data.alt_int8u.value(Short.parseShort(val) & 0xFF);
                    break;
                case "INT16U":
                    data.choice(CmsData.CHOICE_INT16U);
                    data.alt_int16u.value(Integer.parseInt(val) & 0xFFFF);
                    break;
                case "INT32U":
                    data.choice(CmsData.CHOICE_INT32U);
                    data.alt_int32u.value(Long.parseLong(val) & 0xFFFFFFFFL);
                    break;
                case "INT64U":
                    data.choice(CmsData.CHOICE_INT64U);
                    data.alt_int64u.value(new BigInteger(val));
                    break;
                case "FLOAT32":
                    data.choice(CmsData.CHOICE_FLOAT32);
                    data.alt_float32.value(Float.parseFloat(val));
                    break;
                case "FLOAT64":
                    data.choice(CmsData.CHOICE_FLOAT64);
                    data.alt_float64.value(Double.parseDouble(val));
                    break;
                default:
                    if (bType.startsWith("OCTET_STRING")) {
                        data.choice(CmsData.CHOICE_OCTET_STRING);
                        data.alt_octet_string.value(val.getBytes(StandardCharsets.UTF_8));
                    } else if (bType.startsWith("BIT_STRING")) {
                        data.choice(CmsData.CHOICE_BIT_STRING);
                        data.alt_bit_string.value(val);
                    } else if (bType.startsWith("VISIBLE_STRING") || bType.startsWith("UNICODE") || bType.startsWith("UNICODE_STRING")) {
                        fillString(data, val);
                    } else {
                        fillString(data, val);
                    }
                    break;
            }
        } catch (Exception e) {
            fillString(data, val);
        }
        return data;
    }

    /**
     * Convert a value string to CmsData, auto-detecting booleans and integers.
     * Used when bType is unknown (e.g. ReportEngine reading raw val strings).
     */
    public static CmsData autoDetect(String val) {
        CmsData data = new CmsData();
        if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val)
                || "0".equals(val) || "1".equals(val)) {
            data.choice(CmsData.CHOICE_BOOLEAN);
            data.alt_boolean.value("true".equalsIgnoreCase(val) || "1".equals(val));
            return data;
        }
        try {
            long intVal = Long.parseLong(val);
            if (intVal >= Byte.MIN_VALUE && intVal <= Byte.MAX_VALUE) {
                data.choice(CmsData.CHOICE_INT8);
                data.alt_int8.value((int) intVal);
            } else if (intVal >= 0 && intVal <= 0xFFFFFFFFL) {
                data.choice(CmsData.CHOICE_INT32U);
                data.alt_int32u.value(intVal);
            } else if (intVal >= Integer.MIN_VALUE && intVal <= Integer.MAX_VALUE) {
                data.choice(CmsData.CHOICE_INT32);
                data.alt_int32.value((int) intVal);
            } else {
                fillString(data, val);
            }
        } catch (NumberFormatException e) {
            fillString(data, val);
        }
        return data;
    }

    private static void fillString(CmsData data, String val) {
        if (containsNonAscii(val)) {
            data.choice(CmsData.CHOICE_UNICODE_STRING);
            data.alt_unicode_string.value(val);
        } else {
            data.choice(CmsData.CHOICE_VISIBLE_STRING);
            data.alt_visible_string.value(val);
        }
    }

    private static boolean containsNonAscii(String s) {
        if (s == null) return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > 127) return true;
        }
        return false;
    }
}

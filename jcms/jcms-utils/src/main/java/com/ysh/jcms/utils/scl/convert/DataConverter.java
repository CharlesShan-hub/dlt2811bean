package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.data.V;
import com.ysh.jcms.core.data.bitarray.CmsQuality;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Converts {@link DataValueEntry} (bType + val) to a {@link CmsData} CHOICE for
 * PER encoding (GetDataValues, ReportEngine, ...).
 */
public final class DataConverter {

    private static final Logger log = LoggerFactory.getLogger(DataConverter.class);

    private DataConverter() {
    }

    /**
     * Convert a DataValueEntry to a CmsData CHOICE.
     */
    public static CmsData toCmsData(DataValueEntry dv) {
        String bType = dv.bType() != null ? dv.bType().toUpperCase() : "";
        String val = dv.val();
        log.debug("toCmsData bType={} ref={} val={}", bType, dv.ref(), val);
        try {
            switch (bType) {
                case "BOOLEAN" :
                    return new CmsData().alt_boolean("true".equalsIgnoreCase(val) || "1".equals(val));
                case "INT8" :
                    return new CmsData().alt_int8(Byte.parseByte(val));
                case "INT16" :
                    return new CmsData().alt_int16(Short.parseShort(val));
                case "INT32" :
                case "ENUM" :
                case "ENUMERATED" :
                case "CODED_ENUM" :
                    return new CmsData().alt_int32(Integer.parseInt(val));
                case "INT64" :
                    return new CmsData().alt_int64(Long.parseLong(val));
                case "INT8U" :
                    return new CmsData().alt_int8u(Short.parseShort(val) & 0xFF);
                case "INT16U" :
                    return new CmsData().alt_int16u(Integer.parseInt(val) & 0xFFFF);
                case "INT32U" :
                    return new CmsData().alt_int32u(Long.parseLong(val) & 0xFFFFFFFFL);
                case "INT64U" :
                    return new CmsData().alt_int64u(new BigInteger(val));
                case "FLOAT32" :
                    return new CmsData().alt_float32(Float.parseFloat(val));
                case "FLOAT64" :
                    return new CmsData().alt_float64(Double.parseDouble(val));
                case "QUALITY" : {
                    CmsQuality q = new CmsQuality();
                    V.setVal(q.inner._v, val);
                    q.syncFromInner();
                    return new CmsData().alt_quality(q);
                }
                case "UTC-TIME" :
                case "TIMESTAMP" : {
                    CmsUtcTime t = new CmsUtcTime();
                    V.setVal(t.inner._v, val);
                    t.syncFromInner();
                    return new CmsData().alt_utc_time(t);
                }
                case "BINARY-TIME" :
                case "ENTRYTIME" : {
                    CmsBinaryTime b = new CmsBinaryTime();
                    V.setVal(b.inner._v, val);
                    b.syncFromInner();
                    return new CmsData().alt_binary_time(b);
                }
                default :
                    if (bType.startsWith("OCTET_STRING"))
                        return new CmsData().alt_octet_string(val.getBytes(StandardCharsets.UTF_8));
                    if (bType.startsWith("BIT_STRING"))
                        return new CmsData().alt_bit_string(val.getBytes(StandardCharsets.UTF_8));
                    log.warn("toCmsData: unknown bType '{}' for ref {}, falling back to string", dv.bType(), dv.ref());
                    return fillString(val);
            }
        } catch (Exception e) {
            log.warn("toCmsData failed for bType={} ref={} val={}, falling back to string", dv.bType(), dv.ref(), val, e);
            return fillString(val);
        }
    }

    /**
     * Auto-detect type from a value string (used when bType is unknown, e.g.
     * ReportEngine).
     */
    public static CmsData autoDetect(String val) {
        if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val) || "0".equals(val) || "1".equals(val))
            return new CmsData().alt_boolean("true".equalsIgnoreCase(val) || "1".equals(val));
        try {
            long intVal = Long.parseLong(val);
            if (intVal >= Byte.MIN_VALUE && intVal <= Byte.MAX_VALUE)
                return new CmsData().alt_int8((int) intVal);
            if (intVal >= 0 && intVal <= 0xFFFFFFFFL)
                return new CmsData().alt_int32u(intVal);
            if (intVal >= Integer.MIN_VALUE && intVal <= Integer.MAX_VALUE)
                return new CmsData().alt_int32((int) intVal);
            return fillString(val);
        } catch (NumberFormatException e) {
            return fillString(val);
        }
    }

    private static CmsData fillString(String val) {
        if (containsNonAscii(val))
            return new CmsData().alt_unicode_string(val);
        return new CmsData().alt_visible_string(val);
    }

    private static boolean containsNonAscii(String s) {
        if (s == null)
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > 127)
                return true;
        }
        return false;
    }
}

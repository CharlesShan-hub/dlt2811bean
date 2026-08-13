package com.ysh.jcms.app.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.jcms.core.data.bitarray.CmsQuality;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;

import java.util.Map;

/**
 * Utility for converting raw string values into {@link CmsData} objects.
 * <p>
 * Handles JSON object values (quality, utc-time, binary-time) and plain string
 * values (visible-string, unicode-string). Shared by {@code SetDataValuesDao}
 * and {@code SetDataSetValuesDao}.
 */
public final class CmsDataFiller {

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

    private CmsDataFiller() {
    }

    /**
     * Parse a raw string value into a {@link CmsData} object.
     * <p>
     * JSON objects ({@code {...}}) are attempted as Quality, UTC-Time, or
     * Binary-Time in that order. Non-JSON values fall back to
     * {@code visible-string} or {@code unicode-string}.
     */
    public static void fillCmsData(CmsData data, String value) {
        if (value != null && value.startsWith("{")) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = MAPPER.readValue(value, Map.class);
                if (tryParseQuality(data, map))
                    return;
                if (tryParseUtcTime(data, map))
                    return;
                if (tryParseBinaryTime(data, map))
                    return;
            } catch (Exception e) {
                // fall through to plain string
            }
        }

        if (containsNonAscii(value)) {
            data.alt_unicode_string(value);
        } else {
            data.alt_visible_string(value);
        }
    }

    private static boolean tryParseQuality(CmsData data, Map<String, Object> map) {
        if (!map.containsKey("validity"))
            return false;
        CmsQuality q = new CmsQuality();
        q.validity = toInt(map.get("validity"));
        q.overflow = toBool(map.get("overflow"));
        q.outOfRange = toBool(map.get("outOfRange"));
        q.badReference = toBool(map.get("badReference"));
        q.oscillatory = toBool(map.get("oscillatory"));
        q.failure = toBool(map.get("failure"));
        q.oldData = toBool(map.get("oldData"));
        q.inconsistent = toBool(map.get("inconsistent"));
        q.inaccurate = toBool(map.get("inaccurate"));
        q.substituted = toBool(map.get("substituted"));
        q.test = toBool(map.get("test"));
        q.operatorBlocked = toBool(map.get("operatorBlocked"));
        data.alt_quality(q);
        return true;
    }

    private static boolean tryParseUtcTime(CmsData data, Map<String, Object> map) {
        if (!map.containsKey("secondsSinceEpoch"))
            return false;
        CmsUtcTime utc = new CmsUtcTime();
        utc.secondsSinceEpoch.value(toLong(map.get("secondsSinceEpoch")));
        utc.fractionOfSecond.value(toInt(map.get("fractionOfSecond")));
        @SuppressWarnings("unchecked")
        Map<String, Object> tq = (Map<String, Object>) map.get("timeQuality");
        if (tq != null) {
            CmsTimeQuality timeQuality = new CmsTimeQuality();
            timeQuality.leap_seconds_known = toBool(tq.get("leap_seconds_known"));
            timeQuality.clock_failure = toBool(tq.get("clock_failure"));
            timeQuality.clock_not_synchronized = toBool(tq.get("clock_not_synchronized"));
            timeQuality.precision = toInt(tq.get("precision"), 24);
            utc.timeQuality(timeQuality);
        }
        data.alt_utc_time(utc);
        return true;
    }

    private static boolean tryParseBinaryTime(CmsData data, Map<String, Object> map) {
        if (!map.containsKey("msOfDay"))
            return false;
        CmsBinaryTime bt = new CmsBinaryTime();
        bt.msOfDay.value(toLong(map.get("msOfDay")));
        bt.daysSince1984.value(toInt(map.get("daysSince1984")));
        data.alt_binary_time(bt);
        return true;
    }

    private static int toInt(Object v) {
        return toInt(v, 0);
    }

    private static int toInt(Object v, int def) {
        if (v instanceof Number)
            return ((Number) v).intValue();
        if (v instanceof String)
            try {
                return Integer.parseInt((String) v);
            } catch (Exception e) {
                /* ignore */
            }
        return def;
    }

    private static long toLong(Object v) {
        if (v instanceof Number)
            return ((Number) v).longValue();
        if (v instanceof String)
            try {
                return Long.parseLong((String) v);
            } catch (Exception e) {
                /* ignore */
            }
        return 0L;
    }

    private static boolean toBool(Object v) {
        if (v instanceof Boolean)
            return (Boolean) v;
        if (v instanceof String)
            return "true".equalsIgnoreCase((String) v);
        if (v instanceof Number)
            return ((Number) v).intValue() != 0;
        return false;
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

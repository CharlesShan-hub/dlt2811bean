package com.ysh.jcms.app.handler.data.setDataValues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.bitarray.CmsQuality;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.core.data.sequence.data.CmsDataRefValueEntry;
import com.ysh.jcms.core.pdu.data.CmsSetDataValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Accessors(fluent = true)
public class SetDataValuesDao extends BaseDao {

    /** Object references, e.g. "LD0/LLN0.Mod.stVal". */
    private List<String> references;

    /** Values to set (as strings), same order as references. */
    private List<String> values;

    /** Optional FunctionalConstraint codes, same order as references. */
    private List<String> fcs;

    @Override
    public CmsType toRequest() {
        // gray("LOG4 toRequest: refs=" + references + ", values=" + values + ", fcs=" +
        // fcs + ", pairCount="
        // + (references != null && values != null ? Math.min(references.size(),
        // values.size()) : 0));
        CmsSetDataValuesRequest req = new CmsSetDataValuesRequest();
        if (references != null && values != null) {
            int size = Math.min(references.size(), values.size());
            for (int i = 0; i < size; i++) {
                String ref = references.get(i);
                String value = values.get(i);
                if (ref == null || ref.isEmpty() || value == null)
                    continue;
                CmsDataRefValueEntry entry = new CmsDataRefValueEntry().reference(ref);
                fillCmsData(entry.value, value);
                if (fcs != null && i < fcs.size()) {
                    String fcStr = fcs.get(i);
                    if (fcStr != null && !fcStr.isEmpty()) {
                        int fc = Integer.parseInt(fcStr);
                        if (fc != 0)
                            entry.fc(fc);
                    }
                }
                req.data.add(entry);
            }
        }
        return req;
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

    private static void fillCmsData(CmsData data, String value) {
        // gray("LOG5 fillCmsData: rawValue=\"" + value + "\"");

        // 尝试 JSON 解析 → 复杂类型
        if (value != null && value.startsWith("{")) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = MAPPER.readValue(value, Map.class);
                if (tryParseQuality(data, map)) {
                    // gray("LOG6 fillCmsData: parsed as QUALITY, choice=" + data.choice() + ",
                    // value=" + value);
                    return;
                }
                if (tryParseUtcTime(data, map)) {
                    // gray("LOG6 fillCmsData: parsed as UTC_TIME, choice=" + data.choice() + ",
                    // value=" + value);
                    return;
                }
                if (tryParseBinaryTime(data, map)) {
                    // gray("LOG6 fillCmsData: parsed as BINARY_TIME, choice=" + data.choice() + ",
                    // value=" + value);
                    return;
                }
                // gray("LOG6 fillCmsData: JSON object but no matching complex type, falling
                // back to string: " + value);
            } catch (Exception e) {
                // gray("LOG6 fillCmsData: JSON parse failed, falling back to string: " + value
                // + " (" + e.getMessage() + ")");
            }
        }

        // 默认：字符串值
        if (containsNonAscii(value)) {
            data.alt_unicode_string(value);
            // gray("LOG6 fillCmsData: stored as UNICODE_STRING, choice=" + data.choice() +
            // ", value=" + value);
        } else {
            data.alt_visible_string(value);
            // gray("LOG6 fillCmsData: stored as VISIBLE_STRING, choice=" + data.choice() +
            // ", value=" + value);
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
                /* ignore */ }
        return def;
    }

    private static long toLong(Object v) {
        if (v instanceof Number)
            return ((Number) v).longValue();
        if (v instanceof String)
            try {
                return Long.parseLong((String) v);
            } catch (Exception e) {
                /* ignore */ }
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

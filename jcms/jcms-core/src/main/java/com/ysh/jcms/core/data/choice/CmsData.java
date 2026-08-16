package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.core.data.bitarray.CmsCheck;
import com.ysh.jcms.core.data.bitarray.CmsQuality;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.core.CmsChoice;
import com.ysh.jcms.data.DefaultInnerOctetString;
import com.ysh.jcms.data.DefaultInnerUtf8String;
import com.ysh.jcms.data.DefaultInnerVisibleString;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerData;
import com.ysh.jcms.data.V;
import com.ysh.jcms.core.data.enumerate.CmsDbpos;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.enumerate.CmsTcmd;
import com.ysh.jcms.core.data.scalar.*;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * Data ::= CHOICE {
 *     error          [0] IMPLICIT ServiceError,
 *     array          [1] IMPLICIT SEQUENCE OF Data,
 *     structure      [2] IMPLICIT SEQUENCE OF Data,
 *     boolean        [3] IMPLICIT BOOLEAN,
 *     int8           [4] IMPLICIT Int8,
 *     int16          [5] IMPLICIT Int16,
 *     int32          [6] IMPLICIT Int32,
 *     int64          [7] IMPLICIT Int64,
 *     int8u          [8] IMPLICIT Int8U,
 *     int16u         [9] IMPLICIT Int16U,
 *     int32u         [10] IMPLICIT Int32U,
 *     int64u         [11] IMPLICIT Int64U,
 *     float32        [12] IMPLICIT Float32,
 *     float64        [13] IMPLICIT Float64,
 *     bit-string     [14] IMPLICIT BIT STRING,
 *     octet-string   [15] IMPLICIT OCTET STRING,
 *     visible-string [16] IMPLICIT VisibleString,
 *     unicode-string [17] IMPLICIT UTF8String,
 *     utc-time       [18] IMPLICIT UtcTime,
 *     binary-time    [19] IMPLICIT BinaryTime,
 *     quality        [20] IMPLICIT Quality,
 *     dbpos          [21] IMPLICIT Dbpos,
 *     tcmd           [22] IMPLICIT Tcmd,
 *     check          [23] IMPLICIT Check
 * } — 7.7.1
 * }
 * </pre>
 *
 * <p>
 * CmsData wraps {@link InnerData} and maps between Cms* convenience types and
 * Inner* raw types.
 */
public class CmsData extends CmsChoice {

    /* ─── CHOICE indices ─── */
    public static final int CHOICE_ERROR = 0;
    public static final int CHOICE_ARRAY = 1;
    public static final int CHOICE_STRUCTURE = 2;
    public static final int CHOICE_BOOLEAN = 3;
    public static final int CHOICE_INT8 = 4;
    public static final int CHOICE_INT16 = 5;
    public static final int CHOICE_INT32 = 6;
    public static final int CHOICE_INT64 = 7;
    public static final int CHOICE_INT8U = 8;
    public static final int CHOICE_INT16U = 9;
    public static final int CHOICE_INT32U = 10;
    public static final int CHOICE_INT64U = 11;
    public static final int CHOICE_FLOAT32 = 12;
    public static final int CHOICE_FLOAT64 = 13;
    public static final int CHOICE_BIT_STRING = 14;
    public static final int CHOICE_OCTET_STRING = 15;
    public static final int CHOICE_VISIBLE_STRING = 16;
    public static final int CHOICE_UNICODE_STRING = 17;
    public static final int CHOICE_UTC_TIME = 18;
    public static final int CHOICE_BINARY_TIME = 19;
    public static final int CHOICE_QUALITY = 20;
    public static final int CHOICE_DBPOS = 21;
    public static final int CHOICE_TCMD = 22;
    public static final int CHOICE_CHECK = 23;

    /* ARRAY / STRUCTURE — same field, different inner fields */
    public List<CmsData> alt_sequence;

    /* @Choice-injected variants */
    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError alt_error;

    @Choice(index = 3, name = "Boolean", sync = Sync.SCALAR, innerField = "Boolean")
    public CmsBoolean alt_boolean;

    @Choice(index = 4, name = "int8", sync = Sync.SCALAR)
    public CmsInt8 alt_int8;

    @Choice(index = 5, name = "int16", sync = Sync.SCALAR)
    public CmsInt16 alt_int16;

    @Choice(index = 6, name = "int32", sync = Sync.SCALAR)
    public CmsInt32 alt_int32;

    @Choice(index = 7, name = "int64", sync = Sync.SCALAR)
    public CmsInt64 alt_int64;

    @Choice(index = 8, name = "int8u", sync = Sync.SCALAR)
    public CmsInt8U alt_int8u;

    @Choice(index = 9, name = "int16u", sync = Sync.SCALAR)
    public CmsInt16U alt_int16u;

    @Choice(index = 10, name = "int32u", sync = Sync.SCALAR)
    public CmsInt32U alt_int32u;

    @Choice(index = 11, name = "int64u", sync = Sync.SCALAR)
    public CmsInt64U alt_int64u;

    @Choice(index = 12, name = "float32", sync = Sync.WRAPPER)
    public CmsFloat32 alt_float32;

    @Choice(index = 13, name = "float64", sync = Sync.WRAPPER)
    public CmsFloat64 alt_float64;

    @Choice(index = 14, name = "bit-string", sync = Sync.RAW)
    public byte[] alt_bit_string;

    @Choice(index = 15, name = "octet-string", sync = Sync.INNER)
    public DefaultInnerOctetString alt_octet_string;

    @Choice(index = 16, name = "visible-string", sync = Sync.INNER)
    public DefaultInnerVisibleString alt_visible_string;

    @Choice(index = 17, name = "unicode-string", sync = Sync.INNER)
    public DefaultInnerUtf8String alt_unicode_string;

    @Choice(index = 18, name = "utc-time", sync = Sync.WRAPPER)
    public CmsUtcTime alt_utc_time;

    @Choice(index = 19, name = "binary-time", sync = Sync.WRAPPER)
    public CmsBinaryTime alt_binary_time;

    @Choice(index = 20, name = "quality", sync = Sync.WRAPPER)
    public CmsQuality alt_quality;

    @Choice(index = 21, name = "dbpos", sync = Sync.SCALAR)
    public CmsDbpos alt_dbpos;

    @Choice(index = 22, name = "tcmd", sync = Sync.SCALAR)
    public CmsTcmd alt_tcmd;

    @Choice(index = 23, name = "check", sync = Sync.WRAPPER)
    public CmsCheck alt_check;

    public CmsData() {
        super(new InnerData());
        this.alt_sequence = new ArrayList<>();
        this.alt_bit_string = new byte[0];
        this.alt_octet_string = new DefaultInnerOctetString();
        this.alt_visible_string = new DefaultInnerVisibleString();
        this.alt_unicode_string = new DefaultInnerUtf8String();
    }

    public CmsData choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters: set value + auto-select choice ─── */

    /** Select error + set value. */
    public CmsData alt_error(int v) {
        choice(CHOICE_ERROR);
        this.alt_error.value(v);
        return this;
    }
    /** Select Boolean + set value. */
    public CmsData alt_boolean(boolean v) {
        choice(CHOICE_BOOLEAN);
        this.alt_boolean.value(v);
        return this;
    }
    /** Select int8 + set value. */
    public CmsData alt_int8(int v) {
        choice(CHOICE_INT8);
        this.alt_int8.value(v);
        return this;
    }
    /** Select int16 + set value. */
    public CmsData alt_int16(int v) {
        choice(CHOICE_INT16);
        this.alt_int16.value(v);
        return this;
    }
    /** Select int32 + set value. */
    public CmsData alt_int32(int v) {
        choice(CHOICE_INT32);
        this.alt_int32.value(v);
        return this;
    }
    /** Select int64 + set value. */
    public CmsData alt_int64(long v) {
        choice(CHOICE_INT64);
        this.alt_int64.value(v);
        return this;
    }
    /** Select int8u + set value. */
    public CmsData alt_int8u(int v) {
        choice(CHOICE_INT8U);
        this.alt_int8u.value(v);
        return this;
    }
    /** Select int16u + set value. */
    public CmsData alt_int16u(int v) {
        choice(CHOICE_INT16U);
        this.alt_int16u.value(v);
        return this;
    }
    /** Select int32u + set value. */
    public CmsData alt_int32u(long v) {
        choice(CHOICE_INT32U);
        this.alt_int32u.value(v);
        return this;
    }
    /** Select int64u + set value. */
    public CmsData alt_int64u(java.math.BigInteger v) {
        choice(CHOICE_INT64U);
        this.alt_int64u.value(v);
        return this;
    }
    /** Select float32 + set value. */
    public CmsData alt_float32(float v) {
        choice(CHOICE_FLOAT32);
        this.alt_float32.value(v);
        return this;
    }
    /** Select float64 + set value. */
    public CmsData alt_float64(double v) {
        choice(CHOICE_FLOAT64);
        this.alt_float64.value(v);
        return this;
    }
    /** Select bit-string + set value. */
    public CmsData alt_bit_string(byte[] v) {
        choice(CHOICE_BIT_STRING);
        this.alt_bit_string = v;
        return this;
    }
    /** Select octet-string + set value. */
    public CmsData alt_octet_string(byte[] v) {
        choice(CHOICE_OCTET_STRING);
        V.setVal(this.alt_octet_string._v, v);
        return this;
    }
    /** Select visible-string + set value. */
    public CmsData alt_visible_string(String v) {
        choice(CHOICE_VISIBLE_STRING);
        V.setVal(this.alt_visible_string._v, v);
        return this;
    }
    /** Select unicode-string + set value. */
    public CmsData alt_unicode_string(String v) {
        choice(CHOICE_UNICODE_STRING);
        V.setVal(this.alt_unicode_string._v, v);
        return this;
    }
    /** Select dbpos + set value. */
    public CmsData alt_dbpos(int v) {
        choice(CHOICE_DBPOS);
        this.alt_dbpos.value(v);
        return this;
    }
    /** Select tcmd + set value. */
    public CmsData alt_tcmd(int v) {
        choice(CHOICE_TCMD);
        this.alt_tcmd.value(v);
        return this;
    }
    /** Select utc-time + set value. */
    public CmsData alt_utc_time(CmsUtcTime v) {
        choice(CHOICE_UTC_TIME);
        this.alt_utc_time.value(v);
        return this;
    }
    /** Select binary-time + set value. */
    public CmsData alt_binary_time(CmsBinaryTime v) {
        choice(CHOICE_BINARY_TIME);
        this.alt_binary_time.value(v);
        return this;
    }
    /** Select quality + set value. */
    public CmsData alt_quality(CmsQuality v) {
        choice(CHOICE_QUALITY);
        this.alt_quality.value(v);
        return this;
    }
    /** Select check + set value. */
    public CmsData alt_check(CmsCheck v) {
        choice(CHOICE_CHECK);
        this.alt_check.value(v);
        return this;
    }

    /**
     * Set choice and value in one call.
     */
    public CmsData value(int ch, Object val) {
        choice(ch);
        switch (ch) {
            case CHOICE_ERROR :
                alt_error.value((Integer) val);
                break;
            case CHOICE_BOOLEAN :
                alt_boolean.value((Boolean) val);
                break;
            case CHOICE_INT8 :
                alt_int8.value((Integer) val);
                break;
            case CHOICE_INT16 :
                alt_int16.value((Integer) val);
                break;
            case CHOICE_INT32 :
                alt_int32.value((Integer) val);
                break;
            case CHOICE_INT64 :
                alt_int64.value((Long) val);
                break;
            case CHOICE_INT8U :
                alt_int8u.value((Integer) val);
                break;
            case CHOICE_INT16U :
                alt_int16u.value((Integer) val);
                break;
            case CHOICE_INT32U :
                alt_int32u.value((Integer) val);
                break;
            case CHOICE_INT64U :
                alt_int64u.value((java.math.BigInteger) val);
                break;
            case CHOICE_FLOAT32 :
                alt_float32.value((Float) val);
                break;
            case CHOICE_FLOAT64 :
                alt_float64.value((Double) val);
                break;
            case CHOICE_BIT_STRING :
                alt_bit_string = (byte[]) val;
                break;
            case CHOICE_OCTET_STRING :
                V.setVal(alt_octet_string._v, (byte[]) val);
                break;
            case CHOICE_VISIBLE_STRING :
                V.setVal(alt_visible_string._v, (String) val);
                break;
            case CHOICE_UNICODE_STRING :
                V.setVal(alt_unicode_string._v, (String) val);
                break;
            case CHOICE_DBPOS :
                alt_dbpos.value((Integer) val);
                break;
            case CHOICE_TCMD :
                alt_tcmd.value((Integer) val);
                break;
            // CHOICE_UTC_TIME through CHOICE_CHECK use value(CmsType) instead
        }
        return this;
    }

    /** Copy choice selection and value from another CmsData (fluent). */
    public CmsData value(CmsData v) {
        int ch = v.choice();
        switch (ch) {
            case CHOICE_ERROR :
                return alt_error(v.alt_error.value());
            case CHOICE_BOOLEAN :
                return alt_boolean(v.alt_boolean.value());
            case CHOICE_INT8 :
                return alt_int8(v.alt_int8.value());
            case CHOICE_INT16 :
                return alt_int16(v.alt_int16.value());
            case CHOICE_INT32 :
                return alt_int32(v.alt_int32.value());
            case CHOICE_INT64 :
                return alt_int64(v.alt_int64.value());
            case CHOICE_INT8U :
                return alt_int8u(v.alt_int8u.value());
            case CHOICE_INT16U :
                return alt_int16u(v.alt_int16u.value());
            case CHOICE_INT32U :
                return alt_int32u(v.alt_int32u.value());
            case CHOICE_INT64U :
                return alt_int64u(v.alt_int64u.value());
            case CHOICE_FLOAT32 :
                return alt_float32(v.alt_float32.value());
            case CHOICE_FLOAT64 :
                return alt_float64(v.alt_float64.value());
            case CHOICE_BIT_STRING :
                this.alt_bit_string = v.alt_bit_string.clone();
                return choice(ch);
            case CHOICE_OCTET_STRING : {
                choice(ch);
                Object src = V.getVal(v.alt_octet_string._v);
                V.setVal(this.alt_octet_string._v, src instanceof byte[] ? ((byte[]) src).clone() : src);
                return this;
            }
            case CHOICE_VISIBLE_STRING :
                choice(ch);
                V.setVal(this.alt_visible_string._v, V.getVal(v.alt_visible_string._v));
                return this;
            case CHOICE_UNICODE_STRING :
                choice(ch);
                V.setVal(this.alt_unicode_string._v, V.getVal(v.alt_unicode_string._v));
                return this;
            case CHOICE_UTC_TIME :
                return alt_utc_time(v.alt_utc_time);
            case CHOICE_BINARY_TIME :
                return alt_binary_time(v.alt_binary_time);
            case CHOICE_QUALITY :
                return alt_quality(v.alt_quality);
            case CHOICE_DBPOS :
                return alt_dbpos(v.alt_dbpos.value());
            case CHOICE_TCMD :
                return alt_tcmd(v.alt_tcmd.value());
            case CHOICE_CHECK :
                return alt_check(v.alt_check);
            case CHOICE_ARRAY :
            case CHOICE_STRUCTURE : {
                choice(ch);
                this.alt_sequence.clear();
                for (CmsData e : v.alt_sequence) {
                    CmsData c = new CmsData();
                    c.value(e);
                    this.alt_sequence.add(c);
                }
                return this;
            }
            default :
                throw new IllegalArgumentException("Unknown Data choice: " + ch);
        }
    }

    /**
     * Convert JER choice map {@code {"variant": value}} to InnerData {@code _v}
     * form.
     */
    private static java.util.LinkedHashMap<String, Object> normalizeChoiceMap(java.util.LinkedHashMap<String, Object> m) {
        java.util.LinkedHashMap<String, Object> out = new java.util.LinkedHashMap<>();
        for (java.util.Map.Entry<String, Object> e : m.entrySet()) {
            if (e.getKey().startsWith("_")) {
                out.put(e.getKey(), e.getValue());
                continue;
            }
            out.put("_choice", e.getKey());
            Object val = e.getValue();
            if (val instanceof java.util.LinkedHashMap) {
                out.put("_", val);
            } else {
                java.util.LinkedHashMap<String, Object> w = new java.util.LinkedHashMap<>();
                V.setVal(w, val);
                out.put("_", w);
            }
            break;
        }
        return out;
    }

    @Override
    public void syncToInner() {
        int ch = choice();
        if (ch < 0)
            return;

        // Handle ARRAY/STRUCTURE (share alt_sequence, manual)
        if (ch == CHOICE_ARRAY || ch == CHOICE_STRUCTURE) {
            String name = ch == CHOICE_ARRAY ? "array" : "structure";
            inner._v.put("_choice", name);
            List<InnerBase> list = new ArrayList<>();
            for (CmsData elem : alt_sequence) {
                elem.syncToInner();
                list.add(elem.inner);
            }
            inner._v.put("_", list);
            return;
        }

        // Let base class handle the rest (via @Choice dispatch)
        super.syncToInner();
    }

    /**
     * Choice type name strings, indexed by choice() value.
     *
     * <pre>
     * CHOICE_NAMES[3] = "boolean", CHOICE_NAMES[6] = "int32", etc.
     * </pre>
     */
    public static final String[] CHOICE_NAMES = {"error", "array", "structure", "boolean", "int8", "int16", "int32", "int64", "int8u",
            "int16u", "int32u", "int64u", "float32", "float64", "bit-string", "octet-string", "visible-string", "unicode-string",
            "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check"};

    /**
     * Convert the value of this CmsData to a human-readable string, based on the
     * current choice() index.
     */
    public String toValueString() {
        int ct = choice();
        switch (ct) {
            case CHOICE_BOOLEAN :
                return Boolean.toString(alt_boolean.value());
            case CHOICE_INT8 :
                return Integer.toString(alt_int8.value());
            case CHOICE_INT16 :
                return Integer.toString(alt_int16.value());
            case CHOICE_INT32 :
                return Integer.toString(alt_int32.value());
            case CHOICE_INT64 :
                return Long.toString(alt_int64.value());
            case CHOICE_INT8U :
                return Integer.toString(alt_int8u.value());
            case CHOICE_INT16U :
                return Integer.toString(alt_int16u.value());
            case CHOICE_INT32U :
                return Long.toString(alt_int32u.value());
            case CHOICE_INT64U :
                return alt_int64u.value().toString();
            case CHOICE_FLOAT32 :
                return Float.toString(alt_float32.value());
            case CHOICE_FLOAT64 :
                return Double.toString(alt_float64.value());
            case CHOICE_VISIBLE_STRING :
                return (String) alt_visible_string.toJsonValue();
            case CHOICE_UNICODE_STRING :
                return (String) alt_unicode_string.toJsonValue();
            case CHOICE_OCTET_STRING :
                return (String) alt_octet_string.toJsonValue();
            case CHOICE_BIT_STRING :
                return new String(alt_bit_string, StandardCharsets.UTF_8);
            case CHOICE_QUALITY : {
                CmsQuality q = alt_quality;
                return "{\"validity\":" + q.validity + ",\"overflow\":" + q.overflow + ",\"outOfRange\":" + q.outOfRange
                        + ",\"badReference\":" + q.badReference + ",\"oscillatory\":" + q.oscillatory + ",\"failure\":" + q.failure
                        + ",\"oldData\":" + q.oldData + ",\"inconsistent\":" + q.inconsistent + ",\"inaccurate\":" + q.inaccurate
                        + ",\"substituted\":" + q.substituted + ",\"test\":" + q.test + ",\"operatorBlocked\":" + q.operatorBlocked + "}";
            }
            case CHOICE_UTC_TIME : {
                CmsUtcTime t = alt_utc_time;
                CmsTimeQuality tq = t.timeQuality;
                return "{\"secondsSinceEpoch\":" + t.secondsSinceEpoch.value() + ",\"fractionOfSecond\":" + t.fractionOfSecond.value()
                        + ",\"timeQuality\":{\"leap_seconds_known\":" + tq.leap_seconds_known + ",\"clock_failure\":" + tq.clock_failure
                        + ",\"clock_not_synchronized\":" + tq.clock_not_synchronized + ",\"precision\":" + tq.precision + "}}";
            }
            case CHOICE_BINARY_TIME : {
                CmsBinaryTime bt = alt_binary_time;
                return "{\"msOfDay\":" + bt.msOfDay.value() + ",\"daysSince1984\":" + bt.daysSince1984.value() + "}";
            }
            default :
                return "(choice=" + ct + ")";
        }
    }

    @Override
    public void syncFromInner() {
        Object ch = inner._v.get("_choice");
        if (!(ch instanceof String)) {
            normalizeVariant();
            ch = inner._v.get("_choice");
            if (!(ch instanceof String))
                return;
        }

        // Handle ARRAY/STRUCTURE (share alt_sequence, manual)
        if ("array".equals(ch) || "structure".equals(ch)) {
            selectedChoiceIndex = "array".equals(ch) ? CHOICE_ARRAY : CHOICE_STRUCTURE;
            Object raw = inner._v.get("_");
            if (raw instanceof java.util.LinkedHashMap) {
                // InnerData.setArray wraps the list as {"_": [...]}
                raw = V.getVal((java.util.Map<String, Object>) raw);
            }
            @SuppressWarnings("unchecked")
            List<Object> src = (List<Object>) raw;
            alt_sequence.clear();
            if (src != null) {
                for (Object elem : src) {
                    CmsData c = new CmsData();
                    if (elem instanceof InnerBase) {
                        c.inner = (InnerBase) elem;
                    } else if (elem instanceof java.util.LinkedHashMap) {
                        // Jackson-deserialized element: {"variant": value} → normalize to _v form
                        c.inner._v = normalizeChoiceMap((java.util.LinkedHashMap<String, Object>) elem);
                    } else {
                        continue;
                    }
                    c.syncFromInner();
                    alt_sequence.add(c);
                }
            }
            return;
        }

        // Let base class handle the rest (via @Choice dispatch)
        super.syncFromInner();
    }
}

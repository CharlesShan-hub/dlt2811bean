package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.bitarray.CmsQuality;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.enumerate.CmsDbpos;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.enumerate.CmsTcmd;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.sequence.time.CmsBinaryTime;
import com.ysh.jcms.data.sequence.time.CmsUtcTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Data ::= CHOICE { 24 alternatives } — 7.7
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
    @Choice(index = 0,  name = "error",            sync = Sync.WRAPPER)
    public CmsServiceError alt_error;

    @Choice(index = 3,  name = "Boolean",          sync = Sync.SCALAR, innerField = "Boolean")
    public CmsBoolean alt_boolean;

    @Choice(index = 4,  name = "int8",             sync = Sync.SCALAR)
    public CmsInt8 alt_int8;

    @Choice(index = 5,  name = "int16",            sync = Sync.SCALAR)
    public CmsInt16 alt_int16;

    @Choice(index = 6,  name = "int32",            sync = Sync.SCALAR)
    public CmsInt32 alt_int32;

    @Choice(index = 7,  name = "int64",            sync = Sync.SCALAR)
    public CmsInt64 alt_int64;

    @Choice(index = 8,  name = "int8u",            sync = Sync.SCALAR)
    public CmsInt8U alt_int8u;

    @Choice(index = 9,  name = "int16u",           sync = Sync.SCALAR)
    public CmsInt16U alt_int16u;

    @Choice(index = 10, name = "int32u",           sync = Sync.SCALAR)
    public CmsInt32U alt_int32u;

    @Choice(index = 11, name = "int64u",           sync = Sync.SCALAR)
    public CmsInt64U alt_int64u;

    @Choice(index = 12, name = "float32",          sync = Sync.WRAPPER)
    public CmsFloat32 alt_float32;

    @Choice(index = 13, name = "float64",          sync = Sync.WRAPPER)
    public CmsFloat64 alt_float64;

    @Choice(index = 14, name = "bit-string",       sync = Sync.RAW)
    public byte[] alt_bit_string;

    @Choice(index = 15, name = "octet-string",     sync = Sync.INNER)
    public DefaultInnerOctetString alt_octet_string;

    @Choice(index = 16, name = "visible-string",   sync = Sync.INNER)
    public DefaultInnerVisibleString alt_visible_string;

    @Choice(index = 17, name = "unicode-string",   sync = Sync.INNER)
    public DefaultInnerUtf8String alt_unicode_string;

    @Choice(index = 18, name = "utc-time",         sync = Sync.WRAPPER)
    public CmsUtcTime alt_utc_time;

    @Choice(index = 19, name = "binary-time",      sync = Sync.WRAPPER)
    public CmsBinaryTime alt_binary_time;

    @Choice(index = 20, name = "quality",          sync = Sync.WRAPPER)
    public CmsQuality alt_quality;

    @Choice(index = 21, name = "dbpos",            sync = Sync.SCALAR)
    public CmsDbpos alt_dbpos;

    @Choice(index = 22, name = "tcmd",             sync = Sync.SCALAR)
    public CmsTcmd alt_tcmd;

    @Choice(index = 23, name = "check",            sync = Sync.WRAPPER)
    public CmsCheck alt_check;

    public CmsData() {
        super(new InnerData());
        this.alt_sequence = new ArrayList<>();
        this.alt_bit_string = new byte[0];
        this.alt_octet_string = new DefaultInnerOctetString();
        this.alt_visible_string = new DefaultInnerVisibleString();
        this.alt_unicode_string = new DefaultInnerUtf8String();
    }

    public CmsData choice(int v) { super.choice(v); return this; }

    /* ─── Fluent setters: set value + auto-select choice ─── */

    /** Select error + set value. */
    public CmsData alt_error(int v) { choice(CHOICE_ERROR); this.alt_error.value(v); return this; }
    /** Select Boolean + set value. */
    public CmsData alt_boolean(boolean v) { choice(CHOICE_BOOLEAN); this.alt_boolean.value(v); return this; }
    /** Select int8 + set value. */
    public CmsData alt_int8(int v) { choice(CHOICE_INT8); this.alt_int8.value(v); return this; }
    /** Select int16 + set value. */
    public CmsData alt_int16(int v) { choice(CHOICE_INT16); this.alt_int16.value(v); return this; }
    /** Select int32 + set value. */
    public CmsData alt_int32(int v) { choice(CHOICE_INT32); this.alt_int32.value(v); return this; }
    /** Select int64 + set value. */
    public CmsData alt_int64(long v) { choice(CHOICE_INT64); this.alt_int64.value(v); return this; }
    /** Select int8u + set value. */
    public CmsData alt_int8u(int v) { choice(CHOICE_INT8U); this.alt_int8u.value(v); return this; }
    /** Select int16u + set value. */
    public CmsData alt_int16u(int v) { choice(CHOICE_INT16U); this.alt_int16u.value(v); return this; }
    /** Select int32u + set value. */
    public CmsData alt_int32u(long v) { choice(CHOICE_INT32U); this.alt_int32u.value(v); return this; }
    /** Select int64u + set value. */
    public CmsData alt_int64u(java.math.BigInteger v) { choice(CHOICE_INT64U); this.alt_int64u.value(v); return this; }
    /** Select float32 + set value. */
    public CmsData alt_float32(float v) { choice(CHOICE_FLOAT32); this.alt_float32.value(v); return this; }
    /** Select float64 + set value. */
    public CmsData alt_float64(double v) { choice(CHOICE_FLOAT64); this.alt_float64.value(v); return this; }
    /** Select bit-string + set value. */
    public CmsData alt_bit_string(byte[] v) { choice(CHOICE_BIT_STRING); this.alt_bit_string = v; return this; }
    /** Select octet-string + set value. */
    public CmsData alt_octet_string(byte[] v) { choice(CHOICE_OCTET_STRING); this.alt_octet_string.value = v; return this; }
    /** Select visible-string + set value. */
    public CmsData alt_visible_string(String v) { choice(CHOICE_VISIBLE_STRING); this.alt_visible_string.value = v; return this; }
    /** Select unicode-string + set value. */
    public CmsData alt_unicode_string(String v) { choice(CHOICE_UNICODE_STRING); this.alt_unicode_string.value = v; return this; }
    /** Select dbpos + set value. */
    public CmsData alt_dbpos(int v) { choice(CHOICE_DBPOS); this.alt_dbpos.value(v); return this; }
    /** Select tcmd + set value. */
    public CmsData alt_tcmd(int v) { choice(CHOICE_TCMD); this.alt_tcmd.value(v); return this; }

    /**
     * Set choice and value in one call.
     */
    public CmsData value(int ch, Object val) {
        choice(ch);
        switch (ch) {
            case CHOICE_ERROR:           alt_error.value((Integer) val); break;
            case CHOICE_BOOLEAN:         alt_boolean.value((Boolean) val); break;
            case CHOICE_INT8:            alt_int8.value((Integer) val); break;
            case CHOICE_INT16:           alt_int16.value((Integer) val); break;
            case CHOICE_INT32:           alt_int32.value((Integer) val); break;
            case CHOICE_INT64:           alt_int64.value((Long) val); break;
            case CHOICE_INT8U:           alt_int8u.value((Integer) val); break;
            case CHOICE_INT16U:          alt_int16u.value((Integer) val); break;
            case CHOICE_INT32U:          alt_int32u.value((Integer) val); break;
            case CHOICE_INT64U:          alt_int64u.value((java.math.BigInteger) val); break;
            case CHOICE_FLOAT32:         alt_float32.value((Float) val); break;
            case CHOICE_FLOAT64:         alt_float64.value((Double) val); break;
            case CHOICE_BIT_STRING:      alt_bit_string = (byte[]) val; break;
            case CHOICE_OCTET_STRING:    alt_octet_string.value = (byte[]) val; break;
            case CHOICE_VISIBLE_STRING:  alt_visible_string.value = (String) val; break;
            case CHOICE_UNICODE_STRING:  alt_unicode_string.value = (String) val; break;
            case CHOICE_DBPOS:           alt_dbpos.value((Integer) val); break;
            case CHOICE_TCMD:            alt_tcmd.value((Integer) val); break;
        }
        return this;
    }

    @Override
    public void syncToInner() {
        int ch = choice();
        if (ch < 0) return;
        InnerData i = (InnerData) inner;

        // Handle ARRAY/STRUCTURE (share alt_sequence, manual)
        if (ch == CHOICE_ARRAY || ch == CHOICE_STRUCTURE) {
            // Clear previous variant data from innerCache (keep "choice")
            innerCache.keySet().removeIf(k -> !"choice".equals(k));

            i._choice = (ch == CHOICE_ARRAY) ? "array" : "structure";
            List<InnerData> list = new ArrayList<>();
            for (CmsData elem : alt_sequence) {
                elem.syncToInner();
                list.add((InnerData) elem.inner);
            }
            if (ch == CHOICE_ARRAY) i.array = list;
            else i.structure = list;
            innerCache.put("alt_sequence", alt_sequence);
            return;
        }

        // Let base class handle the rest (via @Choice dispatch)
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        InnerData i = (InnerData) inner;
        String ch = i._choice;
        if (ch == null) { innerCache.put("choice", -1); return; }

        // Handle ARRAY/STRUCTURE (share alt_sequence, manual)
        if ("array".equals(ch) || "structure".equals(ch)) {
            selectedChoiceIndex = "array".equals(ch) ? CHOICE_ARRAY : CHOICE_STRUCTURE;
            // Clear previous variant data from innerCache (keep "choice")
            innerCache.keySet().removeIf(k -> !"choice".equals(k));

            innerCache.put("choice", selectedChoiceIndex);
            List<InnerData> src = "array".equals(ch) ? i.array : i.structure;
            alt_sequence.clear();
            if (src != null) {
                for (InnerData elem : src) {
                    CmsData c = new CmsData();
                    c.inner = elem;
                    c.syncFromInner();
                    alt_sequence.add(c);
                }
            }
            innerCache.put("alt_sequence", alt_sequence);
            return;
        }

        // Let base class handle the rest (via @Choice dispatch)
        super.syncFromInner();
    }
}

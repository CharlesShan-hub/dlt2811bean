package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsFormatUtil;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.core.CmsEnumerated;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsBitString;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.data.time.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.control.*;
import java.util.Arrays;
import java.util.List;

/**
 * Data ::= CHOICE { 24 alternatives }  —  7.7
 *
 * Flat all-pointer layout, matching cms_data_t in C:
 * [choice*, alt_sequence*, alt_boolean*, ..., alt_check*]
 *
 * nativeSize = 24 × 8 = 192 bytes
 */
public class CmsData extends CmsType {

    /* ─── CHOICE indices ─── */
    public static final int CHOICE_ERROR             = 0;
    public static final int CHOICE_ARRAY             = 1;
    public static final int CHOICE_STRUCTURE         = 2;
    public static final int CHOICE_BOOLEAN           = 3;
    public static final int CHOICE_INT8              = 4;
    public static final int CHOICE_INT16             = 5;
    public static final int CHOICE_INT32             = 6;
    public static final int CHOICE_INT64             = 7;
    public static final int CHOICE_INT8U             = 8;
    public static final int CHOICE_INT16U            = 9;
    public static final int CHOICE_INT32U            = 10;
    public static final int CHOICE_INT64U            = 11;
    public static final int CHOICE_FLOAT32           = 12;
    public static final int CHOICE_FLOAT64           = 13;
    public static final int CHOICE_BIT_STRING        = 14;
    public static final int CHOICE_OCTET_STRING      = 15;
    public static final int CHOICE_VISIBLE_STRING    = 16;
    public static final int CHOICE_UNICODE_STRING    = 17;
    public static final int CHOICE_UTC_TIME          = 18;
    public static final int CHOICE_BINARY_TIME       = 19;
    public static final int CHOICE_QUALITY           = 20;
    public static final int CHOICE_DBPOS             = 21;
    public static final int CHOICE_TCMD              = 22;
    public static final int CHOICE_CHECK             = 23;

    public CmsEnumerated          choice;           /* selector 0..23 */

    /* ARRAY / STRUCTURE — SEQUENCE OF Data */
    public CmsArray<CmsData>      alt_sequence;

    /* all alternatives */
    public CmsServiceError        alt_error;
    public CmsBoolean             alt_boolean;
    public CmsInt8                alt_int8;
    public CmsInt16               alt_int16;
    public CmsInt32               alt_int32;
    public CmsInt64               alt_int64;
    public CmsInt8U               alt_int8u;
    public CmsInt16U              alt_int16u;
    public CmsInt32U              alt_int32u;
    public CmsInt64U              alt_int64u;
    public CmsFloat32             alt_float32;
    public CmsFloat64             alt_float64;
    public CmsBitString           alt_bit_string;
    public CmsUint8Array          alt_octet_string;
    public CmsUint8Array          alt_visible_string;
    public CmsUint8Array          alt_unicode_string;
    public CmsUtcTime             alt_utc_time;
    public CmsBinaryTime          alt_binary_time;
    public CmsQuality             alt_quality;
    public CmsDbpos               alt_dbpos;
    public CmsTcmd                alt_tcmd;
    public CmsCheck               alt_check;

    public CmsData() { super(Codec.DATA);
        this.choice           = new CmsEnumerated();
        this.alt_sequence     = new CmsArray<>(CmsData.class);
        this.alt_error        = new CmsServiceError();
        this.alt_boolean      = new CmsBoolean();
        this.alt_int8         = new CmsInt8();
        this.alt_int16        = new CmsInt16();
        this.alt_int32        = new CmsInt32();
        this.alt_int64        = new CmsInt64();
        this.alt_int8u        = new CmsInt8U();
        this.alt_int16u       = new CmsInt16U();
        this.alt_int32u       = new CmsInt32U();
        this.alt_int64u       = new CmsInt64U();
        this.alt_float32      = new CmsFloat32();
        this.alt_float64      = new CmsFloat64();
        this.alt_bit_string   = new CmsBitString();
        this.alt_octet_string = new CmsUint8Array();
        this.alt_visible_string = new CmsUint8Array();
        this.alt_unicode_string = new CmsUint8Array();
        this.alt_utc_time     = new CmsUtcTime();
        this.alt_binary_time  = new CmsBinaryTime();
        this.alt_quality      = new CmsQuality();
        this.alt_dbpos        = new CmsDbpos();
        this.alt_tcmd         = new CmsTcmd();
        this.alt_check        = new CmsCheck();
    }
    
    public CmsData choice(int v) { this.choice.value(v); return this; }

    /**
     * Map CHOICE index → the correct child field for debug display.
     * Separate from children() which matches C struct memory layout.
     */
    private CmsType choiceChild() {
        int v = choice.value();
        switch (v) {
            case CHOICE_ERROR:             return alt_error;
            case CHOICE_ARRAY:
            case CHOICE_STRUCTURE:         return alt_sequence;
            case CHOICE_BOOLEAN:           return alt_boolean;
            case CHOICE_INT8:              return alt_int8;
            case CHOICE_INT16:             return alt_int16;
            case CHOICE_INT32:             return alt_int32;
            case CHOICE_INT64:             return alt_int64;
            case CHOICE_INT8U:             return alt_int8u;
            case CHOICE_INT16U:            return alt_int16u;
            case CHOICE_INT32U:            return alt_int32u;
            case CHOICE_INT64U:            return alt_int64u;
            case CHOICE_FLOAT32:           return alt_float32;
            case CHOICE_FLOAT64:           return alt_float64;
            case CHOICE_BIT_STRING:        return alt_bit_string;
            case CHOICE_OCTET_STRING:      return alt_octet_string;
            case CHOICE_VISIBLE_STRING:    return alt_visible_string;
            case CHOICE_UNICODE_STRING:    return alt_unicode_string;
            case CHOICE_UTC_TIME:          return alt_utc_time;
            case CHOICE_BINARY_TIME:       return alt_binary_time;
            case CHOICE_QUALITY:           return alt_quality;
            case CHOICE_DBPOS:             return alt_dbpos;
            case CHOICE_TCMD:              return alt_tcmd;
            case CHOICE_CHECK:             return alt_check;
            default:                       return null;
        }
    }

    @Override
    public String toString() {
        CmsType child = choiceChild();
        if (child == null) return "CHOICE {(null)}";

        // Build field name map for the child
        java.util.IdentityHashMap<CmsType, String> fieldNames = new java.util.IdentityHashMap<>();
        for (java.lang.reflect.Field f : child.getClass().getFields()) {
            if (CmsType.class.isAssignableFrom(f.getType())) {
                try { fieldNames.put((CmsType) f.get(child), f.getName()); } catch (Exception e) {}
            }
        }
        return "CHOICE {" + CmsFormatUtil.toString(child, 0, fieldNames) + "}";
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, alt_sequence,
            alt_boolean, alt_int8, alt_int16, alt_int32, alt_int64,
            alt_int8u, alt_int16u, alt_int32u, alt_int64u,
            alt_float32, alt_float64,
            alt_bit_string, alt_octet_string, alt_visible_string, alt_unicode_string,
            alt_utc_time, alt_binary_time, alt_quality,
            alt_dbpos, alt_tcmd, alt_check,
            alt_error);
    }
}
package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.control.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data ::= CHOICE { 24 alternatives } — 7.7
 * <p>
 * CmsData wraps {@link InnerData} and maps between Cms* convenience types and
 * Inner* raw types.
 */
public class CmsData extends CmsType {

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

    public int choice; /* selector 0..23 */

    /* ARRAY / STRUCTURE — SEQUENCE OF Data */
    public List<CmsData> alt_sequence;

    /* all alternatives */
    public CmsServiceError alt_error;
    public CmsBoolean alt_boolean;
    public CmsInt8 alt_int8;
    public CmsInt16 alt_int16;
    public CmsInt32 alt_int32;
    public CmsInt64 alt_int64;
    public CmsInt8U alt_int8u;
    public CmsInt16U alt_int16u;
    public CmsInt32U alt_int32u;
    public CmsInt64U alt_int64u;
    public CmsFloat32 alt_float32;
    public CmsFloat64 alt_float64;
    public byte[] alt_bit_string;
    public byte[] alt_octet_string;
    public String alt_visible_string;
    public String alt_unicode_string;
    public CmsUtcTime alt_utc_time;
    public CmsBinaryTime alt_binary_time;
    public CmsQuality alt_quality;
    public CmsDbpos alt_dbpos;
    public CmsTcmd alt_tcmd;
    public CmsCheck alt_check;

    public CmsData() {
        super(new InnerData());
        this.alt_sequence = new ArrayList<>();
        this.alt_error = new CmsServiceError();
        this.alt_boolean = new CmsBoolean();
        this.alt_int8 = new CmsInt8();
        this.alt_int16 = new CmsInt16();
        this.alt_int32 = new CmsInt32();
        this.alt_int64 = new CmsInt64();
        this.alt_int8u = new CmsInt8U();
        this.alt_int16u = new CmsInt16U();
        this.alt_int32u = new CmsInt32U();
        this.alt_int64u = new CmsInt64U();
        this.alt_float32 = new CmsFloat32();
        this.alt_float64 = new CmsFloat64();
        this.alt_bit_string = new byte[0];
        this.alt_octet_string = new byte[0];
        this.alt_visible_string = "";
        this.alt_unicode_string = "";
        this.alt_utc_time = new CmsUtcTime();
        this.alt_binary_time = new CmsBinaryTime();
        this.alt_quality = new CmsQuality();
        this.alt_dbpos = new CmsDbpos();
        this.alt_tcmd = new CmsTcmd();
        this.alt_check = new CmsCheck();
    }

    public CmsData choice(int v) { this.choice = v; return this; }

    @Override
    public void syncToInner() {
        InnerData i = (InnerData) inner;
        switch (choice) {
            case CHOICE_ERROR:
                i._choice = "error";
                alt_error.syncToInner();
                i.error = (InnerServiceError) alt_error.inner;
                break;
            case CHOICE_ARRAY:
            case CHOICE_STRUCTURE:
                i._choice = (choice == CHOICE_ARRAY) ? "array" : "structure";
                List<InnerData> list = new ArrayList<>();
                for (CmsData elem : alt_sequence) {
                    elem.syncToInner();
                    list.add((InnerData) elem.inner);
                }
                if (choice == CHOICE_ARRAY) i.array = list;
                else i.structure = list;
                break;
            case CHOICE_BOOLEAN:
                i._choice = "Boolean";
                if (i.Boolean == null) i.Boolean = new InnerBoolean();
                i.Boolean.value = alt_boolean.value() ? 1 : 0;
                break;
            case CHOICE_INT8:
                i._choice = "int8";
                if (i.int8 == null) i.int8 = new InnerInt8();
                i.int8.value = alt_int8.value();
                break;
            case CHOICE_INT16:
                i._choice = "int16";
                if (i.int16 == null) i.int16 = new InnerInt16();
                i.int16.value = alt_int16.value();
                break;
            case CHOICE_INT32:
                i._choice = "int32";
                if (i.int32 == null) i.int32 = new InnerInt32();
                i.int32.value = alt_int32.value();
                break;
            case CHOICE_INT64:
                i._choice = "int64";
                if (i.int64 == null) i.int64 = new InnerInt64();
                i.int64.value = alt_int64.value();
                break;
            case CHOICE_INT8U:
                i._choice = "int8u";
                if (i.int8u == null) i.int8u = new InnerInt8U();
                i.int8u.value = alt_int8u.value();
                break;
            case CHOICE_INT16U:
                i._choice = "int16u";
                if (i.int16u == null) i.int16u = new InnerInt16U();
                i.int16u.value = alt_int16u.value();
                break;
            case CHOICE_INT32U:
                i._choice = "int32u";
                if (i.int32u == null) i.int32u = new InnerInt32U();
                i.int32u.value = (int) alt_int32u.value();
                break;
            case CHOICE_INT64U:
                i._choice = "int64u";
                if (i.int64u == null) i.int64u = new InnerInt64U();
                i.int64u.value = alt_int64u.value().longValue();
                break;
            case CHOICE_FLOAT32:
                i._choice = "float32";
                alt_float32.syncToInner();
                i.float32 = (InnerFloat32) alt_float32.inner;
                break;
            case CHOICE_FLOAT64:
                i._choice = "float64";
                alt_float64.syncToInner();
                i.float64 = (InnerFloat64) alt_float64.inner;
                break;
            case CHOICE_BIT_STRING:
                i._choice = "bit-string";
                i.bit_string = alt_bit_string;
                break;
            case CHOICE_OCTET_STRING:
                i._choice = "octet-string";
                i.octet_string = alt_octet_string;
                break;
            case CHOICE_VISIBLE_STRING:
                i._choice = "visible-string";
                i.visible_string = alt_visible_string;
                break;
            case CHOICE_UNICODE_STRING:
                i._choice = "unicode-string";
                i.unicode_string = alt_unicode_string;
                break;
            case CHOICE_UTC_TIME:
                i._choice = "utc-time";
                alt_utc_time.syncToInner();
                i.utc_time = (InnerUtcTime) alt_utc_time.inner;
                break;
            case CHOICE_BINARY_TIME:
                i._choice = "binary-time";
                alt_binary_time.syncToInner();
                i.binary_time = (InnerBinaryTime) alt_binary_time.inner;
                break;
            case CHOICE_QUALITY:
                i._choice = "quality";
                alt_quality.syncToInner();
                i.quality = (InnerQuality) alt_quality.inner;
                break;
            case CHOICE_DBPOS:
                i._choice = "dbpos";
                if (i.dbpos == null) i.dbpos = new InnerDbpos();
                i.dbpos.value = alt_dbpos.value();
                break;
            case CHOICE_TCMD:
                i._choice = "tcmd";
                if (i.tcmd == null) i.tcmd = new InnerTcmd();
                i.tcmd.value = alt_tcmd.value();
                break;
            case CHOICE_CHECK:
                i._choice = "check";
                alt_check.syncToInner();
                i.check = (InnerCheck) alt_check.inner;
                break;
        }
    }

    @Override
    public void syncFromInner() {
        InnerData i = (InnerData) inner;
        String ch = i._choice;
        if (ch == null) { choice = -1; return; }
        switch (ch) {
            case "error":
                choice = CHOICE_ERROR;
                alt_error.inner = i.error;
                alt_error.syncFromInner();
                break;
            case "array":
            case "structure":
                choice = "array".equals(ch) ? CHOICE_ARRAY : CHOICE_STRUCTURE;
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
                break;
            case "Boolean":
                choice = CHOICE_BOOLEAN;
                alt_boolean.value(i.Boolean.value != 0);
                break;
            case "int8":
                choice = CHOICE_INT8;
                alt_int8.value(i.int8.value);
                break;
            case "int16":
                choice = CHOICE_INT16;
                alt_int16.value(i.int16.value);
                break;
            case "int32":
                choice = CHOICE_INT32;
                alt_int32.value(i.int32.value);
                break;
            case "int64":
                choice = CHOICE_INT64;
                alt_int64.value(i.int64.value);
                break;
            case "int8u":
                choice = CHOICE_INT8U;
                alt_int8u.value(i.int8u.value);
                break;
            case "int16u":
                choice = CHOICE_INT16U;
                alt_int16u.value(i.int16u.value);
                break;
            case "int32u":
                choice = CHOICE_INT32U;
                alt_int32u.value(i.int32u.value & 0xFFFFFFFFL);
                break;
            case "int64u":
                choice = CHOICE_INT64U;
                alt_int64u.value(java.math.BigInteger.valueOf(i.int64u.value));
                break;
            case "float32":
                choice = CHOICE_FLOAT32;
                alt_float32.inner = i.float32;
                alt_float32.syncFromInner();
                break;
            case "float64":
                choice = CHOICE_FLOAT64;
                alt_float64.inner = i.float64;
                alt_float64.syncFromInner();
                break;
            case "bit-string":
                choice = CHOICE_BIT_STRING;
                alt_bit_string = i.bit_string;
                break;
            case "octet-string":
                choice = CHOICE_OCTET_STRING;
                alt_octet_string = i.octet_string;
                break;
            case "visible-string":
                choice = CHOICE_VISIBLE_STRING;
                alt_visible_string = i.visible_string;
                break;
            case "unicode-string":
                choice = CHOICE_UNICODE_STRING;
                alt_unicode_string = i.unicode_string;
                break;
            case "utc-time":
                choice = CHOICE_UTC_TIME;
                alt_utc_time.inner = i.utc_time;
                alt_utc_time.syncFromInner();
                break;
            case "binary-time":
                choice = CHOICE_BINARY_TIME;
                alt_binary_time.inner = i.binary_time;
                alt_binary_time.syncFromInner();
                break;
            case "quality":
                choice = CHOICE_QUALITY;
                alt_quality.inner = i.quality;
                alt_quality.syncFromInner();
                break;
            case "dbpos":
                choice = CHOICE_DBPOS;
                alt_dbpos.value(i.dbpos.value);
                break;
            case "tcmd":
                choice = CHOICE_TCMD;
                alt_tcmd.value(i.tcmd.value);
                break;
            case "check":
                choice = CHOICE_CHECK;
                alt_check.inner = i.check;
                alt_check.syncFromInner();
                break;
        }
    }
}

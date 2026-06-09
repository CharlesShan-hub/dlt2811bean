package com.ysh.jcms2.data.choice;

import com.ysh.jcms2.core.CmsArray;
import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.data.enumerated.CmsEnumerated;
import com.ysh.jcms2.data.scalar.*;
import com.ysh.jcms2.data.string.CmsUint8Array;
import com.ysh.jcms2.data.time.*;
import com.ysh.jcms2.data.common.*;
import com.ysh.jcms2.data.control.*;
import java.util.Arrays;
import java.util.List;

/**
 * Data ::= CHOICE { 24 alternatives }  —  7.7
 *
 * For ARRAY / STRUCTURE (alternatives 1, 2), alt_sequence is a
 * CmsArray<CmsData> — elements are CmsData objects, count is auto-managed.
 *
 * nativeSize = (1 + 1 + 21) * 8 = 184 bytes
 */
public class CmsData extends CmsType {

    public CmsEnumerated          choice;           /* selector 0..23 */

    /* ARRAY / STRUCTURE — SEQUENCE OF Data */
    public CmsArray<CmsData>      alt_sequence;     /* CmsArray<CmsData> */

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
    public CmsUint8Array          alt_bit_string;
    public CmsUint8Array          alt_octet_string;
    public CmsUint8Array          alt_visible_string;
    public CmsUint8Array          alt_unicode_string;
    public CmsUtcTime             alt_utc_time;
    public CmsBinaryTime          alt_binary_time;
    public CmsQuality             alt_quality;
    public CmsDbpos               alt_dbpos;
    public CmsTcmd                alt_tcmd;
    public CmsCheck               alt_check;

    public CmsData() {
        this.choice           = new CmsEnumerated();
        this.alt_sequence     = new CmsArray<>();
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
        this.alt_bit_string   = new CmsUint8Array();
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

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, alt_sequence,
            alt_error, alt_boolean, alt_int8, alt_int16, alt_int32, alt_int64,
            alt_int8u, alt_int16u, alt_int32u, alt_int64u,
            alt_float32, alt_float64,
            alt_bit_string, alt_octet_string, alt_visible_string, alt_unicode_string,
            alt_utc_time, alt_binary_time, alt_quality,
            alt_dbpos, alt_tcmd, alt_check);
    }
}

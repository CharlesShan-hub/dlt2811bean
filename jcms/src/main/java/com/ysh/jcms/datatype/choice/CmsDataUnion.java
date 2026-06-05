package com.ysh.jcms.datatype.choice;

import com.sun.jna.Union;
import com.ysh.jcms.datatype.basic.*;
import com.ysh.jcms.datatype.common.CmsDbpos;
import com.ysh.jcms.datatype.common.CmsQuality;
import com.ysh.jcms.datatype.common.CmsServiceError;
import com.ysh.jcms.datatype.common.CmsTcmd;
import com.ysh.jcms.datatype.basic.*;
import com.ysh.jcms.datatype.common.*;
import com.ysh.jcms.datatype.control.CmsCheck;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import com.ysh.jcms.datatype.extended.CmsUtcTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataUnion extends Union {
    public CmsServiceError error = new CmsServiceError();               //  0
    public CmsDataArray array = new CmsDataArray();                     //  1
    public CmsDataStructure structure = new CmsDataStructure();         //  2
    public CmsBoolean boolean_value = new CmsBoolean();                 //  3
    public CmsInt8 int8 = new CmsInt8();                                //  4
    public CmsInt16 int16 = new CmsInt16();                             //  5
    public CmsInt32 int32 = new CmsInt32();                             //  6
    public CmsInt64 int64 = new CmsInt64();                             //  7
    public CmsInt8U int8u = new CmsInt8U();                             //  8
    public CmsInt16U int16u = new CmsInt16U();                          //  9
    public CmsInt32U int32u = new CmsInt32U();                          // 10
    public CmsInt64U int64u = new CmsInt64U();                          // 11
    public CmsFloat32 float32 = new CmsFloat32();                       // 12
    public CmsFloat64 float64 = new CmsFloat64();                       // 13
    public CmsUint8Array bit_string = new CmsUint8Array();              // 14
    public CmsUint8Array octet_string = new CmsUint8Array();            // 15
    public CmsUint8Array visible_string = new CmsUint8Array();          // 16
    public CmsUint8Array utf8_string = new CmsUint8Array();             // 17
    public CmsUtcTime utc_time = new CmsUtcTime();                      // 18
    public CmsBinaryTime binary_time = new CmsBinaryTime();             // 19
    public CmsQuality quality = new CmsQuality();                       // 20
    public CmsDbpos dbpos = new CmsDbpos();                             // 21
    public CmsTcmd tcmd = new CmsTcmd();                                // 22
    public CmsCheck check = new CmsCheck();                             // 23
}
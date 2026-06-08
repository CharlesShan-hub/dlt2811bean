package com.ysh.jcms.datatype.choice;

import com.sun.jna.Union;
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
    public CmsServiceError.ByValue error = new CmsServiceError.ByValue();                //  0
    public CmsDataArray.ByValue array = new CmsDataArray.ByValue();                      //  1
    public CmsDataStructure.ByValue structure = new CmsDataStructure.ByValue();          //  2
    public CmsBoolean.ByValue boolean_value = new CmsBoolean.ByValue();                  //  3
    public CmsInt8.ByValue int8 = new CmsInt8.ByValue();                                 //  4
    public CmsInt16.ByValue int16 = new CmsInt16.ByValue();                              //  5
    public CmsInt32.ByValue int32 = new CmsInt32.ByValue();                              //  6
    public CmsInt64.ByValue int64 = new CmsInt64.ByValue();                              //  7
    public CmsInt8U.ByValue int8u = new CmsInt8U.ByValue();                              //  8
    public CmsInt16U.ByValue int16u = new CmsInt16U.ByValue();                           //  9
    public CmsInt32U.ByValue int32u = new CmsInt32U.ByValue();                           // 10
    public CmsInt64U.ByValue int64u = new CmsInt64U.ByValue();                           // 11
    public CmsFloat32.ByValue float32 = new CmsFloat32.ByValue();                        // 12
    public CmsFloat64.ByValue float64 = new CmsFloat64.ByValue();                        // 13
    public CmsUint8Array.ByValue bit_string = new CmsUint8Array.ByValue();               // 14
    public CmsUint8Array.ByValue octet_string = new CmsUint8Array.ByValue();             // 15
    public CmsUint8Array.ByValue visible_string = new CmsUint8Array.ByValue();           // 16
    public CmsUint8Array.ByValue utf8_string = new CmsUint8Array.ByValue();              // 17
    public CmsUtcTime.ByValue utc_time = new CmsUtcTime.ByValue();                       // 18
    public CmsBinaryTime.ByValue binary_time = new CmsBinaryTime.ByValue();              // 19
    public CmsQuality.ByValue quality = new CmsQuality.ByValue();                        // 20
    public CmsDbpos.ByValue dbpos = new CmsDbpos.ByValue();                              // 21
    public CmsTcmd.ByValue tcmd = new CmsTcmd.ByValue();                                 // 22
    public CmsCheck.ByValue check = new CmsCheck.ByValue();                              // 23

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(
            "error", "array", "structure", "boolean_value", "int8", "int16", "int32", "int64",
            "int8u", "int16u", "int32u", "int64u", "float32", "float64",
            "bit_string", "octet_string", "visible_string", "utf8_string",
            "utc_time", "binary_time", "quality", "dbpos", "tcmd", "check"
        );
    }
}
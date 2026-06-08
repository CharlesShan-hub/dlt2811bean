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

    public Object get(int c) {
        switch (c) {
            case 0:  return error;
            case 1:  return array;
            case 2:  return structure;
            case 3:  return boolean_value;
            case 4:  return int8;
            case 5:  return int16;
            case 6:  return int32;
            case 7:  return int64;
            case 8:  return int8u;
            case 9:  return int16u;
            case 10: return int32u;
            case 11: return int64u;
            case 12: return float32;
            case 13: return float64;
            case 14: return bit_string;
            case 15: return octet_string;
            case 16: return visible_string;
            case 17: return utf8_string;
            case 18: return utc_time;
            case 19: return binary_time;
            case 20: return quality;
            case 21: return dbpos;
            case 22: return tcmd;
            case 23: return check;
            default: return int32;
        }
    }

    /** 根据 choice 值设置 union 的活跃字段。 */
    public void set(int c, Object val) {
        switch (c) {
            case 0:  error = new CmsServiceError.ByValue();   error.value = (int) val; break;
            case 3:  boolean_value = new CmsBoolean.ByValue(); boolean_value.value = (boolean) val; break;
            case 4:  int8 = new CmsInt8.ByValue();            int8.value = (byte) val; break;
            case 5:  int16 = new CmsInt16.ByValue();          int16.value = (short) val; break;
            case 6:  int32 = new CmsInt32.ByValue();          int32.value = (int) val; break;
            case 7:  int64 = new CmsInt64.ByValue();          int64.value = (long) val; break;
            case 8:  int8u = new CmsInt8U.ByValue();          int8u.value = (byte) val; break;
            case 9:  int16u = new CmsInt16U.ByValue();        int16u.value = (short) val; break;
            case 10: int32u = new CmsInt32U.ByValue();        int32u.value = (int) val; break;
            case 11: int64u = new CmsInt64U.ByValue();        int64u.value = (long) val; break;
            case 12: float32 = new CmsFloat32.ByValue();      float32.value = (float) val; break;
            case 13: float64 = new CmsFloat64.ByValue();      float64.value = (double) val; break;
            case 21: dbpos = new CmsDbpos.ByValue();          dbpos.value = (int) val; break;
            case 22: tcmd = new CmsTcmd.ByValue();            tcmd.value = (int) val; break;
            case 18: utc_time = (CmsUtcTime.ByValue) val; break;
            case 19: binary_time = (CmsBinaryTime.ByValue) val; break;
            case 20: quality = (CmsQuality.ByValue) val; break;
            case 23: check = (CmsCheck.ByValue) val; break;
            case 14: case 15: case 16: case 17:
                visible_string = val instanceof byte[]
                    ? new CmsUint8Array.ByValue().value((byte[]) val)
                    : (CmsUint8Array.ByValue) val;
                break;
            case 1:  array = (CmsDataArray.ByValue) val; break;
            case 2:  structure = (CmsDataStructure.ByValue) val; break;
            default: throw new IllegalArgumentException("unsupported choice " + c);
        }
    }
}
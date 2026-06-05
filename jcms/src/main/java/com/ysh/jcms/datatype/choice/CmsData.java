package com.ysh.jcms.datatype.choice;
import com.sun.jna.Structure;

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
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsData extends CmsType {
    public static final int ERROR          = 0;
    public static final int ARRAY          = 1;
    public static final int STRUCTURE      = 2;
    public static final int BOOLEAN        = 3;
    public static final int INT8           = 4;
    public static final int INT16          = 5;
    public static final int INT32          = 6;
    public static final int INT64          = 7;
    public static final int INT8U          = 8;
    public static final int INT16U         = 9;
    public static final int INT32U         = 10;
    public static final int INT64U         = 11;
    public static final int FLOAT32        = 12;
    public static final int FLOAT64        = 13;
    public static final int BIT_STRING     = 14;
    public static final int OCTET_STRING   = 15;
    public static final int VISIBLE_STRING = 16;
    public static final int UTF8_STRING    = 17;
    public static final int UTC_TIME       = 18;
    public static final int BINARY_TIME    = 19;
    public static final int QUALITY        = 20;
    public static final int DBPOS          = 21;
    public static final int TCMD           = 22;
    public static final int CHECK          = 23;

    public int choice;
    public CmsDataUnion value = new CmsDataUnion();

    public CmsData() {
        super();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("choice", "value");
    }

    @Override
    protected int encodeBufSize() { return 4096; }

    @Override
    public byte[] encode() {
        value.setType(unionClass(choice));
        return super.encode();
    }

    @Override
    public CmsData decode(byte[] data) {
        super.decode(data);
        value.setType(unionClass(choice));
        return this;
    }

    private static Class<?> unionClass(int c) {
        switch (c) {
            case 0:  return CmsServiceError.class;
            case 1:  return CmsDataArray.class;
            case 2:  return CmsDataStructure.class;
            case 3:  return CmsBoolean.class;
            case 4:  return CmsInt8.class;
            case 5:  return CmsInt16.class;
            case 6:  return CmsInt32.class;
            case 7:  return CmsInt64.class;
            case 8:  return CmsInt8U.class;
            case 9:  return CmsInt16U.class;
            case 10: return CmsInt32U.class;
            case 11: return CmsInt64U.class;
            case 12: return CmsFloat32.class;
            case 13: return CmsFloat64.class;
            case 14: return CmsUint8Array.class;
            case 15: return CmsUint8Array.class;
            case 16: return CmsUint8Array.class;
            case 17: return CmsUint8Array.class;
            case 18: return CmsUtcTime.class;
            case 19: return CmsBinaryTime.class;
            case 20: return CmsQuality.class;
            case 21: return CmsDbpos.class;
            case 22: return CmsTcmd.class;
            case 23: return CmsCheck.class;
            default: return CmsInt32.class;
        }
    }

    public static class ByValue extends CmsData implements Structure.ByValue {}
}
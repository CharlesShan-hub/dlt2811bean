package com.ysh.jcms.datatype.choice;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.common.CmsServiceError;
import com.ysh.jcms.datatype.basic.CmsInt32;
import com.ysh.jcms.ffi.CmsType;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataDefinition extends CmsType {
    public static final int ERROR      = 0;
    public static final int ARRAY      = 1;
    public static final int STRUCTURE  = 2;
    public static final int BOOLEAN    = 3;
    public static final int INT8       = 4;
    public static final int INT16      = 5;
    public static final int INT32      = 6;
    public static final int INT64      = 7;
    public static final int INT8U      = 8;
    public static final int INT16U     = 9;
    public static final int INT32U     = 10;
    public static final int INT64U     = 11;
    public static final int FLOAT32    = 12;
    public static final int FLOAT64    = 13;
    public static final int VISIBLE_STRING = 14;
    public static final int OCTET_STRING   = 15;
    public static final int BIT_STRING     = 16;
    public static final int UTF8_STRING    = 17;
    public static final int UTC_TIME       = 18;
    public static final int BINARY_TIME    = 19;
    public static final int QUALITY        = 20;
    public static final int DBPOS          = 21;
    public static final int TCMD           = 22;
    public static final int CHECK          = 23;

    public int choice;
    public CmsDataDefinitionUnion value = new CmsDataDefinitionUnion();

    public CmsDataDefinition() {
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
    public CmsDataDefinition decode(byte[] data) {
        super.decode(data);
        value.setType(unionClass(choice));
        return this;
    }

    private static Class<?> unionClass(int c) {
        switch (c) {
            case 0:  return CmsServiceError.class;
            case 1:  return CmsDataDefinitionArray.class;
            case 2:  return CmsDataDefinitionStructure.class;
            default: return CmsInt32.class;  // 3-23 all use string_length (int32)
        }
    }

    public static class ByValue extends CmsDataDefinition implements Structure.ByValue {}
}
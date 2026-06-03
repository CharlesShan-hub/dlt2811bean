package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerBitString;

public class CmsQuality extends AbstractCmsCodedEnum<CmsQuality> {

    public static final int GOOD              = 100;
    public static final int INVALID           = 101;
    public static final int RESERVED_VALIDITY = 102;
    public static final int QUESTIONABLE      = 103;

    public static final int INVALID1 = 0;
    public static final int INVALID2 = 1;
    public static final int OVERFLOW     = 2;
    public static final int OUT_OF_RANGE = 3;
    public static final int BAD_REFERENCE = 4;
    public static final int OSCILLATORY  = 5;
    public static final int FAILURE      = 6;
    public static final int OLD_DATA     = 7;
    public static final int INCONSISTENT = 8;
    public static final int INACCURATE   = 9;
    public static final int SOURCE       = 10;
    public static final int TEST         = 11;
    public static final int OPERATOR_BLOCKED = 12;

    public CmsQuality() {
        this(0);
    }

    public CmsQuality(int value) {
        super("Quality", value, 13);
    }

    @Override
    public void setBit(int pos, boolean val) {
        if (pos == GOOD)             { super.setBit(0, false); super.setBit(1, false); return; }
        if (pos == INVALID)          { super.setBit(0, false); super.setBit(1, true); return; }
        if (pos == RESERVED_VALIDITY){ super.setBit(0, true); super.setBit(1, false); return; }
        if (pos == QUESTIONABLE)     { super.setBit(0, true); super.setBit(1, true); return; }
        if (pos == INVALID1)         throw new IllegalArgumentException("INVALID1 is reserved");
        if (pos == INVALID2)         throw new IllegalArgumentException("INVALID2 is reserved");
        super.setBit(pos, val);
    }

    @Override
    public boolean testBit(int pos) {
        if (pos == GOOD)              return !super.testBit(0) && !super.testBit(1);
        if (pos == INVALID)           return !super.testBit(0) &&  super.testBit(1);
        if (pos == RESERVED_VALIDITY) return  super.testBit(0) && !super.testBit(1);
        if (pos == QUESTIONABLE)      return  super.testBit(0) &&  super.testBit(1);
        if (pos == INVALID1)         throw new IllegalArgumentException("INVALID1 is reserved");
        if (pos == INVALID2)         throw new IllegalArgumentException("INVALID2 is reserved");
        return super.testBit(pos);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_quality_encode(toPerBytes(), buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerBitString.encodeFixedSize(pos, toPerBytes(), size);
    }

    public static CmsQuality decode(byte[] data) {
       if (CmsFFIDatatypes.isAvailable()) {
           byte[] val = new byte[2];
           CmsFFIDatatypes.Holder.INSTANCE.cms_quality_decode(data, data.length, val);
           return new CmsQuality(fromPerBytes(val, 13));
       }
        return new CmsQuality(fromPerBytes(PerBitString.decodeFixedSizeBytes(new PerInputStream(data), 13), 13));
    }
}

package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerInt8;

/**
 * Wraps {@link InnerInt8} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt8 extends CmsType {

    public CmsInt8() {
        super(new InnerInt8());
    }
    public CmsInt8(byte value) {
        this();
        ((InnerInt8) inner).value = value;
    }
    public CmsInt8(int value) {
        this();
        value(value);
    }

    public byte value() {
        return (byte) ((InnerInt8) inner).value;
    }
    public CmsInt8 value(byte v) {
        ((InnerInt8) inner).value = v;
        return this;
    }
    public CmsInt8 value(int v) {
        if (v < Byte.MIN_VALUE || v > Byte.MAX_VALUE)
            throw new IllegalArgumentException("CmsInt8 out of range [" + Byte.MIN_VALUE + "," + Byte.MAX_VALUE + "]: " + v);
        ((InnerInt8) inner).value = v;
        return this;
    }
}

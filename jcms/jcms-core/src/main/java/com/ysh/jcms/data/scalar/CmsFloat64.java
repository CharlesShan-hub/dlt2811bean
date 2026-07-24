package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerFloat64;
import java.nio.ByteBuffer;

/**
 * Wraps {@link InnerFloat64} for PER encode/decode via Rust (libasn1.so).
 * InnerFloat64 stores the double as an 8-byte OCTET STRING (IEEE 754 big-endian).
 */
public class CmsFloat64 extends CmsType {

    public CmsFloat64() {
        super(new InnerFloat64());
    }
    public CmsFloat64(double value) {
        this();
        ((InnerFloat64) inner).value = doubleToBytes(value);
    }

    public double value() {
        return bytesToDouble(((InnerFloat64) inner).value);
    }
    public CmsFloat64 value(double v) {
        ((InnerFloat64) inner).value = doubleToBytes(v);
        return this;
    }

    private static byte[] doubleToBytes(double v) {
        return ByteBuffer.allocate(8).putLong(Double.doubleToLongBits(v)).array();
    }
    private static double bytesToDouble(byte[] b) {
        return Double.longBitsToDouble(ByteBuffer.wrap(b).getLong());
    }
}

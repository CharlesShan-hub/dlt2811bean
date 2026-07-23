package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerFloat64;
import java.nio.ByteBuffer;

/**
 * Wraps {@link InnerFloat64} for PER encode/decode via Rust (libasn1.so).
 * InnerFloat64 stores the double as an 8-byte OCTET STRING (IEEE 754 big-endian).
 */
public class CmsFloat64 extends CmsType {

    private transient InnerFloat64 inner = new InnerFloat64();

    public CmsFloat64() {
        super(Codec.FLOAT64);
    }
    public CmsFloat64(double value) {
        super(Codec.FLOAT64);
        inner.value = doubleToBytes(value);
    }

    public double value() {
        return bytesToDouble(inner.value);
    }
    public CmsFloat64 value(double v) {
        inner.value = doubleToBytes(v);
        return this;
    }

    private static byte[] doubleToBytes(double v) {
        return ByteBuffer.allocate(8).putLong(Double.doubleToLongBits(v)).array();
    }
    private static double bytesToDouble(byte[] b) {
        return Double.longBitsToDouble(ByteBuffer.wrap(b).getLong());
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerFloat64.decode(data);
    }

    @Override
    protected int calcNativeSize() {
        return 8;
    }
    @Override
    public void write() {
        nativePtr.setDouble(0, bytesToDouble(inner.value));
    }
    @Override
    public void read() {
        inner.value = doubleToBytes(nativePtr.getDouble(0));
    }
}

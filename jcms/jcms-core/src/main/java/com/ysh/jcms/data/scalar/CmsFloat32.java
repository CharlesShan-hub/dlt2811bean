package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerFloat32;
import java.nio.ByteBuffer;

/**
 * Wraps {@link InnerFloat32} for PER encode/decode via Rust (libasn1.so).
 * InnerFloat32 stores the float as a 4-byte OCTET STRING (IEEE 754 big-endian).
 */
public class CmsFloat32 extends CmsType {

    private transient InnerFloat32 inner = new InnerFloat32();

    public CmsFloat32() {
        super(Codec.FLOAT32);
    }
    public CmsFloat32(float value) {
        super(Codec.FLOAT32);
        inner.value = floatToBytes(value);
    }

    public float value() {
        return bytesToFloat(inner.value);
    }
    public CmsFloat32 value(float v) {
        inner.value = floatToBytes(v);
        return this;
    }

    private static byte[] floatToBytes(float v) {
        return ByteBuffer.allocate(4).putInt(Float.floatToIntBits(v)).array();
    }
    private static float bytesToFloat(byte[] b) {
        return Float.intBitsToFloat(ByteBuffer.wrap(b).getInt());
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerFloat32.decode(data);
    }

    @Override
    protected int calcNativeSize() {
        return 4;
    }
    @Override
    public void write() {
        nativePtr.setFloat(0, bytesToFloat(inner.value));
    }
    @Override
    public void read() {
        inner.value = floatToBytes(nativePtr.getFloat(0));
    }
}

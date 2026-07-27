package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsFixedOctet;
import com.ysh.jcms.data.InnerFloat32;
import java.nio.ByteBuffer;

/**
 * Wraps {@link InnerFloat32} for PER encode/decode via Rust (libasn1.so).
 * InnerFloat32 stores the float as a 4-byte OCTET STRING (IEEE 754 big-endian).
 */
public class CmsFloat32 extends CmsFixedOctet {

    public CmsFloat32() {
        super(new InnerFloat32());
    }
    public CmsFloat32(float value) {
        this();
        innerSet(floatToBytes(value));
    }

    public float value() {
        byte[] b = (byte[]) innerGet();
        return bytesToFloat(b);
    }
    public CmsFloat32 value(float v) {
        innerSet(floatToBytes(v));
        return this;
    }

    private static byte[] floatToBytes(float v) {
        return ByteBuffer.allocate(4).putInt(Float.floatToIntBits(v)).array();
    }
    private static float bytesToFloat(byte[] b) {
        return Float.intBitsToFloat(ByteBuffer.wrap(b).getInt());
    }
}

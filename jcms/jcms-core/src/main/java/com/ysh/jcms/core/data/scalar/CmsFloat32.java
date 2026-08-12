package com.ysh.jcms.core.data.scalar;

import com.ysh.jcms.data.core.CmsScalar;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerFloat32;
import java.nio.ByteBuffer;

/**
 * <pre>
 * {@code
 * Float32 ::= OCTET STRING (SIZE (4)) — 7.1.4
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerFloat32} for PER encode/decode via Rust (libasn1.so).
 * InnerFloat32 stores the float as a 4-byte OCTET STRING (IEEE 754 big-endian).
 */
public class CmsFloat32 extends CmsScalar {

    public CmsFloat32() {
        super(new InnerFloat32());
    }
    public CmsFloat32(float value) {
        this();
        innerSet(floatToBytes(value));
    }

    public float value() {
        Object v = innerGet();
        byte[] b;
        if (v instanceof byte[]) {
            b = (byte[]) v;
        } else if (v instanceof String) {
            b = InnerBase.unhex((String) v);
        } else {
            return 0f;
        }
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

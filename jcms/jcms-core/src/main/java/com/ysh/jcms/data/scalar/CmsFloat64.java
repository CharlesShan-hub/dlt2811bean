package com.ysh.jcms.data.scalar;

import com.ysh.jcms.data.core.CmsScalar;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerFloat64;
import java.nio.ByteBuffer;

/**
 * <pre>
 * {@code
 * Float64 ::= OCTET STRING (SIZE (8)) — 7.1.4
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerFloat64} for PER encode/decode via Rust (libasn1.so).
 * InnerFloat64 stores the double as an 8-byte OCTET STRING (IEEE 754
 * big-endian).
 */
public class CmsFloat64 extends CmsScalar {

    public CmsFloat64() {
        super(new InnerFloat64());
    }
    public CmsFloat64(double value) {
        this();
        innerSet(doubleToBytes(value));
    }

    public double value() {
        Object v = innerGet();
        byte[] b;
        if (v instanceof byte[]) {
            b = (byte[]) v;
        } else if (v instanceof String) {
            b = InnerBase.unhex((String) v);
        } else {
            return 0d;
        }
        return bytesToDouble(b);
    }
    public CmsFloat64 value(double v) {
        innerSet(doubleToBytes(v));
        return this;
    }

    private static byte[] doubleToBytes(double v) {
        return ByteBuffer.allocate(8).putLong(Double.doubleToLongBits(v)).array();
    }
    private static double bytesToDouble(byte[] b) {
        return Double.longBitsToDouble(ByteBuffer.wrap(b).getLong());
    }
}

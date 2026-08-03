package com.ysh.jcms.data.scalar;

import com.ysh.jcms.data.core.CmsScalar;
import com.ysh.jcms.data.InnerInt64U;
import java.math.BigInteger;

/**
 * <pre>
 * {@code
 * Int64U ::= INTEGER (0..18446744073709551615) — 7.1.2
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerInt64U} for PER encode/decode via Rust (libasn1.so).
 * 64-bit unsigned — Java {@code long} cannot represent values above
 * {@link Long#MAX_VALUE}, so this wrapper uses {@link BigInteger}.
 */
public class CmsInt64U extends CmsScalar {

    private static final BigInteger MIN = BigInteger.ZERO;
    private static final BigInteger MAX = new BigInteger("18446744073709551615");

    public CmsInt64U() {
        super(new InnerInt64U());
    }
    public CmsInt64U(BigInteger value) {
        this();
        value(value);
    }

    public BigInteger value() {
        Long v = (Long) innerGet();
        if (v == null)
            return BigInteger.ZERO;
        if (v >= 0)
            return BigInteger.valueOf(v);
        return BigInteger.valueOf(v).and(MAX);
    }
    public CmsInt64U value(BigInteger v) {
        if (v.compareTo(MIN) < 0 || v.compareTo(MAX) > 0)
            throw new IllegalArgumentException("CmsInt64U out of range [" + MIN + "," + MAX + "]: " + v);
        innerSet(v.longValue());
        return this;
    }
}

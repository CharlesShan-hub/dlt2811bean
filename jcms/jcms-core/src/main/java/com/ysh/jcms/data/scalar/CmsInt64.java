package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsScalar;
import com.ysh.jcms.data.InnerInt64;
import java.math.BigInteger;

/**
 * Wraps {@link InnerInt64} for PER encode/decode via Rust (libasn1.so).
 * <p>
 * Int64 ::= INTEGER (-9223372036854775808..9223372036854775807)
 */
public class CmsInt64 extends CmsScalar {

    private static final BigInteger MIN = BigInteger.valueOf(Long.MIN_VALUE);
    private static final BigInteger MAX = BigInteger.valueOf(Long.MAX_VALUE);

    public CmsInt64() {
        super(new InnerInt64());
    }
    public CmsInt64(long value) {
        this();
        ((InnerInt64) inner).value = value;
        innerSet(((InnerInt64) inner).value);
    }
    public CmsInt64(BigInteger value) {
        this();
        value(value);
    }

    public long value() {
        Long v = (Long) innerGet();
        return v != null ? v : 0L;
    }
    public CmsInt64 value(long v) {
        innerSet(v);
        return this;
    }
    public CmsInt64 value(BigInteger v) {
        if (v.compareTo(MIN) < 0 || v.compareTo(MAX) > 0)
            throw new IllegalArgumentException("CmsInt64 out of range [" + MIN + "," + MAX + "]: " + v);
        innerSet(v.longValue());
        return this;
    }
}

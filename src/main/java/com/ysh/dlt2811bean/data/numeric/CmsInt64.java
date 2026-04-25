package com.ysh.dlt2811bean.data.numeric;

import com.ysh.dlt2811bean.data.type.AbstractCmsNumeric;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * DL/T 2811 INT64 type (§7.1.2) — signed 64-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────────────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                                        │ Bits │ Java type │
 * ├──────────┼──────────────────────────────────────────────┼──────┼───────────┤
 * │ INT64    │ -9223372036854775808 .. 9223372036854775807  │ 64   │ long      │
 * └──────────┴──────────────────────────────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: 64-bit signed integer, two's complement form, 8-byte big-endian byte-aligned encoding.
 *
 * <pre>
 * // Bean usage
 * CmsInt64 val = new CmsInt64(-1000000000000L);
 * val.set(2000000000000L);
 * val.encode(pos);
 *
 * // Chain usage
 * CmsInt64 val2 = new CmsInt64().set(-1000000000000L).encode(pos);
 *
 * // Decode (returns self for chaining)
 * CmsInt64 r = new CmsInt64().decode(pis);
 * long l = r.get();
 * </pre>
 */
public final class CmsInt64 extends AbstractCmsNumeric<CmsInt64, Long> {

    public static final long MIN = Long.MIN_VALUE;
    public static final long MAX = Long.MAX_VALUE;

    public CmsInt64() {
        this(0L);
    }

    public CmsInt64(long value) {
        super("INT64", MIN, MAX, value);
    }

    @Override
    public void encode(PerOutputStream pos) {
        for (int i = 7; i >= 0; i--) {
            pos.writeByteAligned((byte) ((get() >> (i * 8)) & 0xFF));
        }
    }

    @Override
    protected Long decodeValue(PerInputStream pis) throws Exception {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) | (pis.readByteAligned() & 0xFFL);
        }
        return result;
    }

    private static final CmsInt64 SHARED = new CmsInt64();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, long value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsInt64 read(PerInputStream pis) throws Exception {
        return new CmsInt64().decode(pis);
    }
}

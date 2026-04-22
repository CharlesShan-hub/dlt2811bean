package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 INT64 type — signed 64-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                    │ Bits │ Java type │
 * ├──────────┼──────────────────────────┼──────┼───────────┤
 * │ INT64    │ -2^63 .. 2^63-1          │ 64   │ long      │
 * └──────────┴──────────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Uses 8-byte big-endian byte-aligned encoding.
 *
 * <pre>
 * // Bean usage
 * CmsInt64 val = new CmsInt64(-1234567890L);
 * CmsInt64.encode(pos, val);
 *
 * // Quick usage — pass raw long directly
 * CmsInt64.encode(pos, -1234567890L);
 *
 * // Decode always returns a bean
 * CmsInt64 r = CmsInt64.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt64 {

    /** Minimum value for INT64. */
    public static final long MIN = Long.MIN_VALUE;
    /** Maximum value for INT64. */
    public static final long MAX = Long.MAX_VALUE;

    private long value;

    /** All long values are valid for INT64. Always returns true. */
    public static boolean validateValue(long value) {
        return true;
    }

    public CmsInt64() {
        this.value = 0L;
    }

    public CmsInt64(long value) {
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt64 bean. */
    public static void encode(PerOutputStream pos, CmsInt64 val) {
        pos.align();
        for (int i = 7; i >= 0; i--) {
            pos.writeByteAligned((byte) ((val.value >> (i * 8)) & 0xFF));
        }
    }

    /** Encodes a raw long value (no validation needed — all long values are valid). */
    public static void encode(PerOutputStream pos, long val) {
        pos.align();
        for (int i = 7; i >= 0; i--) {
            pos.writeByteAligned((byte) ((val >> (i * 8)) & 0xFF));
        }
    }

    public static CmsInt64 decode(PerInputStream pis) throws PerDecodeException {
        pis.align();
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) | (pis.readByteAligned() & 0xFFL);
        }
        return new CmsInt64(result);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

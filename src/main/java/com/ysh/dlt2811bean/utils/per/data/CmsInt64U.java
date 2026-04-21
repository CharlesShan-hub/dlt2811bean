package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 INT64U type — unsigned 64-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT64U   │ 0 .. 2^64-1          │ 64   │ long      │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Uses 8-byte big-endian byte-aligned encoding.
 *
 * <pre>
 * // Bean usage
 * CmsInt64U val = new CmsInt64U(123456789012345L);
 * CmsInt64U.encode(pos, val);
 *
 * // Quick usage — pass raw long directly
 * CmsInt64U.encode(pos, 123456789012345L);
 *
 * // Decode always returns a bean
 * CmsInt64U r = CmsInt64U.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt64U {

    /** Minimum value for INT64U. */
    public static final long MIN = 0L;
    /** Maximum value for INT64U (2^64-1). */
    public static final long MAX = -1L; // unsigned max represented as signed -1

    private long value;

    /** Validates that the given value is within INT64U range (must be >= 0). Throws if invalid. */
    public static void validateValue(long value) {
        if (value < 0L) {
            throw new IllegalArgumentException("INT64U out of range (must be >= 0): " + value);
        }
    }

    public CmsInt64U() {
        this.value = 0L;
    }

    public CmsInt64U(long value) {
        validateValue(value);
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt64U bean. */
    public static void encode(PerOutputStream pos, CmsInt64U val) {
        for (int i = 7; i >= 0; i--) {
            pos.writeByteAligned((byte) ((val.value >> (i * 8)) & 0xFF));
        }
    }

    /** Encodes a raw long value (with range validation). */
    public static void encode(PerOutputStream pos, long val) {
        validateValue(val);
        for (int i = 7; i >= 0; i--) {
            pos.writeByteAligned((byte) ((val >> (i * 8)) & 0xFF));
        }
    }

    public static CmsInt64U decode(PerInputStream pis) throws PerDecodeException {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) | (pis.readByteAligned() & 0xFFL);
        }
        return new CmsInt64U(result);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

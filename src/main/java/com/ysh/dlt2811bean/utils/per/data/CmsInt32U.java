package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 INT32U type — unsigned 32-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT32U   │ 0 .. 2^32-1          │ 32   │ long      │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Bean usage
 * CmsInt32U val = new CmsInt32U(3000000000L);
 * CmsInt32U.encode(pos, val);
 *
 * // Quick usage — pass raw long directly
 * CmsInt32U.encode(pos, 3000000000L);
 *
 * // Decode always returns a bean
 * CmsInt32U r = CmsInt32U.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt32U {

    /** Minimum value for INT32U. */
    public static final long MIN = 0L;
    /** Maximum value for INT32U. */
    public static final long MAX = 4294967295L;

    private long value;

    /** Validates that the given value is within INT32U range. Throws if invalid. */
    public static void validateValue(long value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("INT32U out of range [0, 4294967295]: " + value);
        }
    }

    public CmsInt32U() {
        this.value = 0L;
    }

    public CmsInt32U(long value) {
        validateValue(value);
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt32U bean. */
    public static void encode(PerOutputStream pos, CmsInt32U val) {
        PerInteger.encode(pos, val.value & 0xFFFFFFFFL, MIN, MAX);
    }

    /** Encodes a raw long value (with range validation). */
    public static void encode(PerOutputStream pos, long val) {
        validateValue(val);
        PerInteger.encode(pos, val & 0xFFFFFFFFL, MIN, MAX);
    }

    public static CmsInt32U decode(PerInputStream pis) throws PerDecodeException {
        return new CmsInt32U(PerInteger.decode(pis, MIN, MAX));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

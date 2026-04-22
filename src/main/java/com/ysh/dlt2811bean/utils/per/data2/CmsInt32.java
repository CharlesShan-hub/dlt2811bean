package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 INT32 type — signed 32-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                    │ Bits │ Java type │
 * ├──────────┼──────────────────────────┼──────┼───────────┤
 * │ INT32    │ -2^31 .. 2^31-1          │ 32   │ int       │
 * └──────────┴──────────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>All int values are valid (same as Java int range).
 *
 * <pre>
 * // Bean usage
 * CmsInt32 val = new CmsInt32(-100000);
 * CmsInt32.encode(pos, val);
 *
 * // Quick usage — pass raw int directly
 * CmsInt32.encode(pos, -100000);
 *
 * // Decode always returns a bean
 * CmsInt32 r = CmsInt32.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt32 {

    /** Minimum value for INT32. */
    public static final int MIN = Integer.MIN_VALUE;
    /** Maximum value for INT32. */
    public static final int MAX = Integer.MAX_VALUE;

    private int value;

    /** All int values are valid for INT32. Always returns true. */
    public static boolean validateValue(int value) {
        return true;
    }

    public CmsInt32() {
        this.value = 0;
    }

    public CmsInt32(int value) {
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt32 bean. */
    public static void encode(PerOutputStream pos, CmsInt32 val) {
        PerInteger.encode(pos, val.value, MIN, MAX);
    }

    /** Encodes a raw int value (no validation needed — all int values are valid). */
    public static void encode(PerOutputStream pos, int val) {
        PerInteger.encode(pos, val, MIN, MAX);
    }

    public static CmsInt32 decode(PerInputStream pis) throws PerDecodeException {
        return new CmsInt32((int) PerInteger.decode(pis, MIN, MAX));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

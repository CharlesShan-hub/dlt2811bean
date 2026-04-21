package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 INT16 type — signed 16-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT16    │ -32768 .. 32767      │ 16   │ int       │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Bean usage
 * CmsInt16 val = new CmsInt16(-1000);
 * CmsInt16.encode(pos, val);
 *
 * // Quick usage — pass raw int directly
 * CmsInt16.encode(pos, -1000);
 *
 * // Decode always returns a bean
 * CmsInt16 r = CmsInt16.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt16 {

    /** Minimum value for INT16. */
    public static final int MIN = -32768;
    /** Maximum value for INT16. */
    public static final int MAX = 32767;

    private int value;

    /** Validates that the given value is within INT16 range. Throws if invalid. */
    public static void validateValue(int value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("INT16 out of range [-32768, 32767]: " + value);
        }
    }

    public CmsInt16() {
        this.value = 0;
    }

    public CmsInt16(int value) {
        validateValue(value);
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt16 bean. */
    public static void encode(PerOutputStream pos, CmsInt16 val) {
        PerInteger.encode(pos, val.value, MIN, MAX);
    }

    /** Encodes a raw int value (with range validation). */
    public static void encode(PerOutputStream pos, int val) {
        validateValue(val);
        PerInteger.encode(pos, val, MIN, MAX);
    }

    public static CmsInt16 decode(PerInputStream pis) throws PerDecodeException {
        return new CmsInt16((int) PerInteger.decode(pis, MIN, MAX));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

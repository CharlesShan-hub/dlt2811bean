package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 INT8 type — signed 8-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────┬──────┬───────────┐
 * │ 2811     │ Range            │ Bits │ Java type │
 * ├──────────┼──────────────────┼──────┼───────────┤
 * │ INT8     │ -128 .. 127      │ 8    │ int       │
 * └──────────┴──────────────────┴──────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Bean usage
 * CmsInt8 val = new CmsInt8(-42);
 * CmsInt8.encode(pos, val);
 *
 * // Quick usage — pass raw int directly
 * CmsInt8.encode(pos, -42);
 *
 * // Decode always returns a bean
 * CmsInt8 r = CmsInt8.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt8 {

    /** Minimum value for INT8. */
    public static final int MIN = -128;
    /** Maximum value for INT8. */
    public static final int MAX = 127;

    private int value;

    /** Validates that the given value is within INT8 range. Throws if invalid. */
    public static void validateValue(int value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("INT8 out of range [-128, 127]: " + value);
        }
    }

    public CmsInt8() {
        this.value = 0;
    }

    public CmsInt8(int value) {
        validateValue(value);
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt8 bean. */
    public static void encode(PerOutputStream pos, CmsInt8 val) {
        PerInteger.encode(pos, val.value, MIN, MAX);
    }

    /** Encodes a raw int value (with range validation). */
    public static void encode(PerOutputStream pos, int val) {
        validateValue(val);
        PerInteger.encode(pos, val, MIN, MAX);
    }

    public static CmsInt8 decode(PerInputStream pis) throws PerDecodeException {
        return new CmsInt8((int) PerInteger.decode(pis, MIN, MAX));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

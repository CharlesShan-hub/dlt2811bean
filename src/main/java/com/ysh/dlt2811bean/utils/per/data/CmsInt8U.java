package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 INT8U type — unsigned 8-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────┬──────┬───────────┐
 * │ 2811     │ Range            │ Bits │ Java type │
 * ├──────────┼──────────────────┼──────┼───────────┤
 * │ INT8U    │ 0 .. 255         │ 8    │ int       │
 * └──────────┴──────────────────┴──────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Bean usage
 * CmsInt8U val = new CmsInt8U(200);
 * CmsInt8U.encode(pos, val);
 *
 * // Quick usage — pass raw int directly
 * CmsInt8U.encode(pos, 200);
 *
 * // Decode always returns a bean
 * CmsInt8U r = CmsInt8U.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt8U {

    /** Minimum value for INT8U. */
    public static final int MIN = 0;
    /** Maximum value for INT8U. */
    public static final int MAX = 255;

    private int value;

    /** Validates that the given value is within INT8U range. Throws if invalid. */
    public static void validateValue(int value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("INT8U out of range [0, 255]: " + value);
        }
    }

    public CmsInt8U() {
        this.value = 0;
    }

    public CmsInt8U(int value) {
        validateValue(value);
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt8U bean. */
    public static void encode(PerOutputStream pos, CmsInt8U val) {
        PerInteger.encode(pos, val.value & 0xFFL, MIN, MAX);
    }

    /** Encodes a raw int value (with range validation). */
    public static void encode(PerOutputStream pos, int val) {
        validateValue(val);
        PerInteger.encode(pos, val & 0xFFL, MIN, MAX);
    }

    public static CmsInt8U decode(PerInputStream pis) throws PerDecodeException {
        return new CmsInt8U((int) PerInteger.decode(pis, MIN, MAX));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

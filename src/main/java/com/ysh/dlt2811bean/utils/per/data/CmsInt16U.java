package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 INT16U type — unsigned 16-bit integer.
 *
 * <pre>
 * ┌──────────┬──────────────────────┬──────┬───────────┐
 * │ 2811     │ Range                │ Bits │ Java type │
 * ├──────────┼──────────────────────┼──────┼───────────┤
 * │ INT16U   │ 0 .. 65535           │ 16   │ int       │
 * └──────────┴──────────────────────┴──────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Bean usage
 * CmsInt16U val = new CmsInt16U(50000);
 * CmsInt16U.encode(pos, val);
 *
 * // Quick usage — pass raw int directly
 * CmsInt16U.encode(pos, 50000);
 *
 * // Decode always returns a bean
 * CmsInt16U r = CmsInt16U.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsInt16U {

    /** Minimum value for INT16U. */
    public static final int MIN = 0;
    /** Maximum value for INT16U. */
    public static final int MAX = 65535;

    private int value;

    /** Validates that the given value is within INT16U range. Throws if invalid. */
    public static void validateValue(int value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("INT16U out of range [0, 65535]: " + value);
        }
    }

    public CmsInt16U() {
        this.value = 0;
    }

    public CmsInt16U(int value) {
        validateValue(value);
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsInt16U bean. */
    public static void encode(PerOutputStream pos, CmsInt16U val) {
        PerInteger.encode(pos, val.value & 0xFFFFL, MIN, MAX);
    }

    /** Encodes a raw int value (with range validation). */
    public static void encode(PerOutputStream pos, int val) {
        validateValue(val);
        PerInteger.encode(pos, val & 0xFFFFL, MIN, MAX);
    }

    public static CmsInt16U decode(PerInputStream pis) throws PerDecodeException {
        return new CmsInt16U((int) PerInteger.decode(pis, MIN, MAX));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

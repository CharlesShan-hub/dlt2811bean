package com.ysh.dlt2811bean.datatypes.code;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * DL/T 2811 double-point position type (§7.3.5, Table 8).
 *
 * <pre>
 * ┌──────┬───────┬────────────────────┬──────────────┐
 * │ Bits │ Value │ Meaning            │ Constant     │
 * ├──────┼───────┼────────────────────┼──────────────┤
 * │ 00   │  0    │ intermediate-state │ INTERMEDIATE │
 * │ 01   │  1    │ off                │ OFF          │
 * │ 10   │  2    │ on                 │ ON           │
 * │ 11   │  3    │ bad-state          │ BAD          │
 * └──────┴───────┴────────────────────┴──────────────┘
 * </pre>
 *
 * <p>Encoded as a fixed 2-bit CODED ENUM (BIT STRING).
 *
 * <pre>
 * // Construction
 * CmsDbpos pos = new CmsDbpos();
 * CmsDbpos pos = new CmsDbpos(CmsDbpos.ON);
 * CmsDbpos pos = new CmsDbpos(2); // same as ON
 *
 * // Setting values
 * pos.set(CmsDbpos.OFF);
 * pos.set(1); // same as OFF
 *
 * // Checking values
 * if (pos.is(CmsDbpos.ON)) { ... }
 * if (pos.is(2)) { ... } // same as ON
 *
 * // Getting the value
 * long value = pos.get(); // returns 0, 1, 2, or 3
 *
 * // Encoding and decoding
 * PerOutputStream posStream = new PerOutputStream();
 * pos.encode(posStream);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsDbpos decoded = new CmsDbpos().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with fixed enumeration values.
 * The size is 2 bits (values 0..3).
 */
public class CmsDbpos extends AbstractCmsCodedEnum<CmsDbpos> {

    /** 00 — intermediate-state */
    public static final int INTERMEDIATE = 0;
    /** 01 — off */
    public static final int OFF = 1;
    /** 10 — on */
    public static final int ON = 2;
    /** 11 — bad-state */
    public static final int BAD = 3;

    /**
     * Constructs a CmsDbpos with default value INTERMEDIATE (0).
     */
    public CmsDbpos() {
        this(INTERMEDIATE);
    }

    public CmsDbpos(long value) {
        super("CmsDbpos", value, 2);
    }

    private static final CmsDbpos SHARED = new CmsDbpos();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, long value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsDbpos read(PerInputStream pis) throws Exception {
        return new CmsDbpos().decode(pis);
    }
}

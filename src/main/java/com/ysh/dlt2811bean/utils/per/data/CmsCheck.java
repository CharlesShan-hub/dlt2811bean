package com.ysh.dlt2811bean.utils.per.data;

/**
 * DLT 2811 check type. (§7.5.3, Table 14).
 *
 * <p>Encoded as a 2-bit fixed-size CODED ENUM (BIT STRING).
 *
 * <pre>
 * ┌──────┬──────┬──────────────────────────┐
 * │ Bits │ Type │ Meaning                  │
 * ├──────┼──────┼──────────────────────────┤
 * │    0 │ flag │ synchrocheck             │
 * │    1 │ flag │ interlock-check          │
 * └──────┴──────┴──────────────────────────┘
 * <pre>
 *
 * <pre>
 *  Construction
 * CmsCheck c = new CmsCheck();
 * CmsCheck c = new CmsCheck(0x03);  from raw 2-bit value
 *
 *  Setting and checking flags
 * c.setBit(CmsCheck.SYNCHROCHECK, true);
 * c.setBit(CmsCheck.INTERLOCK_CHECK, true);
 * c.testBit(CmsCheck.SYNCHROCHECK);  true
 * c.testBit(CmsCheck.INTERLOCK_CHECK);  true
 *
 *  Getting the raw value
 * long raw = c.get();
 *
 *  Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * c.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsCheck decoded = new CmsCheck().decode(pis);
 * <pre>
 *
 * <p>This is a concrete application type with a fixed 2-bit CODED ENUM layout.
 * Bit 0 is synchrocheck; bit 1 is interlock-check.
 */
public class CmsCheck extends AbstractCmsCodedEnum<CmsCheck> {

    // ==================== Bit positions ====================
    public static final int SYNCHROCHECK = 0;
    public static final int INTERLOCK_CHECK = 1;

    /**
     * Constructs a CmsCheck with default value 0 (all flags cleared).
     */
    public CmsCheck(){
        this(0L);
    }

    /**
     * Constructs a CmsCheck from a raw 2-bit value.
     *
     * @param value raw 2-bit CODED ENUM value
     */
    public CmsCheck(long value) {
        super("CmsCheck", value, 2);
    }
}

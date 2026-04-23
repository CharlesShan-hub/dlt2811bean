package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 quality type (§7.3.6, Table 9).
 *
 * <p>Encoded as a 13-bit fixed-size CODED ENUM (BIT STRING).
 *
 * <pre>
 * ┌──────┬──────┬────────────────────┐
 * │ Bits │ Type │ Meaning            │
 * ├──────┼──────┼────────────────────┤
 * │  0~1 │ 2bit │ validity           │
 * │    2 │ flag │ overflow           │
 * │    3 │ flag │ outOfRange         │
 * │    4 │ flag │ badReference       │
 * │    5 │ flag │ oscillatory        │
 * │    6 │ flag │ failure            │
 * │    7 │ flag │ oldData            │
 * │    8 │ flag │ inconsistent       │
 * │    9 │ flag │ inaccurate         │
 * │   10 │ flag │ source             │
 * │   11 │ flag │ test               │
 * │   12 │ flag │ operatorBlocked    │
 * └──────┴──────┴────────────────────┘
 * </pre>
 *
 * <p>Validity values (2-bit, bits 0~1):
 * <ul>
 *   <li>{@link #GOOD} (0) — valid</li>
 *   <li>{@link #INVALID} (1) — invalid</li>
 *   <li>{@link #RESERVED} (2) — reserved</li>
 *   <li>{@link #QUESTIONABLE} (3) — questionable</li>
 * </ul>
 *
 * <pre>
 * // Construction
 * CmsQuality q = new CmsQuality();
 * CmsQuality q = new CmsQuality(0x0804); // from raw 13-bit value
 *
 * // Setting and checking flags
 * q.setBit(CmsQuality.OVERFLOW, true);
 * q.setBit(CmsQuality.TEST, true);
 * q.testBit(CmsQuality.OVERFLOW); // true
 * q.testBit(CmsQuality.FAILURE);  // false
 *
 * // Multi-bit field access
 * q.setBits(CmsQuality.VALIDITY, CmsQuality.VALIDITY_WIDTH, CmsQuality.QUESTIONABLE);
 * q.testBits(CmsQuality.VALIDITY, CmsQuality.VALIDITY_WIDTH, CmsQuality.QUESTIONABLE); // true
 *
 * // Getting the raw value
 * long raw = q.get();
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * q.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsQuality decoded = new CmsQuality().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with a fixed 13-bit CODED ENUM layout.
 * Bits 0~1 form a 2-bit validity field; bits 2~12 are individual flags.
 */
public class CmsQuality extends AbstractCmsCodedEnum<CmsQuality> {

    // ==================== Validity values (2-bit, bits 0~1) ====================
    /** Validity: good (00). */
    public static final int GOOD = 0;
    /** Validity: invalid (01). */
    public static final int INVALID = 1;
    /** Validity: reserved (10). */
    public static final int RESERVED = 2;
    /** Validity: questionable (11). */
    public static final int QUESTIONABLE = 3;

    // ==================== Bit positions ====================
    /** Bits 0~1 — validity (2-bit field). */
    public static final int VALIDITY = 0;
    public static final int VALIDITY_WIDTH = 2;
    /** Bit 2 — measurement overflow. */
    public static final int OVERFLOW = 2;
    /** Bit 3 — value out of range. */
    public static final int OUT_OF_RANGE = 3;
    /** Bit 4 — bad reference. */
    public static final int BAD_REFERENCE = 4;
    /** Bit 5 — oscillatory. */
    public static final int OSCILLATORY = 5;
    /** Bit 6 — failure. */
    public static final int FAILURE = 6;
    /** Bit 7 — old data. */
    public static final int OLD_DATA = 7;
    /** Bit 8 — inconsistent. */
    public static final int INCONSISTENT = 8;
    /** Bit 9 — inaccurate. */
    public static final int INACCURATE = 9;
    /** Bit 10 — source (0=process, 1=substituted). */
    public static final int SOURCE = 10;
    /** Bit 11 — test mode. */
    public static final int TEST = 11;
    /** Bit 12 — operator blocked. */
    public static final int OPERATOR_BLOCKED = 12;

    /**
     * Constructs a CmsQuality with default value 0 (all flags cleared, validity=GOOD).
     */
    public CmsQuality(){
        this(0L);
    }

    /**
     * Constructs a CmsQuality from a raw 13-bit value.
     *
     * @param value raw 13-bit CODED ENUM value
     */
    public CmsQuality(long value) {
        super("CmsQuality", value, 13);
    }

    // ==================== Convenience methods ====================

    /**
     * Gets the validity value (bits 0~1, 2-bit).
     *
     * @return validity value 0~3:
     *         <ul>
     *           <li>{@link #GOOD} (0) — valid</li>
     *           <li>{@link #INVALID} (1) — invalid</li>
     *           <li>{@link #RESERVED} (2) — reserved</li>
     *           <li>{@link #QUESTIONABLE} (3) — questionable</li>
     *         </ul>
     */
    public int getValidity() {
        return (int) getBits(VALIDITY, VALIDITY_WIDTH);
    }

    /**
     * Sets the validity value (bits 0~1, 2-bit).
     *
     * @param validity validity value 0~3
     * @return this instance for chaining
     */
    public CmsQuality setValidity(int validity) {
        setBits(VALIDITY, VALIDITY_WIDTH, validity);
        return this;
    }
}

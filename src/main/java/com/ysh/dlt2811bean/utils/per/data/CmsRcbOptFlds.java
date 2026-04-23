package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 RCB (Report Control Block) optional fields (§7.6.4, Table 16).
 *
 * <p>Encoded as a fixed-size BIT STRING of 10 bits (CODED ENUM).
 *
 * <pre>
 * ┌─────┬─────────────────────────────┐
 * │ Bit │ Meaning                     │
 * ├─────┼─────────────────────────────┤
 * │  0  │ reserved                    │
 * │  1  │ sequence-number             │
 * │  2  │ report-time-stamp           │
 * │  3  │ reason-for-inclusion        │
 * │  4  │ data-set-name               │
 * │  5  │ data-reference              │
 * │  6  │ buffer-overflow             │
 * │  7  │ entryID                     │
 * │  8  │ conf-revision               │
 * │  9  │ segmentation                │
 * └─────┴─────────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Construction
 * CmsRcbOptFlds opt = new CmsRcbOptFlds();
 * CmsRcbOptFlds opt = new CmsRcbOptFlds(0x02); // from raw 10-bit value (bit 1)
 *
 * // Setting and checking flags
 * opt.setBit(CmsRcbOptFlds.SEQUENCE_NUMBER, true);
 * opt.setBit(CmsRcbOptFlds.REASON_FOR_INCLUSION, true);
 * opt.testBit(CmsRcbOptFlds.SEQUENCE_NUMBER); // true
 *
 * // Clear buffer-overflow for URCB usage
 * opt.clearBufferOverflowForUrcb();
 *
 * // Getting the raw value
 * long raw = opt.get();
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * opt.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsRcbOptFlds decoded = new CmsRcbOptFlds().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with a fixed 10-bit CODED ENUM layout.
 * Bit 0 is reserved; bits 1~9 are optional field flags.
 */
public class CmsRcbOptFlds extends AbstractCmsCodedEnum<CmsRcbOptFlds> {

    // ==================== Bit position constants ====================

    /** Bit 0 — reserved. */
    public static final int RESERVED = 0;
    /** Bit 1 — include sequence number in report. */
    public static final int SEQUENCE_NUMBER = 1;
    /** Bit 2 — include report time stamp. */
    public static final int REPORT_TIME_STAMP = 2;
    /** Bit 3 — include reason for inclusion. */
    public static final int REASON_FOR_INCLUSION = 3;
    /** Bit 4 — include data set name. */
    public static final int DATA_SET_NAME = 4;
    /** Bit 5 — include data reference. */
    public static final int DATA_REFERENCE = 5;
    /** Bit 6 — buffer overflow indicator (invalid for URCB, must be 0). */
    public static final int BUFFER_OVERFLOW = 6;
    /** Bit 7 — include entry ID. */
    public static final int ENTRY_ID = 7;
    /** Bit 8 — include configuration revision. */
    public static final int CONF_REVISION = 8;
    /** Bit 9 — segmentation indicator. */
    public static final int SEGMENTATION = 9;

    /**
     * Constructs a CmsRcbOptFlds with default value 0 (all flags cleared).
     */
    public CmsRcbOptFlds() {
        this(0L);
    }

    /**
     * Constructs a CmsRcbOptFlds from a raw 10-bit value.
     *
     * @param value raw 10-bit CODED ENUM value
     */
    public CmsRcbOptFlds(long value) {
        super("CmsRcbOptFlds", value, 10);
    }

    /** Clears buffer-overflow bit for URCB usage. */
    public CmsRcbOptFlds clearBufferOverflowForUrcb() {
        setBit(BUFFER_OVERFLOW, false);
        return this;
    }
}
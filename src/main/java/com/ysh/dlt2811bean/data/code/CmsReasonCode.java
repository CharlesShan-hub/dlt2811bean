package com.ysh.dlt2811bean.data.code;

/**
 * DL/T 2811 reason code for reports (§7.6.3).
 *
 * <p>CODED ENUM — fixed 7-bit string, each bit is a named flag.
 *
 * <pre>
 * ┌─────┬─────────────────────────┐
 * │ Bit │ Meaning                 │
 * ├─────┼─────────────────────────┤
 * │  0  │ reserved (always 0)     │
 * │  1  │ data-change             │
 * │  2  │ quality-change          │
 * │  3  │ data-update             │
 * │  4  │ integrity               │
 * │  5  │ general-interrogation   │
 * │  6  │ application-trigger     │
 * └─────┴─────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Construction
 * CmsReasonCode rc = new CmsReasonCode();
 * CmsReasonCode rc = new CmsReasonCode(0x12); // from raw 7-bit value (bits 1,4)
 *
 * // Setting and checking flags
 * rc.setBit(CmsReasonCode.DATA_CHANGE, true);
 * rc.setBit(CmsReasonCode.INTEGRITY, true);
 * rc.testBit(CmsReasonCode.DATA_CHANGE); // true
 * rc.testBit(CmsReasonCode.INTEGRITY);    // true
 *
 * // Getting the raw value
 * long raw = rc.get();
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * rc.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsReasonCode decoded = new CmsReasonCode().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with a fixed 7-bit CODED ENUM layout.
 * Bit 0 is reserved; bits 1~6 are reason codes.
 */
import com.ysh.dlt2811bean.data.type.AbstractCmsCodedEnum;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

public class CmsReasonCode extends AbstractCmsCodedEnum<CmsReasonCode> {

    // ==================== Bit position constants ====================

    /** Bit 0 — reserved (should always be 0). */
    public static final int RESERVED = 0;
    /** Bit 1 — report triggered by data value change. */
    public static final int DATA_CHANGE = 1;
    /** Bit 2 — report triggered by quality change. */
    public static final int QUALITY_CHANGE = 2;
    /** Bit 3 — report triggered by data update. */
    public static final int DATA_UPDATE = 3;
    /** Bit 4 — integrity period report. */
    public static final int INTEGRITY = 4;
    /** Bit 5 — report triggered by general interrogation. */
    public static final int GENERAL_INTERROGATION = 5;
    /** Bit 6 — report triggered by application. */
    public static final int APPLICATION_TRIGGER = 6;

    /**
     * Constructs a CmsReasonCode with default value 0 (all flags cleared).
     */
    public CmsReasonCode() {
        this(0L);
    }

    /**
     * Constructs a CmsReasonCode from a raw 7-bit value.
     *
     * @param value raw 7-bit CODED ENUM value
     */
    public CmsReasonCode(long value) {
        super("CmsReasonCode", value, 7);
    }

    private static final CmsReasonCode SHARED = new CmsReasonCode();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, long value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsReasonCode read(PerInputStream pis) throws Exception {
        return new CmsReasonCode().decode(pis);
    }
}
package com.ysh.dlt2811bean.data.code;

/**
 * DL/T 2811 trigger conditions for control blocks (§7.6.2).
 *
 * <p>Encoded as a fixed-size BIT STRING of 6 bits (CODED ENUM).
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
 * └─────┴─────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Construction
 * CmsTriggerConditions tc = new CmsTriggerConditions();
 * CmsTriggerConditions tc = new CmsTriggerConditions(0x3E); // from raw 6-bit value
 *
 * // Setting and checking flags
 * tc.setBit(CmsTriggerConditions.DATA_CHANGE, true);
 * tc.setBit(CmsTriggerConditions.INTEGRITY, true);
 * tc.testBit(CmsTriggerConditions.DATA_CHANGE);  // true
 * tc.testBit(CmsTriggerConditions.INTEGRITY);     // true
 *
 * // Getting the raw value
 * long raw = tc.get();
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * tc.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsTriggerConditions decoded = new CmsTriggerConditions().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with a fixed 6-bit CODED ENUM layout.
 * Bit 0 is reserved; bits 1~5 are trigger conditions.
 */
import com.ysh.dlt2811bean.data.type.AbstractCmsCodedEnum;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

public class CmsTriggerConditions extends AbstractCmsCodedEnum<CmsTriggerConditions> {

    // ==================== Bit positions ====================
    /** Bit 0 — reserved (should always be 0). */
    public static final int RESERVED = 0;
    /** Bit 1 — trigger on data value change. */
    public static final int DATA_CHANGE = 1;
    /** Bit 2 — trigger on quality change. */
    public static final int QUALITY_CHANGE = 2;
    /** Bit 3 — trigger on data update. */
    public static final int DATA_UPDATE = 3;
    /** Bit 4 — trigger for integrity period. */
    public static final int INTEGRITY = 4;
    /** Bit 5 — trigger on general interrogation. */
    public static final int GENERAL_INTERROGATION = 5;

    /**
     * Constructs a CmsTriggerConditions with default value 0 (all flags cleared).
     */
    public CmsTriggerConditions(){
        this(0L);
    }

    /**
     * Constructs a CmsTriggerConditions from a raw 6-bit value.
     *
     * @param value raw 6-bit CODED ENUM value
     */
    public CmsTriggerConditions(long value) {
        super("CmsTriggerConditions", value, 6);
    }

    private static final CmsTriggerConditions SHARED = new CmsTriggerConditions();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, long value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsTriggerConditions read(PerInputStream pis) throws Exception {
        return new CmsTriggerConditions().decode(pis);
    }
}

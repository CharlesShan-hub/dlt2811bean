package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 LCB (Log Control Block) optional fields (§7.6.5).
 *
 * <p>Encoded as a fixed-size BIT STRING of 1 bit (CODED ENUM).
 * Bit 0 is always 1 (reserved).
 *
 * <pre>
 * ┌─────┬─────────────────────────────┐
 * │ Bit │ Meaning                     │
 * ├─────┼─────────────────────────────┤
 * │  0  │ always 1 (reserved)         │
 * └─────┴─────────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Construction (default value is 1)
 * CmsLcbOptFlds lcb = new CmsLcbOptFlds();
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * lcb.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsLcbOptFlds decoded = new CmsLcbOptFlds().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with a fixed 1-bit CODED ENUM layout.
 * The only valid value is 1 (bit 0 set).
 */
public class CmsLcbOptFlds extends AbstractCmsCodedEnum<CmsLcbOptFlds> {

    /** Bit 0 — reserved (always 1). */
    public static final int RESERVED = 0;

    /**
     * Constructs a CmsLcbOptFlds with default value 1 (bit 0 set).
     */
    public CmsLcbOptFlds() {
        this(1L);
    }

    /**
     * Constructs a CmsLcbOptFlds from a raw 1-bit value.
     *
     * @param value raw 1-bit CODED ENUM value
     */
    public CmsLcbOptFlds(long value) {
        super("CmsLcbOptFlds", value, 1);
    }
}
package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerEnumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 ENUMERATED type — generic bean (§7.1.6).
 *
 * <p>Encodes as constrained INTEGER (0..maxOrdinal). The underlying INT size
 * (INT8/INT16/...) is determined automatically by the range:
 *
 * <pre>
 * ┌────────────────┬──────────────────┬───────────┐
 * │ Ordinals       │ Range            │ Bits      │
 * ├────────────────┼──────────────────┼───────────┤
 * │ 1              │ 0..0             │ 0         │
 * │ 2              │ 0..1             │ 1         │
 * │ 3..4           │ 0..2 / 0..3      │ 2         │
 * │ 5..8           │ 0..4 / 0..7      │ 3         │
 * │ 9..16          │ 0..8 / 0..15     │ 4         │
 * │ 17..32         │ 0..16 / 0..31    │ 5         │
 * │ 33..64         │ 0..32 / 0..63    │ 6         │
 * │ 65..128        │ 0..64 / 0..127   │ 7         │
 * │ 129..256       │ 0..128/ 0..255   │ 8 (=INT8) │
 * │ 257..65536     │ 0..256/ 0..65535 │ 16(=INT16)│
 * │ >65536         │ large            │ bytes     │
 * └────────────────┴──────────────────┴───────────┘
 * </pre>
 *
 * <p>Concrete enum classes (e.g. CmsSmpMod) should set their own
 * code constants and {@code maxOrdinal} as {@code MAX_CODE}.
 *
 * <pre>
 * // Bean mode — chain setters
 * CmsEnumerated e = new CmsEnumerated()
 *     .setValue(2)
 *     .setMaxOrdinal(8);
 * CmsEnumerated.encode(pos, e);
 *
 * // Quick mode — raw value + explicit maxOrdinal
 * CmsEnumerated.encode(pos, 2, 8);
 * CmsEnumerated.decode(pis, 8);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsEnumerated {

    private int value;
    private Integer maxOrdinal;

    public CmsEnumerated() {
        this.value = 0;
    }

    public CmsEnumerated(int value, int maxOrdinal) {
        this.value = value;
        this.maxOrdinal = maxOrdinal;
    }

    /**
     * Validates that maxOrdinal is set and value is within range.
     *
     * @throws IllegalStateException if maxOrdinal is null
     * @throws IllegalArgumentException if value is out of range [0, maxOrdinal]
     */
    public void validate() {
        if (maxOrdinal == null) {
            throw new IllegalStateException("maxOrdinal is not set");
        }
        if (value < 0 || value > maxOrdinal) {
            throw new IllegalArgumentException(
                    String.format("Enumerated value %d out of range [0, %d]", value, maxOrdinal));
        }
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsEnumerated bean. Automatically determines INT size from maxOrdinal. */
    public static void encode(PerOutputStream pos, CmsEnumerated bean) {
        bean.validate();
        PerEnumerated.encode(pos, bean.value, bean.maxOrdinal);
    }

    /** Encodes a raw int value with explicit maxOrdinal. */
    public static void encode(PerOutputStream pos, int value, int maxOrdinal) {
        if (maxOrdinal < 0) {
            throw new IllegalArgumentException("maxOrdinal must be non-negative");
        }
        if (value < 0 || value > maxOrdinal) {
            throw new IllegalArgumentException(
                    String.format("Enumerated value %d out of range [0, %d]", value, maxOrdinal));
        }
        PerEnumerated.encode(pos, value, maxOrdinal);
    }

    /** Decodes with explicit maxOrdinal. */
    public static CmsEnumerated decode(PerInputStream pis, int maxOrdinal) throws PerDecodeException {
        return new CmsEnumerated(PerEnumerated.decode(pis, maxOrdinal), maxOrdinal);
    }

    @Override
    public String toString() {
        return String.format("Enumerated[%d/%d]", value, maxOrdinal != null ? maxOrdinal : -1);
    }
}

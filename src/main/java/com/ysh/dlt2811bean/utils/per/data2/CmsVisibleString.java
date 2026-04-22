package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerVisibleString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 VISIBLE STRING type (§7.1.5, Table 6).
 *
 * <pre>
 * ┌─────────────────┬──────────────────────────┬───────────┬───────────┐
 * │ 2811            │ Range                   │ Bits      │ Java type │
 * ├─────────────────┼──────────────────────────┼───────────┼───────────┤
 * │ VISIBLE STRING  │ ISO 646 (ASCII-comp.)   │ 8/char    │ String    │
 * └─────────────────┴──────────────────────────┴───────────┴───────────┘
 * </pre>
 *
 * <p>Supports two constraint modes via {@code size} (fixed) or {@code max} (variable).
 * Mutually exclusive — setting one clears the other.
 * <ul>
 *   <li><b>FIXED</b> (SIZE(n)): space-padded, trimmed on decode</li>
 *   <li><b>VARIABLE</b> (SIZE(0..max)): length prefix encoded</li>
 * </ul>
 *
 * <pre>
 * // Bean mode — chain setters
 * CmsVisibleString ref = new CmsVisibleString()
 *     .setValue("LD1/LN0.DO1")
 *     .setMax(255);
 * CmsVisibleString.encode(pos, ref);
 *
 * CmsVisibleString ap = new CmsVisibleString()
 *     .setValue("S1.AccessPoint1")
 *     .setSize(129);
 * CmsVisibleString.encode(pos, ap);
 *
 * // Quick mode — raw value + explicit Mode
 * CmsVisibleString.encode(pos, "LD1/LN0.DO1", Mode.VARIABLE, 255);
 * CmsVisibleString.encode(pos, "S1.AP1", Mode.FIXED, 129);
 * CmsVisibleString.decode(pis, Mode.VARIABLE, 255);
 * CmsVisibleString.decode(pis, Mode.FIXED, 129);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsVisibleString {

    /** Encoding mode: FIXED for SIZE(n), VARIABLE for SIZE(0..max). */
    public enum Mode {
        /** Fixed-size: SIZE(n), space-padded. */
        FIXED,
        /** Variable-size: SIZE(0..max), length prefix encoded. */
        VARIABLE
    }

    private String value;
    private Integer size;
    private Integer max;

    public CmsVisibleString() {
        this.value = "";
    }

    public CmsVisibleString(String value) {
        this.value = value != null ? value : "";
    }

    /** Set fixed size (SIZE(n)). Clears max. */
    public CmsVisibleString size(int size) {
        this.size = size;
        this.max = null;
        return this;
    }

    /** Set variable max (SIZE(0..max)). Clears size. */
    public CmsVisibleString max(int max) {
        this.max = max;
        this.size = null;
        return this;
    }

    /** Returns the effective mode based on which constraint is set. */
    public Mode getMode() {
        if (size != null) return Mode.FIXED;
        if (max != null) return Mode.VARIABLE;
        return null;
    }

    /** Validates that a constraint (size or max) is set. Throws if neither is set. */
    public void validate() {
        if (size == null && max == null) {
            throw new IllegalStateException("VISIBLE STRING constraint not set: call size(n) or max(n)");
        }
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsVisibleString bean. Uses bean's internal size/max constraint. */
    public static void encode(PerOutputStream pos, CmsVisibleString bean) {
        bean.validate();
        if (bean.size != null) {
            PerVisibleString.encodeFixedSize(pos, bean.value, bean.size);
        } else {
            PerVisibleString.encodeConstrained(pos, bean.value, 0, bean.max);
        }
    }

    /** Encodes a raw String with explicit mode. */
    public static void encode(PerOutputStream pos, String value, Mode mode, int length) {
        if (mode == Mode.FIXED) {
            PerVisibleString.encodeFixedSize(pos, value, length);
        } else {
            PerVisibleString.encodeConstrained(pos, value, 0, length);
        }
    }

    /** Decodes with explicit mode. Returns a bean with the corresponding constraint set. */
    public static CmsVisibleString decode(PerInputStream pis, Mode mode, int length) throws PerDecodeException {
        String val;
        if (mode == Mode.FIXED) {
            val = PerVisibleString.decodeFixedSize(pis, length);
            return new CmsVisibleString(val).size(length);
        } else {
            val = PerVisibleString.decodeConstrained(pis, 0, length);
            return new CmsVisibleString(val).max(length);
        }
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}

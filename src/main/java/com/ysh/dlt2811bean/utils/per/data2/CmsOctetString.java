package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerOctetString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 OCTET STRING type (§7.1.5, Table 6).
 *
 * <pre>
 * ┌─────────────────┬──────────────────────────┬───────────┬───────────┐
 * │ 2811            │ Range                    │ Bits      │ Java type │
 * ├─────────────────┼──────────────────────────┼───────────┼───────────┤
 * │ OCTET STRING    │ 0..65535 bytes           │ 8/byte    │ byte[]    │
 * └─────────────────┴──────────────────────────┴───────────┴───────────┘
 * </pre>
 *
 * <p>Supports two constraint modes via {@code size} (fixed) or {@code max} (variable).
 * Mutually exclusive — setting one clears the other.
 * <ul>
 *   <li><b>FIXED</b> (SIZE(n)): {@code size} = fixed byte count, padded with zeros</li>
 *   <li><b>VARIABLE</b> (SIZE(0..max)): {@code max} = maximum byte count, length prefix encoded</li>
 * </ul>
 *
 * <pre>
 * // Bean mode — chain setters
 * CmsOctetString id = new CmsOctetString()
 *     .setValue(new byte[]{0x01, 0x02})
 *     .setSize(8);
 * CmsOctetString.encode(pos, id);
 *
 * CmsOctetString ident = new CmsOctetString()
 *     .setValue(new byte[]{0xAB})
 *     .setMax(64);
 * CmsOctetString.encode(pos, ident);
 *
 * // Quick mode — raw value + explicit Mode
 * CmsOctetString.encode(pos, new byte[]{0x01, 0x02}, Mode.FIXED, 8);
 * CmsOctetString.encode(pos, new byte[]{0xAB}, Mode.VARIABLE, 64);
 * CmsOctetString.decode(pis, Mode.FIXED, 8);
 * CmsOctetString.decode(pis, Mode.VARIABLE, 64);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsOctetString {

    /** Encoding mode: FIXED for SIZE(n), VARIABLE for SIZE(0..max). */
    public enum Mode {
        /** Fixed-size: SIZE(n), padded with zeros. */
        FIXED,
        /** Variable-size: SIZE(0..max), length prefix encoded. */
        VARIABLE
    }

    private byte[] value;
    private Integer size;
    private Integer max;

    public CmsOctetString() {
        this.value = new byte[0];
    }

    public CmsOctetString(byte[] value) {
        this.value = value != null ? value : new byte[0];
    }

    /** Set fixed size (SIZE(n)). Clears max. */
    public CmsOctetString size(int size) {
        this.size = size;
        this.max = null;
        return this;
    }

    /** Set variable max (SIZE(0..max)). Clears size. */
    public CmsOctetString max(int max) {
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
            throw new IllegalStateException("OCTET STRING constraint not set: call size(n) or max(n)");
        }
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsOctetString bean. Uses bean's internal size/max constraint. */
    public static void encode(PerOutputStream pos, CmsOctetString bean) {
        bean.validate();
        if (bean.size != null) {
            PerOctetString.encodeFixedSize(pos, bean.value, bean.size);
        } else {
            PerOctetString.encodeConstrained(pos, bean.value, 0, bean.max);
        }
    }

    /** Encodes a raw byte array with explicit mode. */
    public static void encode(PerOutputStream pos, byte[] value, Mode mode, int length) {
        if (mode == Mode.FIXED) {
            PerOctetString.encodeFixedSize(pos, value, length);
        } else {
            PerOctetString.encodeConstrained(pos, value, 0, length);
        }
    }

    /** Decodes with explicit mode. Returns a bean with the corresponding constraint set. */
    public static CmsOctetString decode(PerInputStream pis, Mode mode, int length) throws PerDecodeException {
        byte[] data;
        if (mode == Mode.FIXED) {
            data = PerOctetString.decodeFixedSize(pis, length);
            return new CmsOctetString(data).size(length);
        } else {
            data = PerOctetString.decodeConstrained(pis, 0, length);
            return new CmsOctetString(data).max(length);
        }
    }

    @Override
    public String toString() {
        if (value == null || value.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < value.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(String.format("%02X", value[i] & 0xFF));
        }
        sb.append(']');
        return sb.toString();
    }
}

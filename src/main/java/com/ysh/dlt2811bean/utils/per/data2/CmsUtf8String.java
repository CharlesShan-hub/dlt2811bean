package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerUtf8String;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 UNICODE STRING (UTF8String) type (§7.1.5, Table 6).
 *
 * <pre>
 * ┌─────────────────┬──────────────────────────┬───────────┬───────────┐
 * │ 2811            │ Range                   │ Bits      │ Java type │
 * ├─────────────────┼──────────────────────────┼───────────┼───────────┤
 * │ UNICODE STRING  │ UTF-8 encoded           │ variable  │ String    │
 * └─────────────────┴──────────────────────────┴───────────┴───────────┘
 * </pre>
 *
 * <p>Supports two constraint modes via {@code size} (fixed) or {@code max} (variable).
 * Mutually exclusive — setting one clears the other.
 * <ul>
 *   <li><b>FIXED</b>: fixed-length, BMP mode only (UCS-2)</li>
 *   <li><b>VARIABLE</b> (SIZE(0..max)): UTF-8 or BMP (2 bytes/char)</li>
 * </ul>
 *
 * <p>BMP mode (UCS-2, 2 bytes/char) can be enabled via {@code bmp} flag.
 *
 * <pre>
 * // Bean mode — variable UTF-8, chain setters
 * CmsUtf8String name = new CmsUtf8String()
 *     .setValue("设备名称")
 *     .setMax(255);
 * CmsUtf8String.encode(pos, name);
 *
 * // Bean mode — variable BMP, chain setters
 * CmsUtf8String bmpName = new CmsUtf8String()
 *     .setValue("设备A")
 *     .setMax(64)
 *     .setBmp(true);
 * CmsUtf8String.encode(pos, bmpName);
 *
 * // Quick mode — raw value + explicit Mode
 * CmsUtf8String.encode(pos, "设备名称", Mode.VARIABLE, 255);
 * CmsUtf8String.encode(pos, "设备A", Mode.VARIABLE, 64, true);  // BMP
 * CmsUtf8String.decode(pis, Mode.VARIABLE, 255);
 * CmsUtf8String.decode(pis, Mode.VARIABLE, 64, true);            // BMP
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsUtf8String {

    /** Encoding mode: FIXED for SIZE(n), VARIABLE for SIZE(0..max). */
    public enum Mode {
        /** Fixed-size: SIZE(n), BMP mode only. */
        FIXED,
        /** Variable-size: SIZE(0..max), length prefix encoded. */
        VARIABLE
    }

    private String value;
    private Integer size;
    private Integer max;
    private boolean bmp = false;

    public CmsUtf8String() {
        this.value = "";
    }

    public CmsUtf8String(String value) {
        this.value = value != null ? value : "";
    }

    /** Set fixed size (SIZE(n)). Clears max. */
    public CmsUtf8String size(int size) {
        this.size = size;
        this.max = null;
        return this;
    }

    /** Set variable max (SIZE(0..max)). Clears size. */
    public CmsUtf8String max(int max) {
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
            throw new IllegalStateException("UTF8 STRING constraint not set: call size(n) or max(n)");
        }
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsUtf8String bean. Uses bean's internal size/max/bmp settings. */
    public static void encode(PerOutputStream pos, CmsUtf8String bean) {
        bean.validate();
        if (bean.bmp) {
            encodeBmp(pos, bean);
        } else {
            encodeUtf8(pos, bean);
        }
    }

    /** Encodes a raw String with explicit mode (UTF-8). */
    public static void encode(PerOutputStream pos, String value, Mode mode, int length) {
        if (mode == Mode.FIXED) {
            throw new UnsupportedOperationException("Fixed-mode UTF-8 is not typical; use unconstrained encoding");
        }
        PerUtf8String.encodeUtf8Constrained(pos, value, 0, length);
    }

    /** Encodes a raw String with explicit mode and BMP flag. */
    public static void encode(PerOutputStream pos, String value, Mode mode, int length, boolean bmp) {
        if (bmp) {
            if (mode == Mode.FIXED) {
                PerUtf8String.encodeBmpFixedSize(pos, value, length);
            } else {
                PerUtf8String.encodeBmpConstrained(pos, value, 0, length);
            }
        } else {
            encode(pos, value, mode, length);
        }
    }

    /** Decodes with explicit mode (UTF-8). */
    public static CmsUtf8String decode(PerInputStream pis, Mode mode, int length) throws PerDecodeException {
        if (mode == Mode.FIXED) {
            throw new UnsupportedOperationException("Fixed-mode UTF-8 decode not typical");
        }
        String val = PerUtf8String.decodeUtf8Constrained(pis, 0, length);
        return new CmsUtf8String(val).max(length);
    }

    /** Decodes with explicit mode and BMP flag. */
    public static CmsUtf8String decode(PerInputStream pis, Mode mode, int length, boolean bmp) throws PerDecodeException {
        if (bmp) {
            String val;
            if (mode == Mode.FIXED) {
                val = PerUtf8String.decodeBmpFixedSize(pis, length);
                return new CmsUtf8String(val).size(length).setBmp(true);
            } else {
                val = PerUtf8String.decodeBmpConstrained(pis, 0, length);
                return new CmsUtf8String(val).max(length).setBmp(true);
            }
        } else {
            return decode(pis, mode, length);
        }
    }

    // ==================== Internal ====================

    private static void encodeUtf8(PerOutputStream pos, CmsUtf8String bean) {
        if (bean.size != null) {
            throw new UnsupportedOperationException("Fixed-mode UTF-8 encoding is not typical");
        }
        PerUtf8String.encodeUtf8Constrained(pos, bean.value, 0, bean.max);
    }

    private static void encodeBmp(PerOutputStream pos, CmsUtf8String bean) {
        if (bean.size != null) {
            PerUtf8String.encodeBmpFixedSize(pos, bean.value, bean.size);
        } else {
            PerUtf8String.encodeBmpConstrained(pos, bean.value, 0, bean.max);
        }
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}

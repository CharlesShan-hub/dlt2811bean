package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerVisibleString;

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
 *     .set("LD1/LN0.DO1")
 *     .max(255);
 * ref.encode(pos);
 *
 * // Decode (returns self for chaining) — must set size or max first
 * CmsVisibleString r = new CmsVisibleString().max(255).decode(pis);
 * String s = r.get();
 *
 * // Or use static read method
 * CmsVisibleString r2 = CmsVisibleString.read(pis, Mode.VARIABLE, 255);
 * </pre>
 */
public final class CmsVisibleString extends AbstractCmsString<CmsVisibleString, String> {

    public CmsVisibleString() {
        super("VISIBLE STRING", "");
    }

    public CmsVisibleString(String value) {
        super("VISIBLE STRING", value != null ? value : "");
    }

    @Override
    protected void encodeFixedSize(PerOutputStream pos) {
        PerVisibleString.encodeFixedSize(pos, get(), size);
    }

    @Override
    protected void encodeConstrained(PerOutputStream pos) {
        PerVisibleString.encodeConstrained(pos, get(), 0, max);
    }

    @Override
    protected String decodeValueFixedSize(PerInputStream pis) throws Exception {
        return PerVisibleString.decodeFixedSize(pis, size);
    }

    @Override
    protected String decodeValueConstrained(PerInputStream pis) throws Exception {
        return PerVisibleString.decodeConstrained(pis, 0, max);
    }

    /** Static write with raw value and explicit mode. */
    public static void write(PerOutputStream pos, String value, Mode mode, int length) {
        if (mode == Mode.FIXED) {
            PerVisibleString.encodeFixedSize(pos, value, length);
        } else {
            PerVisibleString.encodeConstrained(pos, value, 0, length);
        }
    }

    /** Static write with instance (null encodes default empty). */
    public static void write(PerOutputStream pos, CmsVisibleString obj) {
        if (obj == null) {
            new CmsVisibleString().encode(pos);
        } else {
            obj.encode(pos);
        }
    }

    /** Static decode with explicit mode. */
    public static CmsVisibleString read(PerInputStream pis, Mode mode, int length) throws Exception {
        CmsVisibleString result = new CmsVisibleString();
        if (mode == Mode.FIXED) {
            result.size(length);
        } else {
            result.max(length);
        }
        return result.decode(pis);
    }
}

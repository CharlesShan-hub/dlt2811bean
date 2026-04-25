package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerUtf8String;

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
 *     .set("设备名称")
 *     .max(255);
 * name.encode(pos);
 *
 * // Decode (returns self for chaining) — must set size or max first
 * CmsUtf8String r = new CmsUtf8String().max(255).decode(pis);
 * String s = r.get();
 *
 * // Or use static read method
 * CmsUtf8String r2 = CmsUtf8String.read(pis, Mode.VARIABLE, 255);
 * </pre>
 */
public class CmsUtf8String extends AbstractCmsString<CmsUtf8String, String> {

    private boolean bmp = false;

    public CmsUtf8String() {
        this("");
    }

    public CmsUtf8String(String value) {
        this("UTF8String", value);
    }

    /** Subclass constructor — allows subclasses to set a custom type name. */
    protected CmsUtf8String(String typeName, String value) {
        super(typeName, value != null ? value : "");
    }

    @Override
    public CmsUtf8String copy() {
        CmsUtf8String clone = new CmsUtf8String(get());
        clone.bmp = this.bmp;
        if (size != null) clone.size(size);
        if (max != null) clone.max(max);
        return clone;
    }

    /** Enable BMP mode (UCS-2, 2 bytes per character). */
    public CmsUtf8String bmp(boolean bmp) {
        this.bmp = bmp;
        return this;
    }

    public boolean isBmp() { return bmp; }

    @Override
    protected void encodeFixedSize(PerOutputStream pos) {
        if (bmp) {
            PerUtf8String.encodeBmpFixedSize(pos, get(), size);
        } else {
            throw new UnsupportedOperationException("Fixed-mode UTF-8 is not typical; use unconstrained encoding");
        }
    }

    @Override
    protected void encodeConstrained(PerOutputStream pos) {
        if (bmp) {
            PerUtf8String.encodeBmpConstrained(pos, get(), 0, max);
        } else {
            PerUtf8String.encodeUtf8Constrained(pos, get(), 0, max);
        }
    }

    @Override
    protected String decodeValueFixedSize(PerInputStream pis) throws Exception {
        if (bmp) {
            return PerUtf8String.decodeBmpFixedSize(pis, size);
        } else {
            throw new UnsupportedOperationException("Fixed-mode UTF-8 decode not typical");
        }
    }

    @Override
    protected String decodeValueConstrained(PerInputStream pis) throws Exception {
        if (bmp) {
            return PerUtf8String.decodeBmpConstrained(pis, 0, max);
        } else {
            return PerUtf8String.decodeUtf8Constrained(pis, 0, max);
        }
    }

    /** Static write with raw value and explicit mode (UTF-8). */
    public static void write(PerOutputStream pos, String value, Mode mode, int length) {
        write(pos, value, mode, length, false);
    }

    /** Static write with raw value, explicit mode and BMP flag. */
    public static void write(PerOutputStream pos, String value, Mode mode, int length, boolean bmp) {
        CmsUtf8String temp = new CmsUtf8String(value != null ? value : "").bmp(bmp);
        
        if (mode == Mode.FIXED) {
            temp.size(length);
        } else {
            temp.max(length);
        }
        
        temp.encode(pos);
    }

    /** Static write with instance (null encodes default empty). */
    public static void write(PerOutputStream pos, CmsUtf8String obj) {
        if (obj == null) {
            new CmsUtf8String().encode(pos);
        } else {
            obj.encode(pos);
        }
    }

    /** Static decode with explicit mode (UTF-8). */
    public static CmsUtf8String read(PerInputStream pis, Mode mode, int length) throws Exception {
        return read(pis, mode, length, false);
    }

    /** Static decode with explicit mode and BMP flag. */
    public static CmsUtf8String read(PerInputStream pis, Mode mode, int length, boolean bmp) throws Exception {
        if (mode == Mode.FIXED) {
            if (bmp) {
                CmsUtf8String result = new CmsUtf8String().size(length).bmp(true);
                return result.decode(pis);
            } else {
                throw new UnsupportedOperationException("Fixed-mode UTF-8 decode not typical");
            }
        } else {
            CmsUtf8String result = new CmsUtf8String();
            result.max(length);
            result.bmp(bmp);
            return result.decode(pis);
        }
    }
}

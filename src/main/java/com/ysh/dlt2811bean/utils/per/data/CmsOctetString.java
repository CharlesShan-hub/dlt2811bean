package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerOctetString;

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
 *     .set(new byte[]{0x01, 0x02})
 *     .size(8);
 * id.encode(pos);
 *
 * CmsOctetString ident = new CmsOctetString()
 *     .set(new byte[]{0xAB})
 *     .max(64);
 * ident.encode(pos);
 *
 * // Decode (returns self for chaining) — must set size or max first
 * CmsOctetString r = new CmsOctetString().size(8).decode(pis);
 * byte[] bytes = r.get();
 *
 * // Or use static read method
 * CmsOctetString r2 = CmsOctetString.read(pis, Mode.FIXED, 8);
 * </pre>
 */
public class CmsOctetString extends AbstractCmsString<CmsOctetString, byte[]> {

    public CmsOctetString() {
        this(new byte[0]);
    }

    public CmsOctetString(byte[] value) {
        this("OCTET STRING", value);
    }

    /** Subclass constructor — allows subclasses to set a custom type name. */
    protected CmsOctetString(String typeName, byte[] value) {
        super(typeName, value != null ? value : new byte[0]);
    }

    @Override
    public void encodeFixedSize(PerOutputStream pos){
        PerOctetString.encodeFixedSize(pos, get(), size);
    }
    
    @Override
    public void encodeConstrained(PerOutputStream pos){
        PerOctetString.encodeConstrained(pos, get(), 0, max);
    }

    @Override
    protected byte[] decodeValueFixedSize(PerInputStream pis) throws Exception {
        return PerOctetString.decodeFixedSize(pis, size);
    }

    @Override
    protected byte[] decodeValueConstrained(PerInputStream pis) throws Exception {
        return PerOctetString.decodeConstrained(pis, 0, max);
    }

    /** Static write with raw value and explicit mode. */
    public static void write(PerOutputStream pos, byte[] value, Mode mode, int length) {
        if (mode == Mode.FIXED) {
            PerOctetString.encodeFixedSize(pos, value, length);
        } else {
            PerOctetString.encodeConstrained(pos, value, 0, length);
        }
    }

    /** Static decode with explicit mode. */
    public static CmsOctetString read(PerInputStream pis, Mode mode, int length) throws Exception {
        CmsOctetString result = new CmsOctetString();
        if (mode == Mode.FIXED) {
            result.size(length);
        } else {
            result.max(length);
        }
        return result.decode(pis);
    }

    @Override
    public CmsOctetString copy() {
        CmsOctetString clone = new CmsOctetString(get().clone());
        if (size != null) clone.size(size);
        if (max != null) clone.max(max);
        return clone;
    }

    @Override
    public String toString() {
        if (get() == null || get().length == 0) return "OCTET STRING: []";
        StringBuilder sb = new StringBuilder("OCTET STRING: [");
        for (int i = 0; i < get().length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(String.format("%02X", get()[i] & 0xFF));
        }
        sb.append(']');
        return sb.toString();
    }
}

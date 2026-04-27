package com.ysh.dlt2811bean.datatypes.string;

import com.ysh.dlt2811bean.per.io.PerInputStream;

/**
 * DL/T 2811 EntryID type (§7.3.8).
 *
 * <pre>
 * EntryID ::= OCTET STRING (SIZE(8))
 * </pre>
 *
 * <p>Fixed-size 8-byte opaque identifier. No internal structure defined by the standard.
 *
 * <pre>
 * // Construct
 * CmsEntryID id = new CmsEntryID(new byte[8]);
 * CmsEntryID id = new CmsEntryID();   // all zeros
 *
 * // Encode / Decode
 * id.encode(pos);
 * CmsEntryID r = new CmsEntryID().decode(pis);
 *
 * // Access
 * r.get();  // → byte[8]
 * </pre>
 */
public class CmsEntryID extends CmsOctetString {

    /** Fixed size per §7.3.8. */
    public static final int SIZE = 8;

    public CmsEntryID() {
        super("EntryID", new byte[SIZE]);
        size(SIZE);
    }

    public CmsEntryID(byte[] value) {
        super("EntryID", new byte[SIZE]);
        size(SIZE);
        set(value);
    }

    @Override
    public CmsEntryID set(byte[] value) {
        super.set(value);
        if (value.length != SIZE) {
            throw new IllegalArgumentException(
                    "EntryID length must be " + SIZE + ", got " + value.length);
        }
        return this;
    }

    @Override
    public CmsEntryID copy() {
        return new CmsEntryID(get().clone());
    }

    @Override
    public CmsEntryID decode(PerInputStream pis) throws Exception {
        return (CmsEntryID) super.decode(pis);
    }

    @Override
    public String toString() {
        byte[] data = get();
        if (data == null || data.length == 0) return "EntryID: []";
        StringBuilder sb = new StringBuilder("EntryID: [");
        for (int i = 0; i < data.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(String.format("%02X", data[i] & 0xFF));
        }
        sb.append(']');
        return sb.toString();
    }
}

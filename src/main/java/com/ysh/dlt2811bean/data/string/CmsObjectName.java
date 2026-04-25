package com.ysh.dlt2811bean.data.string;

import com.ysh.dlt2811bean.per.io.PerInputStream;

/**
 * DL/T 2811 ObjectName type (§7.3.1).
 *
 * <pre>
 * ObjectName ::= VisibleString(SIZE(0..64))
 * </pre>
 *
 * <p>Variable-length visible string, max 64 characters.
 * Used to identify objects within a server (e.g. logical device name,
 * logical node name, data object name).
 *
 * <pre>
 * // Construct
 * CmsObjectName name = new CmsObjectName("LD1");
 * CmsObjectName name = new CmsObjectName();   // empty
 *
 * // Encode / Decode
 * name.encode(pos);
 * CmsObjectName r = new CmsObjectName().decode(pis);
 *
 * // Access
 * r.get();  // → "LD1"
 * </pre>
 */
public class CmsObjectName extends CmsVisibleString {

    /** Maximum length defined by §7.3.1. */
    public static final int MAX_LENGTH = 64;

    public CmsObjectName() {
        super("ObjectName", "");
        max(MAX_LENGTH);
    }

    public CmsObjectName(String value) {
        super("ObjectName", "");
        max(MAX_LENGTH);
        set(value);
    }

    @Override
    public CmsObjectName set(String value) {
        super.set(value);
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "ObjectName length " + value.length() + " exceeds max " + MAX_LENGTH);
        }
        return this;
    }

    @Override
    public CmsObjectName copy() {
        return new CmsObjectName(get());
    }

    @Override
    public CmsObjectName decode(PerInputStream pis) throws Exception {
        return (CmsObjectName) super.decode(pis);
    }
}

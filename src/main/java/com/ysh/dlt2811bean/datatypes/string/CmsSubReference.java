package com.ysh.dlt2811bean.datatypes.string;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * DL/T 2811 SubReference type (§7.3.3).
 *
 * <pre>
 * SubReference ::= VisibleString (SIZE(0..129))
 * </pre>
 *
 * <p>Relative reference under a parent node, e.g. "LN.DO.DA.BDA" or "DA.BDA".
 * SubReference is a relative path and must not contain '/' (which belongs to ObjectReference).
 *
 * <pre>
 * // Construct
 * CmsSubReference ref = new CmsSubReference("LN.DO.DA");
 * CmsSubReference ref = new CmsSubReference();   // empty
 *
 * // Encode / Decode
 * ref.encode(pos);
 * CmsSubReference r = new CmsSubReference().decode(pis);
 *
 * // Access
 * r.get();  // → "LN.DO.DA"
 * </pre>
 */
public class CmsSubReference extends CmsVisibleString {

    /** Maximum length per §7.3.3. */
    public static final int MAX_LENGTH = 129;

    public CmsSubReference() {
        super("SubReference", "");
        max(MAX_LENGTH);
    }

    public CmsSubReference(String value) {
        super("SubReference", "");
        max(MAX_LENGTH);
        set(value);
    }

    @Override
    public CmsSubReference set(String value) {
        super.set(value);
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "SubReference length " + value.length() + " exceeds max " + MAX_LENGTH);
        }
        validate(value);
        return this;
    }

    @Override
    public CmsSubReference copy() {
        return new CmsSubReference(get());
    }

    @Override
    public CmsSubReference decode(PerInputStream pis) throws Exception {
        return (CmsSubReference) super.decode(pis);
    }

    private static final CmsSubReference SHARED = new CmsSubReference();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, String value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsSubReference read(PerInputStream pis) throws Exception {
        return new CmsSubReference().decode(pis);
    }

    /** Validate sub-reference constraints: no '/' allowed. */
    public static void validate(String value) {
        if (value != null && value.indexOf('/') >= 0) {
            throw new IllegalArgumentException(
                    "SubReference must not contain '/': " + value);
        }
    }
}

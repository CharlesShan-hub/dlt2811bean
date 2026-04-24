package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;

/**
 * DL/T 2811 ObjectReference type (§7.3.2).
 *
 * <pre>
 * ObjectReference ::= VisibleString (SIZE(0..129))
 * </pre>
 *
 * <p>Object reference format: {@code LDName/LNName[.Name[....]]}
 *
 * <p>Constraints:
 * <ul>
 *   <li>No FC (functional constraint) suffix — e.g. {@code .ST} is rejected</li>
 *   <li>No '$' character allowed in the reference</li>
 * </ul>
 *
 * <pre>
 * // Construct
 * CmsObjectReference ref = new CmsObjectReference("LD1/LN1.DO1");
 * CmsObjectReference ref = new CmsObjectReference();   // empty
 *
 * // Encode / Decode
 * ref.encode(pos);
 * CmsObjectReference r = new CmsObjectReference().decode(pis);
 *
 * // Access
 * r.get();  // → "LD1/LN1.DO1"
 * </pre>
 */
public class CmsObjectReference extends CmsVisibleString {

    /** Maximum length per §7.3.2. */
    public static final int MAX_LENGTH = 129;

    public CmsObjectReference() {
        super("ObjectReference", "");
        max(MAX_LENGTH);
    }

    public CmsObjectReference(String value) {
        super("ObjectReference", "");
        max(MAX_LENGTH);
        set(value);
    }

    @Override
    public CmsObjectReference set(String value) {
        super.set(value);
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "ObjectReference length " + value.length() + " exceeds max " + MAX_LENGTH);
        }
        validate(value);
        return this;
    }

    @Override
    public CmsObjectReference decode(PerInputStream pis) throws Exception {
        return (CmsObjectReference) super.decode(pis);
    }

    /** Validate object reference constraints. */
    public static void validate(String value) {
        if (value == null || value.isEmpty()) return;

        if (value.indexOf('$') >= 0) {
            throw new IllegalArgumentException(
                    "ObjectReference must not contain '$': " + value);
        }

        int dot = value.lastIndexOf('.');
        if (dot >= 0 && dot < value.length() - 1) {
            String lastSegment = value.substring(dot + 1);
            if (CmsFC.isValid(lastSegment) || CmsFC.isValid(lastSegment.toUpperCase())) {
                throw new IllegalArgumentException(
                        "ObjectReference must not contain FC suffix '." + lastSegment.toUpperCase()
                                + "': " + value);
            }
        }
    }
}

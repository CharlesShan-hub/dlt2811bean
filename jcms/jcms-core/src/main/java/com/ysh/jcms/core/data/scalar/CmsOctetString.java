package com.ysh.jcms.core.data.scalar;

import com.ysh.jcms.data.DefaultInnerOctetString;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.core.CmsScalar;

/**
 * <pre>
 * {@code
 * OctetString ::= OCTET STRING — 7.1.5
 * }
 * </pre>
 *
 * <p>
 * Generic OCTET STRING wrapper, backed by DefaultInnerOctetString. Sync to the
 * real Inner* field is handled by the parent CmsSequence via @Field injection.
 */
public class CmsOctetString extends CmsScalar {

    public CmsOctetString() {
        super(new DefaultInnerOctetString());
    }
    public CmsOctetString(byte[] v) {
        this();
        value(v);
    }

    /**
     * Return the raw bytes. After decode the Inner tree stores OCTET STRINGs as JER
     * hex strings, so both byte[] and hex-String forms are accepted.
     */
    public byte[] value() {
        Object v = innerGet();
        if (v instanceof byte[])
            return (byte[]) v;
        if (v instanceof String)
            return InnerBase.unhex((String) v);
        return null;
    }

    public CmsOctetString value(byte[] v) {
        innerSet(v);
        return this;
    }
}

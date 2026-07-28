package com.ysh.jcms.data.string;

import com.ysh.jcms.core.CmsScalar;
import com.ysh.jcms.data.DefaultInnerOctetString;

/**
 * Generic OCTET STRING wrapper, backed by DefaultInnerOctetString.
 * Sync to the real Inner* field is handled by the parent CmsSequence
 * via @Field injection.
 */
public class CmsOctetString extends CmsScalar {

    public CmsOctetString() { super(new DefaultInnerOctetString()); }
    public CmsOctetString(byte[] v) { this(); value(v); }

    public byte[] value() { return (byte[]) innerGet(); }
    public CmsOctetString value(byte[] v) { innerSet(v); return this; }
}

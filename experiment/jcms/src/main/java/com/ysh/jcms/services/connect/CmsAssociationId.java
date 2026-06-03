package com.ysh.jcms.services.connect;

import com.ysh.jcms.datatypes.string.CmsOctetString;
import com.ysh.jcms.datatypes.type.AbstractCmsString.Mode;

import java.util.Arrays;

/**
 * AssociationId — OCTET STRING (SIZE(0..64)).
 *
 * <p>Format defined by server; opaque identifier for an application association.
 *
 * <p>Thread-safety: not guaranteed.
 */
public class CmsAssociationId extends CmsOctetString {

    public static final int MAX_LEN = 64;

    public CmsAssociationId() {
        super();
        max(MAX_LEN);
    }

    public CmsAssociationId(byte[] value) {
        super(value);
        max(MAX_LEN);
    }

    public static CmsAssociationId decode(byte[] encoded) {
        CmsOctetString os = CmsOctetString.decode(encoded, Mode.VARIABLE, MAX_LEN);
        CmsAssociationId id = new CmsAssociationId(os.get());
        return id;
    }

    @Override
    public CmsAssociationId copy() {
        byte[] v = get();
        return new CmsAssociationId(v != null ? v.clone() : new byte[0]);
    }
}

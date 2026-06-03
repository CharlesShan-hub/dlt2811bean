package com.ysh.jcms.services.connect;

import com.ysh.jcms.datatypes.string.CmsOctetString;
import com.ysh.jcms.datatypes.type.AbstractCmsString.Mode;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.types.PerOctetString;

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

    @Override
    protected void ffiDecode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_associationId_decode(data, data.length, v);
        this.value = CmsOctetString.decode(encoded, Mode.VARIABLE, MAX_LEN).get();
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = (CmsOctetString.decode(encoded, Mode.VARIABLE, MAX_LEN).get());
    }

    public static CmsAssociationId from(byte[] data) {
        return new CmsAssociationId().decode(data);
    }

    @Override
    public CmsAssociationId copy() {
        byte[] v = get();
        return new CmsAssociationId(v != null ? v.clone() : new byte[0]);
    }
}

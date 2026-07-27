package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerGetServerDirectoryRequestPDU;
import com.ysh.jcms.data.common.CmsObjectReference;

/**
 * GetServerDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, objectClass [0]
 * IMPLICIT ObjectClass, referenceAfter [1] IMPLICIT ObjectReference OPTIONAL }
 * — 8.3.1
 */
public class CmsGetServerDirectoryRequest extends CmsSequence {

    public CmsGetServerDirectoryRequest() {
        super(new InnerGetServerDirectoryRequestPDU());
    }

    public int getObjectClass() { return getInt("objectClass"); }
    public CmsGetServerDirectoryRequest objectClass(int v) {
        setInt("objectClass", v); return this;
    }

    /** Lazy cached wrapper backed by inner.referenceAfter. */
    public CmsObjectReference refAfter() {
        return getWrapper("referenceAfter", CmsObjectReference.class);
    }
    public CmsGetServerDirectoryRequest refAfter(byte[] v) {
        return refAfter(v != null ? new String(v) : null);
    }
    public CmsGetServerDirectoryRequest refAfter(String v) {
        setPresent("referenceAfter", v != null);
        if (v != null) refAfter().value(v);
        return this;
    }
}

package com.ysh.jcms.pdu.directory;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetServerDirectoryRequestPDU;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * GetServerDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, objectClass [0]
 * IMPLICIT ObjectClass, referenceAfter [1] IMPLICIT ObjectReference OPTIONAL }
 * — 8.3.1
 */
public class CmsGetServerDirectoryRequest extends CmsSequence {

    public CmsGetServerDirectoryRequest() {
        super(new InnerGetServerDirectoryRequestPDU());
    }

    public int getObjectClass() {
        return getInt("objectClass");
    }
    public CmsGetServerDirectoryRequest objectClass(int v) {
        setInt("objectClass", v);
        return this;
    }

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    public CmsGetServerDirectoryRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsGetServerDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
}

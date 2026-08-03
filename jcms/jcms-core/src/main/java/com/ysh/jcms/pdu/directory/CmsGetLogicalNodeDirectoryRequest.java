package com.ysh.jcms.pdu.directory;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryRequestPDU;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.choice.CmsReferenceChoice;

/**
 * <pre>
 * {@code
 * GetLogicalNodeDirectory-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT CHOICE {
 *         ldName        [0] IMPLICIT ObjectName,
 *         lnReference   [1] IMPLICIT ObjectReference
 *     },
 *     acsiClass       [1] IMPLICIT ACSIClass,
 *     referenceAfter  [2] IMPLICIT ObjectReference OPTIONAL
 * } — 8.3.3
 * }
 * </pre>
 */
public class CmsGetLogicalNodeDirectoryRequest extends CmsSequence {

    @CmsField
    public CmsReferenceChoice reference;

    @CmsField
    public CmsAcsiClass acsiClass;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    public CmsGetLogicalNodeDirectoryRequest() {
        super(new InnerGetLogicalNodeDirectoryRequestPDU());
    }

    public CmsGetLogicalNodeDirectoryRequest reference(CmsReferenceChoice v) {
        this.reference.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest acsiClass(int v) {
        this.acsiClass.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest referenceAfter(byte[] v) {
        if (v != null && v.length > 0) {
            this.referenceAfter.value(new String(v, StandardCharsets.UTF_8));
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
}

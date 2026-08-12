package com.ysh.jcms.core.pdu.directory;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllDataValuesRequestPDU;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.choice.CmsReferenceChoice;

/**
 * <pre>
 * {@code
 * GetAllDataValues-RequestPDU ::= SEQUENCE {
 *     reference        [0] IMPLICIT CHOICE {
 *         ldName         [0] IMPLICIT ObjectName,
 *         lnReference    [1] IMPLICIT ObjectReference
 *     },
 *     fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
 * } — 8.3.4
 * }
 * </pre>
 */
public class CmsGetAllDataValuesRequest extends CmsSequence {

    @CmsField
    public CmsReferenceChoice reference;

    @CmsField(optional = true)
    public CmsFC fc;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    public CmsGetAllDataValuesRequest() {
        super(new InnerGetAllDataValuesRequestPDU());
    }

    public CmsGetAllDataValuesRequest reference(CmsReferenceChoice v) {
        this.reference.value(v);
        return this;
    }
    public CmsGetAllDataValuesRequest fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
        return this;
    }
    public CmsGetAllDataValuesRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsGetAllDataValuesRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
}

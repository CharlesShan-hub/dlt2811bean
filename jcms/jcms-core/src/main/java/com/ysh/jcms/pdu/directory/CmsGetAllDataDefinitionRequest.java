package com.ysh.jcms.pdu.directory;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllDataDefinitionRequestPDU;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.choice.CmsReferenceChoice;

/**
 * GetAllDataDefinition-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT ReferenceChoice, fc [1] IMPLICIT FunctionalConstraint OPTIONAL,
 * referenceAfter [2] IMPLICIT ObjectReference OPTIONAL } — 8.3.5
 */
public class CmsGetAllDataDefinitionRequest extends CmsSequence {

    @CmsField
    public CmsReferenceChoice reference;

    @CmsField(optional = true)
    public CmsFC fc; /* OPTIONAL */

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter; /* OPTIONAL */

    public CmsGetAllDataDefinitionRequest() {
        super(new InnerGetAllDataDefinitionRequestPDU());
    }

    public CmsGetAllDataDefinitionRequest reference(CmsReferenceChoice v) { this.reference.value(v); return this; }
    public CmsGetAllDataDefinitionRequest fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
        return this;
    }
    public CmsGetAllDataDefinitionRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsGetAllDataDefinitionRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
}

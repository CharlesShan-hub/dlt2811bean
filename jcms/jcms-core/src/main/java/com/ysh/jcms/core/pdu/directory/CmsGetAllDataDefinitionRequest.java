package com.ysh.jcms.core.pdu.directory;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllDataDefinitionRequestPDU;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.choice.CmsReferenceChoice;

/**
 * <pre>
 * {@code
 * GetAllDataDefinition-RequestPDU ::= SEQUENCE {
 *     reference        [0] IMPLICIT CHOICE {
 *         ldName         [0] IMPLICIT ObjectName,
 *         lnReference    [1] IMPLICIT ObjectReference
 *     },
 *     fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
 * } — 8.3.5
 * }
 * </pre>
 */
public class CmsGetAllDataDefinitionRequest extends CmsSequence {

    @CmsField
    public CmsReferenceChoice reference;

    @CmsField(optional = true)
    public CmsFC fc;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    public CmsGetAllDataDefinitionRequest() {
        super(new InnerGetAllDataDefinitionRequestPDU());
    }

    public CmsGetAllDataDefinitionRequest reference(CmsReferenceChoice v) {
        this.reference.value(v);
        return this;
    }
    /** Convenience: auto-detect ldName (no "/") vs lnReference (contains "/"). */
    public CmsGetAllDataDefinitionRequest reference(String v) {
        Objects.requireNonNull(v, "reference must not be null");
        if (v.contains("/")) {
            this.reference.altLnReference(v);
        } else {
            this.reference.altLdName(v);
        }
        return this;
    }
    public CmsGetAllDataDefinitionRequest fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
        return this;
    }
    public CmsGetAllDataDefinitionRequest fc(String v) {
        if (v != null) {
            this.fc.value(v);
            setPresent("fc", true);
        } else {
            setPresent("fc", false);
        }
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

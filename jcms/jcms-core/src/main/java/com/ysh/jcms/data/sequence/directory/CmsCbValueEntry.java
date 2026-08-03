package com.ysh.jcms.data.sequence.directory;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousGetAllCBValuesResponsePDUCbValue;
import com.ysh.jcms.data.choice.CmsCbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsSubReference;

/**
 * <pre>
 * {@code
 * CBValueEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     value         [1] IMPLICIT CHOICE {
 *         brcb        [0] IMPLICIT BRCB,
 *         urcb        [1] IMPLICIT URCB,
 *         lcb         [2] IMPLICIT LCB,
 *         sgecb       [3] IMPLICIT SGECB,
 *         gocb        [4] IMPLICIT GOCB,
 *         msvcb       [5] IMPLICIT MSVCB
 *     }
 * } — 8.3.6
 * }
 * </pre>
 *
 * <p>
 * Used by GetAllCBValues response (SEQUENCE OF CBValueEntry). Backed by
 * {@link InnerAnonymousGetAllCBValuesResponsePDUCbValue}.
 */
public class CmsCbValueEntry extends CmsSequence {

    @CmsField
    public CmsSubReference reference;
    @CmsField
    public CmsCbValueChoice value;

    public CmsCbValueEntry() {
        super(new InnerAnonymousGetAllCBValuesResponsePDUCbValue());
    }

    public CmsCbValueEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsCbValueEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsCbValueEntry value(CmsCbValueChoice v) {
        this.value.value(v);
        return this;
    }

    /** Copy all field values from another CmsCbValueEntry (fluent). */
    public CmsCbValueEntry value(CmsCbValueEntry v) {
        reference(v.reference.value());
        value(v.value);
        return this;
    }
}

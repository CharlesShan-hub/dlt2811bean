package com.ysh.jcms.pdu.goose;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetGoReferenceRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetGoReference-RequestPDU ::= SEQUENCE {
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     memberOfs       [1] IMPLICIT SEQUENCE OF INT16U
 * } — 8.9.2
 * }
 * </pre>
 */
public class CmsGetGoReferenceRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference gocbReference;

    @CmsField(sequenceOf = true, elementType = CmsInt16U.class)
    public List<CmsInt16U> memberOfs; /* SEQUENCE OF Int16U */

    public CmsGetGoReferenceRequest() {
        super(new InnerGetGoReferenceRequestPDU());
        this.memberOfs = new ArrayList<>();
    }

    public CmsGetGoReferenceRequest gocbReference(String v) {
        this.gocbReference.value(v);
        return this;
    }
    public CmsGetGoReferenceRequest gocbReference(byte[] v) {
        return gocbReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetGoReferenceRequest memberOfs(List<CmsInt16U> v) {
        this.memberOfs = v;
        return this;
    }
}

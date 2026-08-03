package com.ysh.jcms.pdu.goose;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetGoReferenceResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.goose.CmsGoRefFcEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetGoReference-ResponsePDU ::= SEQUENCE {
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     confRev         [1] IMPLICIT INT32U,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     memberData      [3] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         fc          [1] IMPLICIT FunctionalConstraint
 *     }
 * } — 8.9.2
 * }
 * </pre>
 */
public class CmsGetGoReferenceResponse extends CmsSequence {

    @CmsField
    public CmsObjectReference gocbReference;

    @CmsField
    public CmsInt32U confRev;

    @CmsField
    public CmsObjectReference datSet;

    @CmsField(sequenceOf = true, elementType = CmsGoRefFcEntry.class)
    public List<CmsGoRefFcEntry> memberData; /* SEQUENCE OF GoRefFcEntry */

    public CmsGetGoReferenceResponse() {
        super(new InnerGetGoReferenceResponsePDU());
        this.memberData = new ArrayList<>();
    }

    public CmsGetGoReferenceResponse gocbReference(String v) {
        this.gocbReference.value(v);
        return this;
    }
    public CmsGetGoReferenceResponse gocbReference(byte[] v) {
        return gocbReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetGoReferenceResponse confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsGetGoReferenceResponse datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsGetGoReferenceResponse datSet(byte[] v) {
        return datSet(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetGoReferenceResponse memberData(List<CmsGoRefFcEntry> v) {
        this.memberData = v;
        return this;
    }
}

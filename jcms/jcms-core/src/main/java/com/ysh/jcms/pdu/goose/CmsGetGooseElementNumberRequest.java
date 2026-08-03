package com.ysh.jcms.pdu.goose;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetGOOSEElementNumberRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.goose.CmsGoRefFcEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetGOOSEElementNumber-RequestPDU ::= SEQUENCE { gocbReference [0] IMPLICIT
 * ObjectReference, memberData [1] IMPLICIT SEQUENCE OF SEQUENCE { reference [0]
 * IMPLICIT ObjectReference, fc [1] IMPLICIT FunctionalConstraint } } — 8.9.3
 */
public class CmsGetGooseElementNumberRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference gocbReference;

    @CmsField(sequenceOf = true, elementType = CmsGoRefFcEntry.class)
    public List<CmsGoRefFcEntry> memberData; /* SEQUENCE OF GoRefFcEntry */

    public CmsGetGooseElementNumberRequest() {
        super(new InnerGetGOOSEElementNumberRequestPDU());
        this.memberData = new ArrayList<>();
    }

    public CmsGetGooseElementNumberRequest gocbReference(String v) {
        this.gocbReference.value(v);
        return this;
    }
    public CmsGetGooseElementNumberRequest gocbReference(byte[] v) {
        return gocbReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetGooseElementNumberRequest memberData(List<CmsGoRefFcEntry> v) {
        this.memberData = v;
        return this;
    }
}

package com.ysh.jcms.core.pdu.goose;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetGOOSEElementNumberResponsePDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.data.scalar.CmsInt32U;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetGOOSEElementNumber-ResponsePDU ::= SEQUENCE {
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     confRev         [1] IMPLICIT INT32U,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     memberOffset    [3] IMPLICIT SEQUENCE OF INT16U
 * } — 8.9.3
 * }
 * </pre>
 */
public class CmsGetGooseElementNumberResponse extends CmsSequence {

    @CmsField
    public CmsObjectReference gocbReference;

    @CmsField
    public CmsInt32U confRev;

    @CmsField
    public CmsObjectReference datSet;

    @CmsField(sequenceOf = true, elementType = CmsInt16U.class)
    public List<CmsInt16U> memberOffset; /* SEQUENCE OF Int16U */

    public CmsGetGooseElementNumberResponse() {
        super(new InnerGetGOOSEElementNumberResponsePDU());
        this.memberOffset = new ArrayList<>();
    }

    public CmsGetGooseElementNumberResponse gocbReference(String v) {
        this.gocbReference.value(v);
        return this;
    }
    public CmsGetGooseElementNumberResponse gocbReference(byte[] v) {
        return gocbReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetGooseElementNumberResponse confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsGetGooseElementNumberResponse datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsGetGooseElementNumberResponse datSet(byte[] v) {
        return datSet(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetGooseElementNumberResponse memberOffset(List<CmsInt16U> v) {
        this.memberOffset = v;
        return this;
    }
}

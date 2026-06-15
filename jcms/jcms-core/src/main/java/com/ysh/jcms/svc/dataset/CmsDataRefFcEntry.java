package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import java.util.Arrays;
import java.util.List;

/**
 * DataRefFcEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint
 * }
 *
 * Used by CreateDataSet Request, GetDataSetDirectory Response.
 */
public class CmsDataRefFcEntry extends CmsType {

    public CmsObjectReference     reference;
    public CmsFunctionalConstraint fc;

    public CmsDataRefFcEntry() {
        this.reference = new CmsObjectReference();
        this.fc        = new CmsFunctionalConstraint();
    }
    
    // -- chain setters --
    public CmsDataRefFcEntry reference(byte[] v) { this.reference.value(v); return this; }
    public CmsDataRefFcEntry reference(String v) { this.reference.value(v); return this; }
    public CmsDataRefFcEntry fc(byte[] v) { this.fc.value(v); return this; }
    public CmsDataRefFcEntry fc(String v) { this.fc.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fc);
    }
}
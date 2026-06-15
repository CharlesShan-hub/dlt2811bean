package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import java.util.Arrays;
import java.util.List;

/**
 * GoRefFcEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint
 * }
 *
 * Used by GetGoReference response, GetGOOSEElementNumber request.
 */
public class CmsGoRefFcEntry extends CmsType {

    public CmsObjectReference     reference;
    public CmsFunctionalConstraint fc;

    public CmsGoRefFcEntry() {
        this.reference = new CmsObjectReference();
        this.fc        = new CmsFunctionalConstraint();
    }
    
    // -- chain setters --
    public CmsGoRefFcEntry reference(byte[] v) { this.reference.value(v); return this; }
    public CmsGoRefFcEntry reference(String v) { this.reference.value(v); return this; }
    public CmsGoRefFcEntry fc(byte[] v) { this.fc.value(v); return this; }
    public CmsGoRefFcEntry fc(String v) { this.fc.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fc);
    }
}
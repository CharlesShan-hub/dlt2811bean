package com.ysh.jcms.core.data.sequence.block;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.core.data.scalar.*;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.sequence.common.CmsPhyComAddr;
import com.ysh.jcms.core.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * GoCB ::= SEQUENCE {
 *     goEna           [1] IMPLICIT BOOLEAN,
 *     goID            [2] IMPLICIT VisibleString129,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT INT32U,
 *     ndsCom          [5] IMPLICIT BOOLEAN,
 *     dstAddress      [6] IMPLICIT PHYCOMADDR OPTIONAL
 * } — 8.9.4
 * }
 * </pre>
 */
public class CmsGoCb extends CmsSequence {
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsBoolean goEna;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsString goID;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsObjectReference datSet;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsInt32U confRev;
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsBoolean ndsCom;
    @CmsField(optional = true)
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsPhyComAddr dstAddress;

    public CmsGoCb() {
        super(new InnerGoCB());
    }

    public CmsGoCb goEna(boolean v) {
        this.goEna.value(v);
        return this;
    }
    public CmsGoCb goID(String v) {
        this.goID.value(v);
        return this;
    }
    public CmsGoCb datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsGoCb confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsGoCb ndsCom(boolean v) {
        this.ndsCom.value(v);
        return this;
    }
    public CmsGoCb dstAddress(CmsPhyComAddr v) {
        if (v != null) {
            this.dstAddress.value(v);
            setPresent("dstAddress", true);
        } else {
            setPresent("dstAddress", false);
        }
        return this;
    }

    /** Copy all field values from another CmsGoCb (fluent). */
    public CmsGoCb value(CmsGoCb v) {
        goEna(v.goEna.value());
        goID(v.goID.value());
        datSet(v.datSet.value());
        confRev(v.confRev.value());
        ndsCom(v.ndsCom.value());
        if (v.isPresent("dstAddress")) {
            this.dstAddress.value(v.dstAddress);
            setPresent("dstAddress", true);
        } else {
            setPresent("dstAddress", false);
        }
        return this;
    }
}

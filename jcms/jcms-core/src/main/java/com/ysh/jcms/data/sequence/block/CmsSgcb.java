package com.ysh.jcms.data.sequence.block;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

/**
 * <pre>
 * {@code
 * SGCB ::= SEQUENCE {
 *     numOfSG       [1] IMPLICIT INT8U,
 *     actSG         [2] IMPLICIT INT8U,
 *     editSG        [3] IMPLICIT INT8U,
 *     tActEdt       [4] IMPLICIT TimeStamp,
 *     resvTms       [5] IMPLICIT INT16U OPTIONAL
 * } — 8.6.6
 * }
 * </pre>
 */
public class CmsSgcb extends CmsSequence {
    @CmsField
    public CmsInt8U numOfSG;
    @CmsField
    public CmsInt8U actSG;
    @CmsField
    public CmsInt8U editSG;
    @CmsField
    public CmsUtcTime tActEdt; // TimeStamp ::= UtcTime
    @CmsField(optional = true)
    public CmsInt16U resvTms;

    public CmsSgcb() {
        super(new InnerSGCB());
    }

    public CmsSgcb numOfSG(int v) {
        this.numOfSG.value(v);
        return this;
    }
    public CmsSgcb actSG(int v) {
        this.actSG.value(v);
        return this;
    }
    public CmsSgcb editSG(int v) {
        this.editSG.value(v);
        return this;
    }
    public CmsSgcb resvTms(int v) {
        this.resvTms.value(v);
        setPresent("resvTms", true);
        return this;
    }

    /** Copy all field values from another CmsSgcb (fluent). */
    public CmsSgcb value(CmsSgcb v) {
        numOfSG(v.numOfSG.value());
        actSG(v.actSG.value());
        editSG(v.editSG.value());
        if (v.isPresent("resvTms")) {
            this.resvTms.value(v.resvTms.value());
            setPresent("resvTms", true);
        } else {
            setPresent("resvTms", false);
        }
        return this;
    }
}

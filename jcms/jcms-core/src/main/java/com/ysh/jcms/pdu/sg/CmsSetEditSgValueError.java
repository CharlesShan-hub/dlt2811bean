package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerSetEditSGValueErrorPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsServiceError;

import java.util.ArrayList;
import java.util.List;

/**
 * SetEditSGValue-ErrorPDU ::= SEQUENCE { result [0] IMPLICIT SEQUENCE OF
 * ServiceError } — 8.6.3
 */
public class CmsSetEditSgValueError extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsServiceError.class)
    public List<CmsServiceError> result; /* SEQUENCE OF ServiceError */

    public CmsSetEditSgValueError() {
        super(new InnerSetEditSGValueErrorPDU());
        this.result = new ArrayList<>();
    }

    public CmsSetEditSgValueError result(List<CmsServiceError> v) {
        this.result = v;
        return this;
    }
}

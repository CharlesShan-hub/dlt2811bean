package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.InnerServiceError;
import com.ysh.jcms.data.InnerSetDataValuesErrorPDU;
import com.ysh.jcms.data.core.CmsSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * SetDataValues-ErrorPDU ::= SEQUENCE {
 *     result           [0] IMPLICIT SEQUENCE OF ServiceError
 * } — 8.4.2
 */
public class CmsSetDataValuesError extends CmsSequence {

    public List<Integer> result; /* SEQUENCE OF ServiceError (integer values) */

    public CmsSetDataValuesError() {
        super(new InnerSetDataValuesErrorPDU());
        this.result = new ArrayList<>();
    }

    public CmsSetDataValuesError result(List<Integer> v) {
        this.result = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerSetDataValuesErrorPDU inner = (InnerSetDataValuesErrorPDU) this.inner;
        inner.result.clear();
        for (Integer v : result) {
            inner.result.add(new InnerServiceError(v));
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerSetDataValuesErrorPDU inner = (InnerSetDataValuesErrorPDU) this.inner;
        result = new ArrayList<>();
        for (InnerServiceError e : inner.result) {
            result.add(e.value);
        }
    }
}

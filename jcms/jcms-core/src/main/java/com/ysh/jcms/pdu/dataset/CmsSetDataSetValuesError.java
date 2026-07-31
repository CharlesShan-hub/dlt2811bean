package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerServiceError;
import com.ysh.jcms.data.InnerSetDataSetValuesErrorPDU;
import com.ysh.jcms.data.core.CmsSequence;
import java.util.ArrayList;
import java.util.List;

/**
 * SetDataSetValues-ErrorPDU ::= SEQUENCE {
 *     result              [0] IMPLICIT SEQUENCE OF ServiceError
 * } — 8.5.2
 */
public class CmsSetDataSetValuesError extends CmsSequence {

    public List<Integer> result; /* SEQUENCE OF ServiceError (integer values) */

    public CmsSetDataSetValuesError() {
        super(new InnerSetDataSetValuesErrorPDU());
        this.result = new ArrayList<>();
    }

    public CmsSetDataSetValuesError result(List<Integer> v) {
        this.result = v;
        return this;
    }

    @Override
    public void syncToInner() {
        @SuppressWarnings("unchecked")
        List<InnerBase> innerList = (List<InnerBase>) inner._v.get("result");
        innerList.clear();
        for (Integer v : result) {
            innerList.add(new InnerServiceError(v));
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        @SuppressWarnings("unchecked")
        List<InnerBase> innerList = (List<InnerBase>) inner._v.get("result");
        result = new ArrayList<>();
        for (InnerBase e : innerList) {
            Object val = e.getValue();
            result.add(val instanceof Integer ? (Integer) val : (int) val);
        }
    }
}

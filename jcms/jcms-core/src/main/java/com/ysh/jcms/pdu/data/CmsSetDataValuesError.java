package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.InnerBase;
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
        List<Object> innerList = (List<Object>) inner._v.get("result");
        result = new ArrayList<>();
        if (innerList != null) {
            for (Object e : innerList) {
                // After encode: InnerServiceError wrappers; after decode: raw Integers
                Object val = e instanceof InnerBase ? ((InnerBase) e).getValue() : e;
                result.add(val instanceof Integer ? (Integer) val : (int) val);
            }
        }
    }
}

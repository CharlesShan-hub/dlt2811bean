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
        // Rebuild the list instead of mutating whatever _v holds (after decode it
        // is a raw List<Integer>; mutating it with InnerServiceError would corrupt
        // the element types for any later List<Integer> consumer).
        List<InnerBase> innerList = new ArrayList<>();
        for (Integer v : result) {
            innerList.add(new InnerServiceError(v));
        }
        inner._v.put("result", innerList);
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

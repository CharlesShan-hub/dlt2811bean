package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetDataValues-RequestPDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT ObjectReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 *     }
 * } — 8.4.1
 */
public class CmsGetDataValuesRequest extends CmsSequence {

    public List<CmsDataRefEntry> data; /* SEQUENCE OF DataRefEntry */

    public CmsGetDataValuesRequest() {
        super(new InnerGetDataValuesRequestPDU());
        this.data = new ArrayList<>();
    }

    public CmsGetDataValuesRequest data(List<CmsDataRefEntry> v) {
        this.data = v;
        return this;
    }


}

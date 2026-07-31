package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetDataDefinition-RequestPDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT ObjectReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 *     }
 * } — 8.4.4
 */
public class CmsGetDataDefinitionRequest extends CmsSequence {

    public List<CmsDataRefEntry> data; /* SEQUENCE OF DataRefEntry */

    public CmsGetDataDefinitionRequest() {
        super(new InnerGetDataDefinitionRequestPDU());
        this.data = new ArrayList<>();
    }

    public CmsGetDataDefinitionRequest data(List<CmsDataRefEntry> v) {
        this.data = v;
        return this;
    }


}

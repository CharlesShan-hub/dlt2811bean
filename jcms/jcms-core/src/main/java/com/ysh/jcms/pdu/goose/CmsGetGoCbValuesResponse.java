package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.InnerGetGoCbValuesResponsePDU;
import com.ysh.jcms.data.choice.CmsGocbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * GetGoCbValues-ResponsePDU ::= SEQUENCE {
 *     gocb        [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error   [0] IMPLICIT ServiceError,
 *         value   [1] IMPLICIT GoCB
 *     },
 *     moreFollows [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.9.4
 */
public class CmsGetGoCbValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsGocbValueChoice.class)
    public List<CmsGocbValueChoice> gocb; /* SEQUENCE OF GoCBValueChoice */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetGoCbValuesResponse() {
        super(new InnerGetGoCbValuesResponsePDU());
        this.gocb = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetGoCbValuesResponse gocb(List<CmsGocbValueChoice> v) { this.gocb = v; return this; }
    public CmsGetGoCbValuesResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }
}

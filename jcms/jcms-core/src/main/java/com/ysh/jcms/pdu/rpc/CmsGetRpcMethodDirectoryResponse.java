package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDirectoryResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsString;

import java.util.ArrayList;
import java.util.List;

/**
 * GetRpcMethodDirectory-ResponsePDU ::= SEQUENCE {
 *     reference   [0] IMPLICIT SEQUENCE OF VisibleString,
 *     moreFollows [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.13.3
 */
public class CmsGetRpcMethodDirectoryResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsString.class)
    public List<CmsString> reference; /* SEQUENCE OF VisibleString */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetRpcMethodDirectoryResponse() {
        super(new InnerGetRpcMethodDirectoryResponsePDU());
        this.reference = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetRpcMethodDirectoryResponse reference(List<CmsString> v) {
        this.reference = v;
        return this;
    }
    public CmsGetRpcMethodDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}

package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcInterfaceDirectoryResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsString;

import java.util.ArrayList;
import java.util.List;

/**
 * GetRpcInterfaceDirectory-ResponsePDU ::= SEQUENCE {
 *     reference   [0] IMPLICIT SEQUENCE OF VisibleString,
 *     moreFollows [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.13.2
 */
public class CmsGetRpcInterfaceDirectoryResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsString.class)
    public List<CmsString> reference; /* SEQUENCE OF VisibleString */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetRpcInterfaceDirectoryResponse() {
        super(new InnerGetRpcInterfaceDirectoryResponsePDU());
        this.reference = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetRpcInterfaceDirectoryResponse reference(List<CmsString> v) {
        this.reference = v;
        return this;
    }
    public CmsGetRpcInterfaceDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}

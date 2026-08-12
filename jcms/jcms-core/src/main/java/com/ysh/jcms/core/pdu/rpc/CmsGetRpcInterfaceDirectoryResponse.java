package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcInterfaceDirectoryResponsePDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.scalar.CmsString;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetRpcInterfaceDirectory-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF VisibleString,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.13.2
 * }
 * </pre>
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

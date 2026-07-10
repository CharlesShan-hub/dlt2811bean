package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetServerDirectory-ResponsePDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT SEQUENCE OF ObjectReference, moreFollows [1] IMPLICIT BOOLEAN
 * DEFAULT TRUE } — 8.3.1
 */
public class CmsGetServerDirectoryResponse extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetServerDirectoryResponse() {
        super(Codec.GET_SERVER_DIRECTORY_RESPONSE);
        this.reqId = new CmsReqId();
        this.reference = new CmsArray<>(CmsObjectReference.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetServerDirectoryResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetServerDirectoryResponse reference(CmsArray<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }
    public CmsGetServerDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, moreFollows);
    }
}

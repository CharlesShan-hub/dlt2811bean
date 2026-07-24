package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogicalNodeDirectory-ResponsePDU ::= SEQUENCE { reqId Int16U, reference
 * [0] IMPLICIT SEQUENCE OF SubReference, moreFollows [1] IMPLICIT BOOLEAN
 * DEFAULT TRUE } — 8.3.3
 */
public class CmsGetLogicalNodeDirectoryResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsSubReference> reference; /* SEQUENCE OF SubReference */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLogicalNodeDirectoryResponse() {
        super(Codec.GET_LOGICAL_NODE_DIRECTORY_RESPONSE);
        this.reqId = new CmsReqId();
        this.reference = new CmsArray<>(CmsSubReference.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetLogicalNodeDirectoryResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryResponse reference(CmsArray<CmsSubReference> v) {
        this.reference = v;
        return this;
    }
    public CmsGetLogicalNodeDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    /** Convenience: extract reference strings as List. */
    public List<String> refs() {
        List<String> refs = new ArrayList<>();
        for (int i = 0; i < reference.count; i++) {
            refs.add(new String(reference.items.get(i).value()));
        }
        return refs;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, reference, moreFollows);
    }
}

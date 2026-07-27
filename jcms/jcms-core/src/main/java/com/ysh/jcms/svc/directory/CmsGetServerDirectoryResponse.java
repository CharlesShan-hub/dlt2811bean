package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetServerDirectoryResponsePDU;
import com.ysh.jcms.data.InnerObjectReference;
import com.ysh.jcms.data.common.CmsObjectReference;
import java.util.ArrayList;
import java.util.List;

/**
 * GetServerDirectory-ResponsePDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT SEQUENCE OF ObjectReference, moreFollows [1] IMPLICIT BOOLEAN
 * DEFAULT TRUE } — 8.3.1
 */
public class CmsGetServerDirectoryResponse extends CmsType {

    public List<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */
    public boolean moreFollows; /* DEFAULT TRUE */

    public CmsGetServerDirectoryResponse() {
        super(new InnerGetServerDirectoryResponsePDU());
        this.reference = new ArrayList<>();
    }

    public CmsGetServerDirectoryResponse reference(List<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }
    public CmsGetServerDirectoryResponse moreFollows(boolean v) {
        this.moreFollows = v;
        return this;
    }

    /** Convenience: extract LD names as String list. */
    public List<String> ldNames() {
        List<String> names = new ArrayList<>();
        for (CmsObjectReference ref : reference) {
            names.add(ref.value());
        }
        return names;
    }

    @Override
    public void syncToInner() {
        InnerGetServerDirectoryResponsePDU inner = (InnerGetServerDirectoryResponsePDU) this.inner;
        inner.reference.clear();
        for (CmsObjectReference ref : reference) {
            inner.reference.add((InnerObjectReference) ref.inner);
        }
        inner.moreFollows.value = moreFollows ? 1 : 0;
    }

    @Override
    public void syncFromInner() {
        InnerGetServerDirectoryResponsePDU inner = (InnerGetServerDirectoryResponsePDU) this.inner;
        reference = new ArrayList<>();
        for (InnerObjectReference innerRef : inner.reference) {
            CmsObjectReference ref = new CmsObjectReference();
            ref.inner = innerRef;
            reference.add(ref);
        }
        this.moreFollows = inner.moreFollows.value() != 0;
    }
}

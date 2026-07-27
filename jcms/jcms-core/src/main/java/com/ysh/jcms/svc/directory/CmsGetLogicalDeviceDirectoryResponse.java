package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetLogicalDeviceDirectoryResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import com.ysh.jcms.data.common.CmsSubReference;
import java.util.ArrayList;
import java.util.List;

/**
 * GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE { reqId Int16U,
 * lnReference [0] IMPLICIT SEQUENCE OF SubReference, moreFollows [1] IMPLICIT
 * BOOLEAN DEFAULT TRUE } — 8.3.2
 */
public class CmsGetLogicalDeviceDirectoryResponse extends CmsType {

    public List<CmsSubReference> lnReference; /* SEQUENCE OF SubReference */
    public boolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLogicalDeviceDirectoryResponse() {
        super(new InnerGetLogicalDeviceDirectoryResponsePDU());
        this.lnReference = new ArrayList<>();
    }

    public CmsGetLogicalDeviceDirectoryResponse lnReference(List<CmsSubReference> v) {
        this.lnReference = v;
        return this;
    }
    public CmsGetLogicalDeviceDirectoryResponse moreFollows(boolean v) {
        this.moreFollows = v;
        return this;
    }

    /** Convenience: extract LN reference strings as List. */
    public List<String> lnNames() {
        List<String> names = new ArrayList<>();
        for (CmsSubReference ref : lnReference) {
            names.add(ref.value());
        }
        return names;
    }

    @Override
    public void syncToInner() {
        InnerGetLogicalDeviceDirectoryResponsePDU inner = (InnerGetLogicalDeviceDirectoryResponsePDU) this.inner;
        inner.lnReference.clear();
        for (CmsSubReference ref : lnReference) {
            inner.lnReference.add((InnerSubReference) ref.inner);
        }
        inner.moreFollows.value = moreFollows ? 1 : 0;
    }

    @Override
    public void syncFromInner() {
        InnerGetLogicalDeviceDirectoryResponsePDU inner = (InnerGetLogicalDeviceDirectoryResponsePDU) this.inner;
        lnReference = new ArrayList<>();
        for (InnerSubReference innerRef : inner.lnReference) {
            CmsSubReference ref = new CmsSubReference();
            ref.inner = innerRef;
            lnReference.add(ref);
        }
        this.moreFollows = inner.moreFollows.value() != 0;
    }
}

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
 * GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE { reqId Int16U,
 * lnReference [0] IMPLICIT SEQUENCE OF SubReference, moreFollows [1] IMPLICIT
 * BOOLEAN DEFAULT TRUE } — 8.3.2
 */
public class CmsGetLogicalDeviceDirectoryResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsSubReference> lnReference; /* SEQUENCE OF SubReference */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLogicalDeviceDirectoryResponse() {
        super(Codec.GET_LOGICAL_DEVICE_DIRECTORY_RESPONSE);
        this.reqId = new CmsReqId();
        this.lnReference = new CmsArray<>(CmsSubReference.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetLogicalDeviceDirectoryResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetLogicalDeviceDirectoryResponse lnReference(CmsArray<CmsSubReference> v) {
        this.lnReference = v;
        return this;
    }
    public CmsGetLogicalDeviceDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    /** Convenience: extract LN reference strings as List. */
    public List<String> lnNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < lnReference.count; i++) {
            names.add(new String(lnReference.items.get(i).value()));
        }
        return names;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, lnReference, moreFollows);
    }
}

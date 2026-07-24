package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataDirectory-ResponsePDU ::= SEQUENCE { reqId Int16U, dataAttribute [0]
 * IMPLICIT SEQUENCE OF SubRefEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT
 * TRUE } — 8.4.3
 */
public class CmsGetDataDirectoryResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsSubRefEntry> dataAttribute; /* SEQUENCE OF SubRefEntry */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataDirectoryResponse() {
        super(Codec.GET_DATA_DIRECTORY_RESPONSE);
        this.reqId = new CmsReqId();
        this.dataAttribute = new CmsArray<>(CmsSubRefEntry.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetDataDirectoryResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetDataDirectoryResponse dataAttribute(CmsArray<CmsSubRefEntry> v) {
        this.dataAttribute = v;
        return this;
    }
    public CmsGetDataDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    /** Convenience: extract data attribute reference strings as List. */
    public List<String> dataAttributes() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < dataAttribute.count; i++) {
            names.add(new String(dataAttribute.items.get(i).reference.value()));
        }
        return names;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, dataAttribute, moreFollows);
    }
}

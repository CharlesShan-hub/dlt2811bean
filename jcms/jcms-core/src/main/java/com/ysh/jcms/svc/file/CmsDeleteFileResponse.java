package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteFile-ResponsePDU ::= SEQUENCE { reqId Int16U } — 8.12.3
 */
public class CmsDeleteFileResponse extends CmsTypeOld {

    public CmsReqId reqId;

    public CmsDeleteFileResponse() {
        super(Codec.DELETE_FILE_RESPONSE);
        this.reqId = new CmsReqId();
    }

    public CmsDeleteFileResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId);
    }
}

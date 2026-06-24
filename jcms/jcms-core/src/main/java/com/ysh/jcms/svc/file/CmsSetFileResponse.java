package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetFile-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.12.2
 */
public class CmsSetFileResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetFileResponse() { super(Codec.SET_FILE_RESPONSE);
        this.reqId = new CmsReqId();
    }
    
    public CmsSetFileResponse reqId(int v) { this.reqId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }
}
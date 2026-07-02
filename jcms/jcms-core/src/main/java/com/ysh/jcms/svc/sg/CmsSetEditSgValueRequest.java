package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetEditSGValue-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF SGRefValueEntry
 * }  —  8.6.3
 */
public class CmsSetEditSgValueRequest extends CmsType {

    public CmsReqId                         reqId;
    public CmsArray<CmsSgRefValueEntry>     data;   /* SEQUENCE OF SGRefValueEntry */

    public CmsSetEditSgValueRequest() { super(Codec.SET_EDIT_SG_VALUE_REQUEST);
        this.reqId = new CmsReqId();
        this.data  = new CmsArray<>(CmsSgRefValueEntry.class);
    }
    
    public CmsSetEditSgValueRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetEditSgValueRequest data(CmsArray<CmsSgRefValueEntry> v) { this.data = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, data);
    }
}
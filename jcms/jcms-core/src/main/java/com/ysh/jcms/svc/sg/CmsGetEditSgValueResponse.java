package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetEditSGValue-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     value           [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.6.5
 */
public class CmsGetEditSgValueResponse extends CmsType {

    public CmsReqId              reqId;
    public CmsArray<CmsData>     value;        /* SEQUENCE OF Data */
    public CmsBoolean            moreFollows;  /* DEFAULT TRUE */

    public CmsGetEditSgValueResponse() {
        this.reqId       = new CmsReqId();
        this.value       = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }
    
    // -- chain setters --
    public CmsGetEditSgValueResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetEditSgValueResponse value(CmsArray<CmsData> v) { this.value = v; return this; }
    public CmsGetEditSgValueResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, value, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetEditSgValueResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetEditSgValueResponse(nativePtr, data); read(); }
}
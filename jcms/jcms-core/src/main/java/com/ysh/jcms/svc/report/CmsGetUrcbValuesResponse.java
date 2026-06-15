package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetURCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     urcb            [0] IMPLICIT SEQUENCE OF RCBValueChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.7.4
 */
public class CmsGetUrcbValuesResponse extends CmsType {

    public CmsReqId                        reqId;
    public CmsArray<CmsRcbValueChoice>     urcb;         /* SEQUENCE OF RCBValueChoice */
    public CmsBoolean                      moreFollows;  /* DEFAULT TRUE */

    public CmsGetUrcbValuesResponse() {
        this.reqId       = new CmsReqId();
        this.urcb        = new CmsArray<>(CmsRcbValueChoice.class);
        this.moreFollows = new CmsBoolean();
    }
    
    // -- chain setters --
    public CmsGetUrcbValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetUrcbValuesResponse urcb(CmsArray<CmsRcbValueChoice> v) { this.urcb = v; return this; }
    public CmsGetUrcbValuesResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, urcb, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetUrcbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetUrcbValuesResponse(nativePtr, data); read(); }
}
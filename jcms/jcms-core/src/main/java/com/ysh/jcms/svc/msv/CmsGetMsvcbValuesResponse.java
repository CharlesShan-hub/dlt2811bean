package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetMSVCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     msvcb           [0] IMPLICIT SEQUENCE OF MSVCBValueChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.10.2
 */
public class CmsGetMsvcbValuesResponse extends CmsType {

    public CmsReqId                            reqId;
    public CmsArray<CmsMsvcbValueChoice>       msvcb;        /* SEQUENCE OF MSVCBValueChoice */
    public CmsBoolean                          moreFollows;  /* DEFAULT TRUE */

    public CmsGetMsvcbValuesResponse() {
        this.reqId       = new CmsReqId();
        this.msvcb       = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }
    
    public CmsGetMsvcbValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetMsvcbValuesResponse msvcb(CmsArray<CmsMsvcbValueChoice> v) { this.msvcb = v; return this; }
    public CmsGetMsvcbValuesResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, msvcb, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetMsvcbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetMsvcbValuesResponse(nativePtr, data); read(); }
}
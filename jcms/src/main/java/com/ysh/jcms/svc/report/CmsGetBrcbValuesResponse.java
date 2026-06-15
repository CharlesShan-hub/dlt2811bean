package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetBRCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     brcb            [0] IMPLICIT SEQUENCE OF RCBValueChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.7.2
 */
public class CmsGetBrcbValuesResponse extends CmsType {

    public CmsReqId                        reqId;
    public CmsArray<CmsRcbValueChoice>     brcb;         /* SEQUENCE OF RCBValueChoice */
    public CmsBoolean                      moreFollows;  /* DEFAULT TRUE */

    public CmsGetBrcbValuesResponse() {
        this.reqId       = new CmsReqId();
        this.brcb        = new CmsArray<>(CmsRcbValueChoice.class);
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, brcb, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetBrcbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetBrcbValuesResponse(nativePtr, data); read(); }
}

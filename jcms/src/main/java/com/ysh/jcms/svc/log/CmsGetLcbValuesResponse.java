package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     lcb             [0] IMPLICIT SEQUENCE OF LCBValueChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.8.2
 */
public class CmsGetLcbValuesResponse extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsLcbValueChoice>    lcb;          /* SEQUENCE OF LCBValueChoice */
    public CmsBoolean                     moreFollows;  /* DEFAULT TRUE */

    public CmsGetLcbValuesResponse() {
        this.reqId       = new CmsReqId();
        this.lcb         = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, lcb, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetLcbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetLcbValuesResponse(nativePtr, data); read(); }
}

package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogStatusValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     log             [0] IMPLICIT SEQUENCE OF LogStatusValueChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.8.6
 */
public class CmsGetLogStatusValuesResponse extends CmsType {

    public CmsReqId                                reqId;
    public CmsArray<CmsLogStatusValueChoice>       log;          /* SEQUENCE OF LogStatusValueChoice */
    public CmsBoolean                              moreFollows;  /* DEFAULT TRUE */

    public CmsGetLogStatusValuesResponse() {
        this.reqId       = new CmsReqId();
        this.log         = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, log, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetLogStatusValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetLogStatusValuesResponse(nativePtr, data); read(); }
}

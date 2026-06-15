package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetURCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     urcb            [0] IMPLICIT SEQUENCE OF SetURCBEntry
 * }  —  8.7.5
 */
public class CmsSetUrcbValuesRequest extends CmsType {

    public CmsReqId                        reqId;
    public CmsArray<CmsSetUrcbEntry>       urcb;   /* SEQUENCE OF SetURCBEntry */

    public CmsSetUrcbValuesRequest() {
        this.reqId = new CmsReqId();
        this.urcb  = new CmsArray<>(CmsSetUrcbEntry.class);
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, urcb);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetUrcbValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetUrcbValuesRequest(nativePtr, data); read(); }
}

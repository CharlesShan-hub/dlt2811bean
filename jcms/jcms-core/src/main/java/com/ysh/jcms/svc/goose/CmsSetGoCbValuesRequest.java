package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetGoCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     gocb            [0] IMPLICIT SEQUENCE OF SetGoCBEntry
 * }  —  8.9.5
 */
public class CmsSetGoCbValuesRequest extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsSetGoCbEntry>      gocb;   /* SEQUENCE OF SetGoCBEntry */

    public CmsSetGoCbValuesRequest() {
        this.reqId = new CmsReqId();
        this.gocb  = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, gocb);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetGoCbValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetGoCbValuesRequest(nativePtr, data); read(); }
}

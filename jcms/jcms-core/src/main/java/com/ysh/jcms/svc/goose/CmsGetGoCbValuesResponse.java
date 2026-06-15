package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGoCbValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     gocb            [0] IMPLICIT SEQUENCE OF GoCBValueChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.9.4
 */
public class CmsGetGoCbValuesResponse extends CmsType {

    public CmsReqId                          reqId;
    public CmsArray<CmsGocbValueChoice>      gocb;         /* SEQUENCE OF GoCBValueChoice */
    public CmsBoolean                        moreFollows;  /* DEFAULT TRUE */

    public CmsGetGoCbValuesResponse() {
        this.reqId       = new CmsReqId();
        this.gocb        = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }
    
    public CmsGetGoCbValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetGoCbValuesResponse gocb(CmsArray<CmsGocbValueChoice> v) { this.gocb = v; return this; }
    public CmsGetGoCbValuesResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, gocb, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetGoCbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetGoCbValuesResponse(nativePtr, data); read(); }
}
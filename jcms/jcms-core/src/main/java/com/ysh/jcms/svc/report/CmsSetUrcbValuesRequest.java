package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsSetUrcbValuesRequest() { super(Codec.SET_URCB_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.urcb  = new CmsArray<>(CmsSetUrcbEntry.class);
    }
    
    public CmsSetUrcbValuesRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetUrcbValuesRequest urcb(CmsArray<CmsSetUrcbEntry> v) { this.urcb = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, urcb);
    }
}
package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     value           [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.4.1
 */
public class CmsGetDataValuesResponse extends CmsType {

    public CmsReqId              reqId;
    public CmsArray<CmsData>     value;        /* SEQUENCE OF Data */
    public CmsBoolean            moreFollows;  /* DEFAULT TRUE */

    public CmsGetDataValuesResponse() {
        this.reqId       = new CmsReqId();
        this.value       = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }
    
    // -- chain setters --
    public CmsGetDataValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetDataValuesResponse value(CmsArray<CmsData> v) { this.value = v; return this; }
    public CmsGetDataValuesResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, value, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetDataValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetDataValuesResponse(nativePtr, data); read(); }
}
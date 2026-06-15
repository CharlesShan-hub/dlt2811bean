package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     cbValue         [0] IMPLICIT SEQUENCE OF CBValueEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.3.6
 */
public class CmsGetAllCbValuesResponse extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsCbValueEntry>      cbValue;      /* SEQUENCE OF CBValueEntry */
    public CmsBoolean                     moreFollows;  /* DEFAULT TRUE */

    public CmsGetAllCbValuesResponse() {
        this.reqId       = new CmsReqId();
        this.cbValue     = new CmsArray<>(CmsCbValueEntry.class);
        this.moreFollows = new CmsBoolean();
    }
    
    // -- chain setters --
    public CmsGetAllCbValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetAllCbValuesResponse cbValue(CmsArray<CmsCbValueEntry> v) { this.cbValue = v; return this; }
    public CmsGetAllCbValuesResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, cbValue, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetAllCbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetAllCbValuesResponse(nativePtr, data); read(); }
}
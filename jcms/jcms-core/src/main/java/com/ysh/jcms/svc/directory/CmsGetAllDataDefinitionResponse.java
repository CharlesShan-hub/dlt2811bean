package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllDataDefinition-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF DataDefinitionEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.3.5
 */
public class CmsGetAllDataDefinitionResponse extends CmsType {

    public CmsReqId                            reqId;
    public CmsArray<CmsDataDefinitionEntry>    data;          /* SEQUENCE OF DataDefinitionEntry */
    public CmsBoolean                          moreFollows;   /* DEFAULT TRUE */

    public CmsGetAllDataDefinitionResponse() {
        this.reqId       = new CmsReqId();
        this.data        = new CmsArray<>(CmsDataDefinitionEntry.class);
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, data, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetAllDataDefinitionResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetAllDataDefinitionResponse(nativePtr, data); read(); }
}

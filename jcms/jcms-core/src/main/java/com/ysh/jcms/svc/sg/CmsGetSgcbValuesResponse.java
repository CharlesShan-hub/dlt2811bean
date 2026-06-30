package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetSGCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     sgscb           [0] IMPLICIT SEQUENCE OF SGCBValueChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.6.6
 */
public class CmsGetSgcbValuesResponse extends CmsType {

    public CmsReqId                           reqId;
    public CmsArray<CmsSgcbValueChoice>       sgscb;        /* SEQUENCE OF SGCBValueChoice */
    public CmsBoolean                         moreFollows;  /* DEFAULT TRUE */

    public CmsGetSgcbValuesResponse() { super(Codec.GET_SGCB_VALUES_RESPONSE);
        this.reqId       = new CmsReqId();
        this.sgscb       = new CmsArray<>(CmsSgcbValueChoice.class);
        this.moreFollows = new CmsBoolean();
    }
    
    public CmsGetSgcbValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetSgcbValuesResponse sgscb(CmsArray<CmsSgcbValueChoice> v) { this.sgscb = v; return this; }
    public CmsGetSgcbValuesResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, sgscb, moreFollows);
    }
}
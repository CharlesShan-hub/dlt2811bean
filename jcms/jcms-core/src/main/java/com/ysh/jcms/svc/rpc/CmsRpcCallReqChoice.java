package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerated.CmsEnumerated;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * RpcCallReqChoice ::= CHOICE {
 *     reqData     [0] IMPLICIT Data,
 *     callID      [1] IMPLICIT OCTET STRING
 * }  —  8.13.6
 *
 * Used by RpcCall request.
 */
public class CmsRpcCallReqChoice extends CmsType {

    public static final int REQ_DATA = 0;
    public static final int CALL_ID  = 1;

    public CmsEnumerated  choice;         /* 0=reqData, 1=callID */
    public CmsData        altReqData;
    public CmsUint8Array  altCallId;

    public CmsRpcCallReqChoice() {
        this.choice     = new CmsEnumerated();
        this.altReqData = new CmsData();
        this.altCallId  = new CmsUint8Array();
    }
    
    // -- chain setters --
    public CmsRpcCallReqChoice choice(int v) { this.choice.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, altReqData, altCallId);
    }
}
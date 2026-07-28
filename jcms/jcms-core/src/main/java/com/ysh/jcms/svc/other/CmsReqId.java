package com.ysh.jcms.svc.other;

import com.ysh.jcms.data.scalar.CmsInt16U;

/**
 * ReqId ::= Int16U — Int16U ::= INTEGER (0..65535) typedef cms_int16u_t
 * cms_req_id_t;
 *
 * Alias for CmsInt16U.
 */
public class CmsReqId extends CmsInt16U {

    public CmsReqId() {
    }
    public CmsReqId(int value) {
        super(value);
    }
}

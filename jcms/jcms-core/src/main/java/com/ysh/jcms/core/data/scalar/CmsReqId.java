package com.ysh.jcms.core.data.scalar;

/**
 * <pre>
 * {@code
 * ReqId ::= Int16U — Int16U ::= INTEGER (0..65535)
 * }
 * </pre>
 *
 * <p>
 * Alias for CmsInt16U (typedef cms_int16u_t cms_req_id_t).
 */
public class CmsReqId extends CmsInt16U {

    public CmsReqId() {
    }
    public CmsReqId(int value) {
        super(value);
    }
}

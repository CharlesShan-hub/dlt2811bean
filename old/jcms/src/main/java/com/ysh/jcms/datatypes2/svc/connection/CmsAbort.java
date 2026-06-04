package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.svc.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;

/**
 * cms_abort_t — AbortPDU.
 *
 * C: typedef struct { cms_association_id_t assoc_id; cms_abort_reason_t reason; } cms_abort_t;
 */
public class CmsAbort extends Structure {
    public CmsAssociationId assocId = new CmsAssociationId();
    public int reason;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("assocId", "reason");
    }
}

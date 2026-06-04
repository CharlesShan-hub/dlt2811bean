package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.svc.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;

/**
 * cms_release_request_t — Release-RequestPDU.
 *
 * C: typedef struct { cms_association_id_t assoc_id; } cms_release_request_t;
 */
public class CmsReleaseRequest extends Structure {
    public CmsAssociationId assocId = new CmsAssociationId();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("assocId");
    }
}

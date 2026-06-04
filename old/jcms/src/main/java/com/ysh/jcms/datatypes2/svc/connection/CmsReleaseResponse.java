package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.svc.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;

/**
 * cms_release_response_t — Release-ResponsePDU.
 *
 * C: typedef struct {
 *     cms_association_id_t assoc_id;
 *     cms_service_error_t  service_error;
 * } cms_release_response_t;
 */
public class CmsReleaseResponse extends Structure {
    public CmsAssociationId assocId = new CmsAssociationId();
    public int serviceError;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("assocId", "serviceError");
    }
}

package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.svc.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;

/**
 * cms_associate_response_t — Associate-ResponsePDU.
 *
 * C: typedef struct {
 *     cms_association_id_t assoc_id;
 *     cms_service_error_t  service_error;
 *     int                  has_auth;
 *     uint8_t              cert[2048];
 *     int                  cert_len;
 *     int64_t              signed_time;
 *     uint8_t              sig_val[2048];
 *     int                  sig_len;
 * } cms_associate_response_t;
 */
public class CmsAssociateResponse extends Structure {
    public CmsAssociationId assocId = new CmsAssociationId();
    public int serviceError;
    public CmsBoolean hasAuth = new CmsBoolean();
    public byte[] cert = new byte[2048];
    public int certLen;
    public long signedTime;
    public byte[] sigVal = new byte[2048];
    public int sigLen;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("assocId", "serviceError", "hasAuth",
            "cert", "certLen", "signedTime", "sigVal", "sigLen");
    }
}

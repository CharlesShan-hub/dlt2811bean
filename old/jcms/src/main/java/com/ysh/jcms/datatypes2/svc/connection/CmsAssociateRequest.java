package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;

import java.util.Arrays;
import java.util.List;

/**
 * cms_associate_request_t — Associate-RequestPDU.
 *
 * C: typedef struct {
 *     char     sap_ref[65];
 *     int      has_auth;
 *     uint8_t  cert[2048];
 *     int      cert_len;
 *     int64_t  signed_time;
 *     uint8_t  sig_val[2048];
 *     int      sig_len;
 * } cms_associate_request_t;
 */
public class CmsAssociateRequest extends Structure {
    public byte[] sapRef = new byte[65];
    public CmsBoolean hasAuth = new CmsBoolean();
    public byte[] cert = new byte[2048];
    public int certLen;
    public long signedTime;
    public byte[] sigVal = new byte[2048];
    public int sigLen;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("sapRef", "hasAuth", "cert", "certLen", "signedTime", "sigVal", "sigLen");
    }
}
